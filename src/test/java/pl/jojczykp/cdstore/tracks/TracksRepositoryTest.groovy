package pl.jojczykp.cdstore.tracks

import org.apache.hadoop.hbase.client.Delete
import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.client.ResultScanner
import org.apache.hadoop.hbase.client.Scan
import org.apache.hadoop.hbase.client.Table
import org.apache.hadoop.hbase.filter.Filter
import org.apache.hadoop.hbase.filter.FilterList
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter
import org.apache.hadoop.hbase.util.Bytes
import pl.jojczykp.cdstore.albums.Album
import pl.jojczykp.cdstore.albums.AlbumId
import pl.jojczykp.cdstore.exceptions.ItemNotFoundException
import spock.lang.Specification

import static org.apache.hadoop.hbase.CellUtil.createCell
import static org.apache.hadoop.hbase.filter.CompareFilter.CompareOp.EQUAL
import static org.apache.hadoop.hbase.filter.CompareFilter.CompareOp.NOT_EQUAL
import static pl.jojczykp.cdstore.albums.AlbumId.randomAlbumId
import static pl.jojczykp.cdstore.tracks.Track.aTrack
import static pl.jojczykp.cdstore.tracks.TrackId.randomTrackId

class TracksRepositoryTest extends Specification {

	static byte[] F_DATA = toBytes("data")
	static byte[] C_ALBUM_ID = toBytes("albumId")
	static byte[] C_TITLE = toBytes("title")

	static AlbumId albumId = randomAlbumId()
	static TrackId trackId = randomTrackId()
	static String title = "A Title"

	Table table = Mock(Table.class)
	Result result = Mock(Result.class)
	ResultScanner resultScanner = Mock(ResultScanner.class)

	TracksRepository repository = new TracksRepository(table)

	Put p
	Get g
	Scan s
	Delete d

	def "should create track"() {
		given:
			Track newTrack = aTrack()
					.albumId(albumId)
					.title(title)
					.build()
		when:
			Track createdTrack = repository.createTrack(newTrack)
		then:
			1 * table.put(_) >> { arguments -> p = arguments[0] }
			p.row == toBytes(createdTrack.id)
			p.has(F_DATA, C_ALBUM_ID, toBytes(albumId))
			p.has(F_DATA, C_TITLE, toBytes(title))
			createdTrack == newTrack.toBuilder().id(createdTrack.id).build()
	}

	def "should get track"() {
		when:
			Track track = repository.getTrack(trackId)
		then:
			1 * table.get(_) >> { arguments -> g = arguments[0]; result }
			1 * result.isEmpty() >> false
			1 * result.getValue(F_DATA, C_ALBUM_ID) >> toBytes(albumId)
			1 * result.getValue(F_DATA, C_TITLE) >> toBytes(title)
			track == aTrack().id(trackId).albumId(albumId).title(title).build()
			track == aTrack().id(trackId).albumId(albumId).title(title).build()
	}

	def "should fail getting not existing track"() {
		when:
			repository.getTrack(trackId)
		then:
			1 * table.get(_) >> { arguments -> g = arguments[0]; result }
			1 * result.isEmpty() >> true
			g.row == toBytes(trackId)
			ItemNotFoundException ex = thrown()
			ex.message == "track with given id not found"
	}

	def "should get all tracks from album"() {
		given:
			Album album = new Album(randomAlbumId(), "Album Title 1")
			Set<Track> createdTracks = [
					new Track(randomTrackId(), album.id, "Track Title 1"),
					new Track(randomTrackId(), album.id, "Track Title 2"),
			]
		when:
			def returnedTracks = repository.getTracks(album.id)
		then:
			1 * table.getScanner(_) >> { arguments -> s = arguments[0] ; resultScanner }
			1 * resultScanner.spliterator() >> toScannerResults(createdTracks).spliterator()
			isAlbumIdFilter(s.filter, album.id)
			returnedTracks == createdTracks
	}

	def "should update track"() {
		given:
			Track patch = aTrack().albumId(albumId).title("New Title").build()
		when:
			repository.updateTrack(trackId, patch)
		then:
			1 * table.checkAndPut(toBytes(trackId), F_DATA, C_ALBUM_ID, NOT_EQUAL, toBytes("not existing"), _) >>
					{ arguments -> p = arguments[5]; true }
			p.row == toBytes(trackId)
			p.has(F_DATA, C_ALBUM_ID, toBytes(patch.albumId))
			p.has(F_DATA, C_TITLE, toBytes(patch.title))
	}

