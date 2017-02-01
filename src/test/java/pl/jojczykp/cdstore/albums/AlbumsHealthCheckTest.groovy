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
		when:
			HealthCheck.Result result = healthCheck.check()
		then:
			1 * repository.getAlbum((AlbumId)_) >> { throw new ItemNotFoundException("message") }
			result == HealthCheck.Result.healthy()
	}

	def "should return unhealthy"() {
		given:
			def reason = new RuntimeException("message")
		when:
			HealthCheck.Result result = healthCheck.check()
		then:
			1 * repository.getAlbum((AlbumId)_) >> { throw reason }
			result == HealthCheck.Result.unhealthy(reason)
	}

}
