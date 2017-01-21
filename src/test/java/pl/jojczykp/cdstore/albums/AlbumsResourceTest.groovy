package pl.jojczykp.cdstore.albums

import spock.lang.Specification

import javax.ws.rs.core.Response

import static Album.anAlbum

class AlbumsResourceTest extends Specification {

	UUID id = new UUID(6, 7)
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
			Album result = resource.getAlbum(id)
		then:
			1 * manager.getAlbum(id) >> album1
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
			Album result = resource.updateAlbum(id, album1)
		then:
			1 * manager.updateAlbum(id, album1) >> album2
		result == album2
	}

	def "should delegate delete album to manager"() {
		when:
			resource.deleteAlbum(id)
		then:
			1 * manager.deleteAlbum(id)
	}

}
