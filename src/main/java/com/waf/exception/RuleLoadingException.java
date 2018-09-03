package com.waf.exception;

public class RuleLoadingException extends Exception {
    
    public RuleLoadingException() {
    }
    public RuleLoadingException(String msg) {
        super(msg);
    }
    public RuleLoadingException(Throwable cause) {
        super(cause);
    }
    public RuleLoadingException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
}
