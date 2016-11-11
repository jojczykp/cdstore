package pl.jojczykp.cdstore.main;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import pl.jojczykp.cdstore.cd.CdConfiguration;
import pl.jojczykp.cdstore.cd.CdHealthCheck;
import pl.jojczykp.cdstore.cd.CdManager;
import pl.jojczykp.cdstore.cd.CdRepository;
import pl.jojczykp.cdstore.cd.CdResource;

public class CdStoreApplication extends Application<CdStoreConfiguration> {

	public static void main(String... args) throws Exception {
		new CdStoreApplication().run(args);
	}

	@Override
	public String getName() {
		return "cd-store";
	}

	@Override
	public void run(CdStoreConfiguration cdStoreConfiguration, Environment environment) {
		registerCd(cdStoreConfiguration.getCd(), environment);
	}

	private void registerCd(CdConfiguration configuration, Environment environment) {
		CdRepository repository = new CdRepository(configuration.getDbUrl());
		CdManager manager = new CdManager(repository);
		CdResource resource = new CdResource(manager);
		environment.jersey().register(resource);

		CdHealthCheck healthCheck = new CdHealthCheck(configuration);
		environment.healthChecks().register(healthCheck.getName(), healthCheck);
	}
}
