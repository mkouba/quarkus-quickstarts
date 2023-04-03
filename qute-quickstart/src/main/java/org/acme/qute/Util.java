package org.acme.qute;

import io.quarkus.qute.TemplateData;

@TemplateData
public class Util {

    public static int getTheRightNumber() {
        return 42;
    }
}
