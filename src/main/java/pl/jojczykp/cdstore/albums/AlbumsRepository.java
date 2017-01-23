package pl.jojczykp.cdstore.albums;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import pl.jojczykp.cdstore.exceptions.ItemAlreadyExistsException;
import pl.jojczykp.cdstore.exceptions.ItemNotFoundException;

import java.util.Set;
import java.util.Spliterator;

import static java.util.stream.Collectors.toSet;
import static java.util.stream.StreamSupport.stream;
import static pl.jojczykp.cdstore.albums.Album.anAlbum;
import static pl.jojczykp.cdstore.albums.AlbumId.randomAlbumId;

public class AlbumsRepository {

	private static final String TABLE_NAME = "cdstore-Albums";
	private static final String ATTR_ID = "id";
	private static final String ATTR_TITLE = "title";

	private final Table table;

	public AlbumsRepository(AmazonDynamoDB amazonDynamoDB) {
		table = new DynamoDB(amazonDynamoDB).getTable(TABLE_NAME);
	}

	public Album createAlbum(Album album) {
		AlbumId id = randomAlbumId();

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

	public boolean albumExists(AlbumId id) {
		return (findAlbum(id) != null);
	}

	public Album getAlbum(AlbumId id) {
		Item maybeAlbum = findAlbum(id);

		if (maybeAlbum == null) {
			throw new ItemNotFoundException("album with given id not found");
		}

		return toAlbum(maybeAlbum);
	}

	private Item findAlbum(AlbumId id) {
		return table.getItem(new GetItemSpec()
                    .withPrimaryKey(new PrimaryKey(ATTR_ID, id.toString())));
	}

	public Set<Album> getAlbums() {
		Spliterator<Item> items = table.scan().spliterator();
		return stream(items, true)
				.map(this::toAlbum)
				.collect(toSet());
	}

	public Album updateAlbum(AlbumId id, Album album) {
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

	public void deleteAlbum(AlbumId id) {
		try {
			table.deleteItem(new DeleteItemSpec()
					.withPrimaryKey(new PrimaryKey(ATTR_ID, id.toString()))
					.withConditionExpression("attribute_exists(" + ATTR_ID + ")")
					.withReturnValues(ReturnValue.NONE));
		} catch (ConditionalCheckFailedException e) {
			throw new ItemNotFoundException("album with given id not found");
		}

	}

	private Album toAlbum(Item item) {
		return anAlbum()
				.id(AlbumId.fromString(item.getString(ATTR_ID)))
				.title(item.getString(ATTR_TITLE))
				.build();
	}

}
