package pl.jojczykp.cdstore.albums

import com.sun.jersey.api.client.ClientResponse
import spock.lang.Specification

import static javax.ws.rs.core.Response.Status.NOT_FOUND
import static pl.jojczykp.cdstore.albums.AlbumId.randomAlbumId
import static pl.jojczykp.cdstore.client.albums.CreateAlbumRequest.aCreateAlbumRequest
import static pl.jojczykp.cdstore.client.albums.DeleteAlbumRequest.aDeleteAlbumRequest
import static pl.jojczykp.cdstore.client.albums.GetAlbumRequest.aGetAlbumRequest
import static pl.jojczykp.cdstore.test_utils.JsonUtils.toMap

class DeleteAlbumIT extends Specification {

	String title = "Some Title"

	def "should delete album"() {
		given:
			AlbumId id = aCreateAlbumRequest()
					.withTitle(title)
					.makeSuccessfully()
					.getId()
		when:
			aDeleteAlbumRequest()
					.withId(id)
					.makeSuccessfully()
		then:
			aGetAlbumRequest()
					.withAlbumId(id)
					.make()
					.getStatus() == NOT_FOUND.statusCode
	}

	def "should fail deleting not existing album"() {
		given:
			AlbumId notExistingAlbumId = randomAlbumId()
		when:
			ClientResponse response = aDeleteAlbumRequest()
					.withId(notExistingAlbumId)
					.make()
		then:
			response.status == NOT_FOUND.statusCode
			toMap(response) == [code: 101, message: 'album with given id not found']
	}

}
