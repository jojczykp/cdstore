package pl.jojczykp.cdstore.client.albums;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import lombok.AllArgsConstructor;
import lombok.experimental.Wither;
import pl.jojczykp.cdstore.albums.Album;
import pl.jojczykp.cdstore.client.Request;

import java.util.List;

import static javax.ws.rs.core.Response.Status.OK;
import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.jojczykp.cdstore.albums.AlbumsResource.ALBUM_LIST_MEDIA_TYPE;

@AllArgsConstructor(access = PRIVATE)
public class GetAlbumsRequest extends Request {

	@Wither private String titleSubstring;

	public static GetAlbumsRequest aGetAlbumsRequest() {
		return new GetAlbumsRequest(null);
	}

	public List<Album> makeSuccessfully() {
		ClientResponse response = make();
		assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

		return response.getEntity(new GenericType<List<Album>>() {});
	}

	public ClientResponse make() {
		Client client = Client.create();

		WebResource resource = client.resource(serverUrl).path("albums");

		if (titleSubstring != null) {
			resource = resource.queryParam("titleSubstring", titleSubstring);
		}

		ClientResponse response = resource
				.accept(ALBUM_LIST_MEDIA_TYPE)
				.get(ClientResponse.class);

		response.bufferEntity();

		return response;
	}
}
