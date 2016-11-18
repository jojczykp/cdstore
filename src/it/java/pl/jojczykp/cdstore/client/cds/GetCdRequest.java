package pl.jojczykp.cdstore.client.cds;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import pl.jojczykp.cdstore.cds.Cd;
import pl.jojczykp.cdstore.client.Request;

import java.util.UUID;

import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.jojczykp.cdstore.cds.CdResource.CD_MEDIA_TYPE;

public class GetCdRequest extends Request {

	private UUID id;

	private GetCdRequest() {
	}

	public static GetCdRequest aGetCdRequest() {
		return new GetCdRequest();
	}

	public GetCdRequest withId(UUID id) {
		this.id = id;
		return this;
	}

	public Cd makeSuccessfully() {
		ClientResponse response = make();
		assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

		return response.getEntity(Cd.class);
	}

	public ClientResponse make() {
		Client client = Client.create();

		ClientResponse response = client
				.resource(serverUrl).path("cds").path(id.toString())
				.accept(CD_MEDIA_TYPE)
				.get(ClientResponse.class);

		response.bufferEntity();

		return response;
	}
}
