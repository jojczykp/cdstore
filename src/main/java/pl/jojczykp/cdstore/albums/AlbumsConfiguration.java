package pl.jojczykp.cdstore.albums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

public class AlbumsConfiguration {

	@NotEmpty
	@JsonProperty
	@Getter
	private String dbUrl;

}
