package org.acme.qute;

import io.quarkus.qute.TemplateEnum;

@TemplateEnum
public enum Color {

    RED, GREEN, BLUE;
    
    public static Color favoriteColor() {
        return RED;
    }
    
}
