package pl.jojczykp.cdstore.tracks;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.PATCH;
import pl.jojczykp.cdstore.albums.AlbumId;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Set;

import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;

@Path("/albums/{album_id}/tracks")
public class TracksResource {

	public static final String TRACK_MEDIA_TYPE = "application/vnd-cdstore-track.1+json";
	public static final String TRACK_LIST_MEDIA_TYPE = "application/vnd-cdstore-track-list.1+json";

	private final TracksManager manager;

	public TracksResource(TracksManager manager) {
		this.manager = manager;
	}

	@POST
	@Timed
	@Produces(TRACK_MEDIA_TYPE)
	@Consumes(TRACK_MEDIA_TYPE)
	public Response createTrack(Track track) {
		Track created = manager.createTrack(track);

		return Response
				.status(CREATED)
				.entity(created)
				.build();
	}

	@GET
	@Timed
	@Produces(TRACK_MEDIA_TYPE)
	@Path("/{track_id}")
	public Track getTrack(
			@PathParam("album_id") AlbumId albumId,
			@PathParam("track_id") TrackId trackId
	) {
		return manager.getTrack(albumId, trackId);
	}

	@GET
	@Timed
	@Produces(TRACK_LIST_MEDIA_TYPE)
	public Set<Track> getTracks(@PathParam("album_id") AlbumId albumId) {
		return manager.getTracks(albumId);
	}

	@PATCH
	@Timed
	@Consumes(TRACK_MEDIA_TYPE)
	@Produces(TRACK_MEDIA_TYPE)
	@Path("/{track_id}")
	public Track updateTrack(
			@PathParam("album_id") AlbumId albumId,
			@PathParam("track_id") TrackId trackId,
			Track patch) {
		return manager.updateTrack(albumId, trackId, patch);
	}

	@DELETE
	@Timed
	@Path("/{track_id}")
	public Response deleteTrack(
			@PathParam("album_id") AlbumId albumId,
			@PathParam("track_id") TrackId trackId
	) {
		manager.deleteTrack(albumId, trackId);

		return Response
				.status(NO_CONTENT)
				.build();
	}

}
