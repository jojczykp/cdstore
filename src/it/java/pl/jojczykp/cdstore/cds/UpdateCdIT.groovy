package pl.jojczykp.cdstore.cds

import spock.lang.Specification

import static pl.jojczykp.cdstore.cds.Cd.CdBuilder.aCd
import static pl.jojczykp.cdstore.client.cds.CreateCdRequest.aCreateCdRequest
import static pl.jojczykp.cdstore.client.cds.UpdateCdRequest.anUpdateCdRequest

class UpdateCdIT extends Specification {

	String title = "Some Title"
	String newTitle = "Some New Title"

	def "should update cd"() {
		given:
			Cd cd = aCreateCdRequest()
					.withTitle(title)
					.makeSuccessfully()
		when:
			Cd result = anUpdateCdRequest()
					.withId(cd.getId())
					.withTitle(newTitle)
					.makeSuccessfully()
		then:
			result == aCd()
					.from(cd)
					.withTitle(newTitle)
					.build()
	}

}
