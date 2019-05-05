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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import java.io.File;
import java.io.FileWriter;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.io.IOException;

/**
 * This JavaFX app lets the user play scales.
 * 
 * @author Ian Stewart, Ian Hawkins, Angie Mead, Melissa Kohl
 */
public class TuneComposer extends Application {

    /**
     * A MidiPlayer for all notes to use.
     */
    public static final MidiPlayer PLAYER = new MidiPlayer(100, 60);

    /**
     * A list of instrument values to associate with MidiPlayer channels
     */
    private final int[] timbreList = new int[] { 0, 6, 12, 19, 21, 24, 40, 60 };

    /**
     * A line moves from left to right across the main pane. It crosses each note as
     * that note is played.
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
     * The primary stage.
     */
    private Stage tuneStage;
    
    /**
     * A group of menu buttons that can be enabled/disabled depending on what is
     * present in the Pane.
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
    @FXML
    private MenuItem newButton;
    @FXML
    private MenuItem openButton;
    @FXML
    private MenuItem saveButton;
    @FXML
    private MenuItem saveAsButton;
    @FXML
    private MenuItem cutButton;
    @FXML
    private MenuItem copyButton;
    @FXML
    private MenuItem pasteButton;

    /**
     * Plays notes that have been added. Called when the Play button is clicked.
     */
    public void startPlaying() {
        PLAYER.stop();
        PLAYER.clear();
        for (int i = 0; i < 8; i++) {
            PLAYER.addMidiEvent(ShortMessage.PROGRAM_CHANGE + i, timbreList[i], 0, 0, 0);
        }
        NoteHandler.schedule();

        PLAYER.play();
        playLine.play(NoteHandler.lastNote);
    }

    /**
     * When user selects "Start" menu item, start playing composition
     * 
     * @param ignored not used
     */
    @FXML
    public void handleStartPlaying(ActionEvent ignored) {
        startPlaying();
        ButtonHandler.updateAllButtons();
    }

    /**
     * Stops playing composition. Called when the Stop button is clicked. Does not
     * remove notes from the screen or from allNotes.
     */
    public void stopPlaying() {
        PLAYER.stop();
        playLine.stop();

    }

    /**
     * When the user selects "Stop" menu item, stop playing composition
     * 
     * @param ignored not used
     */
    @FXML
    protected void handleStopPlaying(ActionEvent ignored) {
        stopPlaying();
        ButtonHandler.updateAllButtons();
    }

    /**
     * When the user selects the "Exit" menu item, exit the program.
     * 
     * @param event the menu selection event
     */
    @FXML
    protected void handleExitMenuItemAction(ActionEvent event) {
        FileManager.exit(tuneStage, notePane);
    }

    /**
     * Un-does the last action when the user selects "Undo".
     * 
     * @param event the menu selection event
     */
    @FXML
    protected void handleUndo(ActionEvent event) {
        HistoryManager.undo(notePane);
    }

    /**
     * Re-does the last action when the user selects "Redo".
     * 
     * @param event the menu selection event
     */
    @FXML
    protected void handleRedo(ActionEvent event) {
        HistoryManager.redo(notePane);
    }

    /**
     * Initializes FXML. Called automatically. (1) adds 127 gray lines to background
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

        ButtonHandler.setButtons(undoButton, redoButton, groupButton, ungroupButton, selectAllButton, deleteButton,
                playButton, stopButton, newButton, openButton, saveButton, saveAsButton, cutButton, copyButton,
                pasteButton, playLine);
    }

    /**
     * Construct a note from a click. Called via FXML.
     * 
     * @param event a mouse click
     */
    public void handleClick(MouseEvent event) {
        if (playLine.isPlaying()) {
            stopPlaying();
        } else if (isDragSelecting) {
            isDragSelecting = false;
            selection.endRectangle();
        } else if (ClickHandler.clickInPane) {
            ClickHandler.handleClick(event, notePane, instrumentToggle);
        }
        ClickHandler.clickInPane = true;
        ButtonHandler.updateAllButtons();
    }

