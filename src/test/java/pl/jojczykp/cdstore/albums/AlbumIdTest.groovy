package pl.jojczykp.cdstore.albums

import spock.lang.Specification

import static java.util.UUID.randomUUID
import static pl.jojczykp.cdstore.albums.AlbumId.randomAlbumId

class AlbumIdTest extends Specification {

	def "should generate random id"() {
		given:
			def albumId1 = randomAlbumId()
		when:
			def albumId2 = randomAlbumId()
		then:
			albumId1 != null
			albumId2 != null
			albumId1 != albumId2
	}

	def "should generate id from string and have corresponding toString"() {
		given:
			def idAsString = randomUUID().toString()
		when:
			def result = AlbumId.fromString(idAsString)
		then:
			result.toString() == idAsString
	}

}
