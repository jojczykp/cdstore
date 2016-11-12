package pl.jojczykp.cdstore.main;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import pl.jojczykp.cdstore.cds.CdConfiguration;

import javax.validation.constraints.NotNull;

public class CdStoreConfiguration extends Configuration {

	@NotNull
	@JsonProperty
	private CdConfiguration cd;

	@JsonProperty
	public CdConfiguration getCd() {
		return cd;
	}

}
