package com.github.davidjgonzalez.intellij.aemscriptconsole.configuration;

import com.github.davidjgonzalez.intellij.aemscriptconsole.Messages;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static com.github.davidjgonzalez.intellij.aemscriptconsole.Constants.*;

public class SettingsEditorImpl extends SettingsEditor<RunConfigurationImpl> {
    private JPanel configurationPanel;

    private JTextField host;
    private JPasswordField password;
    private JTextField user;
    private JButton testConnectionButton;
    private JTextPane testConnectionStatus;
    private JButton installButton;
    private JTextPane installStatus;
    private JTextField resourcePath;

    public SettingsEditorImpl() {
        super();

        testConnectionButton.addActionListener(new TestConnectionClicked());
        installButton.addActionListener(new InstallButtonClicked());
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return configurationPanel;
    }

    private String getHost() {
        return StringUtils.trim(host.getText());
    }

    private String getUser() {
        return StringUtils.trim(user.getText());
    }

    private String getPassword() {
        return StringUtils.trim(new String(password.getPassword()));
    }

    private String getResourcePath() {
        return StringUtils.trim(resourcePath.getText());
    }

    @Override
    protected void resetEditorFrom(RunConfigurationImpl runConfiguration) {
        host.setText(StringUtils.defaultIfBlank(runConfiguration.getHost(), RUN_CONFIGURATION_HOST_DEFAULT));
        user.setText(StringUtils.defaultIfBlank(runConfiguration.getUser(), RUN_CONFIGURATION_USER_DEFAULT));
        password.setText(StringUtils.defaultIfBlank(runConfiguration.getPassword(), RUN_CONFIGURATION_PASSWORD_DEFAULT));
        resourcePath.setText(runConfiguration.getResourcePath());
    }

    @Override
    protected void applyEditorTo(RunConfigurationImpl runConfiguration) throws ConfigurationException {
        runConfiguration.setHost(getHost());
        runConfiguration.setUser(getUser());
        runConfiguration.setPassword(getPassword());
        runConfiguration.setResourcePath(getResourcePath());
    }

    private class TestConnectionClicked implements ActionListener {
        public TestConnectionClicked() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            testConnectionStatus.setText(Messages.message("test-connection.testing"));

            final SwingWorker worker = new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    try {
                        return TestConnectionExecutor.testConnection(getHost(), getUser(), getPassword());
                    } catch (IOException exception) {
                        return Messages.message("test-connection.generic-error");
                    }
                }

                @Override
                public void done() {
                    try {
                        testConnectionStatus.setText(get());
                    } catch (Exception ex) {
                        testConnectionStatus.setText(Messages.message(ex.getMessage()));
                    }
                }
            };

            worker.execute();

        }
    }


    private class InstallButtonClicked implements ActionListener {
        public InstallButtonClicked() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            installStatus.setText(Messages.message("install-on-server.installing"));

            final SwingWorker worker = new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    try {
                        return InstallOnServerExecutor.installOnServer(getHost(),
                                getUser(),
                                getPassword(),
                                InstallOnServerExecutor.downloadPackage());
                    } catch (Exception ex) {
                        return Messages.message(ex.getMessage());
                    }
                }

                @Override
                public void done() {
                    try {
                        installStatus.setText(get());
                    } catch (Exception ex) {
                        installStatus.setText(Messages.message(ex.getMessage()));
                    }
                }
            };

            worker.execute();
        }
    }
}
