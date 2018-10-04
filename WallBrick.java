package Application;

import java.awt.*;


/**
 * Klasa klocka przeszkody
 */
public class WallBrick extends Brick {

    boolean skrajny=false;
    /**
     * Konstruktor klasy SokobanBrick sluzacy do tworzenia obiektow
     *
     * @param x      polozenie klocka w plaszczyznie x
     * @param y      polozenie klocka w plaszyczyznie y
     */
    public WallBrick( double x, double y,boolean s)
    {

        super( x, y);
        skrajny=s;
        brickColor = Color.BLUE;


    };
}