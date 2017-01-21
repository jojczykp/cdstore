package pl.jojczykp.cdstore.client.albums;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import pl.jojczykp.cdstore.client.Request;

import java.util.UUID;

import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static org.assertj.core.api.Assertions.assertThat;

public class DeleteAlbumRequest extends Request {

	private UUID id;

	private DeleteAlbumRequest() {
	}

	public static DeleteAlbumRequest aDeleteAlbumRequest() {
		return new DeleteAlbumRequest();
	}

	public DeleteAlbumRequest withId(UUID id) {
		this.id = id;
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
				.resource(serverUrl).path("albums").path(id.toString())
				.delete(ClientResponse.class);

		response.bufferEntity();

		return response;
	}

}
