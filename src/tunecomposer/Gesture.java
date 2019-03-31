
package tunecomposer;

import java.util.ArrayList;
import java.util.HashSet;

import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;


public class Gesture implements Playable {

    private HashSet<Playable> allPlayables;

    private boolean isSelected;

    //TODO: use Rectangle!!!
    private Rectangle outerRect;

    public Gesture() {
        allPlayables = new HashSet();
        isSelected = true;
    }

    public void addPlayable(Playable play) {
        allPlayables.add(play);
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

    public ArrayList<Rectangle> getRectangle(){
        ArrayList<Rectangle> arr = new ArrayList();
        allPlayables.forEach((n) -> {
            arr.addAll(n.getRectangle());
        });
        return arr;
    }

    public boolean getSelected(){
        return isSelected;
    }

    public void setSelected(boolean selected){
        //TODO: outer rectangle
        allPlayables.forEach((n) -> {
            n.setSelected(selected);
        });
    }

    public boolean inLastFive(MouseEvent event){
        //TODO
        return true;
    }
    
    public void setMovingCoords(MouseEvent event){
        allPlayables.forEach((n) -> {
            n.setMovingCoords(event);
        });
    }

    public void setMovingDuration(MouseEvent event){
        allPlayables.forEach((n) -> {
            n.setMovingDuration(event);
        });
    }
    
    public void moveDuration(MouseEvent event){
        allPlayables.forEach((n) -> {
            n.moveDuration(event);
        });
    }
    public void moveNote(MouseEvent event){
        allPlayables.forEach((n) -> {
            n.moveNote(event);
        });
    }

    public void stopMoving(MouseEvent event){
        allPlayables.forEach((n) -> {
            n.stopMoving(event);
        });
    }
    public void stopDuration(MouseEvent event){
        allPlayables.forEach((n) -> {
            n.stopDuration(event);
        });
    }
}