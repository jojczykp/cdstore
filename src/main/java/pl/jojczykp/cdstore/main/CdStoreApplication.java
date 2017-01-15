package pl.jojczykp.cdstore.main;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import pl.jojczykp.cdstore.albums.AlbumsConfiguration;
import pl.jojczykp.cdstore.albums.AlbumsHealthCheck;
import pl.jojczykp.cdstore.albums.AlbumsManager;
import pl.jojczykp.cdstore.albums.AlbumsRepository;
import pl.jojczykp.cdstore.albums.AlbumsResource;
import pl.jojczykp.cdstore.exceptions.ItemAlreadyExistsExceptionMapper;
import pl.jojczykp.cdstore.exceptions.ItemNotFoundExceptionMapper;

public class CdStoreApplication extends Application<CdStoreConfiguration> {

	public static void main(String... args) throws Exception {
		new CdStoreApplication().run(args);
	}

	@Override
	public String getName() {
		return "cdstore";
	}

	@Override
	public void run(CdStoreConfiguration cdStoreConfiguration, Environment environment) {
		registerAlbums(cdStoreConfiguration.getAlbums(), environment);
		registerExceptionsMappers(environment);
	}

	private void registerAlbums(AlbumsConfiguration configuration, Environment environment) {
		AlbumsRepository repository = new AlbumsRepository(configuration.getDbUrl());
		AlbumsManager manager = new AlbumsManager(repository);
		AlbumsResource resource = new AlbumsResource(manager);
		environment.jersey().register(resource);

		AlbumsHealthCheck healthCheck = new AlbumsHealthCheck(configuration);
		environment.healthChecks().register(healthCheck.getName(), healthCheck);
	}

	private void registerExceptionsMappers(Environment environment) {
		environment.jersey().register(new ItemNotFoundExceptionMapper());
		environment.jersey().register(new ItemAlreadyExistsExceptionMapper());
	}
}
