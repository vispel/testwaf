package com.waf;

import com.waf.facade.FilterInitializationFacade;
import com.waf.rules.loader.RuleLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.servlet.*;
import java.util.Map;
import java.util.Set;

public class WafSecurityFilter implements Filter {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Nullable
    private FilterConfig filterConfig;
    private SecurityParams securityParams = new SecurityParams();

    private boolean restartCompletelyOnNextRequest = false;


    @Autowired
    private FilterInitializationFacade filterInitializationFacade;


    public static boolean isDebug = false;


    private RuleLoader ruleLoader;


    public void init(FilterConfig filterConfig) throws ServletException {

        Assert.notNull(filterConfig, "FilterConfig must not be null");
        this.filterConfig = filterConfig;
        isDebug = filterInitializationFacade.getDebugValue();
        try {
            filterInitializationFacade.checkMissingProps(filterConfig, getRequiredParams());
            restartCompletelyWhenRequired();
        } catch (Exception e) {
            // not throwing the exception here in order to init the filter lazy (when the error condition is over and keep the app meanwhile blocked)
            logger.error("Unable to initialize security filter", e);
        }

    }

    private Set<String> getRequiredParams() {
        return securityParams.getRequiredParams().keySet();
    }


    public void registerConfigReloadOnNextRequest() {
        this.restartCompletelyOnNextRequest = true;
    }

    private final Object mutex_restartCompletelyWhenRequired = new Object();
    private void restartCompletelyWhenRequired() throws UnavailableException {
        if (this.restartCompletelyOnNextRequest) {
            synchronized (this.mutex_restartCompletelyWhenRequired) {
                if (this.restartCompletelyOnNextRequest) {
                    try {
                        checkRequirementsAndInitialize();
                        this.restartCompletelyOnNextRequest = false;
                        logger.info("Initialized protection layer");
                    } catch (RuntimeException | UnavailableException e) {
                        this.restartCompletelyOnNextRequest = true;
                        // when a full-restart failed, clean up any open stuff that might have been already initialized during our first init try
                        // to have a clean beginning for the subsequent restart try
                        try { destroy(); } catch (Exception e2) { e2.printStackTrace(); }
                        e.printStackTrace();
                        throw e;
                    }
                }
            }
        }
    }

    private void checkRequirementsAndInitialize() throws UnavailableException {
        for(Map.Entry<String,Object> param: securityParams.getParams().entrySet()){
            filterInitializationFacade.checkAndInitialize(param);
        }
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {

    }

    public void destroy() {

    }
}
