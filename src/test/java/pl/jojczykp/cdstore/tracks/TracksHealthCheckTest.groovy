package pl.jojczykp.cdstore.tracks

import com.codahale.metrics.health.HealthCheck
import pl.jojczykp.cdstore.exceptions.ItemNotFoundException
import spock.lang.Specification

class TracksHealthCheckTest extends Specification {

	TracksRepository repository = Mock(TracksRepository)
	TracksHealthCheck healthCheck = new TracksHealthCheck(repository)

	def "should return name"() {
		when:
			String name = healthCheck.name
		then:
			name == "tracks"
	}

	def "should return healthy if random item has been found"() {
		when:
			HealthCheck.Result result = healthCheck.check()
		then:
			result == HealthCheck.Result.healthy()
	}

	def "should return healthy if random item has not been found"() {
		when:
			HealthCheck.Result result = healthCheck.check()
		then:
			1 * repository.getTrack((TrackId)_) >> { throw new ItemNotFoundException("message") }
			result == HealthCheck.Result.healthy()
	}

	def "should return unhealthy"() {
		given:
			def reason = new RuntimeException("message")
		when:
			HealthCheck.Result result = healthCheck.check()
		then:
			1 * repository.getTrack((TrackId)_) >> { throw reason }
			result == HealthCheck.Result.unhealthy(reason)
	}

}
