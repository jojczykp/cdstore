package pl.jojczykp.cdstore.albums

import spock.lang.Specification

import static Album.anAlbum
import static pl.jojczykp.cdstore.albums.AlbumId.randomAlbumId

class AlbumTest extends Specification {

	AlbumId albumId = randomAlbumId()
	String title = "a title"

	def "should return with getters what was set in constructor"() {
		when:
			Album album = anAlbum()
					.id(albumId)
					.title(title)
					.build()
		then:
			album.id == albumId
			album.title == title
	}

}
