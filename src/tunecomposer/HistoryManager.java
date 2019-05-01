package tunecomposer;

import java.util.Set;
import java.util.Stack;
import javafx.scene.layout.Pane;

class HistoryManager {
    
    /**
     * Stacks holding different states should the user undo/redo.
     */
    private static Stack<Set<Playable>> undoStack = new Stack<Set<Playable>>();
    private static Stack<Set<Playable>> redoStack = new Stack<Set<Playable>>();

    /**
     * Adds current state to the undo stack and removes all states from redo.
     */
    public static void addEvent() {
        undoStack.push(NoteHandler.copyCurrentState());
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
        Set<Playable> state = undoStack.pop();
        redoStack.push(NoteHandler.copyCurrentState());
        NoteHandler.restore(state, notePane);
        ButtonHandler.updateAllButtons();
    }

    /**
     * Re-does the most recently un-done change.
     * @param notePane the pane
     */
    public static void redo(Pane notePane) {
        Set<Playable> state = redoStack.pop();
        undoStack.push(NoteHandler.copyCurrentState());
        NoteHandler.restore(state, notePane);
        ButtonHandler.updateAllButtons();
    }
}