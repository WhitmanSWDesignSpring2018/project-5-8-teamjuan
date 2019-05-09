package tunecomposer;

import java.util.Random;
import javafx.scene.layout.Pane;

public class RandomComposition {

    /**
     * Creates a new random composition of between 20 and 119 notes.
     * @param notePane the pane
     */
    public static void createRand(Pane notePane) {
        Random rand = new Random();
        int numNotes = rand.nextInt(100) + 20;
        for(int i = 0; i < numNotes; i++) {
            createRandomNote(notePane);
        }
    }

    /**
     * Creates a new random note.
     * @param notePane the pane
     */
    private static void createRandomNote(Pane notePane) {
        Random rand = new Random();
        double width = rand.nextDouble() * 400 + 5;
        double x = Math.max(0, rand.nextDouble() * 10000 - width);
        double y = rand.nextDouble() * 1280;
        Instrument randInst = Instrument.getRandomInst();
        Note randNote = new Note(x, y, randInst, width);
        placeNote(randNote, notePane);
    }

    /**
     * Places the note on the pane.
     * @param note the note
     * @param notePane the pane
     */
    private static void placeNote(Note note, Pane notePane) {
        NoteHandler.allPlayables.add(note);
        
        note.getRectangle().forEach((n) -> {
            notePane.getChildren().add(n);
        });
    }
}