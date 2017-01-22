package pl.jojczykp.cdstore.tracks

import spock.lang.Specification

import static Track.aTrack

class TrackTest extends Specification {

	UUID id = new UUID(1, 2)
	UUID albumId = new UUID(2, 3)
	String title = "a title"

	def "should return with getters what was set in constructor"() {
		when:
			Track Track = aTrack()
					.id(id)
					.albumId(albumId)
					.title(title)
					.build()
		then:
			Track == aTrack().id(id).albumId(albumId).title(title).build()
	}

}
