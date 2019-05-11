
package tunecomposer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;

public class Gesture implements Playable {

    /**
     * Fields for the Gesture class.
     */
    private Set<Playable> allPlayables;

    private boolean isSelected;

    private MoveableRect outerRect;

    private double upperXBound = 0;

    private double upperYBound = 0;

    private double lowerXBound = 10001;

    private double lowerYBound = 10001;

    private double margin = 5;

    /**
     * Creates a clone of the given gesture.
     * 
     * @param gest gesture to be cloned
     */
    public Gesture(Gesture gest) {
        this.allPlayables = new HashSet<Playable>();
        gest.allPlayables.forEach((playable) -> {
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
        allPlayables = new HashSet<Playable>();
        isSelected = true;
    }

    /**
     * Gets information relevant to a Gesture.
     */
    public Gesture(boolean isSelected, double margin, Set<Playable> allPlayables, MoveableRect outerRect) {
        this.isSelected = isSelected;
        this.margin = margin;
        this.allPlayables = allPlayables;
        this.outerRect = outerRect;
        setMouseHandlers();
        setSelected(isSelected);
    }

    /**
     * Converts Gesture info into XML string
     * 
     * @return gesture in XML format
     */
    public String toString() {
        String newString = "<Gesture isSelected=\"" + isSelected + "\" margin=\"" + margin + "\">"
                + outerRect.toString();
        for (Playable playable : allPlayables) {
            newString += playable.toString();
        }
        newString += "</Gesture>";
        return newString;
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
        outerRect.setMargin(margin);
    }

    /**
     * Gets the margin value.
     * 
     * @return margin
     */
    private double getMargin() {
        return margin;
    }

    /**
     * Adds playable to this Gesture.
     * @param play Playable to add
     */
    public void addPlayable(Playable play) {
        allPlayables.add(play);
        updateBounds(play);
    }

    /**
     * Calculates bounds of gesture based on playable
     * @param play Playable to base bounds off of
     */
    private void updateBounds(Playable play) {
        upperXBound = Math.max(play.getBounds().getMaxX(), upperXBound);
        upperYBound = Math.max(play.getBounds().getMaxY(), upperYBound);
        lowerXBound = Math.min(play.getBounds().getMinX(), lowerXBound);
        lowerYBound = Math.min(play.getBounds().getMinY(), lowerYBound);
    }

    /**
     * Updates location of outer rectangle based on contained playables.
     */
    private void updateOuterRectBounds() {
        upperXBound = 0;
        upperYBound = 0;
        lowerXBound = 10001;
        lowerYBound = 10001;
        allPlayables.forEach((play) -> {
            updateBounds(play);
        });
        setOuterRectBounds();
        outerRect.updateInnerFields();
    }

    /**
     * Updates the outer rectangle.
     */
    public void updateRectangle() {
        updateOuterRectBounds();
    }

    /**
     * Sets bounds of outer rectangle to calculated bound fields.
     */
    private void setOuterRectBounds() {
        outerRect.setX(lowerXBound);
        outerRect.setY(lowerYBound);
        outerRect.setWidth(upperXBound - lowerXBound);
        outerRect.setHeight(upperYBound - lowerYBound);
    }

    /**
     * Creates the rectangle that groups the Gesture.
     */
    public void createRectangle() {
        outerRect = new MoveableRect();

        setOuterRectBounds();
        outerRect.updateInnerFields();
        setSelected(true);

        setMouseHandlers();

        setMargin();
    }

    /**
     * Sets the mouse handlers for mouse presses, drags, and releases.
     */
    private void setMouseHandlers() {
        outerRect.setMouseTransparent(false);

        outerRect.setOnMousePressed((MouseEvent pressedEvent) -> {
            ClickHandler.handleNoteClick(pressedEvent, this);
            ClickHandler.handleNotePress(pressedEvent, this);
        });

        outerRect.setOnMouseDragged((MouseEvent dragEvent) -> {
            ClickHandler.handleNoteDrag(dragEvent);
        });

        outerRect.setOnMouseReleased((MouseEvent releaseEvent) -> {
            ClickHandler.handleNoteStopDragging(releaseEvent);
        });
    }

    /**
     * Gets the outer rectangle for the moveable rectangle.
     * 
     * @return outer rectanlge
     */
    public MoveableRect getOuterRectangle() {
        return outerRect;
    }

    /**
     * Gets all the Playable values from the HashSet.
     * 
     * @return all playables
     */
    public Set<Playable> getPlayables() {
        return allPlayables;
    }

    /**
     * Gets bounds from the outer rectangle.
     * 
     * @return outer rectangle bounds
     */
    public Bounds getBounds() {
        return outerRect.getLayoutBounds();
    }

    /**
     * Passes all playables into the schedule method so that they can play.
     */
    public void schedule() {
        allPlayables.forEach((n) -> {
            n.schedule();
        });
    }

    /**
     * Updates each note.
     */
    public void updateLastNote() {
        allPlayables.forEach((n) -> {
            n.updateLastNote();
        });
    }

    /**
     * Adds all gesture and note rectangles to arraylist.
     * 
     * @return arraylist of playables
     */
    public List<MoveableRect> getAllRectangles() {
        List<MoveableRect> temp = new ArrayList<MoveableRect>();
        allPlayables.forEach((n) -> {
            temp.addAll(n.getAllRectangles());
        });
        temp.add(outerRect);
        return temp;
    }

    /**
     * Checks if playable is selected.
     * 
     * @return boolean
     */
    public boolean getSelected() {
        return isSelected;
    }

    /**
     * Selects gestures.
     * 
     * @param selected boolean representing selection
     */
    public void setSelected(boolean selected) {
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
}
