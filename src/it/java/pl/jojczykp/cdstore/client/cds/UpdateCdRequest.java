package pl.jojczykp.cdstore.client.cds;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import pl.jojczykp.cdstore.cds.Cd;

import java.util.UUID;

import static com.sun.jersey.client.urlconnection.URLConnectionClientHandler.PROPERTY_HTTP_URL_CONNECTION_SET_METHOD_WORKAROUND;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.jojczykp.cdstore.cds.Cd.CdBuilder.aCd;
import static pl.jojczykp.cdstore.cds.CdResource.CD_MEDIA_TYPE;

public class UpdateCdRequest {

	private UUID id;
	private String title;

	private UpdateCdRequest() {
	}

	public static UpdateCdRequest anUpdateCdRequest() {
		return new UpdateCdRequest();
	}

	public UpdateCdRequest withId(UUID id) {
		this.id = id;
		return this;
	}

	public UpdateCdRequest withTitle(String title) {
		this.title = title;
		return this;
	}

	public Cd makeSuccessfully() {
		ClientResponse response = make();
		assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

		return response.getEntity(Cd.class);
	}

	public ClientResponse make() {
		Client client = Client.create();
		client.getProperties().put(PROPERTY_HTTP_URL_CONNECTION_SET_METHOD_WORKAROUND, true);

		ClientResponse response = client
				.resource("http://localhost:8080").path("cds").path(id.toString())
				.accept(CD_MEDIA_TYPE)
				.type(CD_MEDIA_TYPE)
				.entity(aCd()
						.withTitle(title)
						.build())
				.method("PATCH", ClientResponse.class);

		response.bufferEntity();

		return response;
	}
}
