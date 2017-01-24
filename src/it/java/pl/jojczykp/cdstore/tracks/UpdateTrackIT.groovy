package pl.jojczykp.cdstore.tracks

import com.sun.jersey.api.client.ClientResponse
import pl.jojczykp.cdstore.albums.AlbumId
import spock.lang.Specification

import static javax.ws.rs.core.Response.Status.NOT_FOUND
import static pl.jojczykp.cdstore.albums.AlbumId.randomAlbumId
import static pl.jojczykp.cdstore.client.albums.CreateAlbumRequest.aCreateAlbumRequest
import static pl.jojczykp.cdstore.client.tracks.CreateTrackRequest.aCreateTrackRequest
import static pl.jojczykp.cdstore.client.tracks.UpdateTrackRequest.anUpdateTrackRequest
import static pl.jojczykp.cdstore.test_utils.JsonUtils.toMap
import static pl.jojczykp.cdstore.tracks.TrackId.randomTrackId

class UpdateTrackIT extends Specification {

	String albumTitle = "Album Title"
	String oldTrackTitle = "Old Track Title"
	String newTrackTitle = "New Track Title"

	def "should update track"() {
		given:
			AlbumId albumId = aCreateAlbumRequest()
					.withTitle(albumTitle)
					.makeSuccessfully()
					.id
			Track track = aCreateTrackRequest()
					.withAlbumId(albumId)
					.withTitle(oldTrackTitle)
					.makeSuccessfully()
		when:
			Track result = anUpdateTrackRequest()
					.withAlbumId(albumId)
					.withTrackId(track.id)
					.withTitle(newTrackTitle)
					.makeSuccessfully()
		then:
			result == track.toBuilder()
					.title(newTrackTitle)
					.build()
	}

	def "should fail updating not existing track"() {
		given:
			AlbumId albumId = aCreateAlbumRequest()
					.withTitle(albumTitle)
					.makeSuccessfully()
					.id
			TrackId notExistingTrackId = randomTrackId()
		when:
			ClientResponse response = anUpdateTrackRequest()
					.withAlbumId(albumId)
					.withTrackId(notExistingTrackId)
					.withTitle(newTrackTitle)
					.make()
		then:
			response.status == NOT_FOUND.statusCode
			toMap(response) == [code: 101, message: 'track with given id not found']
	}

	def "should fail updating track for not existing album"() {
		given:
			AlbumId notExistingAlbumId = randomAlbumId()
			TrackId notExistingTrackId = randomTrackId()
		when:
			ClientResponse response = anUpdateTrackRequest()
					.withAlbumId(notExistingAlbumId)
					.withTrackId(notExistingTrackId)
					.withTitle(newTrackTitle)
					.make()
		then:
			response.status == NOT_FOUND.statusCode
			toMap(response) == [code: 101, message: 'album with given id not found']
	}

}
