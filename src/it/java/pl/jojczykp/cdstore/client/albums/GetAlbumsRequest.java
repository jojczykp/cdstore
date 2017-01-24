package pl.jojczykp.cdstore.client.albums;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import lombok.AllArgsConstructor;
import pl.jojczykp.cdstore.albums.Album;
import pl.jojczykp.cdstore.client.Request;

import java.util.List;

import static javax.ws.rs.core.Response.Status.OK;
import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.jojczykp.cdstore.albums.AlbumsResource.ALBUM_LIST_MEDIA_TYPE;

@AllArgsConstructor(access = PRIVATE)
public class GetAlbumsRequest extends Request {

	public static GetAlbumsRequest aGetAlbumsRequest() {
		return new GetAlbumsRequest();
	}

	public List<Album> makeSuccessfully() {
		ClientResponse response = make();
		assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

		return response.getEntity(new GenericType<List<Album>>() {});
	}

	public ClientResponse make() {
		Client client = Client.create();

		ClientResponse response = client
				.resource(serverUrl).path("albums")
				.accept(ALBUM_LIST_MEDIA_TYPE)
				.get(ClientResponse.class);

		response.bufferEntity();

		return response;
	}
}
