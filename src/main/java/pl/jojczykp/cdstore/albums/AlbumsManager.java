package pl.jojczykp.cdstore.albums;

import java.util.Set;
import java.util.UUID;

public class AlbumsManager {

	private AlbumsRepository repository;

	public AlbumsManager(AlbumsRepository repository) {
		this.repository = repository;
	}

	public Album createAlbum(Album album) {
		return repository.createAlbum(album);
	}

	public Album getAlbum(UUID id) {
		return repository.getAlbum(id);
	}

	public Set<Album> getAlbums() {
		return repository.getAlbums();
	}

	public Album updateAlbum(UUID id, Album album) {
		return repository.updateAlbum(id, album);
	}

	public void deleteAlbum(UUID id) {
		repository.deleteAlbum(id);
	}

}
