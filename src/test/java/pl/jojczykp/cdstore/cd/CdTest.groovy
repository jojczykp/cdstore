package pl.jojczykp.cdstore.cd

import spock.lang.Specification

class CdTest extends Specification {

	UUID id = new UUID(1, 2)
	String title = "a title"

	def "should return with getters what was set in constructor"() {
		when:
			Cd cd = new Cd(id, title)
		then:
			cd.id == id
			cd.title == title
	}
}
