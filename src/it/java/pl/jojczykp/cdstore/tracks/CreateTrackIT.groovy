package pl.jojczykp.cdstore.tracks

import com.sun.jersey.api.client.ClientResponse
import pl.jojczykp.cdstore.albums.Album
import pl.jojczykp.cdstore.albums.AlbumId
import spock.lang.Specification

import static javax.ws.rs.core.Response.Status.NOT_FOUND
import static pl.jojczykp.cdstore.albums.AlbumId.randomAlbumId
import static pl.jojczykp.cdstore.client.albums.CreateAlbumRequest.aCreateAlbumRequest
import static pl.jojczykp.cdstore.client.tracks.CreateTrackRequest.aCreateTrackRequest
import static pl.jojczykp.cdstore.test_utils.JsonUtils.toMap

class CreateTrackIT extends Specification {

	String albumTitle = "Some Album Title"
	String trackTitle = "Some Track Title"

	def "should create a new track"() {
		given:
			Album album = aCreateAlbumRequest()
					.withTitle(albumTitle)
					.makeSuccessfully()
		when:
			Track track = aCreateTrackRequest(album.id)
					.withTitle(trackTitle)
					.makeSuccessfully()
		then:
			track.albumId == album.id
			track.title == trackTitle
	}

	def "should fail creating a new track for not existing album"() {
		given:
			AlbumId nonExistingAlbumId = randomAlbumId()
		when:
			ClientResponse response = aCreateTrackRequest(nonExistingAlbumId)
					.withTitle(trackTitle)
					.make()
		then:
			response.status == NOT_FOUND.statusCode
			toMap(response) == [code: 101, message: 'album with given id not found']
	}

}
