package pl.jojczykp.cdstore.cds

import spock.lang.Specification

import static pl.jojczykp.cdstore.cds.Cd.CdBuilder.aCd
import static pl.jojczykp.cdstore.client.cds.CreateCdRequest.aCreateCdRequest
import static pl.jojczykp.cdstore.client.cds.GetCdRequest.aGetCdRequest
import static pl.jojczykp.cdstore.client.cds.GetCdsRequest.aGetCdsRequest

class GetCdIT extends Specification {

	def "should get cd by id"() {
		String title = "Some Title"

		given:
			Cd cd = aCreateCdRequest()
					.withTitle(title)
					.makeSuccessfully()
		when:
			Cd result = aGetCdRequest()
					.withId(cd.getId())
					.makeSuccessfully()
		then:
			result == cd
	}

	def "should get all cds"() {
		Cd cd1 = aCd().withTitle("Title 1").build()
		Cd cd2 = aCd().withTitle("Title 2").build()

		given:
			Cd cdCreated1 = aCreateCdRequest().withCd(cd1).makeSuccessfully()
			Cd cdCreated2 = aCreateCdRequest().withCd(cd2).makeSuccessfully()
		when:
			List<Cd> result = aGetCdsRequest().makeSuccessfully()
		then:
			result.containsAll(cdCreated1, cdCreated2)
	}

}
