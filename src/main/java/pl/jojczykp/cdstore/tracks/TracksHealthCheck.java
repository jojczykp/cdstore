package pl.jojczykp.cdstore.tracks;

import com.codahale.metrics.health.HealthCheck;
import pl.jojczykp.cdstore.exceptions.ItemNotFoundException;

public class TracksHealthCheck extends HealthCheck {

	private final TracksRepository repository;

	public TracksHealthCheck(TracksRepository repository) {
		this.repository = repository;
	}

	public String getName() {
		return "tracks";
	}

	@Override
	protected Result check() {
		try {
			repository.getTrack(TrackId.randomTrackId());
		} catch (ItemNotFoundException e) {
			/* Ignore - healthy */
		} catch (Exception e) {
			return Result.unhealthy(e);
		}

		return Result.healthy();
	}
}
