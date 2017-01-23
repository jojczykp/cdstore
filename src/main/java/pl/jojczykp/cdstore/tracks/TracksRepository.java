package pl.jojczykp.cdstore.tracks;

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

    public Track getTrack(TrackId id) {
        return data.get(id);
    }

}
