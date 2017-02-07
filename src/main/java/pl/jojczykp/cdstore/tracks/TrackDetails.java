package pl.jojczykp.cdstore.tracks;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
@Builder(builderMethodName = "aTrackDetails")
public class TrackDetails {

	@JsonProperty
	private final String title;

}
