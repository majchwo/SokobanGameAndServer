package Application;


import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
/**
 * Klasa podstawowego klocka przeszkody
 */
public class Brick {
    /**
     * Zmienna opisujaca klocek jako prostokat
     */
    public Rectangle2D brickRect;

    /**
     * znormalizowana pozycja X klocka
     */
    public double brickX;
    /**
     * mnoznik rozmiaru klocka
     */
    public double multiplier=1;
    /**
     * znormalizowana pozycja Y klocka
     */
    public double brickY;
    /**
     * kolor klocka
     */
    public Color brickColor;
    /**
     * Konstruktor klasy Brick sluzacy do tworzenia obiektow
     *
     * @param x      polozenie klocka w plaszczyznie x
     * @param y      polozenie klocka w plaszyczyznie y
     */
    public Brick( double x, double y)
    {


        brickX=x;
        brickY=y;

        brickRect= new Rectangle2D.Double(brickX,brickY,20,20);

    };
    /**
     * Metoda sluzaca zmianie położenia klocka
     * @param x przesuniecie w poziomie
     * @param y przesuniecie w pionie
     */
    public void move(int x, int y) {
       brickX+=x;
        brickY+=y;
    }
    /**
     * Metoda sluzaca do sprawdzenia kolizji z lewej strony
     * @param b klocek z ktorym kolizje sprawdzamy
     * @return czy zaszla kolizja
     */
    public boolean lewakolizja(Brick b) {
        if (((this.brickX - 20) == b.brickX) &&
                (this.brickY == b.brickY)) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * Metoda sluzaca do sprawdzenia kolizji z prawej strony
     * @param b klocek z ktorym kolizje sprawdzamy
     * @return czy zaszla kolizja
     */
    public boolean prawakolizja(Brick b) {
        if (((this.brickX + 20) == b.brickX)
                && (this.brickY == b.brickY)) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * Metoda sluzaca do sprawdzenia kolizji z góry
     * @param b klocek z ktorym kolizje sprawdzamy
     * @return czy zaszla kolizja
     */
    public boolean gorakolizja(Brick b) {
        if (((this.brickY - 20) == b.brickY) &&
                (this.brickX == b.brickX)) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * Metoda sluzaca do sprawdzenia kolizji z dolu
     * @param b klocek z ktorym kolizje sprawdzamy
     * @return czy zaszla kolizja
     */
    public boolean dolkolizja(Brick b) {
        if (((this.brickY + 20) == b.brickY)
                && (this.brickX == b.brickX)) {
            return true;
        } else {
            return false;
        }
    }
    public boolean anglekolizja(Brick b) {
        if (((this.brickY + 20) == b.brickY)
                && (this.brickX+20 == b.brickX)) {
            return true;
        } else if(((this.brickY - 20) == b.brickY)
                && (this.brickX+20 == b.brickX)) {
            return true;
        }else if(((this.brickY + 20) == b.brickY)
                && (this.brickX-20 == b.brickX)) {
            return true;
        }else if(((this.brickY - 20) == b.brickY)
                && (this.brickX-20 == b.brickX)) {
            return true;
        }else

        return false;
    }
    /**
     * Metoda sluzaca do sprawdzenia czy klocki sa w tej samej pozycji
     * @param b klocek z ktorym pozycje sprawdzamy
     * @return czy klocki sa w tej samej pozycji
     */
    public boolean equalplace(Brick b) {
        if (((this.brickY) == b.brickY)
                && (this.brickX == b.brickX)) {
            return true;
        } else {
            return false;
        }
    }
}

