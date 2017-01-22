package pl.jojczykp.cdstore.tracks;

public class TracksManager {

	private TracksRepository repository;

	public TracksManager(TracksRepository repository) {
		this.repository = repository;
	}

	public Track createTrack(Track track) {
		return repository.createTrack(track);
	}

}
