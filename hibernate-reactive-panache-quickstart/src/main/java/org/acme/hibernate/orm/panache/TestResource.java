package org.acme.hibernate.orm.panache;

import java.time.Duration;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpServerRequest;

@Path("test")
public class TestResource {

    @CheckedTemplate
    static class Templates {

        static native TemplateInstance fruit(Fruit fruit);

    }

    @Inject
    HttpServerRequest request;

    @GET
    @Path("template")
    public Uni<TemplateInstance> template(@QueryParam("id") Long id) {
        return Fruit.findById(id).onItem().transform(f -> Templates.fruit((Fruit) f));
    }

    @GET
    @Path("simple")
    public Uni<String> simple(@QueryParam("id") Long id) {
        return Fruit.findById(id).onItem().transform(f -> request.getParam("id"));
    }

    @GET
    @Path("no-hibernate")
    public Uni<String> noHibernate(@QueryParam("id") Long id) {
        return Uni.createFrom().item("hello")
                .onItem().delayIt().by(Duration.ofMillis(100))
                .onItem().transform(f -> request.getParam("id"));
    }

}
