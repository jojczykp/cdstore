package pl.jojczykp.cdstore.tracks

import com.sun.jersey.api.client.ClientResponse
import pl.jojczykp.cdstore.albums.Album
import pl.jojczykp.cdstore.albums.AlbumId
import spock.lang.Specification

import static javax.ws.rs.core.Response.Status.NOT_FOUND
import static pl.jojczykp.cdstore.albums.AlbumId.randomAlbumId
import static pl.jojczykp.cdstore.client.albums.CreateAlbumRequest.aCreateAlbumRequest
import static pl.jojczykp.cdstore.client.tracks.CreateTrackRequest.aCreateTrackRequest
import static pl.jojczykp.cdstore.client.tracks.GetTrackRequest.aGetTrackRequest
import static pl.jojczykp.cdstore.client.tracks.UpdateTrackRequest.anUpdateTrackRequest
import static pl.jojczykp.cdstore.test_utils.JsonUtils.toMap
import static pl.jojczykp.cdstore.tracks.Track.aTrack
import static pl.jojczykp.cdstore.tracks.TrackId.randomTrackId

class UpdateTrackIT extends Specification {

	String albumTitle = "Album Title"
	String oldTrackTitle = "Old Track Title"
	String newTrackTitle = "New Track Title"

	def "should update track"() {
		given:
			Album album = aCreateAlbumRequest()
					.withTitle(albumTitle)
					.makeSuccessfully()
			Track created = aCreateTrackRequest(album.id)
					.withTitle(oldTrackTitle)
					.makeSuccessfully()
		when:
			anUpdateTrackRequest(album.id, created.id)
					.withTitle(newTrackTitle)
					.makeSuccessfully()
		then:
			Track updated = aGetTrackRequest(created.albumId, created.id)
					.makeSuccessfully()
			Track expected = aTrack()
					.albumId(album.id)
					.id(created.id)
					.title(newTrackTitle)
					.build()
			updated == expected
	}

	def "should fail updating not existing track"() {
		given:
			Album album = aCreateAlbumRequest()
					.withTitle(albumTitle)
					.makeSuccessfully()
			TrackId notExistingTrackId = randomTrackId()
		when:
			ClientResponse response = anUpdateTrackRequest(album.id, notExistingTrackId)
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
			ClientResponse response = anUpdateTrackRequest(notExistingAlbumId, notExistingTrackId)
					.withTitle(newTrackTitle)
					.make()
		then:
			response.status == NOT_FOUND.statusCode
			toMap(response) == [code: 101, message: 'album with given id not found']
	}

}
