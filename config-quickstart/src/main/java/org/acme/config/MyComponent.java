package org.acme.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class MyComponent {

    @Inject
    Charlie charlie;

    @Inject
    @Simple
    Bravo bravo;

    @ConfigProperty(name = "foo")
    String foo;

    public String ping() {
        return charlie.ping() + " and " + foo;
    }

    public String pong() {
        return charlie.pong() + " and " + foo;
    }

    void onBoolean(@Observes Boolean payload, Delta delta) {
        delta.start();
    }

}
