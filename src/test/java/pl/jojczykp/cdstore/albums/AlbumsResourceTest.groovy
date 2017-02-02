package pl.jojczykp.cdstore.albums

import spock.lang.Specification

import javax.ws.rs.core.Response

import static Album.anAlbum
import static pl.jojczykp.cdstore.albums.AlbumDetails.anAlbumDetails
import static pl.jojczykp.cdstore.albums.AlbumId.randomAlbumId

class AlbumsResourceTest extends Specification {

	AlbumId albumId = randomAlbumId()
	AlbumDetails albumDetails = anAlbumDetails().build()
	Album album = anAlbum().build()

	AlbumsManager manager = Mock(AlbumsManager)
	AlbumsResource resource = new AlbumsResource(manager)

	def "should delegate create album to manager"() {
		when:
			Response result = resource.createAlbum(albumDetails)
		then:
			1 * manager.createAlbum(Album.from(albumDetails)) >> album
			result.entity == album
	}

	def "should delegate get album by id to manager"() {
		when:
			Album result = resource.getAlbum(albumId)
		then:
			1 * manager.getAlbum(albumId) >> album
			result == album
	}

	def "should delegate get albums to manager"() {
		given:
			String maybeTitleSubstring = "in title on null"
			Set<Album> expectedResult = [album] as Set
		when:
			Set<Album> result = resource.getAlbums(maybeTitleSubstring)
		then:
			1 * manager.getAlbums(maybeTitleSubstring) >> expectedResult
			result == expectedResult
	}

	def "should delegate update album to manager"() {
		when:
			Album result = resource.updateAlbum(albumId, albumDetails)
		then:
			1 * manager.updateAlbum(Album.from(albumId, albumDetails)) >> album
			result == album
	}

	def "should delegate delete album to manager"() {
		when:
			resource.deleteAlbum(albumId)
		then:
			1 * manager.deleteAlbum(albumId)
	}

}
