package pl.jojczykp.cdstore.cds

import spock.lang.Specification

import static pl.jojczykp.cdstore.client.cds.CreateCdRequest.aCreateCdRequest
import static pl.jojczykp.cdstore.client.cds.UpdateCdRequest.anUpdateCdRequest

class UpdateCdIT extends Specification {

	String title = "Some Title"
	String newTitle = "Some New Title"

	def "should update cd"() {
		given:
			UUID id = aCreateCdRequest()
					.withTitle(title)
					.makeSuccessfully()
					.getId()
		when:
			Cd result = anUpdateCdRequest()
					.withId(id)
					.withTitle(newTitle)
					.makeSuccessfully()
		then:
			result == new Cd(id: id, title: newTitle)
	}

}
