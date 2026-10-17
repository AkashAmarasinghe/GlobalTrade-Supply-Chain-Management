package com.globaltrade.interceptor;
import jakarta.interceptor.*;
import java.util.logging.Logger;
@Interceptor public class PerformanceInterceptor {
    private static final Logger LOG=Logger.getLogger(PerformanceInterceptor.class.getName());
    @AroundInvoke public Object measure(InvocationContext ctx) throws Exception {
        long s=System.nanoTime();
        try {
            return ctx.proceed();
        }
        finally {
            long ms=(System.nanoTime()-s)/1_000_000L;
            LOG.info(() -> "PERF "+ctx.getTarget().getClass().getSimpleName()+"."+ctx.getMethod().getName()+" = "+ms+" ms");
        }
    }
}
