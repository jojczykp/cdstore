package pl.jojczykp.cdstore.test_utils

import com.sun.jersey.api.client.ClientResponse
import groovy.json.JsonSlurper

class JsonUtils {

    private JsonUtils() {
    }

    static toMap(ClientResponse response) {
        JsonSlurper jsonSlurper = new JsonSlurper()
        jsonSlurper.parse(response.getEntityInputStream())
    }
}
