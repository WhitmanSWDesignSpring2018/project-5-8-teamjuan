package tunecomposer;

import java.util.HashSet;
import java.util.Stack;
import javafx.scene.layout.Pane;

class HistoryManager {
    
    /**
     * Stacks holding different states should the user undo/redo.
     */
    private static Stack<HashSet<Playable>> undoStack = new Stack<HashSet<Playable>>();
    private static Stack<HashSet<Playable>> redoStack = new Stack<HashSet<Playable>>();

    /**
     * Adds current state to the undo stack and removes all states from redo.
     */
    public static void addEvent() {
        HashSet<Playable> currentState = new HashSet<Playable>();
        NoteHandler.allPlayables.forEach((playable) -> {
            if (playable.getClass() == Gesture.class) {
                currentState.add(new Gesture((Gesture) playable)); 
            } else { 
                currentState.add(new Note((Note) playable)); 
            }
        });
        undoStack.push(currentState);
        redoStack.removeAllElements();
    }

    /**
     * Tells whether or not the undoStack is empty
     * @return boolean empty or not empty
     */
    public static boolean undoEmpty() {
        return undoStack.empty();
    }

    /**
     * Tells whether or not the redoStack is empty
     * @return boolean empty or not empty
     */
    public static boolean redoEmpty() {
        return redoStack.empty();
    }

    /**
     * Un-does the most recent change in the pane.
     * @param notePane the pane
     */
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
        ButtonHandler.updateAllButtons();
    }

    /**
     * Resets the state of the pane(playables) back to the given state.
     * @param state the state to be restored
     * @param notePane the pane
     * 
     */
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

    /**
     * Re-does the most recently un-done change.
     * @param notePane the pane
     */
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
        ButtonHandler.updateAllButtons();
    }
}