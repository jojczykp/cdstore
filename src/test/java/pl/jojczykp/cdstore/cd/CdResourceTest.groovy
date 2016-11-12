package pl.jojczykp.cdstore.cd

import spock.lang.Specification

import static pl.jojczykp.cdstore.cd.Cd.CdBuilder.aCd


class CdResourceTest extends Specification {

	UUID id = new UUID(6, 7)
	Cd cd1 = aCd().build()
	Cd cd2 = aCd().build()

	CdManager manager = Mock(CdManager)
	CdResource resource = new CdResource(manager)

	def "should delegate create cd to manager"() {
		when:
			Cd result = resource.createCd(cd1)
		then:
			1 * manager.createCd(cd1) >> cd2
			result == cd2
	}

	def "should delegate get cd by id to manager"() {
		when:
			Cd result = resource.getCd(id)
		then:
			1 * manager.getCd(id) >> cd1
			result == cd1
	}

	def "should delegate get all cds to manager"() {
		when:
			List<Cd> result = resource.getCds()
		then:
			1 * manager.getCds() >> [cd1, cd2]
			result == [cd1, cd2]
	}

}
