package pl.jojczykp.cdstore.cd;

import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.UUID;

@Path("/cds")
public class CdResource {

	private static final String CD_MEDIA_TYPE = "application/vnd-cdstore.1+json";
	private static final String CD_LIST_MEDIA_TYPE = "application/vnd-cdstore-list.1+json";

	private CdManager manager;

	public CdResource(CdManager manager) {
		this.manager = manager;
	}

	@GET
	@Timed
	@Produces(CD_LIST_MEDIA_TYPE)
	@Path("/{id}")
	public Cd getCd(@PathParam("id") UUID id) {
		return manager.getCd(id);
	}

	@GET
	@Timed
	@Produces(CD_MEDIA_TYPE)
	public List<Cd> getCds() {
		return manager.getCds();
	}

}
