package com.waf.rules.loader;

import com.waf.ConfigurationLoader;
import com.waf.exception.RuleLoadingException;
import com.waf.rules.RuleFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.FilterConfig;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ClasspathZipRuleLoader extends FilebasedRuleLoader {

    private static final String PARAM_RULE_FILES_CLASSPATH_REFERENCE = "RuleFilesZipPath";
    private static final String DEFAULT_PATH = "com/waf/rules.zip";

    @Autowired
    private FilterConfig filterConfig;
    @Autowired
    private ConfigurationLoader loader;

    @PostConstruct
    public void setUp () {
        String path = this.loader.getConfigurationValue(PARAM_RULE_FILES_CLASSPATH_REFERENCE);
        this.classpath = StringUtils.isEmpty(path)? DEFAULT_PATH : path;
    }

    @Override
    public RuleFile[] loadRuleFiles() throws RuleLoadingException {
        if (this.classpath == null) throw new IllegalStateException("Path must be set before loading rules files");
        ZipInputStream zipper = null;
        try {
            final List rules = new ArrayList();
            final InputStream input = getClass().getClassLoader().getResourceAsStream(this.classpath);
            if (input == null) throw new FileNotFoundException("Unable to locate zipped rule file on classpath: "+this.classpath);
            zipper = new ZipInputStream( new BufferedInputStream(input) );
            ZipEntry zipEntry;
            do {
                zipEntry = zipper.getNextEntry();
                if (zipEntry != null) {
                    if (!zipEntry.isDirectory()) {
                        String name = zipEntry.getName();
                        if (name != null && isMatchingSuffix(name)) {
                            // remove leading slash if there is one
                            if (name.startsWith("/") && name.length()>1) name = name.substring(1);
                            if (name.startsWith(this.classpath)) { //= OK, we've got a relevant file here
                                final Properties properties = new Properties();
                                properties.load(zipper);
                                rules.add( new RuleFile(name,properties) );
                            }
                        }
                    }
                    zipper.closeEntry();
                }
            } while (zipEntry != null);
            return (RuleFile[])rules.toArray(new RuleFile[0]);
        } catch (Exception e) {
            throw new RuleLoadingException(e);
        } finally {
            if (zipper != null) try { zipper.close(); } catch(IOException ignored) {}
        }
    }
}
