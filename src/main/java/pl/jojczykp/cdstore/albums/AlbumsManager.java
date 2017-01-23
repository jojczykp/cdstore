package pl.jojczykp.cdstore.albums;

import java.util.Set;

public class AlbumsManager {

	private AlbumsRepository repository;

	public AlbumsManager(AlbumsRepository repository) {
		this.repository = repository;
	}

	public Album createAlbum(Album album) {
		return repository.createAlbum(album);
	}

	public Album getAlbum(AlbumId id) {
		return repository.getAlbum(id);
	}

	public Set<Album> getAlbums() {
		return repository.getAlbums();
	}

	public Album updateAlbum(AlbumId id, Album album) {
		return repository.updateAlbum(id, album);
	}

	public void deleteAlbum(AlbumId id) {
		repository.deleteAlbum(id);
	}

}
