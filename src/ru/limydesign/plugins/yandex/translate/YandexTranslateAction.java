package ru.limydesign.plugins.yandex.translate;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * Created by Arsen Bespalov on 12.11.2016.
 * Получает выделенный текст, открывается диалогвое окно переводчика и автоматически переводит.
 */
public class YandexTranslateAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        Editor data = PlatformDataKeys.EDITOR.getData(e.getDataContext());
        Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        if (data != null) {
            final String selectedText = data.getSelectionModel().getSelectedText();
            if (selectedText != null && selectedText.length() > 0) {
                final String splitedText = Splitter.split(selectedText);
                ResultDialog dialog;
                Preferences preferences;
                Locale currentLocale = Locale.getDefault();
                ResourceBundle mess = ResourceBundle.getBundle("Messages", currentLocale);
                try {
                    preferences = Preferences.userNodeForPackage(ru.limydesign.plugins.yandex.translate.ResultDialog.class);
                    String langFrom = preferences.get("langFrom", mess.getString("langFrom"));
                    String langTo = preferences.get("langTo", mess.getString("langTo"));

                    dialog = ResultDialog.createDialog(mess.getString("title"), data, project);

                    dialog.setSelectedText(splitedText);
                    dialog.setFromLangBox(langFrom);
                    dialog.setToLangBox(langTo);

                    dialog.onOK();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
