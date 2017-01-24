package pl.jojczykp.cdstore.albums

import spock.lang.Specification

import javax.ws.rs.core.Response

import static Album.anAlbum
import static pl.jojczykp.cdstore.albums.AlbumId.randomAlbumId

class AlbumsResourceTest extends Specification {

	AlbumId albumId = randomAlbumId()
	Album album1 = anAlbum().build()
	Album album2 = anAlbum().build()

	AlbumsManager manager = Mock(AlbumsManager)
	AlbumsResource resource = new AlbumsResource(manager)

	def "should delegate create album to manager"() {
		when:
			Response result = resource.createAlbum(album1)
		then:
			1 * manager.createAlbum(album1) >> album2
			result.entity == album2
	}

	def "should delegate get album by id to manager"() {
		when:
			Album result = resource.getAlbum(albumId)
		then:
			1 * manager.getAlbum(albumId) >> album1
			result == album1
	}

	def "should delegate get all albums to manager"() {
		given:
			Set<Album> expectedResult = [album1, album2] as Set
		when:
			Set<Album> result = resource.getAlbums()
		then:
			1 * manager.getAlbums() >> expectedResult
			result == expectedResult
	}

	def "should delegate update album to manager"() {
		when:
			Album result = resource.updateAlbum(albumId, album1)
		then:
			1 * manager.updateAlbum(albumId, album1) >> album2
		result == album2
	}

	def "should delegate delete album to manager"() {
		when:
			resource.deleteAlbum(albumId)
		then:
			1 * manager.deleteAlbum(albumId)
	}

}
