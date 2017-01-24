package pl.jojczykp.cdstore.tracks;

import pl.jojczykp.cdstore.exceptions.ItemNotFoundException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    public void deleteTrack(TrackId trackId) {
        boolean notExisted = (data.remove(trackId) == null);
        if (notExisted) {
            throw new ItemNotFoundException("track with given id not found");
        }
    }

}
