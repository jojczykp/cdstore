package pl.jojczykp.cdstore.tracks

import pl.jojczykp.cdstore.albums.AlbumId
import spock.lang.Specification

import javax.ws.rs.core.Response

import static pl.jojczykp.cdstore.albums.AlbumId.randomAlbumId
import static pl.jojczykp.cdstore.tracks.Track.aTrack
import static pl.jojczykp.cdstore.tracks.TrackId.randomTrackId

class TracksResourceTest extends Specification {

	AlbumId albumId = randomAlbumId()
	TrackId trackId = randomTrackId()
	Track track1 = aTrack().build()
	Track track2 = aTrack().build()

	TracksManager manager = Mock(TracksManager)
	TracksResource resource = new TracksResource(manager)

	def "should delegate create track to manager"() {
		when:
			Response result = resource.createTrack(track1)
		then:
			1 * manager.createTrack(track1) >> track2
			result.entity == track2
	}

	def "should delegate get track by id to manager"() {
		when:
			Track result = resource.getTrack(albumId, trackId)
		then:
			1 * manager.getTrack(albumId, trackId) >> track1
			result == track1
	}

	def "should delegate update album to manager"() {
		when:
			resource.updateTrack(albumId, trackId, track1)
		then:
			1 * manager.updateTrack(albumId, trackId, track1)
	}

	def "should delegate delete track to manager"() {
		when:
			resource.deleteTrack(albumId, trackId)
		then:
			1 * manager.deleteTrack(albumId, trackId)
	}

	def "should delegate delete all album tracks to manager"() {
		when:
			resource.deleteAllAlbumTracks(albumId)
		then:
			1 * manager.deleteAllAlbumTracks(albumId)
	}

}