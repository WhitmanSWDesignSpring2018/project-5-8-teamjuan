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
     * Creates new selected Rectangle at given coordinates with given width 
     * and creates a note of the given instrument at the calculated start
     * time and pitch.
     * 
     * @param x    x-coordinate of new rectangle and starting time for note
     * @param y    y-coordinate of new rectangle and pitch of note
     * @param inst instrument that the note should be played
     * @param width width of rectangle
     */
    public Note(double x, double y, Instrument inst, double width) {

        setFields(x, y, width);
        
        instrument = inst;
        noteRect = new MoveableRect();
        createRect();
        setSelected(true);
    }
    /**
     * Creates new selected Rectangle at given coordinates with a default width of
     * 100 pixels and creates a note of the given instrument at the calculated start
     * time and pitch.
     * 
     * @param x     x-coordinate of new rectangle and starting time for note
     * @param y     y-coordinate of new rectangle and pitch of note
     * @param inst  instrument that the note should be played
     */
    public Note(double x, double y, Instrument inst) {
        this(x, y, inst, NoteHandler.DEFAULT_DURATION);
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
     * @param width double desired width of note
     */
    private void setWidth(double width) {
        rectWidth = width;
        noteRect.setWidth(width);
    }

    /**
     * Sets the class fields based on inputs.
     * @param x double x coordinate
     * @param y double y coordinate
     * @param width double width of note
     */
    private void setFields(double x, double y, double width) {
        startTime = (int) x;
        pitch = NoteHandler.MAX_PITCH - (int) y / NoteHandler.RECTHEIGHT;

        x_coord = x;
        y_coord = y - (y % NoteHandler.RECTHEIGHT);

        rectWidth = width;
    }

    /**
     * Updates the class fields based on rectangle.
     */
    public void updateRectangle() {
        setFields(noteRect.getX(), noteRect.getY(), noteRect.getWidth());
        noteRect.updateInnerFields();
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

        setMouseHandlers();
    }

    /**
     * Sets all mouse handlers for mouse presses, drags, and releases.
     */
    public void setMouseHandlers() {
        noteRect.setMouseTransparent(false);

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
    public List<MoveableRect> getAllRectangles() {
        List<MoveableRect> arr = new ArrayList<MoveableRect>();
        arr.add(noteRect);
        return arr;
    }

    /**
     * Adds this Note to the MidiPlayer
     */
    public void schedule() {
        if (ButtonHandler.getCSharp()) {
            TuneComposer.PLAYER.addNote(pitch - ((pitch -9) % 12), NoteHandler.VOLUME, startTime, (int) rectWidth, instrument.ordinal(),
                NoteHandler.TRACK);
        } else {
            TuneComposer.PLAYER.addNote(pitch, NoteHandler.VOLUME, startTime, (int) rectWidth, instrument.ordinal(),
                NoteHandler.TRACK);
        }
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

} 
