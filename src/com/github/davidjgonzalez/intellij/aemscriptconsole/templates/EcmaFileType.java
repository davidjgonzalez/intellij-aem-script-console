package com.github.davidjgonzalez.intellij.aemscriptconsole.templates;

import com.intellij.icons.AllIcons;
import com.intellij.lang.html.HTMLLanguage;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class EcmaFileType extends LanguageFileType {
    public static final String EXTENSION = "ecma";

    @NonNls
    public static final EcmaFileType INSTANCE = new EcmaFileType();

    protected EcmaFileType() {
        super(HTMLLanguage.INSTANCE);
    }

    @Override
    @NotNull
    public String getName() {
        return "AEM Script (ECMA)";
    }

    @Override
    @NotNull
    public String getDescription() {
        return "ECMA for use with AEM Script Console";
    }

    @Override
    @NotNull
    public String getDefaultExtension() {
        return EXTENSION;
    }

    @Override
    public Icon getIcon() {
        return AllIcons.FileTypes.JavaScript;
    }
}