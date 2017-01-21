package pl.jojczykp.cdstore.exceptions;

import com.google.common.collect.ImmutableMap;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

public class ItemNotFoundExceptionMapper implements ExceptionMapper<ItemNotFoundException> {

	@Override
	public Response toResponse(ItemNotFoundException e) {
		return Response
				.status(NOT_FOUND)
				.type(APPLICATION_JSON_TYPE)
				.entity(ImmutableMap.of(
						"code", 101,
						"message", e.getMessage()))
				.build();
	}
}
