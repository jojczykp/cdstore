package pl.jojczykp.cdstore.cd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static pl.jojczykp.cdstore.cd.CdBuilder.aCd;

public class CdRepository {

	private Map<UUID, Cd> content = new HashMap<>();

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

	public Cd getCd(UUID id) {
		return content.get(id);
	}

	public List<Cd> getCds() {
		return content.values().stream().collect(toList());
	}

}
