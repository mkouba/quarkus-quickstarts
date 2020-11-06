package org.acme.qute;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.quarkus.qute.TemplateExtension;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.qute.api.CheckedTemplate;

@Path("items")
public class ItemResource {

    @CheckedTemplate
    static class Templates {
        
        static native TemplateInstance items(List<Item> items);
        
    }
    
    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get() {
        List<Item> data = new ArrayList<>();
        data.add(new Item(new BigDecimal(10), "Apple"));
        data.add(new Item(new BigDecimal(16), "Pear"));
        data.add(new Item(new BigDecimal(30), "Orange"));
        return Templates.items(data);
    }

    /**
     * This template extension method implements the "discountedPrice" computed property.
     */
    @TemplateExtension
    static BigDecimal discountedPrice(Item item) {
        return item.price.multiply(new BigDecimal("0.9"));
    }

}
