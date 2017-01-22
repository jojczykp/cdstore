package pl.jojczykp.cdstore.tracks

import pl.jojczykp.cdstore.albums.Album
import spock.lang.Specification

import static pl.jojczykp.cdstore.client.albums.CreateAlbumRequest.aCreateAlbumRequest
import static pl.jojczykp.cdstore.client.tracks.CreateTrackRequest.aCreateTrackRequest

class CreateTrackIT extends Specification {

	String albumTitle = "Some Album Title"
	String trackTitle = "Some Track Title"

	def "should create a new track"() {
		given:
			Album album = aCreateAlbumRequest()
					.withTitle(albumTitle)
					.makeSuccessfully()
		when:
			Track track = aCreateTrackRequest()
					.withAlbumId(album.getId())
					.withTitle(trackTitle)
					.makeSuccessfully()
		then:
			track.albumId == album.getId()
			track.title == trackTitle
	}

}
