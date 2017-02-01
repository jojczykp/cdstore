package pl.jojczykp.cdstore.tracks

import pl.jojczykp.cdstore.albums.AlbumId
import spock.lang.Specification

import static Track.aTrack
import static pl.jojczykp.cdstore.albums.AlbumId.randomAlbumId
import static pl.jojczykp.cdstore.tracks.TrackId.randomTrackId

class TrackTest extends Specification {

	TrackId trackId = randomTrackId()
	AlbumId albumId = randomAlbumId()
	String title = "a title"

	def "should return with getters what was set in constructor"() {
		when:
			Track track = aTrack()
					.id(trackId)
					.albumId(albumId)
					.title(title)
					.build()
		then:
			track.id == trackId
			track.albumId == albumId
			track.title == title
	}

}
