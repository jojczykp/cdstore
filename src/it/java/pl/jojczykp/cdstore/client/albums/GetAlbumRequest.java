package pl.jojczykp.cdstore.client.albums;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import pl.jojczykp.cdstore.albums.Album;
import pl.jojczykp.cdstore.client.Request;

import java.util.UUID;

import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.jojczykp.cdstore.albums.AlbumsResource.ALBUM_MEDIA_TYPE;

public class GetAlbumRequest extends Request {

	private UUID id;

	private GetAlbumRequest() {
	}

	public static GetAlbumRequest aGetAlbumRequest() {
		return new GetAlbumRequest();
	}

	public GetAlbumRequest withId(UUID id) {
		this.id = id;
		return this;
	}

	public Album makeSuccessfully() {
		ClientResponse response = make();
		assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

		return response.getEntity(Album.class);
	}

	public ClientResponse make() {
		Client client = Client.create();

		ClientResponse response = client
				.resource(serverUrl).path("albums").path(id.toString())
				.accept(ALBUM_MEDIA_TYPE)
				.get(ClientResponse.class);

		response.bufferEntity();

		return response;
	}
}
