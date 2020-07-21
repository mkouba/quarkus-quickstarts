package org.acme.hibernate.reactive;

import static io.quarkus.vertx.web.Route.HandlerType.FAILURE;
import static io.vertx.core.http.HttpMethod.DELETE;
import static io.vertx.core.http.HttpMethod.GET;
import static io.vertx.core.http.HttpMethod.POST;
import static io.vertx.core.http.HttpMethod.PUT;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

import javax.inject.Inject;

import org.hibernate.reactive.mutiny.Mutiny;
import org.jboss.logging.Logger;

import io.quarkus.vertx.web.Body;
import io.quarkus.vertx.web.Param;
import io.quarkus.vertx.web.ReactiveRoutes;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

@RouteBase(path = "ovoce")
public class FruitMutinyRoutes {

    private static final Logger LOGGER = Logger.getLogger(FruitMutinyRoutes.class.getName());

    @Inject
    Mutiny.Session mutinySession;

    @Route(path = "/", methods = GET)
    public Multi<Fruit> get() {
        return ReactiveRoutes.asJsonArray(mutinySession
                .createNamedQuery("Fruits.findAll", Fruit.class).getResults());
    }

    @Route(path = ":id", methods = GET)
    public Uni<Fruit> getSingle(@Param Optional<String> id) {
        return mutinySession.find(Fruit.class, id.map(Integer::valueOf).get());
    }

    @Route(path = "/", methods = POST)
    public Uni<Fruit> create(@Body Fruit fruit, HttpServerResponse response) {
        if (fruit == null || fruit.getId() != null) {
            return Uni.createFrom().failure(new IllegalArgumentException("Id was invalidly set on request."));
        }
        return mutinySession
                .persist(fruit)
                .onItem().transformToUni(session -> mutinySession.flush())
                .onItem().transform(ignore -> {
                    response.setStatusCode(201);
                    return fruit;
                });
    }

    @Route(path = ":id", methods = PUT)
    public Uni<Fruit> update(@Param Optional<String> id, @Body Fruit fruit, HttpServerResponse response) {
        if (fruit == null || fruit.getName() == null) {
            return Uni.createFrom().failure(new IllegalArgumentException("Fruit name was not set on request."));
        }

        // Update function (never returns null)
        Function<Fruit, Uni<Fruit>> update = entity -> {
            entity.setName(fruit.getName());
            return mutinySession.flush()
                    .onItem().transform(ignore -> entity);
        };

        return mutinySession
                .find(Fruit.class, id.map(Integer::valueOf).get())
                // If entity exists then update
                .onItem().ifNotNull().transformToUni(update)
                // else
                .onItem().ifNull().fail();
    }

    @Route(path = ":id", methods = DELETE)
    public Uni<Fruit> delete(@Param Optional<String> id, HttpServerResponse response) {
        // Delete function (never returns null)
        Function<Fruit, Uni<Fruit>> delete = entity -> mutinySession.remove(entity)
                .onItem().transformToUni(ignore -> mutinySession.flush())
                .onItem().transform(ignore -> {
                    response.setStatusCode(204).end();
                    return entity;
                });

        return mutinySession
                .find(Fruit.class, id.map(Integer::valueOf).get())
                // If entity exists then delete
                .onItem().ifNotNull().transformToUni(delete)
                // else
                .onItem().ifNull().fail();
    }

    @Route(path = "/*", type = FAILURE)
    public void error(RoutingContext context) {
        Throwable t = context.failure();
        if (t != null) {
            LOGGER.error("Failed to handle request", t);
            int status = context.statusCode();
            String chunk = "";
            if (t instanceof NoSuchElementException) {
                status = 404;
            } else if (t instanceof IllegalArgumentException) {
                status = 422;
                chunk = new JsonObject().put("code", status)
                        .put("exceptionType", t.getClass().getName()).put("error", t.getMessage()).encode();
            }
            context.response().setStatusCode(status).end(chunk);
        } else {
            // Continue with the default error handler
            context.next();
        }
    }

}
