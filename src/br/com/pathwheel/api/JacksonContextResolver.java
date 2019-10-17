package br.com.pathwheel.api;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import br.com.pathwheel.io.Logger;
import br.com.pathwheel.jdbc.PostgreSql;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class JacksonContextResolver implements ContextResolver<ObjectMapper> {
	
	private ObjectMapper objectMapper;

	public JacksonContextResolver() throws Exception {
		
		Logger.info("Pathwheel API v1.0.0.12 (01/08/2019 13:33:00)");
		PostgreSql.init("pathwheel", "127.0.0.1", "PATHWHEEL", "pathwheel", "xxxxxx", 5, 10);
		
		this.objectMapper = new ObjectMapper();
		this.objectMapper
			.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
	}

	@Override
	public ObjectMapper getContext(Class<?> objectType) {
		return objectMapper;
	}
}