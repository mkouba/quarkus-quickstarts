package org.acme.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;

import io.quarkus.test.component.QuarkusComponentTestExtension;
import jakarta.inject.Inject;

public class MyComponentV3Test {

    @RegisterExtension
    static final QuarkusComponentTestExtension test = new QuarkusComponentTestExtension(MyComponent.class)
            .mock(Charlie.class).createMockitoMock(charlie -> Mockito.when(charlie.pong()).thenReturn("bar"))
            .configProperty("foo", "BAR");

    @Inject
    MyComponent myComponent;

    @Inject
    Charlie charlie;

    @Test
    public void testComponent() {
        when(charlie.ping()).thenReturn("foo");
        assertEquals("foo and BAR", myComponent.ping());
        assertEquals("bar and BAR", myComponent.pong());
    }

}
