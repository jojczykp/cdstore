package pl.jojczykp.cdstore.client.cds;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import pl.jojczykp.cdstore.cds.Cd;

import java.util.List;
import java.util.UUID;

import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.jojczykp.cdstore.cds.CdResource.CD_LIST_MEDIA_TYPE;

public class GetCdsRequest {

	private UUID id;

	private GetCdsRequest() {
	}

	public static GetCdsRequest aGetCdsRequest() {
		return new GetCdsRequest();
	}

	public List<Cd> makeSuccessfully() {
		ClientResponse response = make();
		assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

		return response.getEntity(new GenericType<List<Cd>>() {});
	}

	public ClientResponse make() {
		Client client = Client.create();

		ClientResponse response = client
				.resource("http://localhost:8080").path("cds")
				.accept(CD_LIST_MEDIA_TYPE)
				.get(ClientResponse.class);

		response.bufferEntity();

		return response;
	}
}
