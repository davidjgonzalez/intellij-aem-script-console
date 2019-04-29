package com.github.davidjgonzalez.intellij.aemscriptconsole.execution;

import com.github.davidjgonzalez.intellij.aemscriptconsole.Messages;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

public class ScriptConsoleOutputTemplate {
    public static final String DEFAULT_TEMPLATE = "default.txt";
    private final Map<Key, String> params;
    private final String template;

    public enum Key {
        HOST,
        USER,
        RESOURCE_PATH,
        START_TIME,
        END_TIME,
        DURATION_IN_SECONDS,
        MESSAGE,
        OUTPUT
    }

    private static final String templateName = DEFAULT_TEMPLATE;

    public ScriptConsoleOutputTemplate(final Map<Key, String> params) throws IOException {
        this.params = params;

        final InputStream is = this.getClass().getResourceAsStream("/outputTemplates/" + templateName);
        if (is != null) {
            this.template = IOUtils.toString(is, Charset.defaultCharset());
        } else {
            this.template = Messages.message("output-template.missing-template");
        }
    }

    public String getOutput() {
        String tmp = this.template;
        for (final Map.Entry<Key, String> entry : params.entrySet()) {
            tmp = StringUtils.replace(tmp, "${" + StringUtils.lowerCase(entry.getKey().name()) + "}", entry.getValue());
        }

        return tmp;
    }
}
