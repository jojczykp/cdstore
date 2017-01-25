package pl.jojczykp.cdstore.albums

import pl.jojczykp.cdstore.tracks.TracksRepository
import spock.lang.Specification

import static Album.anAlbum


class AlbumsManagerTest extends Specification {

	AlbumId albumId = AlbumId.randomAlbumId()
	Album album1 = anAlbum().build()
	Album album2 = anAlbum().build()

	AlbumsRepository albumsRepository = Mock(AlbumsRepository)
	TracksRepository tracksRepository = Mock(TracksRepository)
	AlbumsManager manager = new AlbumsManager(albumsRepository, tracksRepository)

	def "should delegate create album to repository"() {
		when:
			Album result = manager.createAlbum(album1)
		then:
			1 * albumsRepository.createAlbum(album1) >> album2
			result == album2
	}

	def "should delegate get album by id to repository"() {
		when:
			Album result = manager.getAlbum(albumId)
		then:
			1 * albumsRepository.getAlbum(albumId) >> album1
			result == album1
	}

	def "should delegate get albums to repository"() {
		given:
			String maybeTitleSubstring = "in title or null"
			Set<Album> expectedResult = [album1, album2] as Set
		when:
			Set<Album> result = manager.getAlbums(maybeTitleSubstring)
		then:
			1 * albumsRepository.getAlbums(maybeTitleSubstring) >> expectedResult
			result == expectedResult
	}

	def "should delegate update album to repository"() {
		when:
			Album result = manager.updateAlbum(albumId, album1)
		then:
			1 * albumsRepository.updateAlbum(albumId, album1) >> album2
			result == album2
	}

	def "should delegate delete album to repository"() {
		when:
			manager.deleteAlbum(albumId)
		then:
			1 * albumsRepository.deleteAlbum(albumId)
			1 * tracksRepository.deleteAlbumTracks(albumId)
	}

}
