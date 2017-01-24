package pl.jojczykp.cdstore.client.tracks;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import pl.jojczykp.cdstore.albums.AlbumId;
import pl.jojczykp.cdstore.client.Request;
import pl.jojczykp.cdstore.tracks.Track;

import java.util.List;

import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.jojczykp.cdstore.tracks.TracksResource.TRACK_LIST_MEDIA_TYPE;

public class GetTracksRequest extends Request {

	private AlbumId albumId;

	private GetTracksRequest() {
	}

	public static GetTracksRequest aGetTracksRequest() {
		return new GetTracksRequest();
	}

	public List<Track> makeSuccessfully() {
		ClientResponse response = make();
		assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

		return response.getEntity(new GenericType<List<Track>>() {});
	}

	public GetTracksRequest withAlbumId(AlbumId albumId) {
		this.albumId = albumId;
		return this;
	}

	public ClientResponse make() {
		Client client = Client.create();

		ClientResponse response = client
				.resource(String.format("%s/albums/%s/tracks", serverUrl, albumId))
				.accept(TRACK_LIST_MEDIA_TYPE)
				.get(ClientResponse.class);

		response.bufferEntity();

		return response;
	}

}
