package pl.jojczykp.cdstore.tracks

import spock.lang.Specification

import static java.util.UUID.randomUUID
import static pl.jojczykp.cdstore.tracks.Track.aTrack

class TracksRepositoryTest extends Specification {

    TracksRepository repository = new TracksRepository()

    def "should create track"() {
        given:
            Track newTrack = aTrack().id(randomUUID()).albumId(randomUUID()).title("A Title").build()
        when:
            Track createdTrack = repository.createTrack(newTrack)
        then:
            createdTrack == dbGetTrack(createdTrack.getId())
            createdTrack == newTrack.toBuilder().id(createdTrack.getId()).build()
    }

    Track dbGetTrack(UUID id) {
        return repository.getTrack(id)
    }

}
