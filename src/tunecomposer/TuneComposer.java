/*
 * CS 300-A, 2017S
 */
package tunecomposer;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.sound.midi.ShortMessage;

/**
 * This JavaFX app lets the user play scales.
 * @author Ian Stewart, Ian Hawkins, Angie Mead, Melissa Kohl
 */
public class TuneComposer extends Application {

     /**
     * A MidiPlayer for all notes to use.
     */
    public static final MidiPlayer PLAYER = new MidiPlayer(100,60);

    /**
     * A list of instrument values to associate with MidiPlayer channels
     */
    private final int[] timbreList = new int[] {0, 6, 12, 19, 21, 24, 40, 60};

    /**
     * A line moves from left to right across the main pane. It crosses each
     * note as that note is played.
     */
    private static PlayLine playLine;
    
    /**
     * Boolean flags to control flow when user clicks in composition panel
     */
    private boolean isDragSelecting = false;

    /**
     * The background of the application.
     */
    @FXML
    private Group background;

    /**
     * The pane in which notes are constructed.
     */
    @FXML
    private Pane notePane;

    /**
     * The pane in which the play line is constructed and plays.
     */
    @FXML
    private AnchorPane playLinePane;

    /**
     * The line wrapped by PlayLine.
     */
    @FXML
    private Line movingLine;

    /**
     * An area for click-and-drag note selection.
     */
    private SelectionArea selection;

    /**
     * Rectangle used in click-and-drag note selection
     */
    @FXML
    private Rectangle selectRect;

    /**
     * A group of sidebar radio buttons for selecting an instrument.
     */
    @FXML
    private ToggleGroup instrumentToggle;
    
    /**
     * A group of menu buttons that can be enabled/disabled
     * depending on what is present in the Pane.
     */
    @FXML
    private MenuItem undoButton;
    @FXML
    private MenuItem redoButton;
    @FXML
    private MenuItem groupButton;
    @FXML
    private MenuItem ungroupButton;
    @FXML
    private MenuItem selectAllButton;
    @FXML
    private MenuItem deleteButton; 
    @FXML
    private MenuItem playButton;
    @FXML
    private MenuItem stopButton; 

    /**
     * Plays notes that have been added.
     * Called when the Play button is clicked.
     */
    public void startPlaying() {
        PLAYER.stop();
        PLAYER.clear();
        for(int i=0; i<8; i++){
            PLAYER.addMidiEvent(ShortMessage.PROGRAM_CHANGE + i, timbreList[i], 0, 0, 0);
        }
        NoteHandler.schedule();

        PLAYER.play();
        playLine.play(NoteHandler.lastNote);
    }

    /**
     * When user selects "Start" menu item, start playing composition
     * @param ignored not used
     */
    @FXML
    public void handleStartPlaying(ActionEvent ignored) {
        startPlaying();
        ButtonHandler.updateAllButtons();
    }

    /**
     * Stops playing composition.
     * Called when the Stop button is clicked. Does not remove notes from the
     * screen or from allNotes.
     */
    public void stopPlaying() {
        PLAYER.stop();
        playLine.stop();

    }

    /**
     * When the user selects "Stop" menu item, stop playing composition
     * @param ignored not used
     */
    @FXML
    protected void handleStopPlaying(ActionEvent ignored) {
        stopPlaying();
        ButtonHandler.updateAllButtons();
    }

    /**
     * When the user selects the "Exit" menu item, exit the program.
     * @param event the menu selection event
     */
    @FXML
    protected void handleExitMenuItemAction(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Un-does the last action when the user selects "Undo".
     * @param event the menu selection event
     */
    @FXML
    protected void handleUndo(ActionEvent event) {
        HistoryManager.undo(notePane);
    }

    /**
     * Re-does the last action when the user selects "Redo".
     * @param event the menu selection event
     */
    @FXML
    protected void handleRedo(ActionEvent event) {
        HistoryManager.redo(notePane);
    }

    /**
     * Initializes FXML. Called automatically.
     * (1) adds 127 gray lines to background
     * (2) initializes the playLine(set to invisible)
     */
    public void initialize() {
        // Add gray lines to background
        for (int i = 1; i < 128; i++) {
            Line row = new Line(0, 10 * i, 2000, 10 * i);
            row.getStyleClass().add("row-divider");
            background.getChildren().add(row);
        }

        playLine = new PlayLine(movingLine);

        // Let mouse events go through to notePane
        playLinePane.setMouseTransparent(true);

        selection = new SelectionArea(selectRect);

        ButtonHandler.setButtons(undoButton, redoButton, 
                                groupButton, ungroupButton, 
                                selectAllButton, deleteButton, 
                                playButton, stopButton,
                                playLine);
    }

    /**
     * Construct a note from a click. Called via FXML.
     * @param event a mouse click
     */
    public void handleClick(MouseEvent event) {
        if (playLine.isPlaying()) {
            stopPlaying();
        } else if (isDragSelecting){
            isDragSelecting = false;
            selection.endRectangle();
        } else if (NoteHandler.clickInPane) {
            NoteHandler.handleClick(event, notePane, instrumentToggle);
        }
        NoteHandler.clickInPane = true;
        ButtonHandler.updateAllButtons();
    }

    /**
     * Automatically-called when user drags mouse. Stops playing if composition
     * is playing, and starts dragging selection rectangle if mouse click is
     * not on a Playable note.
     * @param event mouse click
     */
    public void startDrag(MouseEvent event) {
        if (playLine.isPlaying()) {
            stopPlaying();
            playButton.setDisable(true);
        } else if (NoteHandler.clickInPane) {
            selection.handleSelectionStartDrag(event);
            isDragSelecting = true;
        }
    }

    /**
     * Automatically-called when user drags mouse. Stops playing if composition
     * is playing, and continues to drag selection rectangle if initial mouse 
     * click was not on a Playable note.
     * @param event mouse click
     */
    public void continueDrag(MouseEvent event) {
        if (playLine.isPlaying()) {
            stopPlaying();
        } else if (NoteHandler.clickInPane) {
            selection.handleSelectionContinueDrag(event);
        }
    }

    /**
    * Groups playables together as a gesture.  Called from FXML.
    * @param event the menu selection event
    */
    @FXML
    private void handleGroup(ActionEvent event) {
        NoteHandler.group(notePane);
    }

    /**
    * Ungroups playables.  Called from FXML.
    * @param event the menu selection event
    */
    @FXML
    private void handleUngroup(ActionEvent event) {
        NoteHandler.ungroup(notePane);
    }
    
    /**
     * Delete all selected notes. Called from FXML.
     * @param event the menu selection event
     */
    @FXML
    private void handleDelete(ActionEvent event) {
        HistoryManager.addEvent();
        NoteHandler.delete(notePane); 
        ButtonHandler.updateAllButtons();
    }
    
    /**
     * Select all notes. Called from FXML.
     * @param event the menu selection event
     */
    @FXML
    private void handleSelectAll(ActionEvent event) {
        HistoryManager.addEvent();
        NoteHandler.selectAll(true);
        ButtonHandler.updateAllButtons();
    } 

    /**
     * Construct the scene and start the application.
     * @param primaryStage the stage for the main window
     * @throws java.io.IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("TuneComposer.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("Scale Player");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest((WindowEvent we) -> {
            System.exit(0);
        });
        
        primaryStage.show();
    }


    /**
     * Launch the application.
     * @param args the command line arguments are ignored
     */
    public static void main(String[] args) {
        launch(args);
    }

}
