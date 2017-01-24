package pl.jojczykp.cdstore.client.tracks;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import pl.jojczykp.cdstore.albums.AlbumId;
import pl.jojczykp.cdstore.client.Request;
import pl.jojczykp.cdstore.tracks.Track;

import static javax.ws.rs.core.Response.Status.CREATED;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.jojczykp.cdstore.tracks.Track.aTrack;
import static pl.jojczykp.cdstore.tracks.TracksResource.TRACK_MEDIA_TYPE;

public class CreateTrackRequest extends Request {

	private AlbumId albumId;
	private String title;

	private CreateTrackRequest() {
	}

	public static CreateTrackRequest aCreateTrackRequest() {
		return new CreateTrackRequest();
	}

	public CreateTrackRequest withAlbumId(AlbumId albumId) {
		this.albumId = albumId;
		return this;
	}

	public CreateTrackRequest withTitle(String title) {
		this.title = title;
		return this;
	}

	public Track makeSuccessfully() {
		ClientResponse response = make();
		assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());

		return response.getEntity(Track.class);
	}

	public ClientResponse make() {
		Client client = Client.create();

		ClientResponse response = client
				.resource(String.format("%s/albums/%s/tracks", serverUrl, albumId))
				.accept(TRACK_MEDIA_TYPE)
				.type(TRACK_MEDIA_TYPE)
				.entity(aTrack()
						.albumId(albumId)
						.title(title)
						.build())
				.post(ClientResponse.class);

		response.bufferEntity();

		return response;
	}
}