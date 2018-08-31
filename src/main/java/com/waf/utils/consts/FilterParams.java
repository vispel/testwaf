package com.waf.utils.consts;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class FilterParams {

    public static Set<String> REQUIRED_FILTER_PARAMS;

    public static final String PARAM_APPLICATION_NAME = "ApplicationName";
    public static final String PARAM_CHARACTER_ENCODING = "CharacterEncoding";
    public static final String PARAM_RULE_FILES_CLASSPATH_REFERENCE = "RuleFilesClasspathReference";
    public static final String PARAM_DEBUG = "Debug";
    public static final String PARAM_PRODUCTION_MODE = "ProductionMode";

    static {

        Set<String> requiredParams = new HashSet<>();
        requiredParams.add(FilterParams.PARAM_DEBUG);
        requiredParams.add(FilterParams.PARAM_PRODUCTION_MODE);
        requiredParams.add(FilterParams.PARAM_APPLICATION_NAME);
        requiredParams.add(FilterParams.PARAM_CHARACTER_ENCODING);
        requiredParams.add(FilterParams.PARAM_RULE_FILES_CLASSPATH_REFERENCE);
        REQUIRED_FILTER_PARAMS = Collections.unmodifiableSet(requiredParams);

    }
    
    public static final Set<String> OPTIONAL_FILTER_PARAMS;

    static {

        Set<String> optionalParams = new HashSet<>();
        optionalParams.add("ShowTimings");
        optionalParams.add("BlockAttackingClientsThreshold");
        optionalParams.add("RedirectWelcomePage");
        optionalParams.add("SessionTimeoutRedirectPage");
        optionalParams.add("LogSessionValuesOnAttack");
        optionalParams.add("LearningModeAggregationDirectory");
        optionalParams.add("ClusterBroadcastPeriod");
        optionalParams.add("ClusterInitialContextFactory");
        optionalParams.add("ClusterJmsProviderUrl");
        optionalParams.add("ClusterJmsConnectionFactory");
        optionalParams.add("ClusterJmsTopic");
        optionalParams.add("HousekeepingInterval");
        optionalParams.add("RuleReloadingInterval");
        OPTIONAL_FILTER_PARAMS = Collections.unmodifiableSet(optionalParams);

    }



}
