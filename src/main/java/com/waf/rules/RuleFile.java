package com.waf.rules;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Properties;

public class RuleFile implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final Properties properties;

    public RuleFile(final String name, final Properties properties) {
        if (StringUtils.isEmpty(name) || properties == null ) throw new IllegalArgumentException("name and properties must not be null");
        this.name = name;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public Properties getProperties() {
        return properties;
    }

    public String toString() {
        return this.name;
    }
}
