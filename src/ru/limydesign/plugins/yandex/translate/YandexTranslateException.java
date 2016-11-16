package ru.limydesign.plugins.yandex.translate;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

class YandexTranslateException extends Exception {

    private final ResponseCode response;
    private static final ResourceBundle MESS = ResourceBundle.getBundle("Messages", Locale.getDefault());

    enum ResponseCode {
        SUCCESS(200, MESS.getString("success")),
        INVALID_KEY(401, MESS.getString("invalid_key")),
        BLOCKED_KEY(402, MESS.getString("blocked_key")),
        TEXT_LIMIT(404, MESS.getString("text_limit")),
        MAX_TEXT(413, MESS.getString("max_text")),
        CANT_TRANSLATE(422, MESS.getString("cant_translate")),
        INVALID_LANG_PAIR(501, MESS.getString("invalid_lang_pair"));

        final int code;
        final String message;

        ResponseCode(int code, String message) {
            this.code = code;
            this.message = message;
        }

        private static final Map<Integer, ResponseCode> map;

        static {
            map = new HashMap<Integer, ResponseCode>();
            for (ResponseCode v: ResponseCode.values()) {
                map.put(v.code, v);
            }
        }

        public static ResponseCode getByCode(int i) {
            return map.get(i);
        }
    }

    YandexTranslateException(int code) {
        ResponseCode response = ResponseCode.getByCode(code);
        if (response == null) {
            throw new IllegalArgumentException(MESS.getString("exception_no_code"));
        }
        if (response.code == ResponseCode.SUCCESS.code) {
            throw new IllegalArgumentException(MESS.getString("not_exception"));
        }
        this.response = response;
    }
}
