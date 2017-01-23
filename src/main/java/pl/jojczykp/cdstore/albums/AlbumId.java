package pl.jojczykp.cdstore.albums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.UUID;

import static java.util.UUID.randomUUID;

public class AlbumId {

    private final UUID uuid;

    private AlbumId(UUID uuid) {
        this.uuid = uuid;
    }

    public static AlbumId randomAlbumId() {
        return new AlbumId(randomUUID());
    }

    public static AlbumId fromString(String uuidString) {
        return new AlbumId(UUID.fromString(uuidString));
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof AlbumId) && uuid.equals(((AlbumId) obj).uuid);
    }

    @Override
    @JsonValue
    public String toString() {
        return uuid.toString();
    }

}
