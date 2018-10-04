package Application;


import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
/**
 * Klasa klocka celu
 */
public class PackageBrick extends Brick {

    /**
     * Konstruktor klasy PackageBrick sluzacy do tworzenia obiektow
     *
     * @param x      polozenie klocka w plaszczyznie x
     * @param y      polozenie klocka w plaszyczyznie y
     */
    public PackageBrick( double x, double y)
    {
        super( x, y);
        brickColor=Color.YELLOW;


    };
}

