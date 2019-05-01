/*
 * CS 300-A, 2017S
 */
package tunecomposer;

import java.util.HashSet;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

/**
 * A unique rectangle drawn when selecting notes in an area.
 * 
 * @author Ian Hawkins, Angie Mead
 */
public class SelectionArea {

    /**
     * Fields for SelectionArea Rectangle
     */
    private final Rectangle rect;
    private double originX;
    private double originY;

    /**
     * List of notes being selected by the selection area
     */
    public static HashSet<Playable> selectedNotes = new HashSet<Playable>();
    
    /**
     * Construct SelectionArea object
     * @param newRect Rectangle being passed in
     */
    public SelectionArea(Rectangle newRect) {
        rect = newRect;
    }

    /**
     * Move lower-right corner of selection rectangle with the dragging mouse
     * @param event mouse drag
     */
    public void handleSelectionStartDrag(MouseEvent event) {
        
        startRectangle(event.getX(), event.getY());

        if(!event.isControlDown()){
            NoteHandler.selectAll(false);
        }
    }

    /**
     * Continue to update notes throughout drag. Called from FXML
     * @param event Current value of MouseEvent
     */
    public void handleSelectionContinueDrag(MouseEvent event) {
        update(event.getX(), event.getY());

        NoteHandler.allPlayables.forEach((note) -> {

            // Thanks to Paul for suggesting the `intersects` method.
            if(getRectangle().intersects(note.getBounds())) {
                selectedNotes.add(note);
                note.setSelected(true);
            } else {
                if(selectedNotes.contains(note)) {
                    note.setSelected(false);
                    selectedNotes.remove(note); 
                }
            }
        });
    }
    
    /**
     * Begin to draw selection box rectangle
     * @param initX starting X coordinate
     * @param initY starting Y coordinate
     */
    private void startRectangle(double initX, double initY) {
        rect.setX(initX);
        rect.setY(initY);
        originX = initX;
        originY = initY;
        rect.setVisible(true);
    }
    
    /**
     * Updates selection rectangle as its being dragged
     * @param newX updated rectangle width
     * @param newY updated rectangle height
     */
    public void update(double newX, double newY) {
        if (newX < originX){
            rect.setX(newX);
            rect.setWidth(originX - newX);
        } else{
            rect.setWidth(newX - originX);
        }
        if (newY < originY){
            rect.setY(newY);
            rect.setHeight(originY - newY);
        } else{
            rect.setHeight(newY - originY);
        }
    }
    
    /**
     * Reset selection rectangle after done dragging
     */
    public void endRectangle(){
        rect.setVisible(false);
        rect.setX(0);
        rect.setY(0);
        rect.setWidth(0);
        rect.setHeight(0);
    }
    
    /**
     * Get rectangle object
     * @return rectangle used by SelectionArea
     */
    public Rectangle getRectangle() {
        return rect;
    }
}
