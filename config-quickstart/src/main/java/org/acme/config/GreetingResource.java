package org.acme.config;

import java.util.Optional;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.runtime.ApplicationConfig;
import io.quarkus.vertx.http.runtime.HttpBuildTimeConfig;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/greeting")
public class GreetingResource {

    @ConfigProperty(name = "greeting.message")
    String message;

    @ConfigProperty(name = "greeting.suffix", defaultValue = "!")
    String suffix;

    @ConfigProperty(name = "greeting.name")
    Optional<String> name;

    @Inject
    ApplicationConfig appConfig;

    @Inject
    HttpBuildTimeConfig httpConfig;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return message + " " + name.orElse("world") + suffix;
    }

    @GET
    @Path("config")
    public String config() {
        return String.format("rootPath: %s, appName: %s", httpConfig.rootPath, appConfig.name().orElse("n/a"));
    }
}
