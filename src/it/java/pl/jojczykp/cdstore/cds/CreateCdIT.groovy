package pl.jojczykp.cdstore.cds

import spock.lang.Specification

import static pl.jojczykp.cdstore.client.cds.CreateCdRequest.aCreateCdRequest

class CreateCdIT extends Specification {

	String title = "Some Title"

	def "should create a new cd"() {
		when:
			Cd result = aCreateCdRequest()
					.withTitle(title)
					.makeSuccessfully()
		then:
			result.title == title

	}
}
