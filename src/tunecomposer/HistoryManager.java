package tunecomposer;
import java.util.HashSet;
import java.util.Stack;
import javafx.scene.layout.Pane;

class HistoryManager {
    
    private static Stack<HashSet> undoStack = new Stack<HashSet>();
    private static Stack<HashSet> redoStack = new Stack<HashSet>();

    public static void addEvent() {
        HashSet<Playable> currentNotes = new HashSet<Playable>(NoteHandler.allPlayables);
        undoStack.push(currentNotes);
        redoStack.removeAllElements();
    }

    public static void undo(Pane notePane) {
        HashSet<Playable> state = undoStack.pop();
        redoStack.push(new HashSet<Playable>(NoteHandler.allPlayables));
        restore(state, notePane);
    }

    private static void restore(HashSet<Playable> state, Pane notePane) {
        NoteHandler.allPlayables.forEach((playable) -> {
            notePane.getChildren().removeAll(playable.getRectangle());
        });
        NoteHandler.allPlayables.clear();
        NoteHandler.allPlayables = new HashSet<Playable>(state);
        NoteHandler.allPlayables.forEach((playable) -> {
            notePane.getChildren().addAll(playable.getRectangle());
        });
    }

    public static void redo(Pane notePane) {
        HashSet<Playable> state = redoStack.pop();
        undoStack.push(new HashSet<Playable>(NoteHandler.allPlayables));
        restore(state, notePane);
    }
}