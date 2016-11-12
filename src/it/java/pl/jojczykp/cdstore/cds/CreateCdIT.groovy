package pl.jojczykp.cdstore.cds

import spock.lang.Specification

import static java.util.UUID.randomUUID
import static pl.jojczykp.cdstore.client.cds.CreateCdRequest.aCreateCdRequest

class CreateCdIT extends Specification {

	UUID id = randomUUID();
	String title = "Some Title"

	def "should create a new cd"() {
		when:
			Cd result = aCreateCdRequest()
					.withId(id)
					.withTitle(title)
					.makeSuccessfully()
		then:
			result == new Cd(id: id, title: title)

	}
}
