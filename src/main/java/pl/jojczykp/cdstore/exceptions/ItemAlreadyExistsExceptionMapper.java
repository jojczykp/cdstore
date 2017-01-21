package pl.jojczykp.cdstore.exceptions;

import com.google.common.collect.ImmutableMap;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.CONFLICT;

public class ItemAlreadyExistsExceptionMapper implements ExceptionMapper<ItemAlreadyExistsException> {

	@Override
	public Response toResponse(ItemAlreadyExistsException e) {
		return Response
				.status(CONFLICT)
				.type(APPLICATION_JSON_TYPE)
				.entity(ImmutableMap.of(
						"code", 102,
						"message", e.getMessage()))
				.build();
	}
}
