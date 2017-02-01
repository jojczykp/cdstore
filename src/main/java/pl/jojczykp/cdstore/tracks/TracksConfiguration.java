package pl.jojczykp.cdstore.tracks;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

@AllArgsConstructor
public class TracksConfiguration {

	@NotEmpty
	@JsonProperty
	@Getter
	private String zookeeperQuorum;

	@NotEmpty
	@JsonProperty
	@Getter
	private int zookeeperClientPort;

	@NotEmpty
	@JsonProperty
	@Getter
	private String hbaseMaster;

}
