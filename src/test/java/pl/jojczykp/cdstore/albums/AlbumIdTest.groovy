package pl.jojczykp.cdstore.albums

import spock.lang.Specification

import static java.util.UUID.randomUUID
import static pl.jojczykp.cdstore.albums.AlbumId.randomAlbumId

class AlbumIdTest extends Specification {

    def "should generate random id"() {
        given:
            def id1 = randomAlbumId()
        when:
            def id2 = randomAlbumId()
        then:
            id1 != null
            id2 != null
            id1 != id2
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