package com.github.davidjgonzalez.intellij.aemscriptconsole.templates;

import com.intellij.icons.AllIcons;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class JspFileType extends LanguageFileType {
    public static final String EXTENSION = "jsp";

    @NonNls
    public static final JspFileType INSTANCE = new JspFileType();

    protected JspFileType() {
        super(JavaLanguage.INSTANCE);
    }

    @Override
    @NotNull
    public String getName() {
        return "AEM Script (JSP)";
    }

    @Override
    @NotNull
    public String getDescription() {
        return "JSP for use with AEM Script Console";
    }

    @Override
    @NotNull
    public String getDefaultExtension() {
        return EXTENSION;
    }

    @Override
    public Icon getIcon() {
        return AllIcons.FileTypes.Jsp;
    }
}