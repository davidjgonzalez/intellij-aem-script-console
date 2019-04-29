package com.github.davidjgonzalez.intellij.aemscriptconsole.execution;

import com.github.davidjgonzalez.intellij.aemscriptconsole.Constants;
import com.github.davidjgonzalez.intellij.aemscriptconsole.Messages;
import com.github.davidjgonzalez.intellij.aemscriptconsole.configuration.RunConfigurationImpl;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.lang3.StringUtils;

import static com.github.davidjgonzalez.intellij.aemscriptconsole.Constants.RESOURCE_PATH_DEFAULT;

public class ScriptConsoleScript {
    private Document document;
    private VirtualFile file;
    private String resourcePath;

    public ScriptConsoleScript(final Project project, final RunConfigurationImpl runConfiguration) {
        this.document = getDocument(project);

        if (this.document != null) {
            this.file = FileDocumentManager.getInstance().getFile(document);
            parseParameters(getScriptData());

            if (resourcePath == null) {
                resourcePath = runConfiguration.getResourcePath();
            }
        }
    }

    public String getFileName() {
        if (file != null) {
            return file.getName();
        } else {
            return Messages.message("script.document.unsaved");
        }
    }

    public String getExtension() {
        if (file != null) {
            return StringUtils.defaultIfBlank(file.getExtension(), Constants.DEFAULT_EXTENSION);
        } else {
            return Constants.DEFAULT_EXTENSION;
        }
    }

    public String getScriptData() {
        if (document != null) {
            return StringUtils.defaultIfBlank(this.document.getText(), Messages.message("script.document.empty"));
        } else {
            return Messages.message("script.document.missing");
        }
    }

    private Document getDocument(Project project) {
        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        Editor selectedEditor = fileEditorManager.getSelectedTextEditor();

        if (selectedEditor != null) {
            return selectedEditor.getDocument();
        } else {
            return null;
        }
    }

    public String getResourcePath() {
        return StringUtils.defaultIfEmpty(resourcePath, RESOURCE_PATH_DEFAULT);
    }

    private void parseParameters(final String scriptData) {
        final String[] lines = scriptData.split(System.lineSeparator());

        for (final String line : lines) {
            String candidate = null;
            if (StringUtils.startsWith(line, "//")) {
                // Line Java/JavaScript comment
                candidate = StringUtils.trim(StringUtils.substringAfter(line, "//"));
            } else if (StringUtils.startsWith(line, "/*") && StringUtils.endsWith(line, "*/")) {
                // Enclosed Java/JavaScript comment
                candidate = StringUtils.trim(StringUtils.substringBetween(line, "/*", "*/"));
            } else if (StringUtils.startsWith(line, "<!--") && StringUtils.endsWith(line, "-->")) {
                // HTML comment
                candidate = StringUtils.trim(StringUtils.substringBetween(line, "<!--", "-->"));
            }

            if (candidate != null) {
                final String[] keyValue = StringUtils.split(candidate, "=", 2);

                if (keyValue.length == 2) {
                    switch (StringUtils.trim(keyValue[0])) {
                        case "resourcePath":
                            this.resourcePath = StringUtils.trim(keyValue[1]);
                    }
                }
            } else {
                break;
            }
        }
    }
}
