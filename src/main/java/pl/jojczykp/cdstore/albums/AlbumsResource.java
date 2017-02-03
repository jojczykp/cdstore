package pl.jojczykp.cdstore.albums;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.PATCH;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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
import static javax.ws.rs.core.Response.Status.NO_CONTENT;

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
	public Album getAlbum(
			@ApiParam("Album Id") @PathParam("album_id") AlbumId albumId
	) {
		return manager.getAlbum(albumId);
	}

	@GET
	@Timed
	@Produces(ALBUM_LIST_MEDIA_TYPE)
	@ApiOperation("Returns details of multiple albums")
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
	public Album updateAlbum(
			@ApiParam("Album Id") @PathParam("album_id") AlbumId albumId,
			@ApiParam("Album Details") AlbumDetails albumDetails
	) {
		return manager.updateAlbum(Album.from(albumId, albumDetails));
	}

	@DELETE
	@Timed
	@Path("/{album_id}")
	@ApiOperation("Deletes a single album")
	public Response deleteAlbum(
			@ApiParam("Album Id") @PathParam("album_id") AlbumId albumId
	) {
		manager.deleteAlbum(albumId);

		return Response
				.status(NO_CONTENT)
				.build();
	}

}
