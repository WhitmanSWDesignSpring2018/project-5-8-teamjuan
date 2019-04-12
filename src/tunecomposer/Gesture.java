
package tunecomposer;

import java.util.ArrayList;
import java.util.HashSet;

import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;


public class Gesture implements Playable {

    private HashSet<Playable> allPlayables;

    private boolean isSelected;

    private MoveableRect outerRect;

    private double upperXBound;

    private double upperYBound;

    private double lowerXBound;

    private double lowerYBound;

    private double margin;

    public Gesture() {
        upperXBound = 0;
        upperYBound = 0;
        lowerXBound = 3000;
        lowerYBound = 3000;
        margin = 5;
        allPlayables = new HashSet<Playable>();
        isSelected = true;
    }

    private void setMargin() {
        double max = 0;
        for (Playable play : allPlayables) {
            max = Math.max(play.getBounds().getMinX(), max);
        }
        margin = max + 5 - outerRect.getX();
    }

    private double getMargin() {
        return margin;
    }

    public void addPlayable(Playable play) {
        allPlayables.add(play);
        upperXBound = Math.max(play.getBounds().getMaxX(), upperXBound);
        upperYBound = Math.max(play.getBounds().getMaxY(), upperYBound);
        lowerXBound = Math.min(play.getBounds().getMinX(), lowerXBound);
        lowerYBound = Math.min(play.getBounds().getMinY(), lowerYBound);
    }

    public void createRectangle() {
        outerRect = new MoveableRect();

        outerRect.setX(lowerXBound);
        outerRect.setY(lowerYBound); 
        outerRect.setWidth(upperXBound - lowerXBound);
        outerRect.setHeight(upperYBound - lowerYBound);
        outerRect.updateCoordinates();
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

    public MoveableRect getOuterRectangle() {
        return outerRect;
    }

    public HashSet<Playable> getPlayables() {
        return allPlayables; 
    }

    public Bounds getBounds() {
        return outerRect.getLayoutBounds();
    }

    public void schedule() {
        allPlayables.forEach((n) -> {
            n.schedule();
        });
    }

    public void updateLastNote(){
        allPlayables.forEach((n) -> {
            n.updateLastNote();
        });
    }

    public ArrayList<MoveableRect> getRectangle(){
        ArrayList<MoveableRect> arr = new ArrayList<MoveableRect>();
        allPlayables.forEach((n) -> {
            arr.addAll(n.getRectangle());
        });
        return arr;
    }

    public boolean getSelected(){
        return isSelected;
    }

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

    public boolean inLastFive(MouseEvent event){
        return outerRect.inLastFive(event);
    }
    
    public void onMousePressed(MouseEvent event){

        outerRect.setMovingCoords(event);

        allPlayables.forEach((n) -> {
            n.onMousePressed(event);
        });
    }

    public void onMousePressedLastFive(MouseEvent event){
        
        outerRect.setMovingDuration(event); 

        allPlayables.forEach((n) -> {
            n.onMousePressedLastFive(event);
        });
    }
    
    public void onMouseDraggedLastFive(MouseEvent event){

        outerRect.moveDuration(event, getMargin());

        allPlayables.forEach((n) -> {
            n.onMouseDraggedLastFive(event);
        });
    }

    public void onMouseDragged(MouseEvent event){
        
        outerRect.moveNote(event);

        allPlayables.forEach((n) -> {
            n.onMouseDragged(event);
        });
    }

    public void onMouseReleased(MouseEvent event){
        
        outerRect.stopMoving(event);

        allPlayables.forEach((n) -> {
            n.onMouseReleased(event);
        });
    }
    
    public void onMouseReleasedLastFive(MouseEvent event){
        
        outerRect.stopDuration(event, getMargin());

        allPlayables.forEach((n) -> {
            n.onMouseReleasedLastFive(event);
        });
    }
}