package ru.limydesign.plugins.yandex.translate;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.net.URISyntaxException;

/**
 * Created by Arsen Bespalov on 13.11.2016.
 * Показывает диалоговое окно переводчика.
 */
public class YandexTranslateDialog extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        ResultDialog dialog;
        try {
            dialog = ResultDialog.createDialog("Яндекс.Переводчик", null);
            dialog.setFromLangBox("русский");
            dialog.setToLangBox("английский");
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
    }
}
