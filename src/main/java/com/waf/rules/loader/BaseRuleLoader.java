package com.waf.rules.loader;

import org.springframework.util.Assert;

public abstract class BaseRuleLoader implements RuleLoader {

    protected String classpath;

    public void setClasspath(String category) {
        Assert.notNull(category,"path to category must not be null");
        this.classpath = category.trim();
    }

    public String getClasspath() {
        return this.classpath;
    }

}
