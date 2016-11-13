package ru.limydesign.plugins.yandex.translate;

import org.json.JSONException;

import java.io.IOException;
import java.util.*;

/**
 * Created by Arsen Bespalov on 12.11.2016.
 */
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

    static {
        try {
            langPairs = YandexTranslateClient.getLangPairs();
        } catch (IOException | JSONException | YandexTranslateException e) {
            throw new RuntimeException("Не могу загрузить список языков. " + e.getMessage());
        }

        langShortcuts = new HashMap<>();

        langShortcuts.put("азербайджанский", "az");
        langShortcuts.put("албанский", "sq");
        langShortcuts.put("амхарский", "am");
        langShortcuts.put("английский", "en");
        langShortcuts.put("арабский", "ar");
        langShortcuts.put("армянский", "hy");
        langShortcuts.put("африкаанс", "af");
        langShortcuts.put("баскский", "eu");
        langShortcuts.put("башкирский", "ba");
        langShortcuts.put("белорусский", "be");
        langShortcuts.put("бенгальский", "bn");
        langShortcuts.put("болгарский", "bg");
        langShortcuts.put("боснийский", "bs");
        langShortcuts.put("валлийский", "cy");
        langShortcuts.put("венгерский", "hu");
        langShortcuts.put("вьетнамский", "vi");
        langShortcuts.put("гаитянский (креольский)", "ht");
        langShortcuts.put("галисийский", "gl");
        langShortcuts.put("голландский", "nl");
        langShortcuts.put("горномарийский", "mrj");
        langShortcuts.put("греческий", "el");
        langShortcuts.put("грузинский", "ka");
        langShortcuts.put("гуджарати", "gu");
        langShortcuts.put("датский", "da");
        langShortcuts.put("иврит", "he");
        langShortcuts.put("идиш", "yi");
        langShortcuts.put("индонезийский", "id");
        langShortcuts.put("ирландский", "ga");
        langShortcuts.put("итальянский", "it");
        langShortcuts.put("исландский", "is");
        langShortcuts.put("испанский", "es");
        langShortcuts.put("казахский", "kk");
        langShortcuts.put("каннада", "kn");
        langShortcuts.put("каталанский", "ca");
        langShortcuts.put("киргизский", "ky");
        langShortcuts.put("китайский", "zh");
        langShortcuts.put("корейский", "ko");
        langShortcuts.put("коса", "xh");
        langShortcuts.put("латынь", "la");
        langShortcuts.put("латышский", "ly");
        langShortcuts.put("литовский", "lt");
        langShortcuts.put("малагасийский", "mg");
        langShortcuts.put("малайский", "ms");
        langShortcuts.put("малаялам", "ml");
        langShortcuts.put("мальтийский", "mt");
        langShortcuts.put("македонский", "mk");
        langShortcuts.put("маори", "mi");
        langShortcuts.put("маратхи", "mr");
        langShortcuts.put("марийский", "mhr");
        langShortcuts.put("монгольский", "mn");
        langShortcuts.put("немецкий", "de");
        langShortcuts.put("непальский", "ne");
        langShortcuts.put("норвежский", "no");
        langShortcuts.put("панджаби", "pa");
        langShortcuts.put("папьяменто", "pap");
        langShortcuts.put("персидский", "fa");
        langShortcuts.put("персидский", "fa");
        langShortcuts.put("польский", "pl");
        langShortcuts.put("португальский", "pt");
        langShortcuts.put("румынский", "ro");
        langShortcuts.put("русский", "ru");
        langShortcuts.put("себуанский", "ceb");
        langShortcuts.put("сербский", "sr");
        langShortcuts.put("сингальский", "si");
        langShortcuts.put("словацкий", "sk");
        langShortcuts.put("словенский", "sl");
        langShortcuts.put("суахили", "sw");
        langShortcuts.put("сунданский", "su");
        langShortcuts.put("таджикский", "tg");
        langShortcuts.put("тайский", "th");
        langShortcuts.put("тагальский", "tl");
        langShortcuts.put("тамильский", "ta");
        langShortcuts.put("татарский", "tt");
        langShortcuts.put("телугу", "te");
        langShortcuts.put("турецкий", "tr");
        langShortcuts.put("узбекский", "uz");
        langShortcuts.put("украинский", "uk");
        langShortcuts.put("урду", "ur");
        langShortcuts.put("финский", "fi");
        langShortcuts.put("французский", "fr");
        langShortcuts.put("хинди", "hj");
        langShortcuts.put("хорватский", "hr");
        langShortcuts.put("чешский", "cs");
        langShortcuts.put("шведский", "sv");
        langShortcuts.put("шотландский", "gd");
        langShortcuts.put("эстонский", "et");
        langShortcuts.put("эсперанто", "eo");
        langShortcuts.put("яванский", "jv");
        langShortcuts.put("японский", "ja");
    }

    static String getTranslatePair(final String from, final String to) {
        final String fromShortcut = langShortcuts.get(from);
        final String toShortcut = langShortcuts.get(to);

        String newPair = (fromShortcut + "-" + toShortcut).intern();

        if (!langPairs.contains(newPair)) {
            throw new NullPointerException("Нет такой пары языков");
        }

        return newPair;
    }

    static List<String> getLangs() {
        ArrayList<String> fullLangs = new ArrayList<>(langShortcuts.keySet());
        Collections.sort(fullLangs);
        return fullLangs;
    }
}
