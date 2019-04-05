package tunecomposer;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class MoveableRect extends Rectangle {
    
    private static final int MARGIN = 5;
    private static final double RECTHEIGHT = 10;

    /**
     * Offsets for dragging Rectangle
     */
    private double xOffset;
    private double yOffset;
    private double widthOffset; 
    private double x_coord;
    private double y_coord;
    private double rectWidth;

    /**
     * Sets the fields to the current coordinates.
     */
    public void updateCoordinates() {
        x_coord = getX();
        y_coord = getY();
        rectWidth = getWidth();
    }

    /**
     * When the user presses the mouse to start dragging, calculate the offset
     * between the upper-left corner of the Rectangle and where the mouse is
     * in the Rectangle
     * @param event mouse click
     */
    public void setMovingCoords(MouseEvent event) {
        xOffset = event.getX() - x_coord;
        yOffset = event.getY() - y_coord;
    }
    
    /**
     * While the user is dragging the mouse, move the Rectangle with it
     * @param event mouse drag
     */
    public void moveNote(MouseEvent event) {
        setX(event.getX() - xOffset);
        setY(event.getY() - yOffset );
    }
    
    /**
     * When the user stops dragging the mouse, set Note fields to the
     * Rectangle's current location
     * @param event mouse click
     */
    public void stopMoving(MouseEvent event) {
        double x = event.getX() - xOffset;
        double y = event.getY() - yOffset;
        x_coord = x;
        y_coord = y - (y % RECTHEIGHT);
        
        setX(x_coord);
        setY(y_coord);
    }
    
    /**
     * Check whether the user has clicked within the last 5 pixels
     * of the Rectangle
     * @param event mouse click
     * @return true if mouse is within the last 5 pixels of the Rectangle
     */
    public boolean inLastFive(MouseEvent event) {
        return (event.getX() > x_coord + rectWidth - MARGIN);
    }
    
    /**
     * When the user clicks near the right end of the Rectangle, calculate 
     * the offset between the right edge of the Rectangle and where the mouse 
     * is in the Rectangle 
     * @param event mouse click
     */
    public void setMovingDuration(MouseEvent event) {
        widthOffset = x_coord + rectWidth - event.getX();
    }
    
    /**
     * While the user is dragging the mouse, change the width of the Rectangle
     * @param event mouse drag
     * @param margin the minimum width
     */
    public void moveDuration(MouseEvent event, double margin) {
        double tempWidth = event.getX() - x_coord + widthOffset;
        if (tempWidth < margin) tempWidth = margin;
        setWidth(tempWidth);
    }
    
    /**
     * When the user stops dragging the mouse, set Rectangle width
     * to final width
     * @param event 
     * @param margin is minimum width
     */
    public void stopDuration(MouseEvent event, double margin) {
        rectWidth = event.getX() - x_coord + widthOffset;
        if (rectWidth < margin) rectWidth = margin;
        
        setWidth(rectWidth);
    }

}