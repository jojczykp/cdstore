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
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;

@Path("/albums")
public class AlbumsResource {

	public static final String ALBUM_MEDIA_TYPE = "application/vnd-cdstore-album.1+json";
	public static final String ALBUM_LIST_MEDIA_TYPE = "application/vnd-cdstore-album-list.1+json";

	private AlbumsManager manager;

	public AlbumsResource(AlbumsManager manager) {
		this.manager = manager;
	}

	@POST
	@Timed
	@Produces(ALBUM_MEDIA_TYPE)
	@Consumes(ALBUM_MEDIA_TYPE)
	public Response createAlbum(Album album) {
		Album created = manager.createAlbum(album);

		return Response
				.status(CREATED)
				.entity(created)
				.build();
	}

	@GET
	@Timed
	@Produces(ALBUM_MEDIA_TYPE)
	@Path("/{id}")
	public Album getAlbum(@PathParam("id") UUID id) {
		return manager.getAlbum(id);
	}

	@GET
	@Timed
	@Produces(ALBUM_LIST_MEDIA_TYPE)
	public List<Album> getAlbums() {
		return manager.getAlbums();
	}

	@PATCH
	@Timed
	@Consumes(ALBUM_MEDIA_TYPE)
	@Produces(ALBUM_MEDIA_TYPE)
	@Path("/{id}")
	public Album updateAlbum(@PathParam("id") UUID id, Album album) {
		return manager.updateAlbum(id, album);
	}

	@DELETE
	@Timed
	@Path("/{id}")
	public Response deleteAlbum(@PathParam("id") UUID id) {
		manager.deleteAlbum(id);

		return Response
				.status(NO_CONTENT)
				.build();
	}

}
