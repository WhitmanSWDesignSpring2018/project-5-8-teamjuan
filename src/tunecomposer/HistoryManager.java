package tunecomposer;

import java.util.EmptyStackException;
import java.util.HashSet;
import java.util.Stack;

import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;

class HistoryManager {
    
    private static Stack<HashSet<Playable>> undoStack = new Stack<HashSet<Playable>>();
    private static Stack<HashSet<Playable>> redoStack = new Stack<HashSet<Playable>>();

    public static void addEvent(MenuItem undoButton, MenuItem redoButton) {
        undoButton.setDisable(false);
        redoButton.setDisable(true);
        HashSet<Playable> currentNotes = new HashSet<Playable>(NoteHandler.allPlayables);
        undoStack.push(currentNotes);
        redoStack.removeAllElements();
    }

    public static boolean undoEmpty() {
        return undoStack.empty();
    }

    public static void undo(Pane notePane, MenuItem undoButton, MenuItem redoButton) {
        HashSet<Playable> state = undoStack.pop();
        redoStack.push(new HashSet<Playable>(NoteHandler.allPlayables));
        restore(state, notePane);
        if (undoStack.isEmpty()) {
            undoButton.setDisable(true);
        }
        redoButton.setDisable(false);
    }

    private static void restore(HashSet<Playable> state, Pane notePane) {
        NoteHandler.allPlayables.forEach((playable) -> {
            notePane.getChildren().removeAll(playable.getAllRectangles());
        });
        NoteHandler.allPlayables.clear();
        NoteHandler.allPlayables = new HashSet<Playable>(state);
        NoteHandler.allPlayables.forEach((playable) -> {
            notePane.getChildren().addAll(playable.getRectangle());
        });
    }

    public static void redo(Pane notePane, MenuItem undoButton, MenuItem redoButton) {
        HashSet<Playable> state = redoStack.pop();
        undoStack.push(new HashSet<Playable>(NoteHandler.allPlayables));
        restore(state, notePane);
        if (redoStack.isEmpty()) {
            redoButton.setDisable(true);
        }
        undoButton.setDisable(false);
    }
}