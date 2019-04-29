package com.github.davidjgonzalez.intellij.aemscriptconsole.configuration;

import com.github.davidjgonzalez.intellij.aemscriptconsole.Constants;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;

public class ConfigurationFactoryImpl extends ConfigurationFactory {
    private static final String FACTORY_NAME = Constants.PLUGIN_DISPLAY_NAME + " configuration factory";

    protected ConfigurationFactoryImpl(ConfigurationType type) {
        super(type);
    }

    @Override
    public RunConfiguration createTemplateConfiguration(Project project) {
        // Called once per project to create the template run configuration.
        return new RunConfigurationImpl(project, this, Constants.PLUGIN_DISPLAY_NAME);
    }

    @Override
    public String getName() {
        return FACTORY_NAME;
    }
}