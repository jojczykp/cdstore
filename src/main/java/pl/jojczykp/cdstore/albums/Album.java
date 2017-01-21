package pl.jojczykp.cdstore.albums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true, builderMethodName = "anAlbum")
public class Album {

	@JsonProperty
	private final @Getter UUID id;

	@JsonProperty
	private final @Getter String title;

}