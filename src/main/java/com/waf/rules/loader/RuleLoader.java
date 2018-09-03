package com.waf.rules.loader;

import com.waf.exception.RuleLoadingException;
import com.waf.rules.RuleFile;

public interface RuleLoader {

    void setClasspath(String category);
    String getClasspath();
    RuleFile[] loadRuleFiles() throws RuleLoadingException;
}
