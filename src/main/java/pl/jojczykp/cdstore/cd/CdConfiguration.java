package pl.jojczykp.cdstore.cd;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class CdConfiguration {

	@NotEmpty
	@JsonProperty
	private String dbUrl;

	public String getDbUrl() {
		return dbUrl;
	}
}
