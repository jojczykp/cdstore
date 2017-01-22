package pl.jojczykp.cdstore.albums;

import com.codahale.metrics.health.HealthCheck;
import pl.jojczykp.cdstore.exceptions.ItemNotFoundException;

import java.util.UUID;

public class AlbumsHealthCheck extends HealthCheck {

	private AlbumsRepository repository;

	public AlbumsHealthCheck(AlbumsRepository repository) {
		this.repository = repository;
	}

	public String getName() {
		return "albums";
	}

	@Override
	protected Result check() {
		try {
			repository.getAlbum(UUID.randomUUID());
		} catch (ItemNotFoundException e) {
			/* Ignore - healthy */
		} catch (Exception e) {
			return Result.unhealthy(e);
		}

		return Result.healthy();
	}
}
