package pl.jojczykp.cdstore.albums

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement
import com.amazonaws.services.dynamodbv2.model.KeyType
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput
import pl.jojczykp.cdstore.exceptions.ItemNotFoundException
import spock.lang.Specification

import static Album.anAlbum
import static java.util.UUID.randomUUID
import static pl.jojczykp.cdstore.albums.AlbumsRepository.ATTR_ID
import static pl.jojczykp.cdstore.albums.AlbumsRepository.TABLE_NAME

class AlbumsRepositoryTest extends Specification {

	AmazonDynamoDB dynamoDB
	AlbumsRepository repository

	def setup() {
		System.setProperty("sqlite4java.library.path", "target/dynamodb/DynamoDBLocal_lib")
		dynamoDB = DynamoDBEmbedded.create()
		createTable(dynamoDB)
		repository = new AlbumsRepository(dynamoDB)
	}

	def createTable(AmazonDynamoDB dynamoDB) {
		dynamoDB.createTable(new CreateTableRequest()
				.withTableName(TABLE_NAME)
				.withAttributeDefinitions(new AttributeDefinition(ATTR_ID, "S"))
				.withKeySchema(new KeySchemaElement(ATTR_ID, KeyType.HASH))
				.withProvisionedThroughput(new ProvisionedThroughput(1L, 1L)))
	}

	def cleanup() {
		dynamoDB?.shutdown()
	}

	def "should create album"() {
		given:
			Album newAlbum = anAlbum().title("A Title").build()
		when:
			Album createdAlbum = repository.createAlbum(newAlbum)
		then:
			createdAlbum == dbGetAlbum(createdAlbum.getId())
			createdAlbum == newAlbum.toBuilder().id(createdAlbum.getId()).build()
	}

	def "should get album by id"() {
		given:
			UUID id = randomUUID()
			String title = "Some Title"
			dbPutAlbum(id, title)
		when:
			Album album = repository.getAlbum(id)
		then:
			album.id == id
			album.title == title
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
		given:
			Set<Album> createdAlbums = [
					new Album(new UUID(1, 1), "Title 1"),
					new Album(new UUID(2, 2), "Title 2"),
					new Album(new UUID(3, 3), "Title 3")
			]
			createdAlbums.each { dbPutAlbum(it.id, it.title) }
		when:
			Set<Album> returnedAlbums = repository.getAlbums()
		then:
			returnedAlbums == createdAlbums
	}

	def "should update album"() {
		given:
			UUID id = randomUUID()
			Album patch = anAlbum().title("New Title").build()
			Album expectedAlbum = anAlbum().id(id).title(patch.getTitle()).build()
			dbPutAlbum(id, "Old Title")
		when:
			Album updatedAlbum = repository.updateAlbum(id, patch)
		then:
			dbGetAlbum(id) == expectedAlbum
			updatedAlbum == expectedAlbum
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
			dbPutAlbum(id, "A Title")
		when:
			repository.deleteAlbum(id)
		then:
			dbGetAlbum(id) == null
	}

	def "should succeed on deleting not existing album"() {
		given:
			UUID id = randomUUID()
		when:
			repository.deleteAlbum(id)
		then:
			true
	}

	private void dbPutAlbum(UUID id, String title) {
		dynamoDB.putItem("cdstore-Albums", [
				"id"   : new AttributeValue(id.toString()),
				"title": new AttributeValue(title)
		])
	}

	private Album dbGetAlbum(UUID id) {
		def item = dynamoDB.getItem("cdstore-Albums", [
				"id": new AttributeValue(id.toString())
		]).item

		return (item == null) ? null : toItem(item)
	}

	private static Album toItem(Map<String, AttributeValue> item) {
		anAlbum()
				.id(UUID.fromString(item.get("id").getS()))
				.title(item.get("title").getS())
				.build()
	}

}
