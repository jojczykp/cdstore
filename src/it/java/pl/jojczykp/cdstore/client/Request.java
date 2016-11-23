package pl.jojczykp.cdstore.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static io.dropwizard.jackson.Jackson.newObjectMapper;

public abstract class Request {

	private static final String HOST = "localhost";
	private static final File CONFIG = Paths.get("cfg", "development.yml").toFile();

	protected String serverUrl;

	protected Request() {
		try {
			ObjectNode serverConfiguration = parseConfiguration();
			JsonNode connector = getServerConnector(serverConfiguration);

			String type = connector.get("type").asText();
			int port = connector.get("port").asInt();

			serverUrl = String.format("%s://%s:%d", type, HOST, port);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private ObjectNode parseConfiguration() throws IOException {
		ObjectMapper mapper = newObjectMapper(new YAMLFactory());
		final JsonNode node = mapper.readTree(CONFIG);
		return mapper.readTree(new TreeTraversingParser(node));
	}

	private JsonNode getServerConnector(ObjectNode configuration) {
		return configuration.get("server")
				.get("applicationConnectors").get(0);
	}

}
