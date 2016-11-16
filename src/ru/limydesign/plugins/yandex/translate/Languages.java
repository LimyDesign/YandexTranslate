package ru.limydesign.plugins.yandex.translate;

import org.json.JSONException;

import java.io.IOException;
import java.util.*;

final class Languages {

    /**
     * Содержит все ярлыки поддерживаемых языков используемая для создания пары перевода.
     * Содержит полное имя в качестве ключа (например: "Русский") и ярлык (например: "ru").
     */
    private static HashMap<String, String> langShortcuts = null;

    /**
     * Яндекс.Переводчик допускает переводы с любого на любой язык, но только из поддерживаемого списка.
     * Чтобы получить весь список необходимо вызвать метод {@code getLangs}.
     */
    private static Set<String> langPairs = null;

    private static ResourceBundle MESS = ResourceBundle.getBundle("Messages", Locale.getDefault());

    static {
        try {
            langPairs = YandexTranslateClient.getLangPairs();
        } catch (IOException | JSONException | YandexTranslateException e) {
            throw new RuntimeException(MESS.getString("err_load_lang_pairs") + " " + e.getMessage());
        }

        try {
            langShortcuts = YandexTranslateClient.getLangs();
        } catch (IOException | JSONException | YandexTranslateException e) {
            throw new RuntimeException(MESS.getString("err_load_languages") + " " + e.getMessage());
        }
    }

    static String getTranslatePair(final String from, final String to) {
        final String fromShortcut = langShortcuts.get(from);
        final String toShortcut = langShortcuts.get(to);

        String newPair = (fromShortcut + "-" + toShortcut).intern();

        if (!langPairs.contains(newPair)) {
            throw new NullPointerException(MESS.getString("err_lang_pair"));
        }

        return newPair;
    }

    static List<String> getLangs() {
        ArrayList<String> fullLangs = new ArrayList<>(langShortcuts.keySet());
        Collections.sort(fullLangs);
        return fullLangs;
    }
}
