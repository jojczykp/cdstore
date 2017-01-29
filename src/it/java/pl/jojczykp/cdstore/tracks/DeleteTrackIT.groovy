package pl.jojczykp.cdstore.tracks

import com.sun.jersey.api.client.ClientResponse
import pl.jojczykp.cdstore.albums.Album
import pl.jojczykp.cdstore.albums.AlbumId
import spock.lang.Specification

import static javax.ws.rs.core.Response.Status.NOT_FOUND
import static pl.jojczykp.cdstore.albums.AlbumId.randomAlbumId
import static pl.jojczykp.cdstore.client.albums.CreateAlbumRequest.aCreateAlbumRequest
import static pl.jojczykp.cdstore.client.tracks.CreateTrackRequest.aCreateTrackRequest
import static pl.jojczykp.cdstore.client.tracks.DeleteTrackRequest.aDeleteTrackRequest
import static pl.jojczykp.cdstore.client.tracks.DeleteTracksRequest.aDeleteTracksRequest
import static pl.jojczykp.cdstore.client.tracks.GetTrackRequest.aGetTrackRequest
import static pl.jojczykp.cdstore.client.tracks.GetTracksRequest.aGetTracksRequest
import static pl.jojczykp.cdstore.test_utils.JsonUtils.toMap
import static pl.jojczykp.cdstore.tracks.TrackId.randomTrackId

class DeleteTrackIT extends Specification {

	String title = "Some Title"

	def "should delete track"() {
		given:
			Album album = aCreateAlbumRequest()
					.withTitle(title)
					.makeSuccessfully()
			Track track = aCreateTrackRequest()
					.withTitle(title)
					.withAlbumId(album.id)
					.makeSuccessfully()
		when:
			aDeleteTrackRequest()
					.withAlbumId(album.id)
					.withTrackId(track.id)
					.makeSuccessfully()
		then:
			aGetTrackRequest()
					.withAlbumId(album.id)
					.withTrackId(track.id)
					.make()
					.status == NOT_FOUND.statusCode
	}

	def "should fail deleting not existing track from existing album"() {
		given:
			Album album = aCreateAlbumRequest()
					.withTitle(title)
					.makeSuccessfully()
			TrackId notExistingTrackId = randomTrackId()
		when:
			ClientResponse response = aDeleteTrackRequest()
					.withAlbumId(album.id)
					.withTrackId(notExistingTrackId)
					.make()
		then:
			response.status == NOT_FOUND.statusCode
			toMap(response) == [code: 101, message: 'track with given id not found']
	}

	def "should fail deleting track from not existing album"() {
		given:
			AlbumId notExistingAlbumId = randomAlbumId()
			TrackId notExistingTrackId = randomTrackId()
		when:
			ClientResponse response = aDeleteTrackRequest()
					.withAlbumId(notExistingAlbumId)
					.withTrackId(notExistingTrackId)
					.make()
		then:
			response.status == NOT_FOUND.statusCode
			toMap(response) == [code: 101, message: 'album with given id not found']
	}

	def "should delete all album tracks"() {
		given:
			Album album = aCreateAlbumRequest()
					.withTitle(title)
					.makeSuccessfully()
			aCreateTrackRequest()
					.withTitle(title + " 1")
					.withAlbumId(album.id)
					.makeSuccessfully()
			aCreateTrackRequest()
					.withTitle(title + " 2")
					.withAlbumId(album.id)
					.makeSuccessfully()
		when:
			aDeleteTracksRequest()
					.withAlbumId(album.id)
					.makeSuccessfully()
		then:
			aGetTracksRequest()
					.withAlbumId(album.id)
					.makeSuccessfully()
					.isEmpty()
	}

	def "should fail deleting all album tracks from not existing album"() {
		given:
			AlbumId notExistingAlbumId = randomAlbumId()
		when:
			ClientResponse response = aDeleteTracksRequest()
					.withAlbumId(notExistingAlbumId)
					.make()
		then:
			response.status == NOT_FOUND.statusCode
			toMap(response) == [code: 101, message: 'album with given id not found']
	}

}
