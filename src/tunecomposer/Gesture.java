
package tunecomposer;

import java.util.ArrayList;
import java.util.HashSet;

import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;


public class Gesture implements Playable {

    private HashSet<Playable> allGestures;

    private boolean isSelected;

    //TODO: use Rectangle!!!
    private Rectangle outerRect;

    public Gesture() {
        allGestures = new HashSet();
        isSelected = true;
    }

    public void addGesture(Gesture gest) {
        allGestures.add(gest);
    }

    public Bounds getBounds() {
        return outerRect.getLayoutBounds();
    }

    public void schedule() {
        allGestures.forEach((n) -> {
            n.schedule();
        });
    }

    public void updateLastNote(){
        allGestures.forEach((n) -> {
            n.updateLastNote();
        });
    }

    public ArrayList<Rectangle> getRectangle(){
        ArrayList<Rectangle> arr = new ArrayList();
        allGestures.forEach((n) -> {
            arr.addAll(n.getRectangle());
        });
        return arr;
    }

    public boolean getSelected(){
        return isSelected;
    }

    public void setSelected(boolean selected){
        //TODO: outer rectangle
        allGestures.forEach((n) -> {
            n.setSelected(selected);
        });
    }

    public boolean inLastFive(MouseEvent event){
        //TODO
        return true;
    }
    
    public void setMovingCoords(MouseEvent event){
        allGestures.forEach((n) -> {
            n.setMovingCoords(event);
        });
    }

    public void setMovingDuration(MouseEvent event){
        allGestures.forEach((n) -> {
            n.setMovingDuration(event);
        });
    }
    
    public void moveDuration(MouseEvent event){
        allGestures.forEach((n) -> {
            n.moveDuration(event);
        });
    }
    public void moveNote(MouseEvent event){
        allGestures.forEach((n) -> {
            n.moveNote(event);
        });
    }

    public void stopMoving(MouseEvent event){
        allGestures.forEach((n) -> {
            n.stopMoving(event);
        });
    }
    public void stopDuration(MouseEvent event){
        allGestures.forEach((n) -> {
            n.stopDuration(event);
        });
    }
}