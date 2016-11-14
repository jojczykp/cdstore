package pl.jojczykp.cdstore.cds;

import java.util.List;
import java.util.UUID;

public class CdManager {

	private CdRepository repository;

	public CdManager(CdRepository repository) {
		this.repository = repository;
	}

	public Cd createCd(Cd cd) {
		return repository.createCd(cd);
	}

	public Cd getCd(UUID id) {
		return repository.getCd(id);
	}

	public List<Cd> getCds() {
		return repository.getCds();
	}

	public Cd updateCd(UUID id, Cd cd) {
		return repository.updateCd(id, cd);
	}

	public void deleteCd(UUID id) {
		repository.deleteCd(id);
	}

}
