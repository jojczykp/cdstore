package pl.jojczykp.cdstore.tracks

import spock.lang.Specification

import static pl.jojczykp.cdstore.albums.AlbumId.randomAlbumId
import static pl.jojczykp.cdstore.tracks.Track.aTrack
import static pl.jojczykp.cdstore.tracks.TrackId.randomTrackId

class TracksRepositoryTest extends Specification {

    TracksRepository repository = new TracksRepository()

    def "should create track"() {
        given:
            Track newTrack = aTrack().id(randomTrackId()).albumId(randomAlbumId()).title("A Title").build()
        when:
            Track createdTrack = repository.createTrack(newTrack)
        then:
            createdTrack == dbGetTrack(createdTrack.getId())
            createdTrack == newTrack.toBuilder().id(createdTrack.getId()).build()
    }

    Track dbGetTrack(TrackId id) {
        return repository.getTrack(id)
    }

}
