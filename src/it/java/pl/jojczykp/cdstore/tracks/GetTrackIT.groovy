package pl.jojczykp.cdstore.tracks

import com.sun.jersey.api.client.ClientResponse
import pl.jojczykp.cdstore.albums.AlbumId
import spock.lang.Specification

import static javax.ws.rs.core.Response.Status.NOT_FOUND
import static pl.jojczykp.cdstore.albums.AlbumId.randomAlbumId
import static pl.jojczykp.cdstore.client.albums.CreateAlbumRequest.aCreateAlbumRequest
import static pl.jojczykp.cdstore.tracks.TrackId.randomTrackId
import static pl.jojczykp.cdstore.client.tracks.CreateTrackRequest.aCreateTrackRequest
import static pl.jojczykp.cdstore.client.tracks.GetTrackRequest.aGetTrackRequest
import static pl.jojczykp.cdstore.client.tracks.GetTracksRequest.aGetTracksRequest
import static pl.jojczykp.cdstore.test_utils.JsonUtils.toMap

class GetTrackIT extends Specification {

	String albumTitle = "Some Album Title"
	String trackTitle = "Some Track Title"

	def "should get track by ids"() {
		given:
			AlbumId albumId = aCreateAlbumRequest()
					.withTitle(albumTitle)
					.makeSuccessfully()
					.id
			Track track = aCreateTrackRequest()
					.withAlbumId(albumId)
					.withTitle(trackTitle)
					.makeSuccessfully()
		when:
			Track result = aGetTrackRequest()
					.withAlbumId(track.albumId)
					.withTrackId(track.id)
					.makeSuccessfully()
		then:
			result == track
	}

	def "should fail getting not existing track"() {
		given:
			AlbumId albumId = aCreateAlbumRequest()
					.withTitle(albumTitle)
					.makeSuccessfully()
					.id
			TrackId trackId = randomTrackId()
		when:
			ClientResponse response = aGetTrackRequest()
					.withAlbumId(albumId)
					.withTrackId(trackId)
					.make()
		then:
			response.status == NOT_FOUND.statusCode
			toMap(response) == [code: 101, message: 'track with given id not found']
	}

	def "should get all tracks from album"() {
		given:
			AlbumId albumId = aCreateAlbumRequest()
					.withTitle(albumTitle)
					.makeSuccessfully()
					.id
			Track track1 = aCreateTrackRequest()
					.withAlbumId(albumId)
					.withTitle("Title 1")
					.makeSuccessfully()
			Track track2 = aCreateTrackRequest()
					.withAlbumId(albumId)
					.withTitle("Title 2")
					.makeSuccessfully()
		when:
			List<Track> result = aGetTracksRequest()
					.withAlbumId(albumId)
					.makeSuccessfully()
		then:
			result.containsAll(track1, track2)
	}

	def "should fail getting all tracks from not existing album"() {
		given:
			AlbumId albumId = randomAlbumId()
		when:
			ClientResponse response = aGetTracksRequest()
					.withAlbumId(albumId)
					.make()
		then:
			response.status == NOT_FOUND.statusCode
			toMap(response) == [code: 101, message: 'album with given id not found']
	}

}
