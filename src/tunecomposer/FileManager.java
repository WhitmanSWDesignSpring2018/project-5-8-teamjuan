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
    

    public static void newFile(Stage tuneStage, Pane notePane) {
        if (!unsaved) {
            clearComposition(notePane);
        }
        else {
            promptUnsaved(tuneStage, notePane);
        }
        ButtonHandler.updateAllButtons();
    }

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

    public static void save(Stage tuneStage) {
        setUnsaved(false);
        if (current == null) {
            saveAs(tuneStage);
        }
        else {
            saveFile(NoteHandler.createXMLClipboardString(), current);
        }
        ButtonHandler.updateAllButtons();
    }

    private static void saveFile(String content, File file){
        try {
            FileWriter fileWriter = null;
             
            fileWriter = new FileWriter(file, false);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            System.out.println("error during save");
        }
         
    }

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



    public static void setUnsaved(boolean s) {
        unsaved = s;
    }

    public static boolean getUnsaved() {
        return unsaved;
    }

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

    private static void clearComposition(Pane notePane) {
        HistoryManager.clearHistory();
        NoteHandler.allPlayables.forEach((playable) -> {
            notePane.getChildren().removeAll(playable.getAllRectangles());
        });
        NoteHandler.allPlayables.clear();
    }

}