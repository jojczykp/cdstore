package pl.jojczykp.cdstore.albums;

import com.codahale.metrics.health.HealthCheck;

public class AlbumsHealthCheck extends HealthCheck {

	private AlbumsConfiguration configuration;

	public AlbumsHealthCheck(AlbumsConfiguration configuration) {
		this.configuration = configuration;
	}

	public String getName() {
		return "albums";
	}

	@Override
	protected Result check() {
		return Result.healthy();
	}
}
