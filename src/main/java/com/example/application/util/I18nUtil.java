package com.example.application.util;

import com.vaadin.flow.server.VaadinSession;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class I18nUtil {

    public static String get(String key, Object... params) {
        // Hae kieliasetus nykyisestä sessiosta
        Locale locale = VaadinSession.getCurrent().getLocale();

        // Muodostetaan oikea ResourceBundle -polku
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);

        // Tarkistetaan, että käännös löytyy
        if (bundle == null) {
            throw new RuntimeException("Translation bundle not found for locale: " + locale);
        }

        // Haetaan ja palautetaan käännös
        String message = bundle.getString(key);
        return MessageFormat.format(message, params);
    }
}
