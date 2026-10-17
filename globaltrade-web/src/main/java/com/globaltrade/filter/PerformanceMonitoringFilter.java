package com.globaltrade.filter;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.logging.Logger;
@WebFilter("/*") public class PerformanceMonitoringFilter implements Filter {
    private static final Logger LOG=Logger.getLogger(PerformanceMonitoringFilter.class.getName());
    public void doFilter(ServletRequest req,ServletResponse res,FilterChain chain) throws IOException,ServletException {
        long s=System.nanoTime();
        try {
            chain.doFilter(req,res);
        }
        finally {
            long ms=(System.nanoTime()-s)/1_000_000L;
            LOG.fine("WEB request completed in "+ms+" ms");
        }
    }
}
