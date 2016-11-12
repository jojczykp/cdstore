package pl.jojczykp.cdstore.cds

import spock.lang.Specification
import com.codahale.metrics.health.HealthCheck

class CdHealthCheckTest extends Specification {

	CdConfiguration configuration = Mock(CdConfiguration)
	CdHealthCheck healthCheck = new CdHealthCheck(configuration)

	def "should return name"() {
		when:
			String name = healthCheck.name
		then:
			name == "cds"
	}

	def "should be healthy"() {
		when:
			HealthCheck.Result result = healthCheck.check()
		then:
			result == HealthCheck.Result.healthy()
	}

}
