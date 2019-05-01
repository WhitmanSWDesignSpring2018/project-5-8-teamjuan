package tunecomposer;

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

    private static PlayLine playline;

    private static int numCurrentlySelected;

    public static void setButtons(MenuItem undo, MenuItem redo, 
                                  MenuItem group, MenuItem ungroup, 
                                  MenuItem selectAll, MenuItem delete, 
                                  MenuItem play, MenuItem stop,
                                  PlayLine line) {
        undoButton = undo;
        redoButton = redo;
        groupButton = group;
        ungroupButton = ungroup;
        selectAllButton = selectAll;
        deleteButton = delete;
        playButton = play;
        stopButton = stop;

        playline = line;
    }

    public static void updateAllButtons() {
        updateGroupingButtons();
        updateExistenceButtons();
        updatePlayingButtons();
        updateUndoingButtons();
    }

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

    private static void updateGroupingButtons() {
        numCurrentlySelected = 0;
        ungroupButton.setDisable(true);
        NoteHandler.allPlayables.forEach((playable) -> {
            if (playable.getSelected()) {
                numCurrentlySelected++;
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

    private static void updateExistenceButtons() {
        numCurrentlySelected = 0;
        deleteButton.setDisable(true);
        NoteHandler.allPlayables.forEach((playable) -> {
            if (playable.getSelected()) {
                numCurrentlySelected++;
                deleteButton.setDisable(false);
            }
        });
        if (numCurrentlySelected == NoteHandler.allPlayables.size()) {
            selectAllButton.setDisable(true);
        } else {
            selectAllButton.setDisable(false);
        }
    }

    private static void updatePlayingButtons() {
        if (NoteHandler.allPlayables.size() > 0 && ! playline.isPlaying()) {
            playButton.setDisable(false);
        } else {
            playButton.setDisable(true);
        }
        stopButton.setDisable(! playline.isPlaying());
    }

}