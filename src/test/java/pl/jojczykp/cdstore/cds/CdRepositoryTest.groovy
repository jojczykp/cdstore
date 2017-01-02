package pl.jojczykp.cdstore.cds

import pl.jojczykp.cdstore.exceptions.EntityNotFoundException
import spock.lang.Specification

import static java.util.UUID.randomUUID
import static pl.jojczykp.cdstore.cds.Cd.aCd


class CdRepositoryTest extends Specification {

	String dbUrl = "s3://com.abc/db"
	CdRepository repository = new CdRepository(dbUrl)

	def "should create cd"() {
		given:
			Cd newCd = aCd().title("A Title").build()
		when:
			Cd createdCd = repository.createCd(newCd)
		then:
			createdCd == newCd.toBuilder().id(createdCd.getId()).build()
			// TODO assert call to DB made once db connection implemented
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
					new Cd(new UUID(1, 1), dbUrl + " 1"),
					new Cd(new UUID(2, 2), dbUrl + " 2"),
					new Cd(new UUID(3, 3), dbUrl + " 3")
			]
	}

	def "should update cd"() {
		given:
			Cd originalCd = repository.createCd(aCd().id(new UUID(1, 1)).title("Old Title").build())
			Cd patch = aCd().title("New Title").build()
			Cd expectedCd = originalCd.toBuilder().title(patch.getTitle()).build()
		when:
			Cd updatedCd = repository.updateCd(originalCd.getId(), patch)
		then:
			updatedCd == expectedCd
			// TODO assert call to DB made once db connection implemented
	}

	def "should fail on update cd if it does not exists"() {
		given:
			UUID id = randomUUID()
			Cd patch = aCd().title("New Title").build()
		when:
			repository.updateCd(id, patch)
		then:
			EntityNotFoundException ex = thrown()
			ex.message == "cd with given id not found"
	}

	def "should delete cd"() {
		given:
			UUID id = randomUUID()
			Cd cd = aCd().id(id).title("A Title").build()
			repository.createCd(cd)
		when:
			repository.deleteCd(id)
		then:
			true
	}

	def "should succeed on deleting not existing cd"() {
		given:
			UUID id = randomUUID()
		when:
			repository.deleteCd(id)
		then:
			true
	}

}
