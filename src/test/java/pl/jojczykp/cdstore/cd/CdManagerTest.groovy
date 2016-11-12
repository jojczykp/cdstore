package pl.jojczykp.cdstore.cd

import spock.lang.Specification

import static pl.jojczykp.cdstore.cd.Cd.CdBuilder.aCd


class CdManagerTest extends Specification {

	UUID id = new UUID(6, 7)
	Cd cd1 = aCd().build()
	Cd cd2 = aCd().build()

	CdRepository repository = Mock(CdRepository)
	CdManager manager = new CdManager(repository)

	def "should delegate create cd to repository"() {
		when:
			Cd result = manager.createCd(cd1)
		then:
			1 * repository.createCd(cd1) >> cd2
			result == cd2
	}

	def "should delegate get cd by id to repository"() {
		when:
			Cd result = manager.getCd(id)
		then:
			1 * repository.getCd(id) >> cd1
			result == cd1
	}

	def "should delegate get all cds to repository"() {
		when:
			List<Cd> result = manager.getCds()
		then:
			1 * repository.getCds() >> [cd1, cd2]
			result == [cd1, cd2]
	}
}
