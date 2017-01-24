package pl.jojczykp.cdstore.tracks;

import pl.jojczykp.cdstore.albums.AlbumId;
import pl.jojczykp.cdstore.albums.AlbumsRepository;
import pl.jojczykp.cdstore.exceptions.ItemNotFoundException;

public class TracksManager {

	private final AlbumsRepository albumsRepository;
	private final TracksRepository tracksRepository;

	public TracksManager(AlbumsRepository albumsRepository, TracksRepository tracksRepository) {
		this.albumsRepository = albumsRepository;
		this.tracksRepository = tracksRepository;
	}

	public Track createTrack(Track track) {
		confirmAlbumExistsOrThrow(track.getAlbumId());
		return tracksRepository.createTrack(track);
	}

	public Track getTrack(AlbumId albumId, TrackId trackId) {
		confirmAlbumExistsOrThrow(albumId);
		return tracksRepository.getTrack(trackId);
	}

	public void deleteTrack(AlbumId albumId, TrackId trackId) {
		confirmAlbumExistsOrThrow(albumId);
		tracksRepository.deleteTrack(trackId);
	}

	private void confirmAlbumExistsOrThrow(AlbumId albumId) {
		if (!albumsRepository.albumExists(albumId)) {
			throw new ItemNotFoundException("album with given id not found");
		}
	}

}
