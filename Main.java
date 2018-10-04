/**
 * Created by Wojciech & Arkadiusz
 */
package Application;

import javax.swing.*;
import java.awt.*;
/**
 * Klasa sluzaca do uruchomienia programu
 */
public class Main {
    /**
     * Metoda main clienta
     * @param args argumenty
     */
    public static void main(String[] args) {
	// write your code here
        JFrame frame = new mainWindow();

        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
               // JFrame frame = new mainWindow();
                frame.setTitle("SOKOBAN");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                frame.setVisible(true);

            }




        });
        while (true) {


            frame.repaint();
            /*if(((mainWindow) frame).Won()==true)
            {
                ((mainWindow) frame).endOfGame();
            }*/


           // ((mainWindow) frame).InfoFrame.repaint();
           // frame.validate();


            try
            {
                Thread.sleep(15);
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }
        }
    }

}
