package pl.jojczykp.cdstore.albums

import spock.lang.Specification

import static pl.jojczykp.cdstore.client.albums.CreateAlbumRequest.aCreateAlbumRequest
import static pl.jojczykp.cdstore.client.albums.UpdateAlbumRequest.anUpdateAlbumRequest

class UpdateAlbumIT extends Specification {

	String title = "Some Title"
	String newTitle = "Some New Title"

	def "should update album"() {
		given:
			Album album = aCreateAlbumRequest()
					.withTitle(title)
					.makeSuccessfully()
		when:
			Album result = anUpdateAlbumRequest()
					.withId(album.getId())
					.withTitle(newTitle)
					.makeSuccessfully()
		then:
			result == album.toBuilder()
					.title(newTitle)
					.build()
	}

}
