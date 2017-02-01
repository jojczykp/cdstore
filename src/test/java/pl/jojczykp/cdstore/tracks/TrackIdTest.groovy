package pl.jojczykp.cdstore.tracks

import spock.lang.Specification

import static java.util.UUID.randomUUID
import static pl.jojczykp.cdstore.tracks.TrackId.randomTrackId

class TrackIdTest extends Specification {

	def "should generate random id"() {
		given:
			def trackId1 = randomTrackId()
		when:
			def trackId2 = randomTrackId()
		then:
			trackId1 != null
			trackId2 != null
			trackId1 != trackId2
	}

	def "should generate id from string and have corresponding toString"() {
		given:
			def idAsString = randomUUID().toString()
		when:
			def result = TrackId.fromString(idAsString)
		then:
			result.toString() == idAsString
	}

}