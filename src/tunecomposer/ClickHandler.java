package tunecomposer;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class ClickHandler {

    public static boolean clickInPane = true;
    private static boolean changeDuration = false;

    /**
     * When a user clicks in the pane, creates a note.
     * @param event mouse click
     * @param notePane the pane 
     * @param instrumentToggle ToggleGroup that sets instrument type
     */
    public static void handleClick(MouseEvent event, Pane notePane, ToggleGroup instrumentToggle) {
        HistoryManager.addEvent();
        if (! event.isControlDown()) {
            NoteHandler.selectAll(false);
        }
        RadioButton selectedButton = (RadioButton)instrumentToggle.getSelectedToggle();

        Instrument instrument = Instrument.getInstrument(selectedButton);
        Note note = new Note(event.getX(), event.getY(), instrument);
        
        NoteHandler.allPlayables.add(note);
        
        note.getRectangle().forEach((n) -> {
            notePane.getChildren().add(n);
        });
        ButtonHandler.updateAllButtons();
    }

    /**
     * When user presses on a note, set the notes to be selected or 
     * unselected accordingly.
     * @param event mouse click
     * @param note note Rectangle that was clicked
     */
    public static void handleNoteClick(MouseEvent event, Playable note) {
        HistoryManager.addEvent();
        clickInPane = false;
        boolean control = event.isControlDown();
        boolean selected = note.getSelected();
        if (! control && ! selected) {
            NoteHandler.selectAll(false);
            note.setSelected(true);
        } else if ( control && ! selected) {
            note.setSelected(true);
        } else if (control && selected) {
            note.setSelected(false);
        }
        ButtonHandler.updateAllButtons();
    }
    
    /**
     * When user presses on a note, set offsets in each Note in case the user
     * drags the mouse.
     * @param event mouse click
     * @param note note Rectangle that was clicked
     */
    public static void handleNotePress(MouseEvent event, Playable note) {
        changeDuration = note.clickedOnRightEdge(event);
        NoteHandler.allPlayables.forEach((n) -> {
            if (n.getSelected()) {
                if (changeDuration) {
                    n.onMousePressedRightEdge(event);
                } else {
                    n.onMousePressed(event);
                }
            }
        });
    }
    
    /**
     * When the user drags the mouse on a note Rectangle, move all selected
     * notes
     * @param event mouse drag
     */
    public static void handleNoteDrag(MouseEvent event) {
        NoteHandler.allPlayables.forEach((n) -> {
            if (n.getSelected()) {
                if (changeDuration) {
                    n.onMouseDraggedRightEdge(event);
                } else {
                    n.onMouseDragged(event);
                }
            }
        });
    }
    
    /**
     * When the user stops dragging the mouse, stop moving the selected notes
     * @param event mouse click
     */
    public static void handleNoteStopDragging(MouseEvent event) {
        clickInPane = false;
        NoteHandler.allPlayables.forEach((n) -> {
            if (n.getSelected()) {
                if (changeDuration) {
                    n.onMouseReleasedRightEdge(event);
                } else {
                    n.onMouseReleased(event);
                }
            }
        });
        changeDuration = false;
        ButtonHandler.updateAllButtons();
    }
}