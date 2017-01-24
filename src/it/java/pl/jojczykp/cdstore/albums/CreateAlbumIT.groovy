package pl.jojczykp.cdstore.albums

import spock.lang.Specification

import static pl.jojczykp.cdstore.client.albums.CreateAlbumRequest.aCreateAlbumRequest

class CreateAlbumIT extends Specification {

	String title = "Some Title"

	def "should create a new album"() {
		when:
			Album result = aCreateAlbumRequest()
					.withTitle(title)
					.makeSuccessfully()
		then:
			result.title == title

	}

}
