package tunecomposer;

import javafx.scene.control.MenuItem;

class ButtonHandler {


    public static MenuItem undoButton;
    public static MenuItem redoButton;
    public static MenuItem groupButton;
    public static MenuItem ungroupButton;
    public static MenuItem selectAllButton;
    public static MenuItem deleteButton; 
    public static MenuItem playButton;
    public static MenuItem stopButton;

    public static void setButtons(MenuItem undo, MenuItem redo, MenuItem group, MenuItem ungroup, MenuItem selectAll, MenuItem delete, MenuItem play, MenuItem stop) {
        undoButton = undo;
        redoButton = redo;
        groupButton = group;
        ungroupButton = ungroup;
        selectAllButton = selectAll;
        deleteButton = delete;
        playButton = play;
        stopButton = stop;
    }

}