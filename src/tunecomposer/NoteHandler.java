package tunecomposer;

import java.util.ArrayList;
import java.util.HashSet;

import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class NoteHandler {
        
    /**
     * Constants for playing note in MidiPlayer
     */
    public static final int VOLUME = 127;
    public static final int MAX_PITCH = 128;
    public static final int DEFAULT_DURATION = 100;
    public static final int TRACK = 0;
    
    /**
     * Constants for Rectangle in composition panel
     */
    public static final int RECTHEIGHT = 10;
    public static final int MARGIN = 5;

    public static boolean clickInPane = true;
    private static boolean changeDuration = false;

    /**
     * End time for MidiPlayer
     */
    public static double lastNote = 0;

    protected static HashSet<Playable> allPlayables = new HashSet<Playable>();

    /**
     * Sets selection values for all of the notes
     * @param selected true to select all
     */
    public static void selectAll(boolean selected) {
        allPlayables.forEach((note) -> {
            note.setSelected(selected);
        });
    }

    /**
     * Schedules the set of notes for playing with the midiplayer.
     */
    public static void schedule() {
        allPlayables.forEach((note) -> {
            note.schedule();
            note.updateLastNote();
        });
    }

    public static void delete(Pane notePane) {
        ArrayList<Playable> toDelete = new ArrayList<Playable>();
        allPlayables.forEach((playable) -> {
            if (playable.getSelected()) {
                toDelete.add(playable);
                if( playable.getClass() == Gesture.class )
                    notePane.getChildren().remove(((Gesture)playable).getOuterRectangle());
                notePane.getChildren().removeAll(playable.getRectangle());
            }
        });
        allPlayables.removeAll(toDelete);
    }

    public static void group(Pane notePane, MenuItem undoButton, MenuItem redoButton) {
        HistoryManager.addEvent(undoButton, redoButton);
        Gesture gest = new Gesture();
        HashSet<Playable> toRemove = new HashSet<Playable>();
        NoteHandler.allPlayables.forEach((playable) -> {
            if (playable.getSelected()) {
                gest.addPlayable(playable);
                toRemove.add(playable);
            }
        });

        NoteHandler.allPlayables.add(gest);
        NoteHandler.allPlayables.removeAll(toRemove);
        gest.createRectangle();
        notePane.getChildren().add(gest.getOuterRectangle());
    }

    public static void ungroup(Pane notePane, MenuItem undoButton, MenuItem redoButton) {
        HistoryManager.addEvent(undoButton, redoButton);
        HashSet<Playable> toRemove = new HashSet<Playable>();
        HashSet<Playable> toAdd = new HashSet<Playable>();
        NoteHandler.allPlayables.forEach((playable) -> {
            if( playable.getSelected() && ( playable.getClass() == Gesture.class ) ) {
                toRemove.add(playable);
                toAdd.addAll(((Gesture)playable).getPlayables());
                notePane.getChildren().remove(((Gesture)playable).getOuterRectangle());
            }
        });
        NoteHandler.allPlayables.addAll(toAdd);
        NoteHandler.allPlayables.removeAll(toRemove);
    }

    public static void handleClick(MouseEvent event, Pane notePane, ToggleGroup instrumentToggle, MenuItem undoButton, MenuItem redoButton) {
        HistoryManager.addEvent(undoButton, redoButton);
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
    }

            /**
     * When user presses on a note, set the notes to be selected or 
     * unselected accordingly.
     * @param event mouse click
     * @param note note Rectangle that was clicked
     */
    public static void handleNoteClick(MouseEvent event, Playable note) {
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
    }
    
    /**
     * When user presses on a note, set offsets in each Note in case the user
     * drags the mouse.
     * @param event mouse click
     * @param note note Rectangle that was clicked
     */
    public static void handleNotePress(MouseEvent event, Playable note) {
        changeDuration = note.inLastFive(event);
        NoteHandler.allPlayables.forEach((n) -> {
            if (n.getSelected()) {
                if (changeDuration) {
                    n.onMousePressedLastFive(event);
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
                    n.onMouseDraggedLastFive(event);
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
                    n.onMouseReleasedLastFive(event);
                } else {
                    n.onMouseReleased(event);
                }
            }
        });
        changeDuration = false;
    }
}