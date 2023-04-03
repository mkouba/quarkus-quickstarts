package org.acme.qute;

import java.util.UUID;

import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Named
@Singleton
public class Generator {

    public String newId() {
        return UUID.randomUUID().toString();
    }
    
}
