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
    private String scriptData;

    public ScriptConsoleScript(final Project project, final RunConfigurationImpl runConfiguration) {
        this.document = getDocument(project);

        if (this.document != null) {
            this.file = FileDocumentManager.getInstance().getFile(document);
            this.scriptData = StringUtils.defaultIfBlank(this.document != null ? this.document.getText() : null, Messages.message("script.document.empty"));

            parseParameters(this.scriptData);

            if ("java".equals(getExtension())) {
                this.scriptData = processJavaServet(this.scriptData);
            }

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
        return scriptData;
    }

    private Document getDocument(Project project) {
        final FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        final Editor selectedEditor = fileEditorManager.getSelectedTextEditor();

        if (selectedEditor != null) {
            return selectedEditor.getDocument();
        } else {
            return null;
        }
    }

    public String getResourcePath() {
        return StringUtils.defaultIfEmpty(resourcePath, RESOURCE_PATH_DEFAULT);
    }


    private String processJavaServet(String scriptData) {
        if (scriptData.matches("^\\s?package\\s+[^;]+;")) {
            scriptData = scriptData.replaceAll("^\\s?package\\s+[^;]+;",
                    "package apps.acs_002dtools.components.aemfiddle.fiddle;");
        } else {
            scriptData = "package apps.acs_002dtools.components.aemfiddle.fiddle;" +
                    System.lineSeparator()
                    + scriptData;
        }

        scriptData = scriptData.replaceAll("\\s?(public|protected|private)?\\s+class\\s+[a-zA-Z0-9_$]+\\s", "public class fiddle ");

        return scriptData;
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
