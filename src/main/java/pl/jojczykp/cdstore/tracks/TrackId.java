package pl.jojczykp.cdstore.tracks;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;

import java.util.UUID;

import static java.util.UUID.randomUUID;

@EqualsAndHashCode(of = "uuid")
public class TrackId {

	private final UUID uuid;

	private TrackId(UUID uuid) {
		this.uuid = uuid;
	}

	public static TrackId randomTrackId() {
		return new TrackId(randomUUID());
	}

	public static TrackId fromString(String uuidString) {
		return new TrackId(UUID.fromString(uuidString));
	}

	@Override
	@JsonValue
	public String toString() {
		return uuid.toString();
	}

}

