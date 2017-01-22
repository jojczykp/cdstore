package pl.jojczykp.cdstore.albums

import com.codahale.metrics.health.HealthCheck
import pl.jojczykp.cdstore.exceptions.ItemNotFoundException
import spock.lang.Specification

class AlbumsHealthCheckTest extends Specification {

	AlbumsRepository repository = Mock(AlbumsRepository)
	AlbumsHealthCheck healthCheck = new AlbumsHealthCheck(repository)

	def "should return name"() {
		when:
			String name = healthCheck.name
		then:
			name == "albums"
	}

	def "should return healthy if random item has been found"() {
		when:
			HealthCheck.Result result = healthCheck.check()
		then:
			result == HealthCheck.Result.healthy()
	}

	def "should return healthy if random item has not been found"() {
		given:
			repository.getAlbum(_) >> { throw new ItemNotFoundException("message") }
		when:
			HealthCheck.Result result = healthCheck.check()
		then:
			result == HealthCheck.Result.healthy()
	}

	def "should return unhealthy"() {
		given:
			def reason = new RuntimeException("message")
			repository.getAlbum(_) >> { throw reason }
		when:
			HealthCheck.Result result = healthCheck.check()
		then:
			result == HealthCheck.Result.unhealthy(reason)
	}

}
