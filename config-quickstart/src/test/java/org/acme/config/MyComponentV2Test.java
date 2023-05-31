package org.acme.config;

import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;

import io.quarkus.test.component.QuarkusComponentTestExtension;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

public class MyComponentV2Test {

    @RegisterExtension
    static final QuarkusComponentTestExtension test = new QuarkusComponentTestExtension(MyComponent.class)
            .configProperty("foo", "whatever");

    @Inject
    Event<Boolean> event;

    @Inject
    Delta delta;

    @Test
    public void testObserver() {
        event.fire(Boolean.TRUE);
        event.fire(Boolean.FALSE);
        Mockito.verify(delta, times(2)).start();
    }

}
