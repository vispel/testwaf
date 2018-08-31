package com.waf;

import com.waf.utils.consts.FilterParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class WafSecurityFilter implements Filter {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Nullable
    private FilterConfig filterConfig;
    private boolean restartCompletelyOnNextRequest = false;

    public void init(FilterConfig filterConfig) throws ServletException {
        Assert.notNull(filterConfig, "FilterConfig must not be null");
        this.filterConfig = filterConfig;

        try {
            checkMissingProps(filterConfig, FilterParams.REQUIRED_FILTER_PARAMS);
            restartCompletelyWhenRequired();
        } catch (Exception e) {
            // not throwing the exception here in order to init the filter laziy (when the error condition is over and keep the app meanwhile blocked)
            logger.error("Unable to initialize security filter", e);
        }



    }

    private static void checkMissingProps(FilterConfig filterConfig, Set<String> requiredProperties) throws ServletException {

        Set<String> missingProps = !CollectionUtils.isEmpty(requiredProperties) ? new HashSet(requiredProperties) : null;
        Enumeration paramNames = filterConfig.getInitParameterNames();

        while(paramNames.hasMoreElements()) {
            String property = (String)paramNames.nextElement();
            String value = filterConfig.getInitParameter(property);
            if (missingProps != null && missingProps.contains(value)) {
                missingProps.remove(property);
            }
        }

        if(!CollectionUtils.isEmpty(missingProps)) {
            throw new ServletException("Initialization from FilterConfig for filter '" + filterConfig.getFilterName() + "' failed; the following required properties were missing: " + StringUtils.collectionToDelimitedString(missingProps, ", "));
        }
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
        ConfigurationLoader loader = filterConfig::getInitParameter;

    }
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {

    }

    public void destroy() {

    }
}
