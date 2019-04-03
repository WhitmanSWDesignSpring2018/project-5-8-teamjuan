package tunecomposer;

import java.util.ArrayList;

import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;

public interface Playable {

    public Bounds getBounds();

    public void schedule();

    public void updateLastNote();

    public ArrayList<MoveableRect> getRectangle();

    public boolean getSelected();
    public void setSelected(boolean selected);

    public boolean inLastFive(MouseEvent event);

    public void onMousePressed(MouseEvent event);
    public void onMousePressedLastFive(MouseEvent event);
    
    public void onMouseDragged(MouseEvent event);
    public void onMouseDraggedLastFive(MouseEvent event);

    public void onMouseReleased(MouseEvent event);
    public void onMouseReleasedLastFive(MouseEvent event);

}