package pl.jojczykp.cdstore.albums

import spock.lang.Specification

import static Album.anAlbum
import static pl.jojczykp.cdstore.client.albums.CreateAlbumRequest.aCreateAlbumRequest
import static pl.jojczykp.cdstore.client.albums.GetAlbumRequest.aGetAlbumRequest
import static pl.jojczykp.cdstore.client.albums.GetAlbumsRequest.aGetAlbumsRequest

class GetAlbumIT extends Specification {

	def "should get album by id"() {
		String title = "Some Title"

		given:
			Album album = aCreateAlbumRequest()
					.withTitle(title)
					.makeSuccessfully()
		when:
			Album result = aGetAlbumRequest()
					.withId(album.getId())
					.makeSuccessfully()
		then:
			result == album
	}

	def "should get all albums"() {
		Album album1 = anAlbum().title("Title 1").build()
		Album album2 = anAlbum().title("Title 2").build()

		given:
			Album albumCreated1 = aCreateAlbumRequest().withAlbum(album1).makeSuccessfully()
			Album albumCreated2 = aCreateAlbumRequest().withAlbum(album2).makeSuccessfully()
		when:
			List<Album> result = aGetAlbumsRequest().makeSuccessfully()
		then:
			result.containsAll(albumCreated1, albumCreated2)
	}

}
