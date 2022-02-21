package org.acme.config;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.concurrent.CompletionStage;

import javax.annotation.Priority;
import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.event.Event;
import javax.enterprise.event.NotificationOptions;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;

import org.jboss.logging.Logger;

@Priority(1)
@Decorator
public class StrEventDecorator implements Event<String> {

    private static final Logger LOG = Logger.getLogger(StrEventDecorator.class);

    @Inject
    @Delegate
    Event<String> delegate;

    @Override
    public void fire(String event) {
        LOG.warn("fire: " + event);
    }

    @Override
    public <U extends String> CompletionStage<U> fireAsync(U event) {
        LOG.warn("fire async: " + event);
        return (CompletionStage<U>) delegate.fireAsync(event.toLowerCase());
    }

    @Override
    public <U extends String> CompletionStage<U> fireAsync(U event, NotificationOptions options) {
        LOG.warn("fire async: " + event);
        return delegate.fireAsync(event, options);
    }

    @Override
    public Event<String> select(Annotation... qualifiers) {
        LOG.warn("select " + Arrays.toString(qualifiers));
        return delegate.select(qualifiers);
    }

    @Override
    public <U extends String> Event<U> select(Class<U> subtype, Annotation... qualifiers) {
        LOG.warn("select " + subtype + " and " + Arrays.toString(qualifiers));
        return delegate.select(subtype, qualifiers);
    }

    @Override
    public <U extends String> Event<U> select(TypeLiteral<U> subtype, Annotation... qualifiers) {
        LOG.warn("select " + subtype + " and " + Arrays.toString(qualifiers));
        return delegate.select(subtype, qualifiers);
    }

}
