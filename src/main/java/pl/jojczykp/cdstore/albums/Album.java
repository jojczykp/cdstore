package pl.jojczykp.cdstore.albums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder(toBuilder = true, builderMethodName = "anAlbum")
public class Album {

	@JsonProperty
	private final AlbumId id;

	@JsonProperty
	private final String title;

}
