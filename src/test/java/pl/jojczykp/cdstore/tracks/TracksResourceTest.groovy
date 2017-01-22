package pl.jojczykp.cdstore.tracks

import spock.lang.Specification

import javax.ws.rs.core.Response

import static pl.jojczykp.cdstore.tracks.Track.aTrack

class TracksResourceTest extends Specification {

    Track track1 = aTrack().build()
    Track track2 = aTrack().build()

    TracksManager manager = Mock(TracksManager)
    TracksResource resource = new TracksResource(manager)

    def "should delegate create track to manager"() {
        when:
            Response result = resource.createTrack(track1)
        then:
            1 * manager.createTrack(track1) >> track2
            result.entity == track2
    }

}