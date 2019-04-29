package com.github.davidjgonzalez.intellij.aemscriptconsole.templates;

import com.intellij.ide.fileTemplates.FileTemplateDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory;

import static com.github.davidjgonzalez.intellij.aemscriptconsole.Constants.PLUGIN_DISPLAY_NAME;
import static com.github.davidjgonzalez.intellij.aemscriptconsole.Constants.PLUGIN_ICON_DEFAULT;

public class FileTemplateGroup implements FileTemplateGroupDescriptorFactory {

    @Override
    public FileTemplateGroupDescriptor getFileTemplatesDescriptor() {
        final FileTemplateDescriptor jspTemplateDescriptor = new FileTemplateDescriptor("AEM Script(jsp).jsp");
        //final FileTemplateDescriptor ecmaTemplateDescriptor = new FileTemplateDescriptor("script.ecma");

        System.out.println("getFileTemplatesDescriptor");
        return new FileTemplateGroupDescriptor(PLUGIN_DISPLAY_NAME, PLUGIN_ICON_DEFAULT, jspTemplateDescriptor);
    }
}
