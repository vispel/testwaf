package com.waf.rules.loader;

import com.waf.rules.RuleFile;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.FilterConfig;

public class FilesystemRuleLoader extends FilebasedRuleLoader {

    public static final String PARAM_RULE_FILES_CLASSPATH_REFERENCE = "RuleFilesBasePath";

    @Autowired
    private FilterConfig filterConfig;

    @Override
    public RuleFile[] loadRuleFiles() {
        return new RuleFile[0];
    }
}
