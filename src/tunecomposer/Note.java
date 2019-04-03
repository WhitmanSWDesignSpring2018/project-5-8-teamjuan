/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.ArrayList;

import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;


/**
 * Note class creates a Rectangle representing the note to be played
 * @author Ian Hawkins, Madi Crowley, Ian Stewart, Melissa Kohl
 */
public class Note implements Playable {
    
    /**
     * Constants for playing note in MidiPlayer
     */
    private static final int VOLUME = 127;
    private static final int MAX_PITCH = 128;
    private static final int DEFAULT_DURATION = 100;
    private static final int TRACK = 0;
    
    /**
     * Constants for Rectangle in composition panel
     */
    private static final int RECTHEIGHT = 10;
    private static final int MARGIN = 5;
    
    /**
     * End time for MidiPlayer
     */
    public static double lastNote = 0;
    
    /**
     * Note fields for creating rectangle and playing note
     */

    private final MoveableRect noteRect;
    private double x_coord;
    private double y_coord;
    private double rectWidth;
    private int startTime;
    private int pitch;
    private final Instrument instrument;
    
    /**
     * Is this note currently selected
     */
    private boolean isSelected;

    /**
     * Creates new selected Rectangle at given coordinates with a default width
     * of 100 pixels and creates a note of the given instrument at the 
     * calculated start time and pitch.
     * @param x x-coordinate of new rectangle and starting time for note
     * @param y y-coordinate of new rectangle and pitch of note
     * @param inst instrument that the note should be played
     */
    public Note(double x, double y, Instrument inst) {

        startTime = (int) x;
        pitch = MAX_PITCH - (int) y / RECTHEIGHT;
        
        x_coord = x;
        y_coord = y - ( y % RECTHEIGHT);
        
        instrument = inst;
        rectWidth = DEFAULT_DURATION;
        
        noteRect = new MoveableRect();
        noteRect.setX(x_coord);
        noteRect.setY(y_coord);
        noteRect.setWidth(rectWidth);
        noteRect.setHeight(RECTHEIGHT);
        noteRect.updateCoordinates();
        noteRect.getStyleClass().addAll("selected", instrument.toString());
        noteRect.setMouseTransparent(false);
        
        isSelected = true;
    }

    /**
     * Update the last note so we know when to stop the player and red line
     */
    public void updateLastNote() {
        if (x_coord + rectWidth > lastNote) {
            lastNote = x_coord + rectWidth;
        }
    }

    public Bounds getBounds() {
        return noteRect.getLayoutBounds();
    }
    
    /**
     * Get this Note's Rectangle object
     * @return this Note's Rectangle
     */
    public ArrayList<MoveableRect> getRectangle() {
        ArrayList<MoveableRect> arr = new ArrayList<MoveableRect>();
        arr.add(noteRect);
        return arr;
    }
    
    /**
     * Adds this Note to the MidiPlayer
     */
    public void schedule() {
        TuneComposer.PLAYER.addNote(pitch, VOLUME, startTime, (int)rectWidth, 
                                    instrument.ordinal(), TRACK);
    }
    
    /**
     * Checks if this note is selected
     * @return true if note is selected, false otherwise
     */
    public boolean getSelected() {
        return isSelected;
    }
    
    /**
     * Set the note to be selected or unselected and updates the style class
     * of the Rectangle
     * @param selected boolean to set selected to
     */
    public void setSelected(boolean selected) {
        isSelected = selected;
        if (selected) {
            noteRect.getStyleClass().clear();
            noteRect.getStyleClass().addAll("selected", 
                                            instrument.toString());
        } else {
            noteRect.getStyleClass().clear();
            noteRect.getStyleClass().addAll("unselected", 
                                            instrument.toString());
        }
    }
    
    /**
     * When the user presses the mouse to start dragging, calculate the offset
     * between the upper-left corner of the Rectangle and where the mouse is
     * in the Rectangle
     * @param event mouse click
     */
    public void onMousePressed(MouseEvent event) {
        noteRect.setMovingCoords(event);
    }
    
    /**
     * While the user is dragging the mouse, move the Rectangle with it
     * @param event mouse drag
     */
    public void onMouseDragged(MouseEvent event) {
        noteRect.moveNote(event);
    }
    
    /**
     * When the user stops dragging the mouse, set Note fields to the
     * Rectangle's current location
     * @param event mouse click
     */
    public void onMouseReleased(MouseEvent event) {
        noteRect.stopMoving(event);
        
        startTime = (int) noteRect.getX();
        pitch = MAX_PITCH - (int) noteRect.getY() / RECTHEIGHT;
        
        x_coord = noteRect.getX();
        y_coord = noteRect.getY() - (noteRect.getY() % RECTHEIGHT);
        
    }
    
    /**
     * Check whether the user has clicked within the last 5 pixels
     * of the Rectangle
     * @param event mouse click
     * @return true if mouse is within the last 5 pixels of the Rectangle
     */
    public boolean inLastFive(MouseEvent event) {
        return noteRect.inLastFive(event);
    }
    
    /**
     * When the user clicks near the right end of the Rectangle, calculate 
     * the offset between the right edge of the Rectangle and where the mouse 
     * is in the Rectangle 
     * @param event mouse click
     */
    public void onMousePressedLastFive(MouseEvent event) {
        noteRect.setMovingDuration(event);
    }
    
    /**
     * While the user is dragging the mouse, change the width of the Rectangle
     * @param event mouse drag
     */
    public void onMouseDraggedLastFive(MouseEvent event) {
        noteRect.moveDuration(event, MARGIN);
    }
    
    /**
     * When the user stops dragging the mouse, set Rectangle width
     * to final width
     * @param event 
     */
    public void onMouseReleasedLastFive(MouseEvent event) {
        noteRect.stopDuration(event);
    }
    
}
