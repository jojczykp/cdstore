package pl.jojczykp.cdstore.client.tracks;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import lombok.AllArgsConstructor;
import lombok.experimental.Wither;
import pl.jojczykp.cdstore.albums.AlbumId;
import pl.jojczykp.cdstore.client.Request;
import pl.jojczykp.cdstore.tracks.TrackId;

import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;

@AllArgsConstructor(access = PRIVATE)
public class DeleteTrackRequest extends Request {

	@Wither private AlbumId albumId;
	@Wither private TrackId trackId;

	public static DeleteTrackRequest aDeleteTrackRequest() {
		return new DeleteTrackRequest(null, null);
	}

	public void makeSuccessfully() {
		ClientResponse response = make();
		assertThat(response.getStatus()).isEqualTo(NO_CONTENT.getStatusCode());
		assertThat(response.getLength()).isEqualTo(-1);
	}

	public ClientResponse make() {
		Client client = Client.create();

		ClientResponse response = client
				.resource(String.format("%s/albums/%s/tracks/%s", serverUrl, albumId, trackId))
				.delete(ClientResponse.class);

		response.bufferEntity();

		return response;
	}

}
