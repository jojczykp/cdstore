package pl.jojczykp.cdstore.tracks

import nl.jqno.equalsverifier.EqualsVerifier
import spock.lang.Specification

import static java.util.UUID.randomUUID
import static pl.jojczykp.cdstore.tracks.TrackId.randomTrackId

class TrackIdTest extends Specification {

    def "should generate random id"() {
        given:
            def id1 = randomTrackId()
        when:
            def id2 = randomTrackId()
        then:
            id1 != null
            id2 != null
            id1 != id2
    }

    def "should generate id from string and have corresponding toString"() {
        given:
            def idAsString = randomUUID().toString()
        when:
            def result = TrackId.fromString(idAsString)
        then:
            result.toString() == idAsString
    }

    def "should have proper equals"() {
        EqualsVerifier.forClass(TrackId.class).verify()
    }

    def "should have different hashCodes if ids are not equal"() {
        given:
            TrackId id1 = randomTrackId()
        when:
            TrackId id2 = randomTrackId()
        then:
            id1.hashCode() != id2.hashCode()
    }

    def "should have same hashCodes if ids are equal"() {
        given:
            TrackId id1 = randomTrackId()
        when:
            TrackId id2 = TrackId.fromString(id1.toString())
        then:
            id1.hashCode() == id2.hashCode()
    }

}