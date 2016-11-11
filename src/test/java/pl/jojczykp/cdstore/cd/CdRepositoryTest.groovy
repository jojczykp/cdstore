package pl.jojczykp.cdstore.cd

import spock.lang.Specification

class CdRepositoryTest extends Specification {

	String dbUrl = "s3://com.abc/db"
	CdRepository repository = new CdRepository(dbUrl)

	def "should get cd by id"() {
		given:
			UUID id = new UUID(1, 1)
		when:
			Cd cd = repository.getCd(id)
		then:
			cd.id == id
			cd.title == dbUrl + " 1"
	}

	def "should get all cds"() {
		when:
			List<Cd> cds = repository.getCds()
		then:
			cds == [
					new Cd(new UUID(1, 1), dbUrl + " 1"),
					new Cd(new UUID(2, 2), dbUrl + " 2"),
					new Cd(new UUID(3, 3), dbUrl + " 3")
			]
	}
}
