package pl.jojczykp.cdstore.tracks

import pl.jojczykp.cdstore.exceptions.ItemNotFoundException
import spock.lang.Specification

import static pl.jojczykp.cdstore.albums.AlbumId.randomAlbumId
import static pl.jojczykp.cdstore.tracks.Track.aTrack
import static pl.jojczykp.cdstore.tracks.TrackId.randomTrackId

class TracksRepositoryTest extends Specification {

    TracksRepository repository = new TracksRepository()

    def "should create track"() {
        given:
            Track newTrack = aTrack()
                    .id(randomTrackId())
                    .albumId(randomAlbumId())
                    .title("A Title")
                    .build()
        when:
            Track createdTrack = repository.createTrack(newTrack)
        then:
            createdTrack == dbGetTrack(createdTrack.getId())
            createdTrack == newTrack.toBuilder().id(createdTrack.getId()).build()
    }

    def "should delete track"() {
        given:
            TrackId id = randomTrackId()
            id = dbPutTrack(id, "Track Title").getId() //TODO simplify once repository implemented
        when:
            repository.deleteTrack(id)
        then:
            dbGetTrack(id) == null
    }

    def "should fail deleting not existing track"() {
        given:
            TrackId id = randomTrackId()
        when:
            repository.deleteTrack(id)
        then:
            ItemNotFoundException ex = thrown()
            ex.message == "track with given id not found"
    }

    Track dbPutTrack(TrackId id, String title) {
        return repository.createTrack(aTrack().title(title).build())
    }

    Track dbGetTrack(TrackId id) {
        return repository.getTrack(id)
    }

}
