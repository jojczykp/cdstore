package pl.jojczykp.cdstore.cds

import spock.lang.Specification

import static pl.jojczykp.cdstore.cds.Cd.aCd

class CdTest extends Specification {

	UUID id = new UUID(1, 2)
	String title = "a title"

	def "should return with getters what was set in constructor"() {
		when:
			Cd cd = aCd()
					.id(id)
					.title(title)
					.build()
		then:
			cd == aCd().id(id).title(title).build()
	}
}
