package ru.limydesign.plugins.yandex.translate;

/**
 * Created by Arsen Bespalov on 12.11.2016.
 */
public class Splitter {

    static String split(final String text) {
        String splitedString = splitDollar(text);
        splitedString = splitUnderscore(splitedString);
        splitedString = splitCamelCase(splitedString);
        return splitedString;
    }

    private static String splitCamelCase(String text) {
        StringBuilder builder = new StringBuilder();
        String regExp = "(?<!(^[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])";

        for (String subString : text.split(regExp)) {
            char lastChar = subString.charAt(subString.length() - 1);
            builder.append(subString);
            if (lastChar != ' ') {
                builder.append(" ");
            }
        }
        return builder.toString().trim();
    }

    private static String splitUnderscore(String text) {
        text = text.replace("_", " ");
        return text.trim();
    }

    private static String splitDollar(String text) {
        text = text.replace("$", " ");
        return text.trim();
    }
}
