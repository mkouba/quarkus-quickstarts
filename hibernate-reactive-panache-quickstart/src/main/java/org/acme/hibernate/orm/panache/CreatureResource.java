package org.acme.hibernate.orm.panache;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.jboss.resteasy.reactive.RestResponse;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.logging.Log;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;

@Path("creatures")
public class CreatureResource {

    @Inject
    UriInfo uriInfo;

    record list(List<Creature> creatures) implements TemplateInstance {
    }

    // Use the transaction so that we only see the commited data
    @WithTransaction
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Uni<String> get() {
        return Creature.find("select c from Creature c left join fetch c.powers cp").<Creature> list()
                .map(c -> new list(c).render());
    }

    @WithTransaction
    @POST
    @Path("delete")
    @Produces(MediaType.TEXT_HTML)
    public Uni<RestResponse<Object>> deleteAll() {
        URI listUri = uriInfo.getRequestUriBuilder().replacePath("/creatures").build();
        return CreaturePower.deleteAll().chain(c -> Creature.deleteAll().replaceWith(RestResponse.seeOther(listUri)));
    }

    @WithTransaction
    @POST
    @Produces(MediaType.TEXT_HTML)
    public Uni<RestResponse<Object>> create() {
        URI listUri = uriInfo.getRequestUriBuilder().build();
        List<Creature> creatures = new ArrayList<>();
        for (int i = 0; i < 20000; i++) {
            Creature c = new Creature();
            c.name = "c" + i;
            c.addCreaturePower(CreaturePower.withName("cp" + i));
            creatures.add(c);
        }
        Log.infof("Going to create %s creatures", creatures.size());
        return Creature.persist(creatures).replaceWith(RestResponse.seeOther(listUri));
    }

}
