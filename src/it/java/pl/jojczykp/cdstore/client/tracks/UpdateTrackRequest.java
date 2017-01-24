package pl.jojczykp.cdstore.client.tracks;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import pl.jojczykp.cdstore.albums.AlbumId;
import pl.jojczykp.cdstore.client.Request;
import pl.jojczykp.cdstore.tracks.Track;
import pl.jojczykp.cdstore.tracks.TrackId;

import static com.sun.jersey.client.urlconnection.URLConnectionClientHandler.PROPERTY_HTTP_URL_CONNECTION_SET_METHOD_WORKAROUND;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.jojczykp.cdstore.tracks.Track.aTrack;
import static pl.jojczykp.cdstore.tracks.TracksResource.TRACK_MEDIA_TYPE;

public class UpdateTrackRequest extends Request {

	private AlbumId albumId;
	private TrackId trackId;
	private String title;

	private UpdateTrackRequest() {
	}

	public static UpdateTrackRequest anUpdateTrackRequest() {
		return new UpdateTrackRequest();
	}

	public UpdateTrackRequest withAlbumId(AlbumId albumId) {
		this.albumId = albumId;
		return this;
	}

	public UpdateTrackRequest withTrackId(TrackId trackId) {
		this.trackId = trackId;
		return this;
	}

	public UpdateTrackRequest withTitle(String title) {
		this.title = title;
		return this;
	}

	public Track makeSuccessfully() {
		ClientResponse response = make();
		assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

		return response.getEntity(Track.class);
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
