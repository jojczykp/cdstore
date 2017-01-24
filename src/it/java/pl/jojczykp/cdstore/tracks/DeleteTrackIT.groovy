package pl.jojczykp.cdstore.tracks

import com.sun.jersey.api.client.ClientResponse
import pl.jojczykp.cdstore.albums.AlbumId
import spock.lang.Specification

import static javax.ws.rs.core.Response.Status.NOT_FOUND
import static pl.jojczykp.cdstore.albums.AlbumId.randomAlbumId
import static pl.jojczykp.cdstore.client.albums.CreateAlbumRequest.aCreateAlbumRequest
import static pl.jojczykp.cdstore.client.tracks.CreateTrackRequest.aCreateTrackRequest
import static pl.jojczykp.cdstore.client.tracks.DeleteTrackRequest.aDeleteTrackRequest
import static pl.jojczykp.cdstore.client.tracks.GetTrackRequest.aGetTrackRequest
import static pl.jojczykp.cdstore.test_utils.JsonUtils.toMap
import static pl.jojczykp.cdstore.tracks.TrackId.randomTrackId

class DeleteTrackIT extends Specification {

	String title = "Some Title"

	def "should delete track"() {
		given:
			AlbumId albumId = aCreateAlbumRequest()
					.withTitle(title)
					.makeSuccessfully()
					.id
			TrackId trackId = aCreateTrackRequest()
					.withTitle(title)
					.withAlbumId(albumId)
					.makeSuccessfully()
					.id
		when:
			aDeleteTrackRequest()
					.withAlbumId(albumId)
					.withTrackId(trackId)
					.makeSuccessfully()
		then:
			aGetTrackRequest()
					.withAlbumId(albumId)
					.withTrackId(trackId)
					.make()
					.status == NOT_FOUND.statusCode
	}

	def "should fail deleting not existing track from existing album"() {
		given:
			AlbumId albumId = aCreateAlbumRequest()
					.withTitle(title)
					.makeSuccessfully()
					.id
			TrackId notExistingTrackId = randomTrackId()
		when:
			ClientResponse response = aDeleteTrackRequest()
					.withAlbumId(albumId)
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

}
