package org.acme.config;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/greeting")
public class GreetingResource {

    @ConfigProperty(name = "greeting.message")
    String message;

    @ConfigProperty(name = "greeting.suffix", defaultValue = "!")
    String suffix;

    @ConfigProperty(name = "greeting.name")
    Optional<String> name;

    @Inject
    Event<String> event;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return message + " " + name.orElse("world") + suffix;
    }

    @Path("fire")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String fire() {
        event.fire("OK");
        return "OK";
    }
    
    @Path("fire-async")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String fireAsync() throws InterruptedException, ExecutionException {
        return event.fireAsync("OK").toCompletableFuture().get();
    }
}
