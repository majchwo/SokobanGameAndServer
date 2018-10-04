package Application;


import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
/**
 * Klasa klocka sterowanego przez gracza
 */
public class SokobanBrick extends Brick {

    /**
     * Konstruktor klasy SokobanBrick sluzacy do tworzenia obiektow
     *
     * @param x      polozenie klocka w plaszczyznie x
     * @param y      polozenie klocka w plaszyczyznie y
     */
    public SokobanBrick( double x, double y)
    {
        super( x, y);
        brickColor=Color.GREEN;


    };
}

