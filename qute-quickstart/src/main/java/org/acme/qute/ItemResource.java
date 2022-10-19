package org.acme.qute;

import java.math.BigDecimal;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.RestPath;

import io.quarkus.qute.CheckedFragment;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateExtension;
import io.quarkus.qute.TemplateInstance;

@Path("items")
public class ItemResource {

    List<Item> items = List.of(new Item(new BigDecimal(10), "Apple"), new Item(new BigDecimal(16), "Pear"),
            new Item(new BigDecimal(30), "Orange"));

    @CheckedTemplate
    static class Templates {

        static native TemplateInstance items(List<Item> items);

        @CheckedFragment
        static native TemplateInstance items$price(Item item);

        @CheckedFragment
        static native TemplateInstance items$priceEdit(Item item);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance items() {
        return Templates.items(items);
    }

    @GET
    @Path("{name}/price")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance itemPrice(@RestPath String name) {
        return Templates.items$price(items.stream().filter(i -> i.name.equals(name)).findFirst().orElseThrow());
    }
    
    @PUT
    @Path("{name}/price")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance saveItemPrice(@RestPath String name, @RestForm String price) {
        Item item = items.stream().filter(i -> i.name.equals(name)).findFirst().orElseThrow();
        item.price = new BigDecimal(price);
        return Templates.items$price(item);
    }

    @GET
    @Path("{name}/priceEdit")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance editItemPrice(@RestPath String name) {
        return Templates.items$priceEdit(items.stream().filter(i -> i.name.equals(name)).findFirst().orElseThrow());
    }

    /**
     * This template extension method implements the "discountedPrice" computed property.
     */
    @TemplateExtension
    static BigDecimal discountedPrice(Item item) {
        return item.price.multiply(new BigDecimal("0.9"));
    }

}
