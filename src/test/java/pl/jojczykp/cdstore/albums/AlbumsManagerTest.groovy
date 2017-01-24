package pl.jojczykp.cdstore.albums

import spock.lang.Specification

import static Album.anAlbum


class AlbumsManagerTest extends Specification {

	AlbumId albumId = AlbumId.randomAlbumId()
	Album album1 = anAlbum().build()
	Album album2 = anAlbum().build()

	AlbumsRepository repository = Mock(AlbumsRepository)
	AlbumsManager manager = new AlbumsManager(repository)

	def "should delegate create album to repository"() {
		when:
			Album result = manager.createAlbum(album1)
		then:
			1 * repository.createAlbum(album1) >> album2
			result == album2
	}

	def "should delegate get album by id to repository"() {
		when:
			Album result = manager.getAlbum(albumId)
		then:
			1 * repository.getAlbum(albumId) >> album1
			result == album1
	}

	def "should delegate get all albums to repository"() {
		given:
			Set<Album> expectedResult = [album1, album2] as Set
		when:
			Set<Album> result = manager.getAlbums()
		then:
			1 * repository.getAlbums() >> expectedResult
			result == expectedResult
	}

	def "should delegate update album to repository"() {
		when:
			Album result = manager.updateAlbum(albumId, album1)
		then:
			1 * repository.updateAlbum(albumId, album1) >> album2
			result == album2
	}

	def "should delegate delete album to repository"() {
		when:
			manager.deleteAlbum(albumId)
		then:
			1 * repository.deleteAlbum(albumId)
	}

}
