package tunecomposer;

import javafx.scene.layout.Pane;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class ClipboardManager {

    private static final Clipboard clippy = Clipboard.getSystemClipboard();
    private static final ClipboardContent content = new ClipboardContent();

    public static void cut(Pane notePane) {
        // add undo/redo event
        // put currently selected notes in clipboard
        // delete currently selected notes from pane
        HistoryManager.addEvent();
        copy(notePane);
        NoteHandler.delete(notePane);
    }

    public static void copy(Pane notePane) {
        String str = NoteHandler.createXMLClipboardString();
        content.putString(str);
        clippy.setContent(content);
    }

    public static void paste(Pane notePane) {
        // add undo/redo event
        // get from clipboard and add to pane
        HistoryManager.addEvent();
        TuneParser.parseString(clippy.getString());

    }

}