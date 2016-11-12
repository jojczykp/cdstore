package pl.jojczykp.cdstore.cds;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

import static org.apache.commons.lang3.builder.ToStringStyle.DEFAULT_STYLE;

public class Cd {

	@JsonProperty
	private UUID id;

	@JsonProperty
	private String title;

	public UUID getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, true);
	}

	@Override
	public boolean equals(Object that) {
		return EqualsBuilder.reflectionEquals(this, that, true);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, DEFAULT_STYLE, true);
	}

	public static class CdBuilder {

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
			Cd cd = new Cd();
			cd.id = id;
			cd.title = title;

			return cd;
		}
	}
}
