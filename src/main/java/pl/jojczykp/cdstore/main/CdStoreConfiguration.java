package pl.jojczykp.cdstore.main;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import lombok.Getter;
import pl.jojczykp.cdstore.albums.AlbumsConfiguration;
import pl.jojczykp.cdstore.tracks.TracksConfiguration;

import javax.validation.constraints.NotNull;

public class CdStoreConfiguration extends Configuration {

	@NotNull
	@JsonProperty
	@Getter
	private SwaggerBundleConfiguration swagger;

	@NotNull
	@JsonProperty
	@Getter
	private AlbumsConfiguration albums;

	@NotNull
	@JsonProperty
	@Getter
	private TracksConfiguration tracks;

}
