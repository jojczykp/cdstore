package pl.jojczykp.cdstore.cds;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.PATCH;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

import static javax.ws.rs.core.Response.Status.CREATED;

@Path("/cds")
public class CdResource {

	public static final String CD_MEDIA_TYPE = "application/vnd-cdstore.1+json";
	public static final String CD_LIST_MEDIA_TYPE = "application/vnd-cdstore-list.1+json";

	private CdManager manager;

	public CdResource(CdManager manager) {
		this.manager = manager;
	}

	@POST
	@Timed
	@Produces(CD_MEDIA_TYPE)
	@Consumes(CD_MEDIA_TYPE)
	public Response createCd(Cd cd) {
		Cd created = manager.createCd(cd);

		return Response
				.status(CREATED)
				.entity(created)
				.build();
	}

	@GET
	@Timed
	@Produces(CD_MEDIA_TYPE)
	@Path("/{id}")
	public Cd getCd(@PathParam("id") UUID id) {
		return manager.getCd(id);
	}

	@GET
	@Timed
	@Produces(CD_LIST_MEDIA_TYPE)
	public List<Cd> getCds() {
		return manager.getCds();
	}

	@PATCH
	@Timed
	@Consumes(CD_MEDIA_TYPE)
	@Produces(CD_MEDIA_TYPE)
	@Path("/{id}")
	public Cd updateCd(@PathParam("id") UUID id, Cd cd) {
		return manager.updateCd(id, cd);
	}

}
