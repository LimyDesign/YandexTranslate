package ru.limydesign.plugins.yandex.translate;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;

import java.net.URISyntaxException;

/**
 * Created by Arsen Bespalov on 12.11.2016.
 * Получает выделенный текст, открывается диалогвое окно переводчика и автоматически переводит.
 */
public class YandexTranslateAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Editor data = PlatformDataKeys.EDITOR.getData(e.getDataContext());
        if (data != null) {
            final String selectedText = data.getSelectionModel().getSelectedText();
            if (selectedText != null && selectedText.length() > 0) {
                final String splitedText = Splitter.split(selectedText);
                ResultDialog dialog;
                try {
                    dialog = ResultDialog.createDialog("Яндекс.Переводчик");

                    dialog.setSelectedText(splitedText);
                    dialog.setFromLangBox("русский");
                    dialog.setToLangBox("английский");

                    dialog.onOK();
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
