package tunecomposer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    /**
     * End time for MidiPlayer
     */
    public static double lastNote = 0;

    /**
     * Hashset that stores all playables.
     */
    protected static Set<Playable> allPlayables = new HashSet<Playable>();

    /**
     * Duplicates current state of notes
     * 
     * @return HashSet of current Playables
     */
    public static Set<Playable> copyCurrentState() {
        Set<Playable> currentState = new HashSet<Playable>();
        allPlayables.forEach((playable) -> {
            if (playable.getClass() == Gesture.class) {
                currentState.add(new Gesture((Gesture) playable));
            } else {
                currentState.add(new Note((Note) playable));
            }
        });
        return currentState;
    }

    /**
     * Resets the state of the pane(playables) back to the given state.
     * 
     * @param state    the state to be restored
     * @param notePane the pane
     * 
     */
    public static void restore(Set<Playable> state, Pane notePane) {
        allPlayables.forEach((playable) -> {
            notePane.getChildren().removeAll(playable.getAllRectangles());
        });
        allPlayables.clear();
        allPlayables = new HashSet<Playable>(state);
        allPlayables.forEach((playable) -> {
            notePane.getChildren().addAll(playable.getAllRectangles());
        });
    }

    /**
     * Restores the state of the Pane.
     */
    public static void restore(Pane notePane) {
        allPlayables.forEach((playable) -> {
            notePane.getChildren().removeAll(playable.getAllRectangles());
        });
        allPlayables.forEach((playable) -> {
            notePane.getChildren().addAll(playable.getAllRectangles());
        });
    }

    /**
     * Sets selection values for all of the notes
     * 
     * @param selected true to select all
     */
    public static void selectAll(boolean selected) {
        allPlayables.forEach((note) -> {
            note.setSelected(selected);
        });
        ButtonHandler.updateAllButtons();
    }

    /**
     * Schedules the set of notes for playing with the midiplayer.
     */
    public static void schedule() {
        lastNote = 0;
        allPlayables.forEach((note) -> {
            note.schedule();
            note.updateLastNote();
        });
    }

    /**
     * Deletes selected playables.
     * 
     * @param notePane the pane
     */
    public static void delete(Pane notePane) {
        List<Playable> toDelete = new ArrayList<Playable>();
        allPlayables.forEach((playable) -> {
            if (playable.getSelected()) {
                toDelete.add(playable);
                notePane.getChildren().removeAll(playable.getAllRectangles());
            }
        });
        allPlayables.removeAll(toDelete);
        ButtonHandler.updateAllButtons();
    }

    /**
     * Groups selected playables into a gesture.
     * 
     * @param notePane the pane
     */
    public static void group(Pane notePane) {
        HistoryManager.addEvent();
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
        ButtonHandler.updateAllButtons();
    }

    /**
     * Ungroups selected gestures.
     * 
     * @param notePane the pane
     */
    public static void ungroup(Pane notePane) {
        HistoryManager.addEvent();
        HashSet<Playable> toRemove = new HashSet<Playable>();
        HashSet<Playable> toAdd = new HashSet<Playable>();
        NoteHandler.allPlayables.forEach((playable) -> {
            if (playable.getSelected() && (playable.getClass() == Gesture.class)) {
                toRemove.add(playable);
                toAdd.addAll(((Gesture) playable).getPlayables());
                notePane.getChildren().remove(((Gesture) playable).getOuterRectangle());
            }
        });
        NoteHandler.allPlayables.addAll(toAdd);
        NoteHandler.allPlayables.removeAll(toRemove);
        ButtonHandler.updateAllButtons();
    }

    /**
     * Converts composition into XML string
     * 
     * @return composition in XML format
     */
    public static String createXMLDocString() {
        String newString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
        newString += "<Composition>";
        for (Playable playable : NoteHandler.allPlayables) {
            newString += playable.toString();
        }
        newString += "</Composition>";
        return newString;
    }

    /**
     * Converts selected playables in composition into XML string
     * 
     * @return selected playables in XML format
     */
    public static String createXMLClipboardString() {
        String newString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
        newString += "<Composition>";
        for (Playable playable : NoteHandler.allPlayables) {
            if (playable.getSelected()) {
                newString += playable.toString();
            }
        }
        newString += "</Composition>";
        return newString;
    }

}