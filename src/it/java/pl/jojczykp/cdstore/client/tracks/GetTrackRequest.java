package pl.jojczykp.cdstore.client.tracks;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import pl.jojczykp.cdstore.albums.AlbumId;
import pl.jojczykp.cdstore.client.Request;
import pl.jojczykp.cdstore.tracks.Track;
import pl.jojczykp.cdstore.tracks.TrackId;

import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.jojczykp.cdstore.albums.AlbumsResource.ALBUM_MEDIA_TYPE;

public class GetTrackRequest extends Request {

	private AlbumId albumId;
	private TrackId trackId;

	private GetTrackRequest() {
	}

	public static GetTrackRequest aGetTrackRequest() {
		return new GetTrackRequest();
	}

	public GetTrackRequest withAlbumId(AlbumId albumId) {
		this.albumId = albumId;
		return this;
	}

	public GetTrackRequest withTrackId(TrackId trackId) {
		this.trackId = trackId;
		return this;
	}

	public Track makeSuccessfully() {
		ClientResponse response = make();
		assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

		return response.getEntity(Track.class);
	}

	public ClientResponse make() {
		Client client = Client.create();

		ClientResponse response = client
				.resource(String.format("%s/albums/%s/tracks/%s", serverUrl, albumId, trackId))
				.accept(ALBUM_MEDIA_TYPE)
				.get(ClientResponse.class);

		response.bufferEntity();

		return response;
	}
}
