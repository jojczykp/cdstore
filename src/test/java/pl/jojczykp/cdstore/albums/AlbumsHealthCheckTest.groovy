package pl.jojczykp.cdstore.albums

import spock.lang.Specification
import com.codahale.metrics.health.HealthCheck

class AlbumsHealthCheckTest extends Specification {

	AlbumsConfiguration configuration = Mock(AlbumsConfiguration)
	AlbumsHealthCheck healthCheck = new AlbumsHealthCheck(configuration)

	def "should return name"() {
		when:
			String name = healthCheck.name
		then:
			name == "albums"
	}

	def "should be healthy"() {
		when:
			HealthCheck.Result result = healthCheck.check()
		then:
			result == HealthCheck.Result.healthy()
	}

}
