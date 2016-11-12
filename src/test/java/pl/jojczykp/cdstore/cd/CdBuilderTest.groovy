package pl.jojczykp.cdstore.cd

import spock.lang.Specification

import static pl.jojczykp.cdstore.cd.Cd.CdBuilder.aCd


class CdBuilderTest extends Specification {

	UUID id = new UUID(1, 2)
	String title = "a title"

	def "should return with getters what was set in builder"() {
		when:
			Cd cd = aCd()
					.withId(id)
					.withTitle(title)
					.build()
		then:
			cd.id == id
			cd.title == title
	}
}
