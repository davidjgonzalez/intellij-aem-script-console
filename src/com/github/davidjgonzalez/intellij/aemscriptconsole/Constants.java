package com.github.davidjgonzalez.intellij.aemscriptconsole;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public class Constants {

    private Constants() {
    }

    public static final Icon PLUGIN_ICON_SMALL = IconLoader.getIcon("/icons/icon_12x12.svg");
    public static final Icon PLUGIN_ICON_MEDIUM = IconLoader.getIcon("/icons/icon_13x13.svg");
    public static final Icon PLUGIN_ICON_LARGE = IconLoader.getIcon("/icons/icon_16x16.svg");
    public static final Icon PLUGIN_ICON_DEFAULT = PLUGIN_ICON_MEDIUM;


    public static final String PLUGIN_DISPLAY_NAME = "AEM Script Console";
    public static final String PLUGIN_NAME = "aem-script-console";
    public static final String PLUGIN_TOOL_WINDOW_ID = PLUGIN_DISPLAY_NAME;

    public static final String DEFAULT_EXTENSION = "jsp";

    public static final String REQUEST_PARAM_SCRIPT_DATA = "scriptdata";
    public static final String REQUEST_PARAM_SCRIPT_EXTENSION = "scriptext";
    public static final String REQUEST_PARAM_SCRIPT_RESOURCE = "resource";

    public static final String SCRIPT_EXECUTOR_PATH = "/etc/acs-tools/aem-fiddle/_jcr_content.run.html";
    public static final String PACKAGE_MANAGER_API = "/crx/packmgr/service.jsp";

    public static final String RUN_CONFIGURATION_HOST_DEFAULT = "http://localhost:4502";
    public static final String RUN_CONFIGURATION_USER_DEFAULT = "admin";
    public static final String RUN_CONFIGURATION_PASSWORD_DEFAULT = "admin";

    public static final String RESOURCE_PATH_DEFAULT = "/etc/acs-tools/aem-fiddle/jcr:content";
}

