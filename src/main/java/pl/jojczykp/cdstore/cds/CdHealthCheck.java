package pl.jojczykp.cdstore.cds;

import com.codahale.metrics.health.HealthCheck;

public class CdHealthCheck extends HealthCheck {

	private CdConfiguration configuration;

	public CdHealthCheck(CdConfiguration configuration) {
		this.configuration = configuration;
	}

	public String getName() {
		return "cds";
	}

	@Override
	protected Result check() {
		return Result.healthy();
	}
}
