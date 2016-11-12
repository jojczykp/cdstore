package pl.jojczykp.cdstore.cds

import pl.jojczykp.cdstore.exceptions.EntityAlreadyExistsException
import pl.jojczykp.cdstore.exceptions.EntityNotFoundException
import spock.lang.Specification

import static pl.jojczykp.cdstore.cds.Cd.CdBuilder.aCd


class CdRepositoryTest extends Specification {

	String dbUrl = "s3://com.abc/db"
	CdRepository repository = new CdRepository(dbUrl)

	def "should create cd"() {
		given:
			UUID id = new UUID(4, 4)
			Cd newCd = aCd().withId(id).build();
		when:
			Cd createdCd = repository.createCd(newCd)
		then:
			createdCd == newCd
			// TODO assert call to DB made once db connection implemented
	}

	def "should fail on create cd with already existing id"() {
		given:
			UUID id = new UUID(1, 1)
			Cd newCd = aCd().withId(id).build();
		when:
			repository.createCd(newCd)
		then:
			EntityAlreadyExistsException ex = thrown()
			ex.message == "cd with given id already exists"
	}

	def "should get cd by id"() {
		given:
			UUID id = new UUID(1, 1)
		when:
			Cd cd = repository.getCd(id)
		then:
			cd.id == id
			cd.title == dbUrl + " 1"
	}

	def "should fail on get cd by not existing id"() {
		given:
			UUID notExistingId = new UUID(9, 9)
		when:
			repository.getCd(notExistingId)
		then:
			EntityNotFoundException ex = thrown()
			ex.message == "cd with given id does not exist"
	}

	def "should get all cds"() {
		when:
			List<Cd> cds = repository.getCds()
		then:
			cds == [
					aCd().withId(new UUID(1, 1)).withTitle(dbUrl + " 1").build(),
					aCd().withId(new UUID(2, 2)).withTitle(dbUrl + " 2").build(),
					aCd().withId(new UUID(3, 3)).withTitle(dbUrl + " 3").build()
			]
	}
}
