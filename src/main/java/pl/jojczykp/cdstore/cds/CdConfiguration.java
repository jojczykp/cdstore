package pl.jojczykp.cdstore.cds;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

public class CdConfiguration {

	@NotEmpty
	@JsonProperty
	@Getter
	private String dbUrl;

}
