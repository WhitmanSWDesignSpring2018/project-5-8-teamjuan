package tunecomposer;

import javafx.scene.layout.Pane;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class ClipboardManager {

    private static final Clipboard clippy = Clipboard.getSystemClipboard();
    private static final ClipboardContent content = new ClipboardContent();

    /**
     * Takes whatver is copied on the clipboard and removes from the Pane to "cut.""
     * 
     * @param notePane
     */
    public static void cut(Pane notePane) {
        // add undo/redo event
        // put currently selected notes in clipboard
        // delete currently selected notes from pane
        HistoryManager.addEvent();
        copy(notePane);
        NoteHandler.delete(notePane);
    }

    /**
     * Creates an XML string of the selected notes to copy and adds to the
     * clipboard.
     * 
     * @param notePane
     */
    public static void copy(Pane notePane) {
        String str = NoteHandler.createXMLClipboardString();
        content.putString(str);
        clippy.setContent(content);
    }

    /**
     * Takes whatever is on the clipboard and adds to the pasne.
     */
    public static void paste(Pane notePane) {
        HistoryManager.addEvent();
        TuneParser.parseString(clippy.getString());
        NoteHandler.restore(notePane);
    }

}