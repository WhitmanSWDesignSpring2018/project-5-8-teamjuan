package tunecomposer;

import java.util.ArrayList;

import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public interface Playable {

    public Bounds getBounds();

    public void schedule();

    public void updateLastNote();

    public ArrayList<Rectangle> getRectangle();

    public boolean getSelected();
    public void setSelected(boolean selected);

    public boolean inLastFive(MouseEvent event);

    public void setMovingCoords(MouseEvent event);
    public void setMovingDuration(MouseEvent event);
    
    public void moveDuration(MouseEvent event);
    public void moveNote(MouseEvent event);

    public void stopMoving(MouseEvent event);
    public void stopDuration(MouseEvent event);

}