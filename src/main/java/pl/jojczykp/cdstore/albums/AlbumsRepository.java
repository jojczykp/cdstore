package pl.jojczykp.cdstore.albums;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import pl.jojczykp.cdstore.exceptions.ItemAlreadyExistsException;
import pl.jojczykp.cdstore.exceptions.ItemNotFoundException;

import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.StreamSupport.stream;
import static pl.jojczykp.cdstore.albums.Album.anAlbum;

public class AlbumsRepository {

	private static final String TABLE_NAME = "cdstore-Albums";
	private static final String ATTR_ID = "id";
	private static final String ATTR_TITLE = "title";

	private final Table table;

	public AlbumsRepository(AmazonDynamoDB amazonDynamoDB) {
		//TODO externalize client setup, tests
		//TODO externalize table creation (puppet?)
		DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
		table = Optional.ofNullable(dynamoDB.getTable(TABLE_NAME)).orElse(createTable(dynamoDB));
	}

	private Table createTable(DynamoDB dynamoDB) {
		dynamoDB.createTable(new CreateTableRequest()
				.withTableName(TABLE_NAME)
				.withAttributeDefinitions(
						new AttributeDefinition("id", "S"))
				.withKeySchema(
						new KeySchemaElement("id", KeyType.HASH))
				.withProvisionedThroughput(new ProvisionedThroughput(1L, 1L)));
		return dynamoDB.getTable(TABLE_NAME);
	}

	public Album createAlbum(Album album) {
		UUID id = randomUUID();

		try {
			table.putItem(new PutItemSpec()
					.withItem(new Item()
							.withPrimaryKey(ATTR_ID, id.toString())
							.withString(ATTR_TITLE, album.getTitle()))
					.withConditionExpression("attribute_not_exists(" + ATTR_ID + ")"));

			return album.toBuilder().id(id).build();
		} catch (ConditionalCheckFailedException e) {
			//Should never happen
			throw new ItemAlreadyExistsException("album with given id already exists");
		}
	}

	public Album getAlbum(UUID id) {
		Item item = table.getItem(new GetItemSpec()
				.withPrimaryKey(new PrimaryKey(ATTR_ID, id.toString())));

		if (item == null) {
			throw new ItemNotFoundException("album with given id does not exist");
		}

		return toAlbum(item);
	}

	public Set<Album> getAlbums() {
		Spliterator<Item> items = table.scan().spliterator();
		return stream(items, true)
				.map(this::toAlbum)
				.collect(toSet());
	}

	public Album updateAlbum(UUID id, Album album) {
		try {
			Item item = table.updateItem(new UpdateItemSpec()
					.withPrimaryKey(new PrimaryKey(ATTR_ID, id.toString()))
					.withConditionExpression("attribute_exists(" + ATTR_ID + ")")
					.withUpdateExpression("set " + ATTR_TITLE + " = :" + ATTR_TITLE)
					.withValueMap(new ValueMap()
							.withString(":" + ATTR_TITLE, album.getTitle()))
					.withReturnValues(ReturnValue.ALL_NEW))
					.getItem();

			return toAlbum(item);
		} catch (ConditionalCheckFailedException e) {
			throw new ItemNotFoundException("album with given id not found");
		}
	}

	public void deleteAlbum(UUID id) {
		table.deleteItem(new PrimaryKey(ATTR_ID, id.toString()));
	}

	private Album toAlbum(Item item) {
		return anAlbum()
				.id(UUID.fromString(item.getString(ATTR_ID)))
				.title(item.getString(ATTR_TITLE))
				.build();
	}

}
