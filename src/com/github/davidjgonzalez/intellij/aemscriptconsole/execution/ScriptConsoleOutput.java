package com.github.davidjgonzalez.intellij.aemscriptconsole.execution;

import com.github.davidjgonzalez.intellij.aemscriptconsole.Messages;
import com.github.davidjgonzalez.intellij.aemscriptconsole.configuration.RunConfigurationImpl;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ScriptConsoleOutput {
    private final static String SIMPLE_DATE_FORMAT_PATTERN = "yyyy-MM-dd h:mm:ss a";
    private final static SimpleDateFormat SDF = new SimpleDateFormat(SIMPLE_DATE_FORMAT_PATTERN);

    private final ScriptConsoleScript script;
    private final RunConfigurationImpl runConfiguration;

    private Calendar startTime;
    private Calendar endTime;
    private int statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

    private String output = Messages.message("output.no-execution");

    public ScriptConsoleOutput(final ScriptConsoleScript script, RunConfigurationImpl runConfiguration) {
        this.startTime = Calendar.getInstance();
        this.script = script;
        this.runConfiguration = runConfiguration;
    }

    public void finish(final int statusCode, final String output) {
        this.endTime = Calendar.getInstance();
        this.statusCode = statusCode;
        this.output = StringUtils.trim(output);
    }

    public void printToConsole(final ConsoleView consoleView) throws IOException {
        endTime = endTime == null ? Calendar.getInstance() : endTime;

        String message;
        ConsoleViewContentType templateFormat;

        final long duration = (endTime.getTimeInMillis() - startTime.getTimeInMillis()) / 1000;

        final Map<ScriptConsoleOutputTemplate.Key, String> params = new HashMap<>();

        params.put(ScriptConsoleOutputTemplate.Key.HOST, runConfiguration.getHost());
        params.put(ScriptConsoleOutputTemplate.Key.USER, runConfiguration.getUser());
        params.put(ScriptConsoleOutputTemplate.Key.RESOURCE_PATH, script.getResourcePath());
        params.put(ScriptConsoleOutputTemplate.Key.START_TIME, SDF.format(this.startTime.getTime()));
        params.put(ScriptConsoleOutputTemplate.Key.END_TIME, SDF.format(this.endTime.getTime()));
        params.put(ScriptConsoleOutputTemplate.Key.DURATION_IN_SECONDS, String.valueOf(duration));

        params.put(ScriptConsoleOutputTemplate.Key.OUTPUT, this.output);

        switch (statusCode) {
            case HttpServletResponse.SC_OK:
                message = Messages.message("output.success");
                templateFormat = ConsoleViewContentType.LOG_INFO_OUTPUT;
                break;
            case HttpServletResponse.SC_UNAUTHORIZED:
                message = Messages.message("output.unauthorized");
                templateFormat = ConsoleViewContentType.LOG_DEBUG_OUTPUT;
                break;
            case HttpServletResponse.SC_CREATED:
                message = Messages.message("output.servlet-missing");
                templateFormat = ConsoleViewContentType.LOG_DEBUG_OUTPUT;
                break;
            default:
                message = Messages.message("output.error");
                templateFormat = ConsoleViewContentType.LOG_ERROR_OUTPUT;
        }

        params.put(ScriptConsoleOutputTemplate.Key.MESSAGE, message);

        final ScriptConsoleOutputTemplate template = new ScriptConsoleOutputTemplate(params);

        consoleView.print(template.getOutput(), templateFormat);
        consoleView.print(this.output, ConsoleViewContentType.LOG_WARNING_OUTPUT);

        consoleView.scrollTo(0);
    }
}