    /**
     * Automatically-called when user drags mouse. Stops playing if composition is
     * playing, and starts dragging selection rectangle if mouse click is not on a
     * Playable note.
     * 
     * @param event mouse click
     */
    public void startDrag(MouseEvent event) {
        if (playLine.isPlaying()) {
            stopPlaying();
            playButton.setDisable(true);
        } else if (ClickHandler.clickInPane) {
            selection.handleSelectionStartDrag(event);
            isDragSelecting = true;
        }
    }

    /**
     * Automatically-called when user drags mouse. Stops playing if composition is
     * playing, and continues to drag selection rectangle if initial mouse click was
     * not on a Playable note.
     * 
     * @param event mouse click
     */
    public void continueDrag(MouseEvent event) {
        if (playLine.isPlaying()) {
            stopPlaying();
        } else if (ClickHandler.clickInPane) {
            selection.handleSelectionContinueDrag(event);
        }
    }

    /**
     * Groups playables together as a gesture. Called from FXML.
     * 
     * @param event the menu selection event
     */
    @FXML
    private void handleGroup(ActionEvent event) {
        NoteHandler.group(notePane);
    }

    /**
     * Ungroups playables. Called from FXML.
     * 
     * @param event the menu selection event
     */
    @FXML
    private void handleUngroup(ActionEvent event) {
        NoteHandler.ungroup(notePane);
    }

    /**
     * Delete all selected notes. Called from FXML.
     * 
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
     * 
     * @param event the menu selection event
     */
    @FXML
    private void handleSelectAll(ActionEvent event) {
        HistoryManager.addEvent();
        NoteHandler.selectAll(true);
        ButtonHandler.updateAllButtons();
    }

    /**
     * Save the document as a new file. Called from FXML.
     * 
     * @param event the menu selection event
     */
    @FXML
    private void handleSaveAs(ActionEvent event){
        FileManager.saveAs(tuneStage);
    }

    /**
     * Save the document. Called from FXML.
     * 
     * @param event the menu selection event
     */
    @FXML
    private void handleSave(ActionEvent event){
        FileManager.save(tuneStage);
    }

    /**
     * Open a document. Called from FXML.
     * @param event the menu selection event
     */
    @FXML
    private void handleOpen(ActionEvent event) {
        FileManager.open(tuneStage, notePane);
    }

    @FXML
    private void handleNew(ActionEvent event) {
        FileManager.newFile(tuneStage, notePane);
    }

    /**
     * 
     * @param event
     */
    @FXML
    private void handleCut(ActionEvent event) {
        ClipboardManager.cut(notePane);
    }

    /**
     * 
     * @param event
     */
    @FXML
    private void handleCopy(ActionEvent event) {
        ClipboardManager.copy(notePane);
    }

    /**
     * 
     * @param event
     */
    @FXML
    private void handlePaste(ActionEvent event) {
        ClipboardManager.paste(notePane);
    }

    /**
     * Launches the about dialog box. Called from FXML.
     * 
     * @param event the menu selection event
     */
    @FXML
    private void launchAbout(ActionEvent event) {
        Alert about = new Alert(AlertType.INFORMATION);
        about.setTitle("About");
        about.setHeaderText("Welcome to TuneComposer!");
        about.setContentText(
                "To make music with me, simply click on the lined pane to add notes. You can change the duration of the notes by dragging them horizontally and change the pitch by dragging them vertically.\n\nIf you want to group notes together, click on them and hit the Group option to group them into a Gesture.\n\nDon't worry if you make mistakes -- I have undo/redo options and am keeping track of everything that you do. I also have cut/copy/paste functionality so editing your tunes is easy!\n\nIf you want to take a stroll down memory lane with me, you can load in old songs you've written with me. You can also save whatever songs you write as we go along!\n\nOne last thing: if you hate pushing my buttons, you can also control me through key presses. When you look through the menu options, you'll also see how to tell me what to do through the keyboard.\n\nLove,\nMadi Crowley, Melissa Kohl, Michelle Zhang, and Sage Levin\na.k.a Team Juan <3");
        about.showAndWait();
    }

    /**
     * Construct the scene and start the application.
     * 
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
        tuneStage = primaryStage;
    }

    /**
     * Launch the application.
     * 
     * @param args the command line arguments are ignored
     */
    public static void main(String[] args) {
        launch(args);
    }

}
