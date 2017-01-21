package pl.jojczykp.cdstore.main;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import lombok.Getter;
import pl.jojczykp.cdstore.albums.AlbumsConfiguration;

import javax.validation.constraints.NotNull;

public class CdStoreConfiguration extends Configuration {

	@NotNull
	@JsonProperty
	@Getter
	private AlbumsConfiguration albums;

}
