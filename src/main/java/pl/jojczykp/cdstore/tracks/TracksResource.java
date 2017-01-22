package pl.jojczykp.cdstore.tracks;

import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.CREATED;

@Path("/albums/{album_id}/tracks")
public class TracksResource {

	public static final String TRACK_MEDIA_TYPE = "application/vnd-cdstore-track.1+json";

	private TracksManager manager;

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

}
