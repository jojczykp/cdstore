package pl.jojczykp.cdstore.tracks

import pl.jojczykp.cdstore.albums.Album
import pl.jojczykp.cdstore.albums.AlbumId
import pl.jojczykp.cdstore.exceptions.ItemNotFoundException
import spock.lang.Specification

import static pl.jojczykp.cdstore.albums.AlbumId.randomAlbumId
import static pl.jojczykp.cdstore.tracks.Track.aTrack
import static pl.jojczykp.cdstore.tracks.TrackId.randomTrackId

class TracksRepositoryTest extends Specification {

	AlbumId albumId = randomAlbumId()
	TrackId trackId1 = randomTrackId()
	TrackId trackId2 = randomTrackId()

	TracksRepository repository = new TracksRepository()

	def "should create track"() {
		given:
			Track newTrack = aTrack()
					.id(trackId1)
					.albumId(albumId)
					.title("A Title")
					.build()
		when:
			Track createdTrack = repository.createTrack(newTrack)
		then:
			createdTrack == dbGetTrack(createdTrack.id)
			createdTrack == newTrack.toBuilder().id(createdTrack.id).build()
	}

	def "should get track"() {
		given:
			String title = "Track Title"
			trackId1 = dbPutTrack(trackId1, albumId, title).id //TODO simplify once repository implemented
		when:
			Track track = repository.getTrack(trackId1)
		then:
			track.id == trackId1
			track.albumId == albumId
			track.title == title
	}

	def "should fail getting not existing track"() {
		when:
			repository.getTrack(trackId1)
		then:
			ItemNotFoundException ex = thrown()
			ex.message == "track with given id not found"
	}

	def "should get tracks from album"() {
		given:
			Album album = new Album(randomAlbumId(), "Album Title 1")
			Set<Track> createdTracks = [
				new Track(randomTrackId(), album.id, "Track Title 1"),
				new Track(randomTrackId(), album.id, "Track Title 2"),
				new Track(randomTrackId(), album.id, "Track Title 3")
		]
// createdTracks.each { dbPutTrack(it.id, it.albumId, it.title) } //TODO simplify once real repository in place
			createdTracks = createdTracks.collect { dbPutTrack(it.id, it.albumId, it.title) }
		when:
			Set<Track> returnedTracks = repository.getTracks(album.id)
		then:
			returnedTracks == createdTracks
	}

	def "should update track"() {
		given:
			TrackId trackId = randomTrackId()
			Track patch = aTrack().title("New Title").build()
			trackId = dbPutTrack(trackId, albumId, "Old Title").id //TODO simplify once repository properly implememted
			Track expectedTrack = aTrack().id(trackId).albumId(albumId).title(patch.title).build()
		when:
			Track updatedTrack = repository.updateTrack(trackId, patch)
		then:
			dbGetTrack(trackId) == expectedTrack
			updatedTrack == expectedTrack
	}

	def "should fail updating not existing track"() {
		given:
			TrackId trackId = randomTrackId()
			Track patch = aTrack().title("New Title").build()
		when:
			repository.updateTrack(trackId, patch)
		then:
			ItemNotFoundException ex = thrown()
			ex.message == "track with given id not found"
	}

	def "should delete track"() {
		given:
			trackId1 = dbPutTrack(trackId1, albumId, "Track Title").id //TODO simplify once repository implemented
		when:
			repository.deleteTrack(trackId1)
		then:
			dbGetTrack(trackId1) == null
	}

	def "should delete album tracks"() {
		given:
			trackId1 = dbPutTrack(trackId1, albumId, "Track Title 1").id //TODO simplify once repository implemented
			trackId2 = dbPutTrack(trackId2, albumId, "Track Title 2").id
		when:
			repository.deleteAlbumTracks(albumId)
		then:
			dbGetTrack(trackId1) == null
			dbGetTrack(trackId2) == null
	}

	def "should fail deleting not existing track"() {
		when:
			repository.deleteTrack(trackId1)
		then:
			ItemNotFoundException ex = thrown()
			ex.message == "track with given id not found"
	}

	def dbPutTrack(TrackId trackId, AlbumId albumId, String title) {
		repository.createTrack(aTrack().albumId(albumId).title(title).build())
	}

	def dbGetTrack(TrackId trackId) {
		try {
			repository.getTrack(trackId)
		} catch (ItemNotFoundException e) {
			null
		}
	}

}
