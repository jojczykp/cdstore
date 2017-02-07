package pl.jojczykp.cdstore.client.albums;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import lombok.AllArgsConstructor;
import pl.jojczykp.cdstore.albums.Album;
import pl.jojczykp.cdstore.albums.AlbumId;
import pl.jojczykp.cdstore.client.Request;

import static javax.ws.rs.core.Response.Status.OK;
import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.jojczykp.cdstore.albums.AlbumsResource.ALBUM_MEDIA_TYPE;

@AllArgsConstructor(access = PRIVATE)
public class GetAlbumRequest extends Request {

	private AlbumId albumId;

	public static GetAlbumRequest aGetAlbumRequest(AlbumId albumId) {
		return new GetAlbumRequest(albumId);
	}

	public Album makeSuccessfully() {
		ClientResponse response = make();
		assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

		return response.getEntity(Album.class);
	}

	public ClientResponse make() {
		Client client = Client.create();

		ClientResponse response = client
				.resource(serverUrl).path("albums").path(albumId.toString())
				.accept(ALBUM_MEDIA_TYPE)
				.get(ClientResponse.class);

		response.bufferEntity();

		return response;
	}
}
