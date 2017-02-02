package pl.jojczykp.cdstore.client.tracks;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import lombok.AllArgsConstructor;
import lombok.experimental.Wither;
import pl.jojczykp.cdstore.albums.AlbumId;
import pl.jojczykp.cdstore.client.Request;
import pl.jojczykp.cdstore.tracks.Track;

import static javax.ws.rs.core.Response.Status.CREATED;
import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.jojczykp.cdstore.tracks.TrackDetails.aTrackDetails;
import static pl.jojczykp.cdstore.tracks.TracksResource.TRACK_MEDIA_TYPE;

@AllArgsConstructor(access = PRIVATE)
public class CreateTrackRequest extends Request {

	private AlbumId albumId;
	@Wither private String title;

	public static CreateTrackRequest aCreateTrackRequest(AlbumId albumId) {
		return new CreateTrackRequest(albumId, null);
	}

	public Track makeSuccessfully() {
		ClientResponse response = make();
		assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());

		return response.getEntity(Track.class);
	}

	public ClientResponse make() {
		Client client = Client.create();

		ClientResponse response = client
				.resource(String.format("%s/albums/%s/tracks", serverUrl, albumId))
				.accept(TRACK_MEDIA_TYPE)
				.type(TRACK_MEDIA_TYPE)
				.entity(aTrackDetails()
						.title(title)
						.build())
				.post(ClientResponse.class);

		response.bufferEntity();

		return response;
	}
}
