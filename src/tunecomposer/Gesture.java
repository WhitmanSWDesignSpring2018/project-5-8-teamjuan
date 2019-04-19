
package tunecomposer;

import java.util.ArrayList;
import java.util.HashSet;

import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;


public class Gesture implements Playable {

    /**
    * Fields for the Gesture class.
    */
    private HashSet<Playable> allPlayables;

    private boolean isSelected;

    private MoveableRect outerRect;

    private double upperXBound;

    private double upperYBound;

    private double lowerXBound;

    private double lowerYBound;

    private double margin;

    public Gesture(Gesture gest) {
        upperXBound = 0;
        upperYBound = 0;
        lowerXBound = 3000;
        lowerYBound = 3000;
        margin = 5;
        allPlayables = new HashSet<Playable>();
        gest.getPlayables().forEach((playable) -> {
            if (playable.getClass() == Gesture.class) {
                addPlayable(new Gesture((Gesture) playable)); 
            } else { 
                addPlayable(new Note((Note) playable)); 
            }
        });
        createRectangle();
        setSelected(gest.getSelected());
    }

    /**
    * Constructs a Gesture.
    */
    public Gesture() {
        upperXBound = 0;
        upperYBound = 0;
        lowerXBound = 3000;
        lowerYBound = 3000;
        margin = 5;
        allPlayables = new HashSet<Playable>();
        isSelected = true;
    }

    public ArrayList<Double> getCoords() {
        ArrayList<Double> arr = new ArrayList<Double>();
        arr.add(upperXBound);
        arr.add(upperYBound);
        arr.add(lowerXBound);
        arr.add(lowerYBound);
        return arr;
    }

    /**
    * Sets the margin for the Gesture.
    */
    private void setMargin() {
        double max = 0;
        for (Playable play : allPlayables) {
            max = Math.max(play.getBounds().getMinX(), max);
        }
        margin = max + 5 - outerRect.getX();
    }

    /**
    * Gets the margin value.
    * @return margin 
    */
    private double getMargin() {
        return margin;
    }

    /**
    * Gets bounds and adds to Playable.
    */
    public void addPlayable(Playable play) {
        allPlayables.add(play);
        upperXBound = Math.max(play.getBounds().getMaxX(), upperXBound);
        upperYBound = Math.max(play.getBounds().getMaxY(), upperYBound);
        lowerXBound = Math.min(play.getBounds().getMinX(), lowerXBound);
        lowerYBound = Math.min(play.getBounds().getMinY(), lowerYBound);
    }

    /**
    * Creates the rectangle that groups the Gesture. 
    */
    public void createRectangle() {
        outerRect = new MoveableRect();

        outerRect.setX(lowerXBound);
        outerRect.setY(lowerYBound); 
        outerRect.setWidth(upperXBound - lowerXBound);
        outerRect.setHeight(upperYBound - lowerYBound);
        outerRect.updateInnerFields();
        outerRect.getStyleClass().add("selected-gesture");
        outerRect.setMouseTransparent(false);

        outerRect.setOnMousePressed((MouseEvent pressedEvent) -> {
            NoteHandler.handleNoteClick(pressedEvent, this);
            NoteHandler.handleNotePress(pressedEvent, this);
        }); 

        outerRect.setOnMouseDragged((MouseEvent dragEvent) -> {
            NoteHandler.handleNoteDrag(dragEvent);
        }); 

        outerRect.setOnMouseReleased((MouseEvent releaseEvent) -> {
            NoteHandler.handleNoteStopDragging(releaseEvent);
        });

        setMargin();
    }

    /**
    * Gets the outer rectangle for the moveable rectangle.
    * @return outer rectanlge 
    */
    public MoveableRect getOuterRectangle() {
        return outerRect;
    }

    /**
    * Gets all the Playale values from the HashSet.
    * @return all playables
    */
    public HashSet<Playable> getPlayables() {
        return allPlayables; 
    }

