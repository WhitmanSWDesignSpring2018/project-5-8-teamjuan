package tunecomposer;

import java.util.HashSet;
import java.util.Stack;
import javafx.scene.layout.Pane;

class HistoryManager {
    
    private static Stack<HashSet<Playable>> undoStack = new Stack<HashSet<Playable>>();
    private static Stack<HashSet<Playable>> redoStack = new Stack<HashSet<Playable>>();

    public static void addEvent() {
        ButtonHandler.undoButton.setDisable(false);
        ButtonHandler.redoButton.setDisable(true);
        HashSet<Playable> currentNotes = new HashSet<Playable>(NoteHandler.allPlayables);
        undoStack.push(currentNotes);
        redoStack.removeAllElements();
    }

    public static boolean undoEmpty() {
        return undoStack.empty();
    }

    public static void undo(Pane notePane) {
        HashSet<Playable> state = undoStack.pop();
        HashSet<Playable> currentState = new HashSet<Playable>();
        NoteHandler.allPlayables.forEach((playable) -> {
            if (playable.getClass() == Gesture.class) {
                currentState.add(new Gesture((Gesture) playable)); 
            } else { 
                currentState.add(new Note((Note) playable)); 
            }
        });
        redoStack.push(currentState);
        restore(state, notePane);
        if (undoStack.isEmpty()) {
            ButtonHandler.undoButton.setDisable(true);
        }
        ButtonHandler.redoButton.setDisable(false);
    }

    private static void restore(HashSet<Playable> state, Pane notePane) {
        NoteHandler.allPlayables.forEach((playable) -> {
            notePane.getChildren().removeAll(playable.getAllRectangles());
        });
        NoteHandler.allPlayables.clear();
        NoteHandler.allPlayables = new HashSet<Playable>(state);
        NoteHandler.allPlayables.forEach((playable) -> {
            notePane.getChildren().addAll(playable.getAllRectangles());
        });
    }

    public static void redo(Pane notePane) {
        HashSet<Playable> state = redoStack.pop();
        HashSet<Playable> currentState = new HashSet<Playable>();
        NoteHandler.allPlayables.forEach((playable) -> {
            if (playable.getClass() == Gesture.class) {
                currentState.add(new Gesture((Gesture) playable)); 
            } else { 
                currentState.add(new Note((Note) playable)); 
            }
        });
        undoStack.push(currentState);
        restore(state, notePane);
        if (redoStack.isEmpty()) {
            ButtonHandler.redoButton.setDisable(true);
        }
        ButtonHandler.undoButton.setDisable(false);
    }
}