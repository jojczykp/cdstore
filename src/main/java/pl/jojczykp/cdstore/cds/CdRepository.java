package pl.jojczykp.cdstore.cds;

import pl.jojczykp.cdstore.exceptions.EntityAlreadyExistsException;
import pl.jojczykp.cdstore.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static pl.jojczykp.cdstore.cds.Cd.aCd;

public class CdRepository {

	private ConcurrentHashMap<UUID, Cd> content = new ConcurrentHashMap<>();

	private String dbUrl;

	public CdRepository(String dbUrl) {
		this.dbUrl = dbUrl;

		createSampleContent();
	}

	private void createSampleContent() {
		createSampleCd(1);
		createSampleCd(2);
		createSampleCd(3);
	}

	private void createSampleCd(int num) {
		UUID id = new UUID(num, num);
		content.put(id, aCd().id(id).title(dbUrl + " " + num).build());
	}

	public Cd createCd(Cd cd) {
		UUID id = randomUUID();
		Cd newCd = cd.toBuilder().id(id).build();

		Cd previous = content.putIfAbsent(id, newCd);

		if (previous != null) {
			throw new EntityAlreadyExistsException("cd with given id already exists");
		} else {
			return newCd;
		}
	}

	public Cd getCd(UUID id) {
		Cd result = content.get(id);
		if (result == null) {
			throw new EntityNotFoundException("cd with given id does not exist");
		}

		return result;
	}

	public List<Cd> getCds() {
		return content.values().stream().collect(toList());
	}

	public Cd updateCd(UUID id, Cd cd) {
		Cd newValue = content.computeIfPresent(id, (i, c) -> cd.toBuilder().id(id).build());
		if (newValue == null) {
			throw new EntityNotFoundException("cd with given id not found");
		} else {
			return newValue;
		}
	}

	public void deleteCd(UUID id) {
		content.remove(id);
	}

}
