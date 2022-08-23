package org.acme.hibernate.orm.panache;

import java.util.function.BiConsumer;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class FruitUpdater {
    
    private BiConsumer<Fruit, Fruit> consumer = (e,f) -> e.name = f.name;;
    
    public void setConsumer(BiConsumer<Fruit, Fruit> consumer) {
        this.consumer  = consumer;
    }

    public void update(Fruit entity, Fruit fruit) {
        consumer.accept(entity, fruit);
    }

}
