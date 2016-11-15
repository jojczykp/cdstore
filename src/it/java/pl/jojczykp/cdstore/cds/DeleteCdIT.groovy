package pl.jojczykp.cdstore.cds

import spock.lang.Specification

import static javax.ws.rs.core.Response.Status.NOT_FOUND
import static pl.jojczykp.cdstore.client.cds.CreateCdRequest.aCreateCdRequest
import static pl.jojczykp.cdstore.client.cds.DeleteCdRequest.aDeleteCdRequest
import static pl.jojczykp.cdstore.client.cds.GetCdRequest.aGetCdRequest

class DeleteCdIT extends Specification {

	String title = "Some Title"

	def "should delete cd"() {
		given:
			UUID id = aCreateCdRequest()
					.withTitle(title)
					.makeSuccessfully()
					.getId()
		when:
			aDeleteCdRequest()
					.withId(id)
					.makeSuccessfully()
		then:
			aGetCdRequest()
					.withId(id)
					.make()
					.getStatus() == NOT_FOUND.statusCode
	}

}
