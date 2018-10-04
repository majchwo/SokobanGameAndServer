package Application;

import java.util.EventObject;
/**
 * Insteface obsługi zdarzeń ruchu
 */
interface MoveListener {
    /**
     * metoda obsługi zdarzeń ruchu
     * @param e zdarzenie do obslugi
     */
    void domove(MoveEvent e);
}
/**
 * Klasa zdarzenia ruchu
 */
public class MoveEvent  {
    /**
     * kierunek ruchu
     */
    public int direction=0;
    /**
     * Konstruktor klasy MoveEvent sluzacy do tworzenia obiektow
     *
     * @param d     kierunek ruchu
     */
    MoveEvent(int d){

        direction=d;



    }

}
