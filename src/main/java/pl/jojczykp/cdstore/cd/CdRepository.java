package pl.jojczykp.cdstore.cd;

import pl.jojczykp.cdstore.main.CdStoreException;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toList;
import static pl.jojczykp.cdstore.cd.Cd.CdBuilder.aCd;

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
		UUID id1 = new UUID(num, num);
		content.put(id1, aCd().withId(id1).withTitle(dbUrl + " " + num).build());
	}

	public Cd createCd(Cd cd) {
		Cd previous = content.putIfAbsent(cd.getId(), cd);
		if (previous != null) {
			throw new CdStoreException("cd with given id already exists");
		} else {
			return cd;
		}
	}

	public Cd getCd(UUID id) {
		Cd result = content.get(id);
		if (result == null) {
			throw new CdStoreException("cd with given id does not exist");
		}

		return result;
	}

	public List<Cd> getCds() {
		return content.values().stream().collect(toList());
	}

}
