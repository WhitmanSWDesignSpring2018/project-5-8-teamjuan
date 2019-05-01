package tunecomposer;

import java.util.List;

import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;

public interface Playable {

    public Bounds getBounds();

    public void schedule();

    public void updateLastNote();

    public List<MoveableRect> getRectangle();

    public boolean getSelected();
    public void setSelected(boolean selected);

    public boolean clickedOnRightEdge(MouseEvent event);

    public void onMousePressed(MouseEvent event);
    public void onMousePressedRightEdge(MouseEvent event);
    
    public void onMouseDragged(MouseEvent event);
    public void onMouseDraggedRightEdge(MouseEvent event);

    public void onMouseReleased(MouseEvent event);
    public void onMouseReleasedRightEdge(MouseEvent event);

	public List<MoveableRect> getAllRectangles();

}