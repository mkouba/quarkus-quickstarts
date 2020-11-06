package org.acme.qute;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class Footer {

    public String getContents() {
        return "Sponsored by Red Hat";
    }

}
