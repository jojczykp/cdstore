package pl.jojczykp.cdstore.client.albums;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import pl.jojczykp.cdstore.albums.Album;
import pl.jojczykp.cdstore.albums.AlbumId;
import pl.jojczykp.cdstore.client.Request;

import static javax.ws.rs.core.Response.Status.CREATED;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.jojczykp.cdstore.albums.Album.anAlbum;
import static pl.jojczykp.cdstore.albums.AlbumsResource.ALBUM_MEDIA_TYPE;

public class CreateAlbumRequest extends Request {

	private AlbumId id;
	private String title;

	private CreateAlbumRequest() {
	}

	public static CreateAlbumRequest aCreateAlbumRequest() {
		return new CreateAlbumRequest();
	}

	public CreateAlbumRequest withTitle(String title) {
		this.title = title;
		return this;
	}

	public CreateAlbumRequest withAlbum(Album album) {
		this.id = album.getId();
		this.title = album.getTitle();
		return this;
	}

	public Album makeSuccessfully() {
		ClientResponse response = make();
		assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());

		return response.getEntity(Album.class);
	}

	public ClientResponse make() {
		Client client = Client.create();

		ClientResponse response = client
				.resource(serverUrl).path("albums")
				.accept(ALBUM_MEDIA_TYPE)
				.type(ALBUM_MEDIA_TYPE)
				.entity(anAlbum()
						.id(id)
						.title(title)
						.build())
				.post(ClientResponse.class);

		response.bufferEntity();

		return response;
	}
}
