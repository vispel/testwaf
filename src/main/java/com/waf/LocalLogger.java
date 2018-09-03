package com.waf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.FilterConfig;

public class LocalLogger {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private FilterConfig filterConfig;


    public void logLocal(final String msg) {
        logLocal(msg, null);
    }

    public void logLocal(final String paramName, Object o) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] msg = paramName.split("(?=\\p{Upper})");
        int iMax = msg.length - 1;
        for (int i = 0; i<iMax ; i++) {
            stringBuilder.append(String.valueOf(msg[i])).append(' ');
        }
        logLocal(stringBuilder.toString() + o.toString());
    }

    public void logLocal(final String msg, final Exception e) {
        if (e != null) {
            if (filterConfig != null && filterConfig.getServletContext() != null) filterConfig.getServletContext().log(msg, e);
            else {
                logger.info(msg+": "+e);
            }
        } else {
            if (filterConfig != null && filterConfig.getServletContext() != null) filterConfig.getServletContext().log(msg);
            else {
                logger.info(msg);
            }
        }
    }
}
