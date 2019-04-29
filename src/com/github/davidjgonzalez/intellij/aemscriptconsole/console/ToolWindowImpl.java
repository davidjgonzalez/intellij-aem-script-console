package com.github.davidjgonzalez.intellij.aemscriptconsole.console;

import com.github.davidjgonzalez.intellij.aemscriptconsole.execution.ScriptConsoleScript;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;

import static com.github.davidjgonzalez.intellij.aemscriptconsole.Constants.*;

public class ToolWindowImpl {

    public static ConsoleView getNewConsoleView(final Project project, final ScriptConsoleScript script) {
        final ToolWindow toolWindow = getToolWindow(project);
        final ContentManager contentManager = toolWindow.getContentManager();
        final ConsoleView consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();

        final Content content = contentManager.getFactory().createContent(
                consoleView.getComponent(),
                script.getFileName(),
                true);

        // A new tabs to front of the list
        contentManager.addContent(content, 0);

        // Make the new content selected (focused)
        contentManager.setSelectedContent(content);

        return consoleView;
    }


    public static ToolWindow getToolWindow(final Project project) {
        final ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);

        ToolWindow toolWindow = toolWindowManager.getToolWindow(PLUGIN_TOOL_WINDOW_ID);

        if (toolWindow == null) {
            toolWindow = toolWindowManager.registerToolWindow(PLUGIN_TOOL_WINDOW_ID, true, ToolWindowAnchor.BOTTOM);
        }

        toolWindow.setIcon(PLUGIN_ICON_MEDIUM);
        toolWindow.show(null);

        return toolWindow;
    }
}
