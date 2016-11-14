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
			Cd newCd = aCd().withId(id).build()
		when:
			Cd createdCd = repository.createCd(newCd)
		then:
			createdCd == newCd
			// TODO assert call to DB made once db connection implemented
	}

	def "should fail on create cd with already existing id"() {
		given:
			UUID id = new UUID(1, 1)
			Cd newCd = aCd().withId(id).build()
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
					new Cd(id: new UUID(1, 1), title: dbUrl + " 1"),
					new Cd(id: new UUID(2, 2), title: dbUrl + " 2"),
					new Cd(id: new UUID(3, 3), title: dbUrl + " 3")
			]
	}

	def "should update cd"() {
		given:
			UUID id = UUID.randomUUID()
			Cd cd = aCd().withId(id).withTitle("Old Title").build()
			Cd patch = aCd().withTitle("New Title").build()
			Cd expectedCd = aCd().from(cd).withTitle(patch.getTitle()).build()
			repository.createCd(cd)
		when:
			Cd updatedCd = repository.updateCd(id, patch)
		then:
			updatedCd == expectedCd
			// TODO assert call to DB made once db connection implemented
	}

	def "should fail on update cd if it does not exists"() {
		given:
			UUID id = UUID.randomUUID()
			Cd patch = aCd().withTitle("New Title").build()
		when:
			repository.updateCd(id, patch)
		then:
			EntityNotFoundException ex = thrown()
			ex.message == "cd with given id not found"
	}

}
