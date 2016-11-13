package ru.limydesign.plugins.yandex.translate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Arsen Bespalov on 12.11.2016.
 */
public class YandexTranslateException extends Exception {

    private final ResponseCode response;

    public enum ResponseCode {
        SUCCESS(200, "Операция выполнена успешно"),
        INVAKID_KEY(401, "Неправильный API-ключ"),
        BLOKED_KEY(402, "API-ключ заблокирован"),
        TEXT_LIMIT(404, "Превышено суточное ограничение на объем переведенного текста"),
        MAX_TEXT(413, "Превышен максимально допустимый размер текста"),
        CANT_TRANSLATE(422, "Текст не может быть переведен"),
        INVALID_LANG_PAIR(501, "Заданное направление перевода не поддерживается");

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
            throw new IllegalArgumentException("Нет такого кода в ResponseCode");
        }
        if (response.code == ResponseCode.SUCCESS.code) {
            throw new IllegalArgumentException("Exception не могут быть применены к ResponseCode.SUCCESS.code");
        }
        this.response = response;
    }
}
