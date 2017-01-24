package pl.jojczykp.cdstore.tracks

import pl.jojczykp.cdstore.albums.Album
import pl.jojczykp.cdstore.albums.AlbumsRepository
import pl.jojczykp.cdstore.exceptions.ItemNotFoundException
import spock.lang.Specification

import static pl.jojczykp.cdstore.albums.Album.anAlbum
import static pl.jojczykp.cdstore.albums.AlbumId.randomAlbumId
import static pl.jojczykp.cdstore.tracks.Track.aTrack

class TracksManagerTest extends Specification {

    Album album = anAlbum().id(randomAlbumId()).build()
    Track track1 = aTrack().albumId(album.id).build()
    Track track2 = aTrack().albumId(album.id).build()

    AlbumsRepository albumsRepository = Mock(AlbumsRepository)
    TracksRepository tracksRepository = Mock(TracksRepository)
    TracksManager manager = new TracksManager(albumsRepository, tracksRepository)

    def "should delegate create track to repository"() {
        when:
            Track result = manager.createTrack(track1)
        then:
            1 * albumsRepository.albumExists(album.id) >> true
            1 * tracksRepository.createTrack(track1) >> track2
            result == track2
    }

    def "should throw exception creating track for not existing album"() {
        when:
            manager.createTrack(track1)
        then:
            1 * albumsRepository.albumExists(album.id) >> false
            0 * tracksRepository.createTrack(track1)
            ItemNotFoundException ex = thrown()
            ex.message == "album with given id not found"
    }

    def "should delegate delete album to repository"() {
        when:
            manager.deleteTrack(album.id, track1.id)
        then:
            1 * albumsRepository.albumExists(album.id) >> true
            1 * tracksRepository.deleteTrack(track1.id)
    }

    def "should throw exception deleting track from not existing album"() {
        when:
            manager.deleteTrack(album.id, track1.id)
        then:
            1 * albumsRepository.albumExists(album.id) >> false
            0 * tracksRepository.deleteTrack(track1.id)
            ItemNotFoundException ex = thrown()
            ex.message == "album with given id not found"
    }

}
