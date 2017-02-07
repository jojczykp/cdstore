package pl.jojczykp.cdstore.albums

import com.sun.jersey.api.client.ClientResponse
import spock.lang.Specification

import static javax.ws.rs.core.Response.Status.NOT_FOUND
import static pl.jojczykp.cdstore.albums.AlbumId.randomAlbumId
import static pl.jojczykp.cdstore.client.albums.CreateAlbumRequest.aCreateAlbumRequest
import static pl.jojczykp.cdstore.client.albums.UpdateAlbumRequest.anUpdateAlbumRequest
import static pl.jojczykp.cdstore.test_utils.JsonUtils.toMap

class UpdateAlbumIT extends Specification {

	String title = "Some Title"
	String newTitle = "Some New Title"

	def "should update album"() {
		given:
			Album album = aCreateAlbumRequest()
					.withTitle(title)
					.makeSuccessfully()
		when:
			Album result = anUpdateAlbumRequest(album.id)
					.withTitle(newTitle)
					.makeSuccessfully()
		then:
			result == album.toBuilder()
					.title(newTitle)
					.build()
	}

	def "should fail updating not existing album"() {
		given:
			AlbumId notExistingAlbumId = randomAlbumId()
		when:
			ClientResponse response = anUpdateAlbumRequest(notExistingAlbumId)
					.withTitle(newTitle)
					.make()
		then:
			response.status == NOT_FOUND.statusCode
			toMap(response) == [code: 101, message: 'album with given id not found']
	}

}
