package pl.jojczykp.cdstore.client.albums;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import pl.jojczykp.cdstore.albums.Album;
import pl.jojczykp.cdstore.albums.AlbumId;
import pl.jojczykp.cdstore.client.Request;

import static com.sun.jersey.client.urlconnection.URLConnectionClientHandler.PROPERTY_HTTP_URL_CONNECTION_SET_METHOD_WORKAROUND;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.jojczykp.cdstore.albums.Album.anAlbum;
import static pl.jojczykp.cdstore.albums.AlbumsResource.ALBUM_MEDIA_TYPE;

public class UpdateAlbumRequest extends Request {

	private AlbumId id;
	private String title;

	private UpdateAlbumRequest() {
	}

	public static UpdateAlbumRequest anUpdateAlbumRequest() {
		return new UpdateAlbumRequest();
	}

	public UpdateAlbumRequest withId(AlbumId id) {
		this.id = id;
		return this;
	}

	public UpdateAlbumRequest withTitle(String title) {
		this.title = title;
		return this;
	}

	public Album makeSuccessfully() {
		ClientResponse response = make();
		assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

		return response.getEntity(Album.class);
	}

	public ClientResponse make() {
		Client client = Client.create();
		client.getProperties().put(PROPERTY_HTTP_URL_CONNECTION_SET_METHOD_WORKAROUND, true);

		ClientResponse response = client
				.resource(serverUrl).path("albums").path(id.toString())
				.accept(ALBUM_MEDIA_TYPE)
				.type(ALBUM_MEDIA_TYPE)
				.entity(anAlbum()
						.title(title)
						.build())
				.method("PATCH", ClientResponse.class);

		response.bufferEntity();

		return response;
	}
}
