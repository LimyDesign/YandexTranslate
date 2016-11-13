package ru.limydesign.plugins.yandex.translate.helpers;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.util.ui.UIUtil;
import ru.limydesign.plugins.yandex.translate.YandexTranslateIcons;

import javax.swing.*;

/**
 * @author Arsen Bespalov
 */
public class IdeHelper {
    public static final String INTELLIJ_IDEA_RULEZZZ = "IntellijIdeaRulezzz ";

    private static void showDialog(final Project project, final String message, final String title, final Icon icon) {
        UIUtil.invokeAndWaitIfNeeded(new Runnable() {
            @Override
            public void run() {
                Messages.showMessageDialog(project, message, title, icon);
            }
        });
    }

    public static void showDialog(Project project, String message, String title) {
        showDialog(project, message, title, YandexTranslateIcons.YANDEX_TRANSLATE_ICON);
    }

    public static void logError(String message) {
        message += " (Если вы видите этот баг, то просьба сообщить о нет на почту support@limydesign.ru)";
        Logger.getInstance("").error(message);
    }
}
