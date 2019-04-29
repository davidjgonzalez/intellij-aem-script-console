package com.github.davidjgonzalez.intellij.aemscriptconsole.configuration;

import com.github.davidjgonzalez.intellij.aemscriptconsole.Messages;
import com.intellij.execution.configurations.ConfigurationTypeBase;

import static com.github.davidjgonzalez.intellij.aemscriptconsole.Constants.*;

public class ConfigurationTypeImpl extends ConfigurationTypeBase {
    protected ConfigurationTypeImpl() {
        super(PLUGIN_NAME,
                PLUGIN_DISPLAY_NAME,
                Messages.message("description"),
                PLUGIN_ICON_LARGE);
        addFactory(new ConfigurationFactoryImpl(this));
    }
}
