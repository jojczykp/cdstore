package pl.jojczykp.cdstore.tracks;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import pl.jojczykp.cdstore.albums.AlbumId;

@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true, builderMethodName = "aTrack")
public class Track {

	@JsonProperty
	private final @Getter TrackId id;

	@JsonProperty
	private final @Getter AlbumId albumId;

	@JsonProperty
	private final @Getter String title;

}
