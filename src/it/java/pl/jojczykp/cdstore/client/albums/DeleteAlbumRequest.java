package pl.jojczykp.cdstore.client.albums;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import lombok.AllArgsConstructor;
import pl.jojczykp.cdstore.albums.AlbumId;
import pl.jojczykp.cdstore.client.Request;

import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;

@AllArgsConstructor(access = PRIVATE)
public class DeleteAlbumRequest extends Request {

	private AlbumId albumId;

	public static DeleteAlbumRequest aDeleteAlbumRequest(AlbumId albumId) {
		return new DeleteAlbumRequest(albumId);
	}

	public void makeSuccessfully() {
		ClientResponse response = make();
		assertThat(response.getStatus()).isEqualTo(NO_CONTENT.getStatusCode());
		assertThat(response.getLength()).isEqualTo(-1);
	}

	public ClientResponse make() {
		Client client = Client.create();

		ClientResponse response = client
				.resource(serverUrl).path("albums").path(albumId.toString())
				.delete(ClientResponse.class);

		response.bufferEntity();

		return response;
	}

}
