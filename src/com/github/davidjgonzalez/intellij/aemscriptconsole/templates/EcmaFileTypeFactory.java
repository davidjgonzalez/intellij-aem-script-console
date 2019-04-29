package com.github.davidjgonzalez.intellij.aemscriptconsole.templates;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

public class EcmaFileTypeFactory extends FileTypeFactory {
    @Override
    public void createFileTypes(@NotNull final FileTypeConsumer consumer) {
        consumer.consume(EcmaFileType.INSTANCE, EcmaFileType.EXTENSION);
    }
}