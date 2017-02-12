package pl.jojczykp.cdstore.albums;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.PATCH;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.Set;

import static javax.ws.rs.core.Response.Status.CREATED;

@Path("/albums")
@Api(value = "albums", description = "Albums related operations")
public class AlbumsResource {

	public static final String ALBUM_MEDIA_TYPE = "application/vnd-cdstore-album.1+json";
	public static final String ALBUM_LIST_MEDIA_TYPE = "application/vnd-cdstore-album-list.1+json";

	private final AlbumsManager manager;

	public AlbumsResource(AlbumsManager manager) {
		this.manager = manager;
	}

	@POST
	@Timed
	@Produces(ALBUM_MEDIA_TYPE)
	@Consumes(ALBUM_MEDIA_TYPE)
	@ApiOperation("Creates a new album")
	@ApiResponse(code = 201, message = "Album created")
	public Response createAlbum(
			@ApiParam("Album details") AlbumDetails albumDetails
	) {
		Album created = manager.createAlbum(Album.from(albumDetails));

		return Response
				.status(CREATED)
				.entity(created)
				.build();
	}

	@GET
	@Timed
	@Produces(ALBUM_MEDIA_TYPE)
	@Path("/{album_id}")
	@ApiOperation("Returns single album details")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Album returned"),
			@ApiResponse(code = 404, message = "Album with given id not found")
	})
	public Album getAlbum(
			@ApiParam("Album Id") @PathParam("album_id") AlbumId albumId
	) {
		return manager.getAlbum(albumId);
	}

	@GET
	@Timed
	@Produces(ALBUM_LIST_MEDIA_TYPE)
	@ApiOperation("Returns details of multiple albums")
	@ApiResponse(code = 200, responseContainer = "Set", message = "Albums returned")
	public Set<Album> getAlbums(
			@ApiParam("Substring present in filtered title") @QueryParam("titleSubstring") String maybeTitleSubstring
	) {
		return manager.getAlbums(maybeTitleSubstring);
	}

	@PATCH
	@Timed
	@Consumes(ALBUM_MEDIA_TYPE)
	@Produces(ALBUM_MEDIA_TYPE)
	@Path("/{album_id}")
	@ApiOperation("Updates details of a given albums")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Album after update"),
			@ApiResponse(code = 404, message = "Album with given id not found")
	})
	public Album updateAlbum(
			@ApiParam("Album Id") @PathParam("album_id") AlbumId albumId,
			@ApiParam("Album Details") AlbumDetails albumDetails
	) {
		return manager.updateAlbum(Album.from(albumId, albumDetails));
	}

	@DELETE
	@Timed
	@Path("/{album_id}")
	@ApiOperation("Deletes a single album with all its tracks")
	@ApiResponses({
			@ApiResponse(code = 204, message = "Album and all its tracks deleted"),
			@ApiResponse(code = 404, message = "Album with given id not found")
	})
	public void deleteAlbum(
			@ApiParam("Album Id") @PathParam("album_id") AlbumId albumId
	) {
		manager.deleteAlbum(albumId);
	}

}
