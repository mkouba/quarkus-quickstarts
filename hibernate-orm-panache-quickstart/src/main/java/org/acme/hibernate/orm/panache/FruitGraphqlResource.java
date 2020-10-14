package org.acme.hibernate.orm.panache;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonString;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

import io.smallrye.graphql.api.Context;

@GraphQLApi
public class FruitGraphqlResource {

    @Inject
    PanacheQuerySupport querySupport;

    @Query
    public List<Fruit> getAllFruits() {
        return Fruit.findAll()
                .select(querySupport.getSelectedFields(), querySupport.getResultTransformer(Fruit.class)).list();
    }

    // Potential extension API

    // A better name is needed
    @RequestScoped
    public static class PanacheQuerySupport {

        final List<String> selectedFields;

        PanacheQuerySupport(Context context) {
            selectedFields = context.getSelectedFields().stream()
                    .map(v -> ((JsonString) v).getString())
                    .collect(Collectors.toList());
        }

        public List<String> getSelectedFields() {
            return selectedFields;
        }

        <E> Function<List<Object>, List<E>> getResultTransformer(Class<E> entityClass) {
            return results -> getFu(entityClass).apply(selectedFields, results);
        }

    }

    static <E> BiFunction<List<String>, List<Object>, List<E>> getFu(Class<E> entityClass) {
        if (entityClass.equals(Fruit.class)) {
            return cast(new FruitFu());
        }
        throw new UnsupportedOperationException();
    }

    // This would be generated automatically

    static class FruitFu implements BiFunction<List<String>, List<Object>, List<Fruit>> {

        @Override
        public List<Fruit> apply(List<String> columns, List<Object> results) {
            List<Fruit> entities = new ArrayList<>();
            for (Object result : results) {
                Fruit fruit = new Fruit();
                if (result.getClass().isArray()) {
                    Object[] resultArray = (Object[]) result;
                    for (int i = 0; i < resultArray.length; i++) {
                        set(columns.get(i), resultArray[i], fruit);
                    }
                } else {
                    set(columns.get(0), result, fruit);
                }
                entities.add(fruit);
            }
            return entities;
        }

        private void set(String column, Object value, Fruit fruit) {
            if ("name".equals(column)) {
                fruit.name = (String) value;
            } else if ("id".equals(column)) {
                fruit.id = (Long) value;
            }
        }

    }

    @SuppressWarnings("unchecked")
    static <T> T cast(Object obj) {
        return (T) obj;
    }

}
