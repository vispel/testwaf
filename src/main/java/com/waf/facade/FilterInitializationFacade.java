package com.waf.facade;

import com.waf.ConfigurationLoader;
import com.waf.LocalLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.waf.utils.consts.FilterParamsName.PARAM_DEBUG;

public class FilterInitializationFacade {


    @Autowired
    private FilterConfig filterConfig;
    @Autowired
    private LocalLogger localLogger;


    private ConfigurationLoader loader;

    private boolean isDebug;

    @PostConstruct
    public void setUp() {
        this.loader = filterConfig::getInitParameter;
        isDebug = getDebugValue();
    }

    public boolean getDebugValue(){
        String debugValue = this.loader.getConfigurationValue(PARAM_DEBUG);
        return StringUtils.isEmpty(debugValue)? Boolean.FALSE : Boolean.valueOf(debugValue);
    }

    public void checkAndInitialize(Map.Entry<String,Object> param) throws UnavailableException {

        String paramName = param.getKey();
        Object paramObject = param.getValue();

        initializeBoolean(param, paramName, paramObject);

        initializeString(param, paramName, paramObject);

        initializeNumber(param, paramName, paramObject);

        initializeClasses(param, paramName, paramObject);

    }

    private void initializeClasses(Map.Entry<String, Object> param, String paramName, Object paramObject) throws UnavailableException {
        if(paramObject instanceof Class) {
            String paramValue = this.loader.getConfigurationValue(paramName);
            try {
                if(!StringUtils.isEmpty(paramValue)) {
                    param.setValue(Class.forName(paramValue));
                }
            } catch (ClassNotFoundException e) {
                throw new UnavailableException("Unable to find" + paramName + " class (" + paramValue + "): " + e.getMessage());
            }
            if (isDebug) localLogger.logLocal(paramName, paramObject);
        }
    }

    private void initializeNumber(Map.Entry<String, Object> param, String paramName, Object paramObject) throws UnavailableException {
        if(paramObject instanceof Number) {
            String paramValue = this.loader.getConfigurationValue(paramName);
            if(!StringUtils.isEmpty(paramValue)) {
                try {
                    initializeShort(param, paramName, paramObject, paramValue);
                    initializeInteger(param,paramName,paramObject,paramValue);
                    initializeLong(param,paramName,paramObject,paramValue);
                } catch(NumberFormatException e) {
                    throw new UnavailableException("Unable to number-parse configured "+ paramName + ": " + paramValue);
                }
            }
            if(isDebug) localLogger.logLocal(paramName, paramObject);
        }
    }

    private void initializeShort(Map.Entry<String, Object> param, String paramName, Object paramObject, String paramValue) throws UnavailableException {
        if(paramObject instanceof Short) {
            if ((Short) paramObject < 0) throw new UnavailableException("Configured" + paramName +" must not be negative: " + paramValue);
            param.setValue(Short.parseShort(paramValue));
        }
    }

    private void initializeInteger(Map.Entry<String, Object> param, String paramName, Object paramObject, String paramValue) throws UnavailableException {
        if(paramObject instanceof Integer) {
            if ((Integer) paramObject < 0) throw new UnavailableException("Configured" + paramName +" must not be negative: " + paramValue);
            param.setValue(Integer.parseInt(paramValue));
        }
    }

    private void initializeLong(Map.Entry<String, Object> param, String paramName, Object paramObject, String paramValue) throws UnavailableException {
        if(paramObject instanceof Long) {
            if ((Long) paramObject < 0) throw new UnavailableException("Configured" + paramName +" must not be negative: " + paramValue);
            param.setValue(Long.parseLong(paramValue));
        }
    }

    private void initializeString(Map.Entry<String, Object> param, String paramName, Object paramObject) {
        if(paramObject instanceof String) {
            String paramValue = this.loader.getConfigurationValue(paramName);
            if(paramValue == null) {
                param.setValue(paramValue);
            }
            if(isDebug) localLogger.logLocal(paramName, paramObject);
        }
    }

    private void initializeBoolean(Map.Entry<String, Object> params, String paramName, Object paramObject) {
        if(paramObject instanceof Boolean) {
            String paramValue = this.loader.getConfigurationValue(paramName);
            if(!StringUtils.isEmpty(paramValue)) {
                params.setValue(Boolean.valueOf(paramValue));
            }
            if(isDebug) localLogger.logLocal(paramName, paramObject);
        }
    }

    public void checkMissingProps(FilterConfig filterConfig, Set<String> requiredProperties) throws ServletException {

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
}
