package pl.jojczykp.cdstore.main;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import pl.jojczykp.cdstore.albums.AlbumsConfiguration;
import pl.jojczykp.cdstore.albums.AlbumsHealthCheck;
import pl.jojczykp.cdstore.albums.AlbumsManager;
import pl.jojczykp.cdstore.albums.AlbumsRepository;
import pl.jojczykp.cdstore.albums.AlbumsResource;
import pl.jojczykp.cdstore.exceptions.ItemAlreadyExistsExceptionMapper;
import pl.jojczykp.cdstore.exceptions.ItemNotFoundExceptionMapper;
import pl.jojczykp.cdstore.tracks.TracksManager;
import pl.jojczykp.cdstore.tracks.TracksRepository;
import pl.jojczykp.cdstore.tracks.TracksResource;

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
		AmazonDynamoDB amazonDynamoDB = amazonDynamoDb(cdStoreConfiguration.getAlbums());

		AlbumsRepository albumsRepository = new AlbumsRepository(amazonDynamoDB);
		AlbumsManager albumsManager = new AlbumsManager(albumsRepository);
		AlbumsResource albumsResource = new AlbumsResource(albumsManager);
		environment.jersey().register(albumsResource);

		AlbumsHealthCheck albumsHealthCheck = new AlbumsHealthCheck(albumsRepository);
		environment.healthChecks().register(albumsHealthCheck.getName(), albumsHealthCheck);

		TracksRepository tracksRepository = new TracksRepository();
		TracksManager tracksManager = new TracksManager(albumsRepository, tracksRepository);
		TracksResource tracksResource = new TracksResource(tracksManager);
		environment.jersey().register(tracksResource);

		environment.jersey().register(new ItemNotFoundExceptionMapper());
		environment.jersey().register(new ItemAlreadyExistsExceptionMapper());
	}

	private AmazonDynamoDB amazonDynamoDb(AlbumsConfiguration albumsConfiguration) {
		return new AmazonDynamoDBClient(
				new ProfileCredentialsProvider(albumsConfiguration.getProfile()))
				.withEndpoint(albumsConfiguration.getEndpoint());
	}

}
