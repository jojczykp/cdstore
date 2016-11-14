package pl.jojczykp.cdstore.cds

import spock.lang.Specification

import static java.util.UUID.randomUUID
import static pl.jojczykp.cdstore.client.cds.CreateCdRequest.aCreateCdRequest
import static pl.jojczykp.cdstore.client.cds.DeleteCdRequest.aDeleteCdRequest

class DeleteCdIT extends Specification {

	UUID id = randomUUID()
	String title = "Some Title"

	def "should delete cd"() {
		given:
			aCreateCdRequest()
					.withId(id)
					.withTitle(title)
					.makeSuccessfully()
		when:
			aDeleteCdRequest()
					.withId(id)
					.makeSuccessfully()
		then:
			true
	}

}
