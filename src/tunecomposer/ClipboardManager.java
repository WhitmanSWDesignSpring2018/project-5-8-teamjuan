package tunecomposer;

import javafx.scene.layout.Pane;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class ClipboardManager {

    private static final Clipboard clippy = Clipboard.getSystemClipboard();
    private static final ClipboardContent content = new ClipboardContent();
    private static boolean clipboardEmpty = true;

    /**
     * Takes whatver is copied on the clipboard and removes from the Pane to "cut.""
     * 
     * @param notePane
     */
    public static void cut(Pane notePane) {
        clipboardEmpty = false;
        HistoryManager.addEvent();
        copy(notePane);
        NoteHandler.delete(notePane);
        ButtonHandler.updateAllButtons();
    }

    /**
     * Creates an XML string of the selected notes to copy and adds to the
     * clipboard.
     * 
     * @param notePane
     */
    public static void copy(Pane notePane) {
        clipboardEmpty = false;
        String str = NoteHandler.createXMLClipboardString();
        content.putString(str);
        clippy.setContent(content);
        ButtonHandler.updateAllButtons();
    }

    /**
     * Takes whatever is on the clipboard and adds to the pasne.
     */
    public static void paste(Pane notePane) {
        HistoryManager.addEvent();
        TuneParser.parseString(clippy.getString(), notePane);
        ButtonHandler.updateAllButtons();
    }

    /**
     * Returns whether the clipboard is empty
     * @return true if clipboard is empty, false otherwise
     */
    public static boolean isClipboardEmpty() {
        return clipboardEmpty;
    }
}