    /**
    * Gets bounds from the outer rectangle.
    * @return outer rectangle bounds
    */
    public Bounds getBounds() {
        return outerRect.getLayoutBounds();
    }

    /**
    * Passes all playables into the schedule method
    * so that they can play.
    */
    public void schedule() {
        allPlayables.forEach((n) -> {
            n.schedule();
        });
    }

    /**
    * Updates each note.
    */ 
    public void updateLastNote(){
        allPlayables.forEach((n) -> {
            n.updateLastNote();
        });
    }

    /**
    * Adds each playable into an array.
    * @return array
    */
    public ArrayList<MoveableRect> getRectangle(){
        ArrayList<MoveableRect> arr = new ArrayList<MoveableRect>();
        allPlayables.forEach((n) -> {
            arr.addAll(n.getRectangle());
        });
        return arr;
    }

    public ArrayList<MoveableRect> getAllRectangles() {
        ArrayList<MoveableRect> temp = new ArrayList<MoveableRect>();
        allPlayables.forEach((n) -> {
            temp.addAll(n.getAllRectangles());
        });
        temp.add(outerRect);
        return temp;
    }

    /**
    * Checks if playable is selected.
    * @return boolean 
    */
    public boolean getSelected(){
        return isSelected;
    }

    /**
    * Selects gestures.
    */
    public void setSelected(boolean selected){
        isSelected = selected;
        if (selected) {
            outerRect.getStyleClass().clear();
            outerRect.getStyleClass().add("selected-gesture");
        } else {
            outerRect.getStyleClass().clear();
            outerRect.getStyleClass().add("unselected-gesture");
        }

        allPlayables.forEach((n) -> {
            n.setSelected(selected);
        });
    }

    /**
    * Returns true/false within the last five of the rectangle.
    * @param event 
    */
    public boolean clickedOnRightEdge(MouseEvent event){
        return outerRect.clickedOnRightEdge(event);
    }
    
    /**
    * Sets the outer rectangle in relationship to the mouse press from user.
    * @param event mouse press
    */
    public void onMousePressed(MouseEvent event){

        outerRect.startLocationChange(event);

        allPlayables.forEach((n) -> {
            n.onMousePressed(event);
        });
    }

    /**
    * Sets the rectangle around the gesture in relationship to
    * the mouse press from the user.
    * @param event mouse press
    */
    public void onMousePressedRightEdge(MouseEvent event){
        
        outerRect.startWidthChange(event); 

        allPlayables.forEach((n) -> {
            n.onMousePressedRightEdge(event);
        });
    }
    
    /**
    * Sets the rectangle around the gesture in relationship to
    * the mouse drag from the user.
    * @param event mouse drag
    */
    public void onMouseDraggedRightEdge(MouseEvent event){

        outerRect.changeWidth(event, getMargin());

        allPlayables.forEach((n) -> {
            n.onMouseDraggedRightEdge(event);
        });
    }

    /**
    * Sets the rectangle in relationship to
    * the mouse drag from the user.
    * @param event mouse drag
    */
    public void onMouseDragged(MouseEvent event){
        
        outerRect.changeLocation(event);

        allPlayables.forEach((n) -> {
            n.onMouseDragged(event);
        });
    }
    
    /**
    * Sets the rectangle in relationship to
    * the mouse release from the user.
    * @param event mouse drag
    */
    public void onMouseReleased(MouseEvent event){
        
        outerRect.stopLocationChange(event);

        allPlayables.forEach((n) -> {
            n.onMouseReleased(event);
        });
    }
    
    /**
    * Sets the rectangle to a gesture in relationship to
    * the mouse release from the user.
    * @param event mouse drag
    */
    public void onMouseReleasedRightEdge(MouseEvent event){
        
        outerRect.stopWidthChange(event, getMargin());

        allPlayables.forEach((n) -> {
            n.onMouseReleasedRightEdge(event);
        });
    }
}
