package com.waf;

import com.waf.rules.loader.ClasspathZipRuleLoader;
import com.waf.rules.loader.RuleLoader;
import com.waf.utils.consts.FilterParamsName;

import java.util.HashMap;
import java.util.Map;

public class SecurityParams {

    private Map<String,Object> requiredParams;
    private Map<String,Object> optionalParams;
    private Map<String,Object> params;
    private Boolean debug = false;

    private Boolean productionMode = true;
    private String applicationName = "DEFAULT_APPLICATION_NAME";
    private String characterEncoding = "UTF-8";

    private RuleLoader ruleLoader = new ClasspathZipRuleLoader();

    private Boolean showTimings = false;
    private String redirectWelcomePage = "";
    private String sessionTimeoutRedirectPage = "";
    private Boolean logSessionValuesOnAttack = false;
    private String learningModeAggregationDirectory = "";
    private Integer blockPeriodMinutes = 20;

    public SecurityParams() {
        requiredParams = new HashMap<>();
        requiredParams.put(FilterParamsName.PARAM_PRODUCTION_MODE, productionMode);
        requiredParams.put(FilterParamsName.PARAM_APPLICATION_NAME, applicationName);
        requiredParams.put(FilterParamsName.PARAM_CHARACTER_ENCODING, characterEncoding);
        requiredParams.put(FilterParamsName.RULE_LOADER, ruleLoader);


        optionalParams = new HashMap<>();
        optionalParams.put(FilterParamsName.PARAM_SHOW_TIMINGS, showTimings);
        optionalParams.put(FilterParamsName.PARAM_REDIRECT_WELCOME_PAGE, redirectWelcomePage);
        optionalParams.put(FilterParamsName.PARAM_SESSION_TIMEOUT_REDIRECT_PAGE, redirectWelcomePage);
        optionalParams.put(FilterParamsName.PARAM_SESSION_TIMEOUT_REDIRECT_PAGE, redirectWelcomePage);
        optionalParams.put(FilterParamsName.PARAM_LOG_SESSION_VALUES_ON_ATTACK, logSessionValuesOnAttack);
        optionalParams.put(FilterParamsName.PARAM_LEARNING_MODE_AGGREGATION_DIRECTORY, learningModeAggregationDirectory);
        optionalParams.put(FilterParamsName.PARAM_BLOCK_ATTACKING_CLIENTS_DURATION, blockPeriodMinutes);


        params = new HashMap<>(requiredParams);
        params.putAll(optionalParams);
    }

    public Map<String, Object> getRequiredParams() {
        return requiredParams;
    }

    public void setRequiredParams(Map<String, Object> requiredParams) {
        this.requiredParams = requiredParams;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public Boolean getDebug() {
        return debug;
    }

    public void setDebug(Boolean debug) {
        this.debug = debug;
    }
}
