package com.hq21tl_homework.error_dialog;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

import com.hq21tl_homework.error_dialog.DialogGUI.WindowPanel;

public class ErrorDialog extends JDialog {

    /**
     * Error levels for the ErrorDialog
     */
    public enum ErrorLevel {
        INFO,
        WARNING,
        ERROR,
        FATAL
    }

    /**
     * Should the dialog block the rest of the GUI or not
     */
    public enum DialogBehaviour {
        BLOCKING_DIALOG,
        NON_BLOCKING_DIALOG
    }

    /**
     * What type pf input should the dialog ask from user
     */
    public enum DialogType {
        OK,
        YES_NO,
        YES_NO_CANCEL
    }

    /**
     * The resulting option the user choose
     */
    public enum DialogResult {
        OK,
        YES,
        NO,
        CANCEL,
        CLOSED
    }

    private final Map<ErrorLevel, String> errorLevelIconPaths = Map.of(
            ErrorLevel.INFO, "error_dialog\\info.png",
            ErrorLevel.WARNING, "error_dialog\\warning.png",
            ErrorLevel.ERROR, "error_dialog\\error.png",
            ErrorLevel.FATAL, "error_dialog\\fatal.png"
    );

    @SuppressWarnings("CallToPrintStackTrace") // Cannot show dialog if dialog fails...
    public ImageIcon getResource(ErrorLevel level) {
        String resourcePath = errorLevelIconPaths.get(level);
        if (resourcePath == null) {
            return null;
        }
        InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (resourceStream == null) {
            return null;
        }
        BufferedImage image = null;
        try {
            image = ImageIO.read(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ImageIcon(image);
    }

    /**
     * Setup for a new ErrorDialog
     * errorTitle - Title of the window
     * errorLevel - Severity of the error
     * dialogType - Response from user
     * dialogBehavivoiur - blocking/non-blocking dialog
     * message - Body of the error
     * stackTrace - If the exception that fires the ErrorDialog has stackTrace, and the dev wants to expose it. Set to NULL to hide.
     */
    public record ErrorDialogSettings(
            String errorTitle,
            ErrorLevel errorLevel,
            DialogType dialogType,
            DialogBehaviour dialogBehaviour,
            String message,
            String stackTrace) {

    }

    private final ErrorDialogSettings settings; //NOSONAR // No need to serialize
    public final boolean hasStackTrace;
    private DialogResult dialogResult = DialogResult.CLOSED;

    public void setDialogResult(DialogResult result) {
        dialogResult = result;
    }

    public ErrorDialogSettings getSettings() {
        return settings;
    }

    private void guiBuiler() {
        if (hasStackTrace) {
            setMinimumSize(new Dimension(400, 500));
        } else {
            setMinimumSize(new Dimension(400, 200));
        }
        setMaximumSize(getMinimumSize());

        if (settings.dialogBehaviour == DialogBehaviour.BLOCKING_DIALOG) {
            setModalityType(Dialog.ModalityType.APPLICATION_MODAL); 
        }else {
            setModalityType(Dialog.ModalityType.MODELESS);
        }

        setLayout(new BorderLayout());
        setTitle(settings.errorTitle);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setAlwaysOnTop(true);
        setFocusable(true);
        setLocationRelativeTo(null);

        WindowPanel windowPanel = new WindowPanel(this);
        windowPanel.initialize(this);

        this.pack();
    }

    /**
     * @param settings ErrorDialogSettings containing all parameters for the
     * ErrorDialog
     */
    public ErrorDialog(ErrorDialogSettings settings) {
        this.settings = settings;
        this.hasStackTrace = (settings.stackTrace != null);
        guiBuiler();
    }

    /**
     * Quick ErrorDialog without all settings within ErrorDialogSettings
     *
     * @param level ErrorLevel
     * @param title Title of the dialog window
     * @param message Content of the error This dialog is a non-blocking dialog
     * with OK response, and null stacktrace.
     */
    public ErrorDialog(ErrorLevel level, String title, String message) {
        ErrorDialogSettings presetSettings = new ErrorDialogSettings(
                title,
                level,
                DialogType.OK,
                DialogBehaviour.BLOCKING_DIALOG,
                message,
                null
        );
        this.settings = presetSettings;
        this.hasStackTrace = (presetSettings.stackTrace != null);
        guiBuiler();
    }

    /**
     * Show error to user and get response.
     *
     * @return DialogResult: The user's response
     */
    public DialogResult showError() {
        setVisible(true);
        return dialogResult;
    }
}
