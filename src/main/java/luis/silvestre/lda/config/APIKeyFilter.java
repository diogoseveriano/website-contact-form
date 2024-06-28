package luis.silvestre.lda.config;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.container.ContainerRequestContext;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;

@ApplicationScoped
public class APIKeyFilter {

    @ConfigProperty(name = "ls.wc.api-key-key")
    private String apiKeyKey;

    @ConfigProperty(name = "ls.wc.api-key")
    private String apiKey;

    @ServerRequestFilter(preMatching = true)
    void filterApiKey(ContainerRequestContext containerRequestContext) throws Exception {
        String apiKeyHeader = containerRequestContext.getHeaderString(apiKeyKey);

        Log.debug("API KEY KEY: " + apiKeyKey);
        Log.debug("API KEY HEADER: " + apiKeyHeader);

        if (apiKeyHeader == null || !apiKeyHeader.equals(apiKey)) {
            throw new Exception("INVALID API-KEY");
        }
    }
}
