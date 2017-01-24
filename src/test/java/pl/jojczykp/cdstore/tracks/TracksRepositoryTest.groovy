package pl.jojczykp.cdstore.tracks

import pl.jojczykp.cdstore.albums.AlbumId
import pl.jojczykp.cdstore.exceptions.ItemNotFoundException
import spock.lang.Specification

import static pl.jojczykp.cdstore.albums.AlbumId.randomAlbumId
import static pl.jojczykp.cdstore.tracks.Track.aTrack
import static pl.jojczykp.cdstore.tracks.TrackId.randomTrackId

class TracksRepositoryTest extends Specification {

    TrackId trackId = randomTrackId()
    AlbumId albumId = randomAlbumId()

    TracksRepository repository = new TracksRepository()

    def "should create track"() {
        given:
            Track newTrack = aTrack()
                    .id(trackId)
                    .albumId(albumId)
                    .title("A Title")
                    .build()
        when:
            Track createdTrack = repository.createTrack(newTrack)
        then:
            createdTrack == dbGetTrack(createdTrack.getId())
            createdTrack == newTrack.toBuilder().id(createdTrack.getId()).build()
    }

    def "should get track"() {
        given:
            String title = "Track Title"
            trackId = dbPutTrack(trackId, albumId, title).getId() //TODO simplify once repository implemented
        when:
            Track track = repository.getTrack(trackId)
        then:
            track.id == trackId
            track.albumId == albumId
            track.title == title
    }

    def "should fail getting not existing track"() {
        when:
            repository.getTrack(trackId)
        then:
            ItemNotFoundException ex = thrown()
            ex.message == "track with given id not found"
    }

    def "should delete track"() {
        given:
            trackId = dbPutTrack(trackId, albumId, "Track Title").getId() //TODO simplify once repository implemented
        when:
            repository.deleteTrack(trackId)
        then:
            dbGetTrack(trackId) == null
    }

    def "should fail deleting not existing track"() {
        when:
            repository.deleteTrack(trackId)
        then:
            ItemNotFoundException ex = thrown()
            ex.message == "track with given id not found"
    }

    def dbPutTrack(TrackId trackId, AlbumId albumId, String title) {
        repository.createTrack(aTrack().albumId(albumId).title(title).build())
    }

    def dbGetTrack(TrackId trackId) {
        try {
            repository.getTrack(trackId)
        } catch (ItemNotFoundException e) {
            null
        }
    }

}
