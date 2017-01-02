package pl.jojczykp.cdstore.client.cds;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import pl.jojczykp.cdstore.cds.Cd;
import pl.jojczykp.cdstore.client.Request;

import java.util.UUID;

import static javax.ws.rs.core.Response.Status.CREATED;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.jojczykp.cdstore.cds.Cd.aCd;
import static pl.jojczykp.cdstore.cds.CdResource.CD_MEDIA_TYPE;

public class CreateCdRequest extends Request {

	private UUID id;
	private String title;

	private CreateCdRequest() {
	}

	public static CreateCdRequest aCreateCdRequest() {
		return new CreateCdRequest();
	}

	public CreateCdRequest withTitle(String title) {
		this.title = title;
		return this;
	}

	public CreateCdRequest withCd(Cd cd) {
		this.id = cd.getId();
		this.title = cd.getTitle();
		return this;
	}

	public Cd makeSuccessfully() {
		ClientResponse response = make();
		assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());

		return response.getEntity(Cd.class);
	}

	public ClientResponse make() {
		Client client = Client.create();

		ClientResponse response = client
				.resource(serverUrl).path("cds")
				.accept(CD_MEDIA_TYPE)
				.type(CD_MEDIA_TYPE)
				.entity(aCd()
						.id(id)
						.title(title)
						.build())
				.post(ClientResponse.class);

		response.bufferEntity();

		return response;
	}
}
