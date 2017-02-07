package pl.jojczykp.cdstore.client.tracks;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import lombok.AllArgsConstructor;
import pl.jojczykp.cdstore.albums.AlbumId;
import pl.jojczykp.cdstore.client.Request;
import pl.jojczykp.cdstore.tracks.Track;

import java.util.List;

import static javax.ws.rs.core.Response.Status.OK;
import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.jojczykp.cdstore.tracks.TracksResource.TRACK_LIST_MEDIA_TYPE;

@AllArgsConstructor(access = PRIVATE)
public class GetTracksRequest extends Request {

	private AlbumId albumId;

	public static GetTracksRequest aGetTracksRequest(AlbumId albumId) {
		return new GetTracksRequest(albumId);
	}

	public List<Track> makeSuccessfully() {
		ClientResponse response = make();
		assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

		return response.getEntity(new GenericType<List<Track>>() {});
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
