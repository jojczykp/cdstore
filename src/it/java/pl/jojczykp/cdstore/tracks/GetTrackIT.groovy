package pl.jojczykp.cdstore.tracks

import com.sun.jersey.api.client.ClientResponse
import pl.jojczykp.cdstore.albums.Album
import pl.jojczykp.cdstore.albums.AlbumId
import spock.lang.Specification

import static javax.ws.rs.core.Response.Status.NOT_FOUND
import static pl.jojczykp.cdstore.albums.AlbumId.randomAlbumId
import static pl.jojczykp.cdstore.client.albums.CreateAlbumRequest.aCreateAlbumRequest
import static pl.jojczykp.cdstore.client.tracks.CreateTrackRequest.aCreateTrackRequest
import static pl.jojczykp.cdstore.client.tracks.GetTrackRequest.aGetTrackRequest
import static pl.jojczykp.cdstore.client.tracks.GetTracksRequest.aGetTracksRequest
import static pl.jojczykp.cdstore.test_utils.JsonUtils.toMap
import static pl.jojczykp.cdstore.tracks.TrackId.randomTrackId

class GetTrackIT extends Specification {

	String albumTitle = "Some Album Title"
	String trackTitle = "Some Track Title"

	def "should get track by ids"() {
		given:
			Album album = aCreateAlbumRequest()
					.withTitle(albumTitle)
					.makeSuccessfully()
			Track track = aCreateTrackRequest(album.id)
					.withTitle(trackTitle)
					.makeSuccessfully()
		when:
			Track result = aGetTrackRequest(track.albumId, track.id).makeSuccessfully()
		then:
			result == track
	}

	def "should fail getting not existing track"() {
		given:
			Album album = aCreateAlbumRequest()
					.withTitle(albumTitle)
					.makeSuccessfully()
			TrackId trackId = randomTrackId()
		when:
			ClientResponse response = aGetTrackRequest(album.id, trackId).make()
		then:
			response.status == NOT_FOUND.statusCode
			toMap(response) == [code: 101, message: 'track with given id not found']
	}

	def "should get tracks from album"() {
		given:
			Album album = aCreateAlbumRequest()
					.withTitle(albumTitle)
					.makeSuccessfully()
			Track track1 = aCreateTrackRequest(album.id)
					.withTitle("Title 1")
					.makeSuccessfully()
			Track track2 = aCreateTrackRequest(album.id)
					.withTitle("Title 2")
					.makeSuccessfully()
		when:
			List<Track> result = aGetTracksRequest(album.id)
					.makeSuccessfully()
		then:
			result.containsAll(track1, track2)
	}

	def "should fail getting all tracks from not existing album"() {
		given:
			AlbumId albumId = randomAlbumId()
		when:
			ClientResponse response = aGetTracksRequest(albumId).make()
		then:
			response.status == NOT_FOUND.statusCode
			toMap(response) == [code: 101, message: 'album with given id not found']
	}

}
