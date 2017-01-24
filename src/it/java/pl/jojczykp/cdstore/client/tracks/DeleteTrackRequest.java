package pl.jojczykp.cdstore.client.tracks;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import pl.jojczykp.cdstore.albums.AlbumId;
import pl.jojczykp.cdstore.client.Request;
import pl.jojczykp.cdstore.tracks.TrackId;

import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static org.assertj.core.api.Assertions.assertThat;

public class DeleteTrackRequest extends Request {

	private AlbumId albumId;
	private TrackId trackId;

	private DeleteTrackRequest() {
	}

	public static DeleteTrackRequest aDeleteTrackRequest() {
		return new DeleteTrackRequest();
	}

	public DeleteTrackRequest withAlbumId(AlbumId albumId) {
		this.albumId = albumId;
		return this;
	}

	public DeleteTrackRequest withTrackId(TrackId trackId) {
		this.trackId = trackId;
		return this;
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
