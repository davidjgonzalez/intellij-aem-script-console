package com.github.davidjgonzalez.intellij.aemscriptconsole.templates;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

public class JspFileTypeFactory extends FileTypeFactory {
    @Override
    public void createFileTypes(@NotNull final FileTypeConsumer consumer) {
        System.out.println("JSP createFileTypes");
        consumer.consume(JspFileType.INSTANCE, JspFileType.EXTENSION);
    }
}