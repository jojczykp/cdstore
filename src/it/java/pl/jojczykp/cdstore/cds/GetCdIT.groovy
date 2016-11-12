package pl.jojczykp.cdstore.cds

import spock.lang.Specification

import static java.util.UUID.randomUUID
import static pl.jojczykp.cdstore.client.cds.CreateCdRequest.aCreateCdRequest
import static pl.jojczykp.cdstore.client.cds.GetCdRequest.aGetCdRequest
import static pl.jojczykp.cdstore.client.cds.GetCdsRequest.aGetCdsRequest

class GetCdIT extends Specification {

	def "should get cd by id"() {
		UUID id = randomUUID()
		String title = "Some Title"

		given:
			Cd cd = aCreateCdRequest()
					.withId(id)
					.withTitle(title)
					.makeSuccessfully()
		when:
			Cd result = aGetCdRequest()
					.withId(id)
					.makeSuccessfully()
		then:
			result == cd
	}

	def "should get all cds"() {
		Cd cd1 = new Cd(id: randomUUID(), title: "Title 1")
		Cd cd2 = new Cd(id: randomUUID(), title: "Title 2")

		given:
			aCreateCdRequest().withCd(cd1).makeSuccessfully()
			aCreateCdRequest().withCd(cd2).makeSuccessfully()
		when:
			List<Cd> result = aGetCdsRequest().makeSuccessfully()
		then:
			result.containsAll(cd1, cd2)
	}

}
