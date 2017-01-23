package pl.jojczykp.cdstore.albums

import spock.lang.Specification

import static Album.anAlbum
import static pl.jojczykp.cdstore.albums.AlbumId.randomAlbumId

class AlbumTest extends Specification {

	AlbumId id = randomAlbumId()
	String title = "a title"

	def "should return with getters what was set in constructor"() {
		when:
			Album album = anAlbum()
					.id(id)
					.title(title)
					.build()
		then:
			album.id == id
			album.title == title
	}

}
