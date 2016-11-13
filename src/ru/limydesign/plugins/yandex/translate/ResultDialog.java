package ru.limydesign.plugins.yandex.translate;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public final class ResultDialog extends JFrame {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton buttonSwap;
    private JButton buttonReplace;
    private JComboBox comboBoxFrom;
    private JComboBox comboBoxTo;
    private JEditorPane paneSelected;
    private JEditorPane paneTranslated;
    private JLabel labelCopy;

    private OnReplaceListener listener;

    private ResultDialog() throws URISyntaxException {
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonReplace);

//        setIconImage(new ImageIcon(getClass().getResource("res/icon.png")).getImage());

        final URI uri = new URI("http://translate.yandex.ru/");
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

        buttonSwap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int from = comboBoxFrom.getSelectedIndex();
                int to = comboBoxTo.getSelectedIndex();
                comboBoxFrom.setSelectedIndex(to);
                comboBoxTo.setSelectedIndex(from);
            }
        });

        buttonReplace.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onReplace();
            }
        });

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onReplace() {
        final String tranlatedText;
        try {
            tranlatedText = getTranslatedText();
            listener.onReplace(tranlatedText);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void onOK() {
        String translatedText;
        try {
            String from = (String) comboBoxFrom.getSelectedItem();
            String to = (String) comboBoxTo.getSelectedItem();

            String langPair = Languages.getTranslatePair(from, to);

            String selectedText = getSelectedText();
            translatedText = YandexTranslateClient.translate(selectedText, langPair);
        } catch (Exception e) {
            translatedText = e.getMessage();
        }
        setTranslatedText(translatedText);
    }

    private void onCancel() {
        dispose();
    }

    public static ResultDialog createDialog(final String title) throws URISyntaxException {
        ResultDialog dialog = new ResultDialog();
        dialog.setTitle(title);

        for (String s : Languages.getLangs()) {
            dialog.comboBoxFrom.addItem(s);
            dialog.comboBoxTo.addItem(s);
        }

        dialog.pack();
        dialog.setMinimumSize(dialog.getSize());
        dialog.setVisible(true);

        return dialog;
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

    public void setSelectedText(final String s) {
        paneSelected.setText(s);
    }

    public void setFromLangBox(final String s) {
        this.comboBoxFrom.setSelectedItem(s);
    }

    public void setToLangBox(final String s) {
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
        final String text = "<html>Переведено сервисом «Яндекс.Переводчик»</html>";
        Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        setCursor(cursor);
        labelCopy.setText(text);
    }

    private void exit() {
        final String text = "<html><u>Переведено сервисом «Яндекс.Переводчик»</u></html>";
        Cursor cursor = Cursor.getDefaultCursor();
        setCursor(cursor);
        labelCopy.setText(text);
    }
}