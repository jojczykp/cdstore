package pl.jojczykp.cdstore.albums;

import java.util.Set;

public class AlbumsManager {

	private final AlbumsRepository repository;

	public AlbumsManager(AlbumsRepository repository) {
		this.repository = repository;
	}

	public Album createAlbum(Album album) {
		return repository.createAlbum(album);
	}

	public Album getAlbum(AlbumId albumId) {
		return repository.getAlbum(albumId);
	}

	public Set<Album> getAlbums() {
		return repository.getAlbums();
	}

	public Album updateAlbum(AlbumId albumId, Album patch) {
		return repository.updateAlbum(albumId, patch);
	}

	public void deleteAlbum(AlbumId albumId) {
		repository.deleteAlbum(albumId);
	}

}
