package com.waf;
@FunctionalInterface
public interface ConfigurationLoader {

    String getConfigurationValue(final String key);

}
