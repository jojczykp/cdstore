package pl.jojczykp.cdstore.main;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import pl.jojczykp.cdstore.cds.CdConfiguration;
import pl.jojczykp.cdstore.cds.CdHealthCheck;
import pl.jojczykp.cdstore.cds.CdManager;
import pl.jojczykp.cdstore.cds.CdRepository;
import pl.jojczykp.cdstore.cds.CdResource;
import pl.jojczykp.cdstore.exceptions.EntityAlreadyExistsExceptionMapper;
import pl.jojczykp.cdstore.exceptions.EntityNotFoundExceptionMapper;

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
		registerExceptionsMappers(environment);
	}

	private void registerCd(CdConfiguration configuration, Environment environment) {
		CdRepository repository = new CdRepository(configuration.getDbUrl());
		CdManager manager = new CdManager(repository);
		CdResource resource = new CdResource(manager);
		environment.jersey().register(resource);

		CdHealthCheck healthCheck = new CdHealthCheck(configuration);
		environment.healthChecks().register(healthCheck.getName(), healthCheck);
	}

	private void registerExceptionsMappers(Environment environment) {
		environment.jersey().register(new EntityNotFoundExceptionMapper());
		environment.jersey().register(new EntityAlreadyExistsExceptionMapper());
	}
}
