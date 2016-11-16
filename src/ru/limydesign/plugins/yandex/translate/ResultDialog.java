package ru.limydesign.plugins.yandex.translate;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public final class ResultDialog extends JFrame {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton buttonSwap;
    private JButton buttonReplace;
    private JComboBox<String> comboBoxFrom;
    private JComboBox<String> comboBoxTo;
    private JEditorPane paneSelected;
    private JEditorPane paneTranslated;
    private JLabel labelCopy;

    private Editor editor;
    private Project project;

    private static ResourceBundle MESS = ResourceBundle.getBundle("Messages", Locale.getDefault());

    private ResultDialog() throws URISyntaxException {
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonReplace);

        setIconImage(new ImageIcon("/icons/yandex_translate.png").getImage());

        final URI uri = new URI(MESS.getString("url"));
        labelCopy.setToolTipText(uri.toString());
        labelCopy.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                open(uri);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                exit();
                enter();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                exit();
            }
        });

        buttonSwap.addActionListener(e -> {
            int from = comboBoxFrom.getSelectedIndex();
            int to = comboBoxTo.getSelectedIndex();
            comboBoxFrom.setSelectedIndex(to);
            comboBoxTo.setSelectedIndex(from);
        });

        buttonReplace.addActionListener(e -> onReplace());

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * Заменяет выделенный в редакторе текст переведенным.
     */
    private void onReplace() {
        final String tranlatedText;
        try {
            tranlatedText = getTranslatedText();
            int start = editor.getSelectionModel().getSelectionStart();
            int end = editor.getSelectionModel().getSelectionEnd();
            Runnable runnable = () -> editor.getDocument().replaceString(start, end, tranlatedText);
            WriteCommandAction.runWriteCommandAction(project, runnable);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Выполняет перевод введенного текста.
     */
    void onOK() {
        String translatedText;
        try {
            String from = (String) comboBoxFrom.getSelectedItem();
            String to = (String) comboBoxTo.getSelectedItem();

            String langPair = Languages.getTranslatePair(from, to);

            String selectedText = getSelectedText();
            translatedText = YandexTranslateClient.translate(selectedText, langPair);

            Preferences preferences = Preferences.userNodeForPackage(ResultDialog.class);
            preferences.put("langFrom", from);
            preferences.put("langTo", to);
        } catch (Exception e) {
            translatedText = e.getMessage();
        }
        setTranslatedText(translatedText);
    }

    /**
     * Закрывает диалоговое окно переводчика.
     */
    private void onCancel() {
        dispose();
    }

    /**
     * Создает диалоговое окно переводчика.
     *
     * @param title Заголовок диалогового окна.
     * @return ResultDialog
     * @throws URISyntaxException Возвращает ошибку в случае неправильных запросов в Languages.
     */
    static ResultDialog createDialog(final String title, Editor editor, Project project) throws URISyntaxException {
        ResultDialog dialog = new ResultDialog();
        dialog.setTitle(title);
        if (editor != null && project != null) {
            dialog.setEditor(editor);
            dialog.setProject(project);
        } else {
            dialog.buttonReplace.setEnabled(false);
        }

        Languages.getLangs().stream().filter(s -> s != null).forEach(s -> {
            dialog.comboBoxFrom.addItem(s);
            dialog.comboBoxTo.addItem(s);
        });

        dialog.pack();
        dialog.setMinimumSize(dialog.getSize());
        dialog.setVisible(true);

        return dialog;
    }

    private void setProject(Project project) {
        this.project = project;
    }

    private void setEditor(Editor editor) {
        this.editor = editor;
    }

    private String getSelectedText() throws BadLocationException {
        int length = paneSelected.getDocument().getLength();
        return paneSelected.getDocument().getText(0, length);
    }

    private String getTranslatedText() throws BadLocationException {
        int length = paneTranslated.getDocument().getLength();
        return paneTranslated.getDocument().getText(0, length);
    }

    private void setTranslatedText(final String s) {
        paneTranslated.setText(s);
    }

    void setSelectedText(final String s) {
        paneSelected.setText(s);
    }

    void setFromLangBox(final String s) {
        this.comboBoxFrom.setSelectedItem(s);
    }

    void setToLangBox(final String s) {
        this.comboBoxTo.setSelectedItem(s);
    }

    private static void open(URI uri) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void enter() {
        Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        setCursor(cursor);
        labelCopy.setText(MESS.getString("copy_text_hover"));
    }

    private void exit() {
        Cursor cursor = Cursor.getDefaultCursor();
        setCursor(cursor);
        labelCopy.setText(MESS.getString("copy_text"));
    }
}