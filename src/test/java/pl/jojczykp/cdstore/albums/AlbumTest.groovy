package pl.jojczykp.cdstore.albums

import spock.lang.Specification

import static Album.anAlbum

class AlbumTest extends Specification {

	UUID id = new UUID(1, 2)
	String title = "a title"

	def "should return with getters what was set in constructor"() {
		when:
			Album album = anAlbum()
					.id(id)
					.title(title)
					.build()
		then:
			album == anAlbum().id(id).title(title).build()
	}
}
