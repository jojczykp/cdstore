package pl.jojczykp.cdstore.albums;

import pl.jojczykp.cdstore.tracks.TracksRepository;

import java.util.Set;

public class AlbumsManager {

	private final AlbumsRepository albumsRepository;
	private final TracksRepository tracksRepository;

	public AlbumsManager(AlbumsRepository albumsRepository, TracksRepository tracksRepository) {
		this.albumsRepository = albumsRepository;
		this.tracksRepository = tracksRepository;
	}

	public Album createAlbum(Album album) {
		return albumsRepository.createAlbum(album);
	}

	public Album getAlbum(AlbumId albumId) {
		return albumsRepository.getAlbum(albumId);
	}

	public Set<Album> getAlbums(String maybeTitleSubstring) {
		return albumsRepository.getAlbums(maybeTitleSubstring);
	}

	public Album updateAlbum(AlbumId albumId, Album patch) {
		return albumsRepository.updateAlbum(albumId, patch);
	}

	public void deleteAlbum(AlbumId albumId) {
		albumsRepository.deleteAlbum(albumId);
		tracksRepository.deleteAlbumTracks(albumId);
	}

}
