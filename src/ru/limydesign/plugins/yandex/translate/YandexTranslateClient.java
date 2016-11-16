package ru.limydesign.plugins.yandex.translate;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by Arsen Bespalov on 12.11.2016.
 * Класс {@code YandexTranslateClient} обеспечивает получение поддерживаемых языков для перевода, а также осуществляет
 * процесс запроса на перевод полученного текста.
 */
class YandexTranslateClient {

    private static final String HOST = "https://translate.yandex.net/api/v1.5/tr.json/";
    private static final String KEY = "trnsl.1.1.20161112T111353Z.4839808e5938b81e.4bc6f78781eac7daebd3e3403e92407cead8e8e4";
    private static final Locale LOCALE = Locale.getDefault();
    private static final ResourceBundle MESS = ResourceBundle.getBundle("Messages", LOCALE);

    private enum Method {
        TRANSLATE("translate"),
        GET_LANGS("getLangs");

        private final String value;

        Method(String param) {
            this.value = param;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private enum Param {
        KEY("?key="),
        UI("&ui="),
        LANG_PAIR("&lang="),
        TEXT("&text=");

        private final String value;

        Param(String param) {
            this.value = param;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * Метод осуществляет запрос на перевод к Яндекс.Переводчику.
     *
     * @param text Текст перевода.
     * @param langPair Указание с какого на какой язык переводить.
     * @return Возвращает конечный результат, полученный от Яндекс.Переводчика.
     * @throws YandexTranslateException Вызывается в случает получения кода ошибки от Яндекс.Переводчика.
     * @throws IOException Вызывается в случае отсутсвия каких либо данных.
     */
    public static String translate(final String text, final String langPair) throws YandexTranslateException, IOException, JSONException {
        if (text == null) {
            throw new NullPointerException(MESS.getString("translation_text_null"));
        }
        if (langPair == null) {
            throw new NullPointerException(MESS.getString("lang_pair_null"));
        }

        String encodedText = URLEncoder.encode(text, "UTF-8");

        final String url = HOST + Method.TRANSLATE + Param.KEY + KEY + Param.LANG_PAIR + langPair + Param.TEXT + encodedText;
        JSONObject json = jsonRequest(url);
        try {
            int code = (int)json.get("code");
            isResponseSeccessfull(code);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray langsArray = (JSONArray)json.get("text");

        return (String) langsArray.get(0);
    }

    /**
     * Метод получает данный о поддерживаемых языка для соответствующей локали и возвращает подготовленную переменную.
     *
     * @return @{code HashMap<String, String>} Значение, ключ
     * @throws IOException Исключение
     * @throws JSONException Исключение
     */
    static HashMap<String, String> getLangs() throws IOException, YandexTranslateException, JSONException {
        final String url = HOST + Method.GET_LANGS + Param.KEY + KEY + Param.UI + LOCALE.getLanguage();
        JSONObject json = jsonRequest(url);
        try {
            int code = (int)json.get("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject langs = (JSONObject)json.get("langs");
        HashMap<String, String> langShortcuts = new HashMap<>();
        Iterator<?> keys = langs.keys();
        while (keys.hasNext()) {
            String key = (String)keys.next();
            String name = (String)langs.get(key);
            langShortcuts.put(name, key);
        }
        return langShortcuts;
    }

    /**
     * Метод получает список всех доступных пар языков для перевода.
     *
     * @return возвращает список языков для перевода.
     * @throws IOException Вызывается в случае непредвиденных ошибок.
     * @throws YandexTranslateException Вызывается когда получает ошибку от Яндекс.Переводчика.
     */
    static Set<String> getLangPairs() throws IOException, YandexTranslateException, JSONException {
        final String url = HOST + Method.GET_LANGS + Param.KEY + KEY + Param.UI + LOCALE.getLanguage();
        JSONObject json = jsonRequest(url);

        try {
            int code = (int)json.get("code");
            isResponseSeccessfull(code);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray langsArray = (JSONArray)json.get("dirs");
        Set<String> langs = new HashSet<>();
        for (int i = 0; i < langsArray.length(); i++) {
            langs.add(langsArray.getString(i));
        }

        return langs;
    }



    /**
     * Ответный код считается верным, если получен 200 код, в ином случае отправит ошибку.
     *
     * @param code код ответа сервера
     * @throws YandexTranslateException возвращает расшифровку ошибки
     */
    private static void isResponseSeccessfull(int code) throws YandexTranslateException {
        if (code != YandexTranslateException.ResponseCode.SUCCESS.code) {
            throw new YandexTranslateException(code);
        }
    }

    /**
     * Обеспечивает процесс запроса к URL и возвращает JSONObject
     *
     * @param url строковая переменная содержащая http
     * @return JSONObject с данными полученными от Яндекс.Переводчика
     * @throws IOException случается в случает ошибки
     */
    @NotNull
    private static JSONObject jsonRequest(String url) throws IOException, JSONException {
        InputStream response = null;
        Scanner scanner = null;
        String request = null;

        try {
            URLConnection connection = new URL(url).openConnection();
            response = connection.getInputStream();

            scanner = new Scanner(response, "UTF-8").useDelimiter("\\A");
            request = scanner.hasNext() ? scanner.next() : "";
        } finally {
            if (response != null) {
                response.close();
            }
            if (scanner != null) {
                scanner.close();
            }
        }

        return new JSONObject(request);
    }
}
