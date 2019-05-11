package tunecomposer;

import java.util.List;

import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;

public interface Playable {

    public Bounds getBounds();

    public void schedule();

    public void updateLastNote();

    public boolean getSelected();
    public void setSelected(boolean selected);

	public List<MoveableRect> getAllRectangles();
    public void updateRectangle();

}
