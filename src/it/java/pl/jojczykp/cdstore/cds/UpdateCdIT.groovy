package pl.jojczykp.cdstore.cds

import spock.lang.Specification

import static java.util.UUID.randomUUID
import static pl.jojczykp.cdstore.client.cds.CreateCdRequest.aCreateCdRequest
import static pl.jojczykp.cdstore.client.cds.UpdateCdRequest.anUpdateCdRequest

class UpdateCdIT extends Specification {

	UUID id = randomUUID()
	String title = "Some Title"
	String newTitle = "Some New Title"

	def "should update cd"() {
		given:
			aCreateCdRequest()
					.withId(id)
					.withTitle(title)
					.makeSuccessfully()
		when:
			Cd result = anUpdateCdRequest()
					.withId(id)
					.withTitle(newTitle)
					.makeSuccessfully()
		then:
			result == new Cd(id: id, title: newTitle)
	}

}
