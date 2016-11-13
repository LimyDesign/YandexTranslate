package ru.limydesign.plugins.yandex.translate;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.util.prefs.Preferences;

/**
 * Created by Arsen Bespalov on 13.11.2016.
 * Показывает диалоговое окно переводчика.
 */
public class YandexTranslateDialog extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        ResultDialog dialog;
        Preferences preferences;
        try {
            preferences = Preferences.userNodeForPackage(ru.limydesign.plugins.yandex.translate.ResultDialog.class);
            String langFrom = preferences.get("langFrom", "русский");
            String langTo = preferences.get("langTo", "английский");

            dialog = ResultDialog.createDialog("Яндекс.Переводчик", null, null);

            dialog.setFromLangBox(langFrom);
            dialog.setToLangBox(langTo);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
