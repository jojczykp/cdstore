package pl.jojczykp.cdstore.albums

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput
import groovy.json.JsonSlurper
import pl.jojczykp.cdstore.exceptions.ItemNotFoundException
import spock.lang.Specification

import static Album.anAlbum
import static pl.jojczykp.cdstore.albums.AlbumId.randomAlbumId

class AlbumsRepositoryTest extends Specification {

	AmazonDynamoDB dynamoDB
	AlbumsRepository repository

	def setup() {
		System.setProperty("sqlite4java.library.path", "target/dynamodb/DynamoDBLocal_lib")
		dynamoDB = DynamoDBEmbedded.create()
		createTable(dynamoDB, "dynamodb/cdstore-Albums.json")
		repository = new AlbumsRepository(dynamoDB)
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

	def "should confirm album exists by id"() {
		given:
			AlbumId albumId = randomAlbumId()
			dbPutAlbum(albumId, "Some Title")
		when:
			boolean exists = repository.albumExists(albumId)
		then:
			exists
	}

	def "should confirm album does not exist by id"() {
		given:
			AlbumId notExistingId = randomAlbumId()
		when:
			boolean exists = repository.albumExists(notExistingId)
		then:
			!exists
	}

	def "should get album by id"() {
		given:
			AlbumId albumId = randomAlbumId()
			String title = "Some Title"
			dbPutAlbum(albumId, title)
		when:
			Album album = repository.getAlbum(albumId)
		then:
			album.id == albumId
			album.title == title
	}

	def "should fail on get album by non-existing id"() {
		given:
			AlbumId notExistingId = randomAlbumId()
		when:
			repository.getAlbum(notExistingId)
		then:
			ItemNotFoundException ex = thrown()
			ex.message == "album with given id not found"
	}

	def "should get all albums"() {
		given:
			Set<Album> createdAlbums = [
					new Album(randomAlbumId(), "Title 1"),
					new Album(randomAlbumId(), "Title 2"),
					new Album(randomAlbumId(), "Title 3")
			]
			createdAlbums.each { dbPutAlbum(it.id, it.title) }
		when:
			Set<Album> returnedAlbums = repository.getAlbums()
		then:
			returnedAlbums == createdAlbums
	}

	def "should update album"() {
		given:
			AlbumId albumId = randomAlbumId()
			Album patch = anAlbum().title("New Title").build()
			Album expectedAlbum = anAlbum().id(albumId).title(patch.getTitle()).build()
			dbPutAlbum(albumId, "Old Title")
		when:
			Album updatedAlbum = repository.updateAlbum(albumId, patch)
		then:
			dbGetAlbum(albumId) == expectedAlbum
			updatedAlbum == expectedAlbum
	}

	def "should fail on update album if it does not exists"() {
		given:
			AlbumId albumId = randomAlbumId()
			Album patch = anAlbum().title("New Title").build()
		when:
			repository.updateAlbum(albumId, patch)
		then:
			ItemNotFoundException ex = thrown()
			ex.message == "album with given id not found"
	}

	def "should delete album"() {
		given:
			AlbumId albumId = randomAlbumId()
			dbPutAlbum(albumId, "A Title")
		when:
			repository.deleteAlbum(albumId)
		then:
			dbGetAlbum(albumId) == null
	}

	def "should fail on deleting not existing album"() {
		given:
			AlbumId albumId = randomAlbumId()
		when:
			repository.deleteAlbum(albumId)
		then:
			ItemNotFoundException ex = thrown()
			ex.message == "album with given id not found"
	}

	def dbPutAlbum(AlbumId albumId, String title) {
		dynamoDB.putItem("cdstore-Albums", [
				"id"   : new AttributeValue(albumId.toString()),
				"title": new AttributeValue(title)
		])
	}

	def dbGetAlbum(AlbumId id) {
		def item = dynamoDB.getItem("cdstore-Albums", [
				"id": new AttributeValue(id.toString())
		]).item

		item == null ? null : toItem(item)
	}

	private static Album toItem(Map<String, AttributeValue> item) {
		anAlbum()
				.id(AlbumId.fromString(item.get("id").getS()))
				.title(item.get("title").getS())
				.build()
	}

	def createTable(AmazonDynamoDB dynamoDB, String jsonTxt) {
		def json = Thread.currentThread().getContextClassLoader().getResource(jsonTxt).text
		def root = new JsonSlurper().parseText(json)

		dynamoDB.createTable(
				new CreateTableRequest()
						.withTableName(root.TableName)
						.withAttributeDefinitions(
						root.AttributeDefinitions.collect {
							new AttributeDefinition(it.AttributeName, it.AttributeType as String)
						})
						.withKeySchema(
						root.KeySchema.collect {
							new KeySchemaElement(it.AttributeName, it.KeyType as String)
						})
						.withProvisionedThroughput(
						new ProvisionedThroughput(
								root.ProvisionedThroughput.ReadCapacityUnits,
								root.ProvisionedThroughput.WriteCapacityUnits)))
	}

}
