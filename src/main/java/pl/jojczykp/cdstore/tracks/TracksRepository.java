package pl.jojczykp.cdstore.tracks;

import pl.jojczykp.cdstore.albums.AlbumId;
import pl.jojczykp.cdstore.exceptions.ItemNotFoundException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toSet;
import static pl.jojczykp.cdstore.tracks.TrackId.randomTrackId;

public class TracksRepository {

    private final Map<TrackId, Track> data = new ConcurrentHashMap<>();

    public Track createTrack(Track track) {
        TrackId id = randomTrackId();
        Track createdTrack = track.toBuilder().id(id).build();
        data.put(createdTrack.getId(), createdTrack);

        return createdTrack;
    }

    public Track getTrack(TrackId trackId) {
        Track result = data.get(trackId);
        if (result == null) {
            throw new ItemNotFoundException("track with given id not found");
        }

        return result;
    }

    public Set<Track> getTracks(AlbumId albumId) {
        return data.values().stream()
                .filter(t -> t.getAlbumId().equals(albumId))
                .collect(toSet());
    }

    public Track updateTrack(TrackId trackId, Track patch) {
        return data.compute(trackId, (id, oldTrack) -> {
            if (oldTrack == null) {
                throw new ItemNotFoundException("track with given id not found");
            } else {
                return oldTrack.toBuilder().title(patch.getTitle()).build();
            }
        });
    }

    public void deleteTrack(TrackId trackId) {
        boolean notExisted = (data.remove(trackId) == null);
        if (notExisted) {
            throw new ItemNotFoundException("track with given id not found");
        }
    }
}
