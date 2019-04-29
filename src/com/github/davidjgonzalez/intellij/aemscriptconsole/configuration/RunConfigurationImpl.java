package com.github.davidjgonzalez.intellij.aemscriptconsole.configuration;

import com.github.davidjgonzalez.intellij.aemscriptconsole.Constants;
import com.github.davidjgonzalez.intellij.aemscriptconsole.Messages;
import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.credentialStore.Credentials;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizerUtil;
import com.intellij.openapi.util.WriteExternalException;
import org.apache.commons.lang3.StringUtils;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = Constants.PLUGIN_NAME,
        storages = {@Storage(Constants.PLUGIN_NAME + "__run-configuration.xml")}
)
public class RunConfigurationImpl extends RunConfigurationBase {
    public static final String KEY_HOST = Constants.PLUGIN_NAME + ".host";
    public static final String KEY_USER = Constants.PLUGIN_NAME + ".user";
    public static final String KEY_PASSWORD = Constants.PLUGIN_NAME + ".password";
    public static final String KEY_RESOURCE_PATH = Constants.PLUGIN_NAME + ".resource-path";

    private String host;
    private String user;
    private String password;
    private String resourcePath;

    public RunConfigurationImpl(Project project, ConfigurationFactory factory, String name) {
        super(project, factory, name);
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new SettingsEditorImpl();
    }

    @Nullable
    @Override
    public SettingsEditor<ConfigurationPerRunnerSettings> getRunnerSettingsEditor(ProgramRunner runner) {
        return null;
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {
        if (!StringUtils.startsWithAny(getHost(), "http://", "https://")) {
            throw new RuntimeConfigurationException(Messages.message("configuration.check.host"));
        }

        if (StringUtils.isBlank(getUser())) {
            throw new RuntimeConfigurationException(Messages.message("configuration.check.user"));
        }

        if (StringUtils.isBlank(getPassword())) {
            throw new RuntimeConfigurationException(Messages.message("configuration.check.password"));
        }

        if (StringUtils.isNotBlank(getResourcePath()) && !StringUtils.startsWith(getResourcePath(), "/")) {
            throw new RuntimeConfigurationException(Messages.message("configuration.check.resource-path"));
        }
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment) throws ExecutionException {
        return null;
    }

    @Override
    public void readExternal(Element element) throws InvalidDataException {
        super.readExternal(element);

        host = JDOMExternalizerUtil.readField(element, KEY_HOST);
        user = JDOMExternalizerUtil.readField(element, KEY_USER);
        resourcePath = JDOMExternalizerUtil.readField(element, KEY_RESOURCE_PATH);
        password = retrievePassword();
    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {
        super.writeExternal(element);

        JDOMExternalizerUtil.writeField(element, KEY_HOST, host);
        JDOMExternalizerUtil.writeField(element, KEY_USER, user);
        JDOMExternalizerUtil.writeField(element, KEY_RESOURCE_PATH, resourcePath);
        storePassword(password);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    /** Safely managed Password **/

    private String retrievePassword() {
        final CredentialAttributes credentialAttributes = createCredentialAttributes(KEY_PASSWORD);
        return PasswordSafe.getInstance().getPassword(credentialAttributes);
    }

    private void storePassword(final String password) {
        final CredentialAttributes credentialAttributes = createCredentialAttributes(KEY_PASSWORD); // see previous sample
        final Credentials credentials = new Credentials(Constants.PLUGIN_NAME, password);
        PasswordSafe.getInstance().set(credentialAttributes, credentials);
    }

    private CredentialAttributes createCredentialAttributes(String key) {
        return new CredentialAttributes(CredentialAttributesKt.generateServiceName(Constants.PLUGIN_NAME, key));
    }
}