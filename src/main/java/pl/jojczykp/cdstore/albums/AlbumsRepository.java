package pl.jojczykp.cdstore.albums;

import pl.jojczykp.cdstore.exceptions.ItemAlreadyExistsException;
import pl.jojczykp.cdstore.exceptions.ItemNotFoundException;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static pl.jojczykp.cdstore.albums.Album.anAlbum;

public class AlbumsRepository {

	private ConcurrentHashMap<UUID, Album> content = new ConcurrentHashMap<>();

	private String dbUrl;

	public AlbumsRepository(String dbUrl) {
		this.dbUrl = dbUrl;

		createSampleContent();
	}

	private void createSampleContent() {
		createSampleAlbum(1);
		createSampleAlbum(2);
		createSampleAlbum(3);
	}

	private void createSampleAlbum(int num) {
		UUID id = new UUID(num, num);
		content.put(id, anAlbum().id(id).title(dbUrl + " " + num).build());
	}

	public Album createAlbum(Album album) {
		UUID id = randomUUID();
		Album newAlbum = album.toBuilder().id(id).build();

		Album previous = content.putIfAbsent(id, newAlbum);

		if (previous != null) {
			throw new ItemAlreadyExistsException("album with given id already exists");
		} else {
			return newAlbum;
		}
	}

	public Album getAlbum(UUID id) {
		Album result = content.get(id);
		if (result == null) {
			throw new ItemNotFoundException("album with given id does not exist");
		}

		return result;
	}

	public List<Album> getAlbums() {
		return content.values().stream().collect(toList());
	}

	public Album updateAlbum(UUID id, Album album) {
		Album newValue = content.computeIfPresent(id, (i, c) -> album.toBuilder().id(id).build());
		if (newValue == null) {
			throw new ItemNotFoundException("album with given id not found");
		} else {
			return newValue;
		}
	}

	public void deleteAlbum(UUID id) {
		content.remove(id);
	}

}
