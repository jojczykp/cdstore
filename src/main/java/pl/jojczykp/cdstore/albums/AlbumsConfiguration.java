package pl.jojczykp.cdstore.albums;

import com.amazonaws.regions.Regions;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

public class AlbumsConfiguration {

	@NotEmpty
	@JsonProperty
	@Getter
	private Regions region;

	@NotEmpty
	@JsonProperty
	@Getter
	private String profile;

	@JsonProperty
	@Getter
	private String endpoint;

}
