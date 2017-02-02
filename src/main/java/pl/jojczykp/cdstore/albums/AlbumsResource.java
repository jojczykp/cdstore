package pl.jojczykp.cdstore.albums;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.PATCH;

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
	public Response createAlbum(AlbumDetails albumDetails) {
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
	public Album getAlbum(@PathParam("album_id") AlbumId albumId) {
		return manager.getAlbum(albumId);
	}

	@GET
	@Timed
	@Produces(ALBUM_LIST_MEDIA_TYPE)
	public Set<Album> getAlbums(
			@QueryParam("titleSubstring") String maybeTitleSubstring
	) {
		return manager.getAlbums(maybeTitleSubstring);
	}

	@PATCH
	@Timed
	@Consumes(ALBUM_MEDIA_TYPE)
	@Produces(ALBUM_MEDIA_TYPE)
	@Path("/{album_id}")
	public Album updateAlbum(
			@PathParam("album_id") AlbumId albumId,
			AlbumDetails albumDetails
	) {
		return manager.updateAlbum(Album.from(albumId, albumDetails));
	}

	@DELETE
	@Timed
	@Path("/{album_id}")
	public Response deleteAlbum(@PathParam("album_id") AlbumId albumId) {
		manager.deleteAlbum(albumId);

		return Response
				.status(NO_CONTENT)
				.build();
	}

}
