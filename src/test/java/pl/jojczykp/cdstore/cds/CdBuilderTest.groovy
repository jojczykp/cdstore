package pl.jojczykp.cdstore.cds

import spock.lang.Specification

import static pl.jojczykp.cdstore.cds.Cd.CdBuilder.aCd


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
			cd == new Cd(id: id, title: title)
	}
}
