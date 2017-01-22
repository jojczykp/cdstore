package pl.jojczykp.cdstore.tracks;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.UUID.randomUUID;

public class TracksRepository {

    private Map<UUID, Track> data = new ConcurrentHashMap<>();

    public Track createTrack(Track track) {
        UUID id = randomUUID();
        Track createdTrack = track.toBuilder().id(id).build();
        data.put(createdTrack.getId(), createdTrack);

        return createdTrack;
    }

    public Track getTrack(UUID id) {
        return data.get(id);
    }

}
