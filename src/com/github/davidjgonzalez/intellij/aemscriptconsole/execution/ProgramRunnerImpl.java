package com.github.davidjgonzalez.intellij.aemscriptconsole.execution;

import com.github.davidjgonzalez.intellij.aemscriptconsole.configuration.RunConfigurationImpl;
import com.github.davidjgonzalez.intellij.aemscriptconsole.console.ToolWindowImpl;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class ProgramRunnerImpl implements ProgramRunner {
    @NotNull
    public String getRunnerId() {
        return DefaultRunExecutor.EXECUTOR_ID;
    }

    public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
        return getRunnerId().equals(executorId) && profile instanceof RunConfigurationImpl;
    }

    @Nullable
    @Override
    public RunnerSettings createConfigurationData(ConfigurationInfoProvider configurationInfoProvider) {
        return null;
    }

    @Override
    public void checkConfiguration(RunnerSettings runnerSettings, @Nullable ConfigurationPerRunnerSettings configurationPerRunnerSettings) throws RuntimeConfigurationException {
    }

    @Override
    public void onProcessStarted(RunnerSettings runnerSettings, ExecutionResult executionResult) {
    }

    @Nullable
    @Override
    public SettingsEditor getSettingsEditor(Executor executor, RunConfiguration runConfiguration) {
        return null;
    }

    @Override
    public void execute(@NotNull ExecutionEnvironment executionEnvironment) throws ExecutionException {

        try {
            final RunConfigurationImpl runConfiguration = (RunConfigurationImpl) executionEnvironment.getRunnerAndConfigurationSettings().getConfiguration();
            final Project project = executionEnvironment.getProject();
            final ScriptConsoleScript script = new ScriptConsoleScript(project, runConfiguration);

            final ConsoleView consoleView = ToolWindowImpl.getNewConsoleView(project, script);

            final ScriptConsoleExecutor scriptExecutor = new ScriptConsoleExecutor(script, runConfiguration);

            final ScriptConsoleOutput output = scriptExecutor.execute();

            scriptExecutor.destroy();

            output.printToConsole(consoleView);

        } catch (IOException e) {
            throw new ExecutionException(e.getMessage(), e);
        }
    }

    @Override
    public void execute(@NotNull ExecutionEnvironment executionEnvironment, @Nullable Callback callback) throws ExecutionException {
        execute(executionEnvironment);
    }
}