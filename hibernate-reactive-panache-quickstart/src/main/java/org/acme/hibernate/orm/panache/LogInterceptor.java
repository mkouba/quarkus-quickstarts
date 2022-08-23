package org.acme.hibernate.orm.panache;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.jboss.logging.Logger;

@LogStart
@Interceptor
public class LogInterceptor {

    private static Logger LOG = Logger.getLogger(LogInterceptor.class);

    @AroundInvoke
    public Object aroundInvoke(InvocationContext context) throws Exception {
        LOG.info("\n\nSTART: " + context.getMethod().getName() + "\n\n");
        return context.proceed();
    }

}
