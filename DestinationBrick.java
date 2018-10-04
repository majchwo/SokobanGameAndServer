package Application;


import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
/**
 * Klasa podstawoego klocka celu
 */
public class DestinationBrick extends Brick {


    /**
     * Konstruktor klasy DestinationBrick sluzacy do tworzenia obiektow
     *
     * @param x      polozenie klocka w plaszczyznie x
     * @param y      polozenie klocka w plaszyczyznie y
     */
    public DestinationBrick( double x, double y)
    {
        super( x, y);

        brickColor=Color.GRAY;


    };
}

