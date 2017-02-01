package pl.jojczykp.cdstore.client.tracks;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import lombok.AllArgsConstructor;
import lombok.experimental.Wither;
import pl.jojczykp.cdstore.albums.AlbumId;
import pl.jojczykp.cdstore.client.Request;
import pl.jojczykp.cdstore.tracks.Track;
import pl.jojczykp.cdstore.tracks.TrackId;

import static javax.ws.rs.core.Response.Status.OK;
import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.jojczykp.cdstore.tracks.TracksResource.TRACK_MEDIA_TYPE;

@AllArgsConstructor(access = PRIVATE)
public class GetTrackRequest extends Request {

	@Wither private AlbumId albumId;
	@Wither private TrackId trackId;

	public static GetTrackRequest aGetTrackRequest() {
		return new GetTrackRequest(null, null);
	}

	public Track makeSuccessfully() {
		ClientResponse response = make();
		assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

		return response.getEntity(Track.class);
	}

	public ClientResponse make() {
		Client client = Client.create();

		ClientResponse response = client
				.resource(String.format("%s/albums/%s/tracks/%s", serverUrl, albumId, trackId))
				.accept(TRACK_MEDIA_TYPE)
				.get(ClientResponse.class);

		response.bufferEntity();

		return response;
	}
}
