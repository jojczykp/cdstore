package pl.jojczykp.cdstore.albums

import pl.jojczykp.cdstore.exceptions.ItemNotFoundException
import spock.lang.Specification

import static java.util.UUID.randomUUID
import static Album.anAlbum


class AlbumsRepositoryTest extends Specification {

	String dbUrl = "s3://com.abc/db"
	AlbumsRepository repository = new AlbumsRepository(dbUrl)

	def "should create album"() {
		given:
			Album newAlbum = anAlbum().title("A Title").build()
		when:
			Album createdAlbum = repository.createAlbum(newAlbum)
		then:
			createdAlbum == newAlbum.toBuilder().id(createdAlbum.getId()).build()
			// TODO assert call to DB made once db connection implemented
	}

	def "should get album by id"() {
		given:
			UUID id = new UUID(1, 1)
		when:
			Album album = repository.getAlbum(id)
		then:
			album.id == id
			album.title == dbUrl + " 1"
	}

	def "should fail on get album by non-existing id"() {
		given:
			UUID notExistingId = new UUID(9, 9)
		when:
			repository.getAlbum(notExistingId)
		then:
			ItemNotFoundException ex = thrown()
			ex.message == "album with given id does not exist"
	}

	def "should get all albums"() {
		when:
			List<Album> albums = repository.getAlbums()
		then:
			albums == [
					new Album(new UUID(1, 1), dbUrl + " 1"),
					new Album(new UUID(2, 2), dbUrl + " 2"),
					new Album(new UUID(3, 3), dbUrl + " 3")
			]
	}

	def "should update album"() {
		given:
			Album originalAlbum = repository.createAlbum(anAlbum().id(new UUID(1, 1)).title("Old Title").build())
			Album patch = anAlbum().title("New Title").build()
			Album expectedAlbum = originalAlbum.toBuilder().title(patch.getTitle()).build()
		when:
			Album updatedAlbum = repository.updateAlbum(originalAlbum.getId(), patch)
		then:
			updatedAlbum == expectedAlbum
			// TODO assert call to DB made once db connection implemented
	}

	def "should fail on update album if it does not exists"() {
		given:
			UUID id = randomUUID()
			Album patch = anAlbum().title("New Title").build()
		when:
			repository.updateAlbum(id, patch)
		then:
			ItemNotFoundException ex = thrown()
			ex.message == "album with given id not found"
	}

	def "should delete album"() {
		given:
			UUID id = randomUUID()
			Album album = anAlbum().id(id).title("A Title").build()
			repository.createAlbum(album)
		when:
			repository.deleteAlbum(id)
		then:
			true
	}

	def "should succeed on deleting not existing album"() {
		given:
			UUID id = randomUUID()
		when:
			repository.deleteAlbum(id)
		then:
			true
	}

}
