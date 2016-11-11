package pl.jojczykp.cdstore.cd;

import java.util.UUID;

public class CdBuilder {

	private UUID id;
	private String title;

	private CdBuilder() {}

	public static CdBuilder aCd() {
		return new CdBuilder();
	}

	public CdBuilder withId(UUID id) {
		this.id = id;
		return this;
	}

	public CdBuilder withTitle(String title) {
		this.title = title;
		return this;
	}

	public Cd build() {
		return new Cd(id, title);
	}
}
