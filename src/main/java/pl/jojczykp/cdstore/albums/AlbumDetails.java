package pl.jojczykp.cdstore.albums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder(builderMethodName = "anAlbumDetails")
public class AlbumDetails {

	@JsonProperty
	private final String title;

}
