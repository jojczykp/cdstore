package pl.jojczykp.cdstore.cd;

import java.util.List;
import java.util.UUID;

public class CdManager {

	private CdRepository repository;

	public CdManager(CdRepository repository) {
		this.repository = repository;
	}

	public Cd getCd(UUID id) {
		return repository.getCd(id);
	}

	public List<Cd> getCds() {
		return repository.getCds();
	}

}
