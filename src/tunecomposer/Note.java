/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;

/**
 * Note class creates a Rectangle representing the note to be played
 * 
 * @author Ian Hawkins, Madi Crowley, Ian Stewart, Melissa Kohl
 */
public class Note implements Playable {

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
     * Creates a new rectangle that is the clone of a given note.
     * 
     * @param note the note to be cloned
     */
    public Note(Note note) {
        List<Double> coords = note.getCoords();
        x_coord = coords.get(0);
        y_coord = coords.get(1);
        rectWidth = coords.get(2);
        List<Integer> noteInfo = note.getNoteInfo();
        startTime = noteInfo.get(0);
        pitch = noteInfo.get(1);
        instrument = note.getInstrument();
        noteRect = new MoveableRect();
        createRect();
        setSelected(note.getSelected());
    }

    /**
     * Creates new selected Rectangle at given coordinates with a default width of
     * 100 pixels and creates a note of the given instrument at the calculated start
     * time and pitch.
     * 
     * @param x    x-coordinate of new rectangle and starting time for note
     * @param y    y-coordinate of new rectangle and pitch of note
     * @param inst instrument that the note should be played
     */
    public Note(double x, double y, Instrument inst) {

        startTime = (int) x;
        pitch = NoteHandler.MAX_PITCH - (int) y / NoteHandler.RECTHEIGHT;

        x_coord = x;
        y_coord = y - (y % NoteHandler.RECTHEIGHT);

        instrument = inst;
        rectWidth = NoteHandler.DEFAULT_DURATION;
        noteRect = new MoveableRect();
        createRect();
        setSelected(true);
    }

    /**
     * Creates new selected Rectangle at given coordinates with given width 
     * and creates a note of the given instrument at the calculated start
     * time and pitch.
     * 
     * @param x     x-coordinate of new rectangle and starting time for note
     * @param y     y-coordinate of new rectangle and pitch of note
     * @param inst  instrument that the note should be played
     * @param width width of rectangle
     */
    public Note(double x, double y, Instrument inst, double width) {
        this(x, y, inst);
        setWidth(width);
        noteRect.updateInnerFields();
    }

    /**
     * Gets all relevant information for a Note.
     * 
     * @param pitch
     * @param startTime
     * @param instrument
     * @param isSelected
     * @param noteRect
     */
    public Note(int pitch, int startTime, Instrument instrument, boolean isSelected, MoveableRect noteRect) {
        this.pitch = pitch;
        this.startTime = startTime;
        this.instrument = instrument;
        this.isSelected = isSelected;
        this.noteRect = noteRect;
        x_coord = noteRect.getX();
        y_coord = noteRect.getY();
        rectWidth = noteRect.getWidth();
        setMouseHandlers();
        setSelected(isSelected);
    }

    /**
     * Converts Note info into XML string
     * 
     * @return note in XML format
     */
    public String toString() {
        return "<Note pitch=\"" + pitch + "\" startTime=\"" + startTime + "\" instrument=\"" + instrument.toString()
                + "\" isSelected=\"" + isSelected + "\">" + noteRect.toString() + "</Note>";
    }

    /**
     * Sets the width to a value during construction.
     */
    private void setWidth(double width) {
        rectWidth = width;
        noteRect.setWidth(width);
    }

    /**
     * Gets coordinates of the note in the form of an arraylist
     * 
     * @return arraylist of coordinates and rectangle width
     */
    public List<Double> getCoords() {
        List<Double> arr = new ArrayList<Double>();
        arr.add(x_coord);
        arr.add(y_coord);
        arr.add(rectWidth);
        return arr;
    }

    /**
     * Gets startTime and pitch of the note.
     * 
     * @return startTime and pitch
     */
    public List<Integer> getNoteInfo() {
        List<Integer> arr = new ArrayList<Integer>();
        arr.add(startTime);
        arr.add(pitch);
        return arr;
    }

    /**
     * Gets the instrument type of the note.
     * 
     * @return instrument type
     */
    public Instrument getInstrument() {
        return instrument;
    }

    /**
     * Sets the attributes and behaviors of the rectangle.
     */
    public void createRect() {
        noteRect.setX(x_coord);
        noteRect.setY(y_coord);
        noteRect.setWidth(rectWidth);
        noteRect.setHeight(NoteHandler.RECTHEIGHT);
        noteRect.updateInnerFields();
        noteRect.setMouseTransparent(false);

        setMouseHandlers();
    }

