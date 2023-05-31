package org.acme.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;

import io.quarkus.test.component.QuarkusComponentTestExtension;
import jakarta.inject.Inject;

public class MyComponentV1Test {

    @RegisterExtension
    static final QuarkusComponentTestExtension test = new QuarkusComponentTestExtension(MyComponent.class)
            .configProperty("foo", "BAR");

    @Inject
    MyComponent myComponent;

    @Inject
    Charlie charlie;

    @Test
    public void testPing1() {
        System.err.println("Thread:" + Thread.currentThread().getName());
        Mockito.when(charlie.ping()).thenReturn("foo");
        assertEquals("foo and BAR", myComponent.ping());
    }

    @Test
    public void testPing2() {
        Mockito.when(charlie.ping()).thenReturn("baz");
        assertEquals("baz and BAR", myComponent.ping());
    }

}
