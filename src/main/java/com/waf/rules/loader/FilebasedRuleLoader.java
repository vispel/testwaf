package com.waf.rules.loader;

public abstract class FilebasedRuleLoader extends BaseRuleLoader {


    protected static final String SUFFIX = "wcr";

    protected boolean isMatchingSuffix(final String filename) {
        if (filename == null) return false;
        final int pos = filename.lastIndexOf('.');
        if (pos == -1) return false;
        return filename.substring(pos).equalsIgnoreCase(SUFFIX);
    }


}
