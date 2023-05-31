package org.acme.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;

import io.quarkus.test.component.QuarkusComponentTestExtension;
import jakarta.inject.Inject;

public class AnotherComponentTest {

    @RegisterExtension
    static final QuarkusComponentTestExtension test = new QuarkusComponentTestExtension(MyComponent.class)
            .configProperty("foo", "BAR")
            .mock(Charlie.class).create(c -> {
                Charlie mock = Mockito.mock(Charlie.class);
                Mockito.when(mock.ping()).thenReturn("foo");
                return mock;
            });

    @Inject
    MyComponent myComponent;

    @Test
    public void testComponent1() {
        assertEquals("foo and BAR", myComponent.ping());
    }

    @Test
    public void testComponent2() {
        assertEquals("foo and BAR", myComponent.ping());
    }

}
