package tunecomposer;

import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;

class ButtonHandler {

    private static MenuItem undoButton;
    private static MenuItem redoButton;
    private static MenuItem groupButton;
    private static MenuItem ungroupButton;
    private static MenuItem selectAllButton;
    private static MenuItem deleteButton;
    private static MenuItem playButton;
    private static MenuItem stopButton;
    private static MenuItem newButton;
    private static MenuItem openButton;
    private static MenuItem saveButton;
    private static MenuItem saveAsButton;
    private static MenuItem cutButton;
    private static MenuItem copyButton;
    private static MenuItem pasteButton;
    private static CheckMenuItem cSharpButton;

    private static PlayLine playline;

    private static int numCurrentlySelected = 0;

    /**
     * Getter method for all MenuItem buttons.
     * 
     * @param MenuItem undo, redo, group ungroup, selectAll, delete, play stop,
     *                 newB, open, save, saveAs, cut, copy, paste, line
     */
    public static void setButtons(MenuItem undo, MenuItem redo, MenuItem group, MenuItem ungroup, MenuItem selectAll,
            MenuItem delete, MenuItem play, MenuItem stop, MenuItem newB, MenuItem open, MenuItem save, MenuItem saveAs,
            MenuItem cut, MenuItem copy, MenuItem paste, CheckMenuItem cSharp, PlayLine line) {
        undoButton = undo;
        redoButton = redo;
        groupButton = group;
        ungroupButton = ungroup;
        selectAllButton = selectAll;
        deleteButton = delete;
        playButton = play;
        stopButton = stop;
        newButton = newB;
        openButton = open;
        stopButton = stop;
        saveButton = save;
        saveAsButton = saveAs;
        cutButton = cut;
        copyButton = copy;
        pasteButton = paste;
        cSharpButton = cSharp;

        playline = line;
    }

    /**
     * Returns if the c sharp button is selected.
     */
    public static boolean getCSharp() {
        return cSharpButton.isSelected();
    }

    /**
     * Updates the state of all relevant buttons.
     */
    public static void updateAllButtons() {
        updateGroupingButtons();
        updateExistenceButtons();
        updatePlayingButtons();
        updateUndoingButtons();
        updateCopyingButtons();
        updateSavingButtons();
    }

    /**
     * Updates undo buttons.
     */
    private static void updateUndoingButtons() {
        if (HistoryManager.undoEmpty()) {
            undoButton.setDisable(true);
        } else {
            undoButton.setDisable(false);
        }
        if (HistoryManager.redoEmpty()) {
            redoButton.setDisable(true);
        } else {
            redoButton.setDisable(false);
        }
    }

    /**
     * Updates grouped buttons.
     */
    private static void updateGroupingButtons() {
        numCurrentlySelected = 0;
        ungroupButton.setDisable(true);
        deleteButton.setDisable(true);
        NoteHandler.allPlayables.forEach((playable) -> {
            if (playable.getSelected()) {
                numCurrentlySelected++;
                deleteButton.setDisable(false);
                if (playable.getClass() == Gesture.class) {
                    ungroupButton.setDisable(false);
                }
            }
        });
        if (numCurrentlySelected > 1) {
            groupButton.setDisable(false);
        } else {
            groupButton.setDisable(true);
        }
    }

    /**
     * Updates the state of buttons.
     */
    private static void updateExistenceButtons() {
        if (numCurrentlySelected == NoteHandler.allPlayables.size()) {
            selectAllButton.setDisable(true);
        } else {
            selectAllButton.setDisable(false);
        }
    }

    /**
     * Updates the state of the Play buttons.
     */
    private static void updatePlayingButtons() {
        if (NoteHandler.allPlayables.size() > 0 && !playline.isPlaying()) {
            playButton.setDisable(false);
        } else {
            playButton.setDisable(true);
        }
        stopButton.setDisable(!playline.isPlaying());
    }

    /**
     * Updates the states of the cut, copy, paste buttons
     */
    private static void updateCopyingButtons() {
        if (numCurrentlySelected > 0) {
            cutButton.setDisable(false);
            copyButton.setDisable(false);
        } else {
            cutButton.setDisable(true);
            copyButton.setDisable(true);
        }

        pasteButton.setDisable(ClipboardManager.isClipboardEmpty());
    }

    /**
     * Updates the state of the save button.
     */
    private static void updateSavingButtons() {
        saveButton.setDisable(!FileManager.getUnsaved());
    }

}