    /**
     * Sets all mouse handlers for mouse presses, drags, and releases.
     */
    public void setMouseHandlers() {
        noteRect.setOnMousePressed((MouseEvent pressedEvent) -> {
            ClickHandler.handleNoteClick(pressedEvent, this);
            ClickHandler.handleNotePress(pressedEvent, this);
        });

        noteRect.setOnMouseDragged((MouseEvent dragEvent) -> {
            ClickHandler.handleNoteDrag(dragEvent);
        });

        noteRect.setOnMouseReleased((MouseEvent releaseEvent) -> {
            ClickHandler.handleNoteStopDragging(releaseEvent);
        });
    }

    /**
     * Update the last note so we know when to stop the player and red line
     */
    public void updateLastNote() {
        if (x_coord + rectWidth > NoteHandler.lastNote) {
            NoteHandler.lastNote = x_coord + rectWidth;
        }
    }

    /**
     * Gets the bounds of the rectangle
     * 
     * @return Bounds of the rectangle
     */
    public Bounds getBounds() {
        return noteRect.getLayoutBounds();
    }

    /**
     * Get this Note's Rectangle object
     * 
     * @return this Note's Rectangle
     */
    public List<MoveableRect> getRectangle() {
        List<MoveableRect> arr = new ArrayList<MoveableRect>();
        arr.add(noteRect);
        return arr;
    }

    /**
     * Gets the note's rectangle using getRectangle() method.
     * 
     * @return this Note's rectangle
     */
    public List<MoveableRect> getAllRectangles() {
        return getRectangle();
    }

    /**
     * Adds this Note to the MidiPlayer
     */
    public void schedule() {
        TuneComposer.PLAYER.addNote(pitch, NoteHandler.VOLUME, startTime, (int) rectWidth, instrument.ordinal(),
                NoteHandler.TRACK);
    }

    /**
     * Checks if this note is selected
     * 
     * @return true if note is selected, false otherwise
     */
    public boolean getSelected() {
        return isSelected;
    }

    /**
     * Set the note to be selected or unselected and updates the style class of the
     * Rectangle
     * 
     * @param selected boolean to set selected to
     */
    public void setSelected(boolean selected) {
        isSelected = selected;
        if (selected) {
            noteRect.getStyleClass().clear();
            noteRect.getStyleClass().addAll("selected", instrument.toString());
        } else {
            noteRect.getStyleClass().clear();
            noteRect.getStyleClass().addAll("unselected", instrument.toString());
        }
    }

    /**
     * When the user presses the mouse to start dragging, calculate the offset
     * between the upper-left corner of the Rectangle and where the mouse is in the
     * Rectangle
     * 
     * @param event mouse click
     */
    public void onMousePressed(MouseEvent event) {
        noteRect.startLocationChange(event);
    }

    /**
     * While the user is dragging the mouse, move the Rectangle with it
     * 
     * @param event mouse drag
     */
    public void onMouseDragged(MouseEvent event) {
        noteRect.changeLocation(event);
    }

    /**
     * When the user stops dragging the mouse, set Note fields to the Rectangle's
     * current location
     * 
     * @param event mouse click
     */
    public void onMouseReleased(MouseEvent event) {
        noteRect.stopLocationChange(event);

        startTime = (int) noteRect.getX();
        pitch = NoteHandler.MAX_PITCH - (int) noteRect.getY() / NoteHandler.RECTHEIGHT;

        x_coord = noteRect.getX();
        y_coord = noteRect.getY() - (noteRect.getY() % NoteHandler.RECTHEIGHT);

    }

    /**
     * Check whether the user has clicked within the last 5 pixels of the Rectangle
     * 
     * @param event mouse click
     * @return true if mouse is within the last 5 pixels of the Rectangle
     */
    public boolean clickedOnRightEdge(MouseEvent event) {
        return noteRect.clickedOnRightEdge(event);
    }

    /**
     * When the user clicks near the right end of the Rectangle, calculate the
     * offset between the right edge of the Rectangle and where the mouse is in the
     * Rectangle
     * 
     * @param event mouse click
     */
    public void onMousePressedRightEdge(MouseEvent event) {
        noteRect.startWidthChange(event);
    }

    /**
     * While the user is dragging the mouse, change the width of the Rectangle
     * 
     * @param event mouse drag
     */
    public void onMouseDraggedRightEdge(MouseEvent event) {
        noteRect.changeWidth(event, NoteHandler.MARGIN);
    }

    /**
     * When the user stops dragging the mouse, set Rectangle width to final width
     * 
     * @param event mouse release
     */
    public void onMouseReleasedRightEdge(MouseEvent event) {
        noteRect.stopWidthChange(event, NoteHandler.MARGIN);
        rectWidth = noteRect.getWidth();
    }

}
