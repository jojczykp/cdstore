package pl.jojczykp.cdstore.tracks;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import pl.jojczykp.cdstore.albums.AlbumId;

@Data
@RequiredArgsConstructor
@Builder(toBuilder = true, builderMethodName = "aTrack")
public class Track {

	@JsonProperty
	private final TrackId id;

	@JsonProperty
	private final AlbumId albumId;

	@JsonProperty
	private final String title;

}
