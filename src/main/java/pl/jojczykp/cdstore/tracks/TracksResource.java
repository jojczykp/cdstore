package pl.jojczykp.cdstore.tracks;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.PATCH;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

@Path("/albums/{album_id}/tracks")
@Api(value = "tracks", description = "Tracks related operations")
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
	@ApiOperation("Creates a new track in a given album")
	@ApiResponse(code = 201, message = "Track created")
	public Response createTrack(
			@ApiParam("Album Id") @PathParam("album_id") AlbumId albumId,
			@ApiParam("Track Details") TrackDetails trackDetails
	) {
		Track created = manager.createTrack(Track.from(albumId, trackDetails));

		return Response
				.status(CREATED)
				.entity(created)
				.build();
	}

	@GET
	@Timed
	@Produces(TRACK_MEDIA_TYPE)
	@Path("/{track_id}")
	@ApiOperation("Returns single track details")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Track returned"),
			@ApiResponse(code = 404, message = "Track with given id not found")
	})
	public Track getTrack(
			@ApiParam("Album Id") @PathParam("album_id") AlbumId albumId,
			@ApiParam("Track Id") @PathParam("track_id") TrackId trackId
	) {
		return manager.getTrack(albumId, trackId);
	}

	@GET
	@Timed
	@Produces(TRACK_LIST_MEDIA_TYPE)
	@ApiOperation("Returns details of multiple tracks")
	@ApiResponse(code = 200, responseContainer = "Set", message = "Tracks returned")
	public Set<Track> getTracks(
			@ApiParam("Album Id") @PathParam("album_id") AlbumId albumId
	) {
		return manager.getTracks(albumId);
	}

	@PATCH
	@Timed
	@Consumes(TRACK_MEDIA_TYPE)
	@Produces(TRACK_MEDIA_TYPE)
	@Path("/{track_id}")
	@ApiOperation("Updates details of a given track")
	@ApiResponses({
			@ApiResponse(code = 204, message = "Track updated"),
			@ApiResponse(code = 404, message = "Track with given id not found")
	})
	public void updateTrack(
			@ApiParam("Album Id") @PathParam("album_id") AlbumId albumId,
			@ApiParam("Track Id") @PathParam("track_id") TrackId trackId,
			@ApiParam("Track Details") TrackDetails trackDetails
	) {
		manager.updateTrack(Track.from(albumId, trackId, trackDetails));
	}

	@DELETE
	@Timed
	@Path("/{track_id}")
	@ApiOperation("Deletes a single track")
	@ApiResponses({
			@ApiResponse(code = 204, message = "Track deleted"),
			@ApiResponse(code = 404, message = "Track with given id not found")
	})
	public void deleteTrack(
			@ApiParam("Album Id") @PathParam("album_id") AlbumId albumId,
			@ApiParam("Track Id") @PathParam("track_id") TrackId trackId
	) {
		manager.deleteTrack(albumId, trackId);
	}

	@DELETE
	@Timed
	@ApiOperation("Deletes all tracks from a given album")
	@ApiResponses({
			@ApiResponse(code = 204, message = "All tracks from album deleted"),
			@ApiResponse(code = 404, message = "Album with given id not found")
	})
	public void deleteAllAlbumTracks(
			@ApiParam("Album Id") @PathParam("album_id") AlbumId albumId
	) {
		manager.deleteAllAlbumTracks(albumId);
	}

}
