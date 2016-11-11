package pl.jojczykp.cdstore.cd;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

import static org.apache.commons.lang3.builder.ToStringStyle.DEFAULT_STYLE;

public class Cd {

	private UUID id;
	private String title;

	Cd(UUID id, String title) {
		this.id = id;
		this.title = title;
	}

	@JsonProperty
	public UUID getId() {
		return id;
	}

	@JsonProperty
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
}
