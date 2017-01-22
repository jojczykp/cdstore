package pl.jojczykp.cdstore.tracks

import spock.lang.Specification

import static pl.jojczykp.cdstore.tracks.Track.aTrack

class TracksManagerTest extends Specification {

    Track track1 = aTrack().build()
    Track track2 = aTrack().build()

    TracksRepository repository = Mock(TracksRepository)
    TracksManager manager = new TracksManager(repository)

    def "should delegate create track to repository"() {
        when:
            Track result = manager.createTrack(track1)
        then:
            1 * repository.createTrack(track1) >> track2
            result == track2
    }

}
