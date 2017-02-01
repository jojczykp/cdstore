package pl.jojczykp.cdstore.albums

import com.sun.jersey.api.client.ClientResponse
import spock.lang.Specification

import static javax.ws.rs.core.Response.Status.NOT_FOUND
import static pl.jojczykp.cdstore.albums.AlbumId.randomAlbumId
import static pl.jojczykp.cdstore.client.albums.CreateAlbumRequest.aCreateAlbumRequest
import static pl.jojczykp.cdstore.client.albums.GetAlbumRequest.aGetAlbumRequest
import static pl.jojczykp.cdstore.client.albums.GetAlbumsRequest.aGetAlbumsRequest
import static pl.jojczykp.cdstore.test_utils.JsonUtils.toMap

class GetAlbumIT extends Specification {

	def "should get album by id"() {
		String title = "Some Title"

		given:
			Album album = aCreateAlbumRequest()
					.withTitle(title)
					.makeSuccessfully()
		when:
			Album result = aGetAlbumRequest()
					.withAlbumId(album.id)
					.makeSuccessfully()
		then:
			result == album
	}

	def "should fail getting not existing album"() {
		given:
			AlbumId albumId = randomAlbumId()
		when:
			ClientResponse response = aGetAlbumRequest()
					.withAlbumId(albumId)
					.make()
		then:
			response.status == NOT_FOUND.statusCode
			toMap(response) == [code: 101, message: 'album with given id not found']
	}

	def "should get albums"() {
		given:
			Album album1 = aCreateAlbumRequest()
					.withTitle("Title 1")
					.makeSuccessfully()

			Album album2 = aCreateAlbumRequest()
					.withTitle("Title 2")
					.makeSuccessfully()
		when:
			List<Album> result = aGetAlbumsRequest()
					.makeSuccessfully()
		then:
			result.containsAll(album1, album2)
	}

	def "should get albums filtered by substring in title"() {
		given:
			String desiredSubstring = "Find Me ${System.currentTimeMillis()}"
			Album album1 = aCreateAlbumRequest()
					.withTitle("Title ${desiredSubstring} 1")
					.makeSuccessfully()

			aCreateAlbumRequest()
					.withTitle("Title SkipMe 2")
					.makeSuccessfully()
		when:
			Set<Album> result = aGetAlbumsRequest()
					.withTitleSubstring(desiredSubstring)
					.makeSuccessfully()
		then:
			result == [album1] as Set
	}

}
