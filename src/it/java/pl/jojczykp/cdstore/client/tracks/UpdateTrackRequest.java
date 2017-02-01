package pl.jojczykp.cdstore.client.tracks;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import lombok.AllArgsConstructor;
import lombok.experimental.Wither;
import pl.jojczykp.cdstore.albums.AlbumId;
import pl.jojczykp.cdstore.client.Request;
import pl.jojczykp.cdstore.tracks.TrackId;

import static com.sun.jersey.client.urlconnection.URLConnectionClientHandler.PROPERTY_HTTP_URL_CONNECTION_SET_METHOD_WORKAROUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.jojczykp.cdstore.tracks.Track.aTrack;
import static pl.jojczykp.cdstore.tracks.TracksResource.TRACK_MEDIA_TYPE;

@AllArgsConstructor(access = PRIVATE)
public class UpdateTrackRequest extends Request {

	private AlbumId albumId;
	private TrackId trackId;
	@Wither private String title;

	public static UpdateTrackRequest anUpdateTrackRequest(AlbumId albumId, TrackId trackId) {
		return new UpdateTrackRequest(albumId, trackId, null);
	}

	public void makeSuccessfully() {
		ClientResponse response = make();
		assertThat(response.getStatus()).isEqualTo(NO_CONTENT.getStatusCode());
	}

	public ClientResponse make() {
		Client client = Client.create();
		client.getProperties().put(PROPERTY_HTTP_URL_CONNECTION_SET_METHOD_WORKAROUND, true);

		ClientResponse response = client
				.resource(String.format("%s/albums/%s/tracks/%s", serverUrl, albumId, trackId))
				.accept(TRACK_MEDIA_TYPE)
				.type(TRACK_MEDIA_TYPE)
				.entity(aTrack()
						.title(title)
						.build())
				.method("PATCH", ClientResponse.class);

		response.bufferEntity();

		return response;
	}
}
