package com.github.davidjgonzalez.intellij.aemscriptconsole;

import com.intellij.AbstractBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

public class Messages extends AbstractBundle {
    private static final String BUNDLE_NAME = "messages";

    private static final Messages INSTANCE = new Messages();

    private Messages() {
        super(BUNDLE_NAME);
    }

    public static String message(@NotNull @PropertyKey(resourceBundle = BUNDLE_NAME) String key, @NotNull Object... params) {
        return INSTANCE.getMessage(key, params);
    }
}