package pl.jojczykp.cdstore.albums

import spock.lang.Specification

import static javax.ws.rs.core.Response.Status.NOT_FOUND
import static pl.jojczykp.cdstore.client.albums.CreateAlbumRequest.aCreateAlbumRequest
import static pl.jojczykp.cdstore.client.albums.DeleteAlbumRequest.aDeleteAlbumRequest
import static pl.jojczykp.cdstore.client.albums.GetAlbumRequest.aGetAlbumRequest

class DeleteAlbumIT extends Specification {

	String title = "Some Title"

	def "should delete album"() {
		given:
			UUID id = aCreateAlbumRequest()
					.withTitle(title)
					.makeSuccessfully()
					.getId()
		when:
			aDeleteAlbumRequest()
					.withId(id)
					.makeSuccessfully()
		then:
			aGetAlbumRequest()
					.withId(id)
					.make()
					.getStatus() == NOT_FOUND.statusCode
	}

}