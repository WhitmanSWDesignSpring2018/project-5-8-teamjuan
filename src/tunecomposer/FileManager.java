package tunecomposer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.ButtonBar;

public class FileManager {

    private static File current = null;

    private static boolean unsaved = false;

    /**
     * Checks if the contents on the Pane are saved, if not prompts user to save and
     * and updates the state of the buttons.
     * 
     * @param tuneStage
     * @param notePane
     */
    public static void newFile(Stage tuneStage, Pane notePane) {
        if (!unsaved) {
            clearComposition(notePane);
        } else {
            promptUnsaved(tuneStage, notePane);
        }
        ButtonHandler.updateAllButtons();
    }

    /**
     * Checks save state of the Pane, loads file from user in to fill the Pane.
     * 
     * @param tuneStage
     * @param notePane
     */
    public static void open(Stage tuneStage, Pane notePane) {
        if (unsaved) {
            promptUnsaved(tuneStage, notePane);
            if (NoteHandler.allPlayables.isEmpty()) {
                openFile(tuneStage, notePane);
            }
        } else {
            clearComposition(notePane);
            openFile(tuneStage, notePane);
        }
        ButtonHandler.updateAllButtons();

    }

    /**
     * Launches the save dialogue box for user to save their composition as a .txt
     * file.
     * 
     * @param tuneStage
     */
    public static void saveAs(Stage tuneStage) {
        FileChooser tuneChooser = new FileChooser();
        tuneChooser.setTitle("Name Composition File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        tuneChooser.getExtensionFilters().add(extFilter);
        File saveFile = tuneChooser.showSaveDialog(tuneStage);
        saveFile(NoteHandler.createXMLClipboardString(), saveFile);
        current = saveFile;
        unsaved = false;
        ButtonHandler.updateAllButtons();
    }

    /**
     * Checks if the state of the Pane is currently saved, then exits program based
     * off of the user's decision.
     * 
     * @param tuneStage
     * @param notePane
     */
    public static void exit(Stage tuneStage, Pane notePane) {
        if (unsaved) {
            promptUnsaved(tuneStage, notePane);
            if (NoteHandler.allPlayables.size() == 0) {
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
    }

    /**
     * Saves the state of the Pane as an XML string.
     * 
     * @param tuneStage
     */
    public static void save(Stage tuneStage) {
        setUnsaved(false);
        if (current == null) {
            saveAs(tuneStage);
        } else {
            saveFile(NoteHandler.createXMLClipboardString(), current);
        }
        ButtonHandler.updateAllButtons();
    }

    /**
     * Saves the .txt file to the computer.
     * 
     * @param content
     * @param file
     */
    private static void saveFile(String content, File file) {
        try {
            FileWriter fileWriter = null;

            fileWriter = new FileWriter(file, false);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            System.out.println("error during save");
        }

    }

    /**
     * Opens file chosen by user and shows its contents onto the Pane.
     * 
     * @param tuneStage
     * @param notePane
     */
    private static void openFile(Stage tuneStage, Pane notePane) {
        FileChooser tuneChooser = new FileChooser();
        tuneChooser.setTitle("Open Composition File");
        tuneChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = tuneChooser.showOpenDialog(tuneStage);
        if (selectedFile != null) {
            TuneParser.parseFile(selectedFile);
            NoteHandler.restore(notePane);
            current = selectedFile;
        }
    }

    /**
     * Denotes the boolean state of the Pane if it is saved at its latest state.
     * 
     * @param bool s
     */
    public static void setUnsaved(boolean s) {
        unsaved = s;
    }

    /**
     * Gets the boolean state of if the Pane is saved.
     * 
     * @return
     */
    public static boolean getUnsaved() {
        return unsaved;
    }

    /**
     * Creates the alert box for the user to choose an option to save their
     * composition.
     * 
     * @param tuneStage
     * @param notePane
     */
    private static void promptUnsaved(Stage tuneStage, Pane notePane) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Current project is modified");
        alert.setHeaderText("Would you like to save me?");
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesButton, noButton, cancelButton);
        alert.showAndWait().ifPresent(type -> {
            if (type == yesButton) {
                save(tuneStage);
                clearComposition(notePane);
            } else if (type == noButton) {
                clearComposition(notePane);
            }
        });
    }

    /**
     * Clears the Pane of its current composition.
     * 
     * @param notePane
     */
    private static void clearComposition(Pane notePane) {
        HistoryManager.clearHistory();
        NoteHandler.allPlayables.forEach((playable) -> {
            notePane.getChildren().removeAll(playable.getAllRectangles());
        });
        NoteHandler.allPlayables.clear();
    }

}