	def "should fail updating not existing track"() {
		given:
			Track patch = aTrack().albumId(albumId).title("New Title").build()
		when:
			repository.updateTrack(trackId, patch)
		then:
			1 * table.checkAndPut(toBytes(trackId), F_DATA, C_ALBUM_ID, NOT_EQUAL, toBytes("not existing"), _) >>
					{ arguments -> p = arguments[5]; false }
			p.row == toBytes(trackId)
			p.has(F_DATA, C_ALBUM_ID, toBytes(patch.albumId))
			p.has(F_DATA, C_TITLE, toBytes(patch.title))
			ItemNotFoundException ex = thrown()
			ex.message == "track with given id not found"
	}

	def "should delete track"() {
		when:
			repository.deleteTrack(trackId)
		then:
			1 * table.checkAndDelete(toBytes(trackId), F_DATA, C_ALBUM_ID, NOT_EQUAL, toBytes("not existing"), _) >>
					{ arguments -> d = arguments[5]; true }
			d.row == toBytes(trackId)
	}

	def "should fail deleting not existing track"() {
		when:
			repository.deleteTrack(trackId)
		then:
			1 * table.checkAndDelete(toBytes(trackId), F_DATA, C_ALBUM_ID, NOT_EQUAL, toBytes("not existing"), _) >>
					{ arguments -> d = arguments[5]; false }
			d.row == toBytes(trackId)
			ItemNotFoundException ex = thrown()
			ex.message == "track with given id not found"
	}

	def "should delete all album tracks"() {
		given:
			Album album = new Album(randomAlbumId(), "Album Title 1")
			List<Track> tracksToDelete = [
					new Track(randomTrackId(), album.id, "Track Title 1"),
					new Track(randomTrackId(), album.id, "Track Title 2"),
					new Track(randomTrackId(), album.id, "Track Title 3"),
			]
			Set<Result> tracksToDeleteAsResult = toScannerResults(tracksToDelete as Set)
			List<Delete> tracksToDeleteAsDeletes = toDeletes(tracksToDelete)
		when:
			repository.deleteAllAlbumTracks(album.id)
		then:
			1 * table.getScanner(_) >> { arguments -> s = arguments[0] ; resultScanner }
			1 * resultScanner.spliterator() >> tracksToDeleteAsResult.spliterator()
			1 * table.delete(tracksToDeleteAsDeletes)
			FilterList filterList = (s.filter as FilterList)
			filterList.filters.size() == 2
			filterList.operator == FilterList.Operator.MUST_PASS_ALL
			isAlbumIdFilter(filterList.filters[0], album.id)
			isFirstKeyOnlyFilter(filterList.filters[1])
	}

	private static boolean isAlbumIdFilter(Filter f, AlbumId albumId) {
		f instanceof  SingleColumnValueFilter &&
		f.family == F_DATA &&
		f.qualifier == C_ALBUM_ID &&
		f.operator == EQUAL &&
		f.latestVersionOnly &&
		f.comparator.value == toBytes(albumId)
	}

	private static boolean isFirstKeyOnlyFilter(Filter f) {
		f instanceof  FirstKeyOnlyFilter
	}

	private static Set<Result> toScannerResults(Set<Track> tracks) {
		tracks.collectMany {[
				Result.create(
						createCell(toBytes(it.id), F_DATA, C_ALBUM_ID, 0L, (byte) 0, toBytes(it.albumId)),
						createCell(toBytes(it.id), F_DATA, C_TITLE, 0L, (byte) 0, toBytes(it.title))),
		]}
	}

	private static List<Delete> toDeletes(List<Track> tracks) {
		tracks.collect { new Delete(toBytes(it.id)) }
	}

	private static byte[] toBytes(String string) {
		Bytes.toBytes(string)
	}

	private static byte[] toBytes(AlbumId albumId) {
		Bytes.toBytes(albumId.toString())
	}

	private static byte[] toBytes(TrackId trackId) {
		Bytes.toBytes(trackId.toString())
	}

}
