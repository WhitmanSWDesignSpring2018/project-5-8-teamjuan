package tunecomposer;

import java.util.Stack;
import javafx.scene.layout.Pane;

class HistoryManager {

    /**
     * Stacks holding different states should the user undo/redo.
     */
    private static Stack<String> undoStack = new Stack<String>();
    private static Stack<String> redoStack = new Stack<String>();

    /**
     * Adds current state to the undo stack and removes all states from redo.
     */
    public static void addEvent() {
        FileManager.setUnsaved(true);
        undoStack.push(NoteHandler.createXMLDocString());
        redoStack.removeAllElements();
    }

    /**
     * Tells whether or not the undoStack is empty
     * 
     * @return boolean empty or not empty
     */
    public static boolean undoEmpty() {
        return undoStack.empty();
    }

    /**
     * Tells whether or not the redoStack is empty
     * 
     * @return boolean empty or not empty
     */
    public static boolean redoEmpty() {
        return redoStack.empty();
    }

    /**
     * Un-does the most recent change in the pane.
     * 
     * @param notePane the pane
     */
    public static void undo(Pane notePane) {
        String state = undoStack.pop();
        redoStack.push(NoteHandler.createXMLDocString());
        NoteHandler.restore(state, notePane);
        ButtonHandler.updateAllButtons();
        FileManager.setUnsaved(true);
    }

    /**
     * Re-does the most recently un-done change.
     * 
     * @param notePane the pane
     */
    public static void redo(Pane notePane) {
        String state = redoStack.pop();
        undoStack.push(NoteHandler.createXMLDocString());
        NoteHandler.restore(state, notePane);
        ButtonHandler.updateAllButtons();
        FileManager.setUnsaved(true);
    }

    /**
     * Clears the history on the stacks, updates the state of the undo/redo menu
     * buttons.
     */
    public static void clearHistory() {
        undoStack.clear();
        redoStack.clear();
        ButtonHandler.updateAllButtons();
    }
}