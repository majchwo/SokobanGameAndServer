package Application;
import org.w3c.dom.Document;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Klasa zawierająca stale uzywane podczas inicjalizacji okna gry
 */
public final class Parameters {

    public static int online;
    /**
     * Zmienna określająca szerokość okna gry
     */
    public static int mainWindowFrameWidth;

    /**
     * Zmienna okreslajaca wysokosc okna gry
     */
    public static int mainWindowFrameHeight;



    /**
     * Zmienna okreslajaca szerokos pola gry (w klockach)
     */
    public static int numberOfBricksX;

    /**
     * Zmienna okreslajaca szerokos pola gry (w klockach)
     */
    public static int numberOfBricksY;

    /**
     * Zmienna określająca szerokość okna informacyjnego Najlepszych wyników
     */
    public static int infoFrameWidth;

    /**
     * Zmienna okreslajaca wysokosc okna informacyjnego Najlepszych wyników
     */
    public static int infoFrameHeight;
    /**
     * Zmienna określająca maksymalny poziom gry
     */
    public static int maxLevel;
    /**
     * Ip serwera gry
     */
    public static String serverIp=new String();
    /**
     * Gniazdko komunikacyjne z serwerem
     */
    public static Socket socket;

    /**
     * Konstruktor klasy zawierajacej stale
     */
    private Parameters(){}
    static
    {
         online = JOptionPane.showConfirmDialog(null,"Chcesz grac online ?","",JOptionPane.YES_NO_OPTION);
        if(online==JOptionPane.NO_OPTION){
        parseConfigurationFile();}
        else
        {
            try {
               FileReader fr = new FileReader("ipconfig.txt");
                BufferedReader bfr = new BufferedReader(fr);
                try {
                   serverIp=bfr.readLine();

                } catch (IOException e) {
                    System.out.println("BlĄD ODCZYTU Z PLIKU!");
                    System.exit(2);
                }
                try {
                    fr.close();
                } catch (IOException e) {
                    System.out.println("BlĄD PRZY ZAMYKANIU PLIKU!");
                    System.exit(3);
                }

            }
            catch (FileNotFoundException e) {
                System.out.println("BlĄD PRZY OTWIERANIU PLIKU!");
                System.exit(1);
            }





            getParamsFormServer(serverIp);

        }


    }



    /**
     * Metoda do inicjacji pól z pliku konfugracyjnego
     */

    public static void parseConfigurationFile() {
        try {

            File xmlInputFile = new File("cfg.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlInputFile);
            doc.getDocumentElement().normalize();
            mainWindowFrameWidth = Integer.parseInt(doc.getElementsByTagName("frameWidth").item(0).getTextContent());
            mainWindowFrameHeight = Integer.parseInt(doc.getElementsByTagName("frameHeight").item(0).getTextContent());
            numberOfBricksX = Integer.parseInt(doc.getElementsByTagName("numberOfBricksX").item(0).getTextContent());
            numberOfBricksY = Integer.parseInt(doc.getElementsByTagName("numberOfBricksY").item(0).getTextContent());
            infoFrameWidth = Integer.parseInt(doc.getElementsByTagName("infoFrameWidth").item(0).getTextContent());
            infoFrameHeight = Integer.parseInt(doc.getElementsByTagName("infoFrameHeight").item(0).getTextContent());
            maxLevel = Integer.parseInt(doc.getElementsByTagName("MaxLevel").item(0).getTextContent());
        } catch (Exception e) {
            System.out.println("Blad wczytywania pliku");
        }
    }

    /**
     * Funkcja pobierająca dane konfiguracyjne z serwera
     * @param Ip Ip serwera
     */
    public static void getParamsFormServer(String Ip)
    {
        try{
        InetAddress addr = InetAddress.getByName(Ip);
         socket = new Socket(addr, 60000);
        OutputStream os = socket.getOutputStream();
        PrintWriter pw = new PrintWriter(os, true);
        String[] commands={"GETFRAMEWIDTH","GETFRAMEHEIGHT","GETINFOFRAMEWIDTH","GETINFOFRAMEHEIGHT","GETMAXLEVEL","GETNUMOFBRICKSX","GETNUMOFBRICKSY"};
        ArrayList<Integer> values=new ArrayList<>();
        for(int i=0;i<commands.length;i++) {
            pw.println(commands[i]);

            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is));
            String response = new String(br.readLine());
            values.add(Integer.parseInt(response.substring(response.indexOf('_')+1)));
        }
        mainWindowFrameWidth=values.get(0);
        mainWindowFrameHeight=values.get(1);
        infoFrameWidth=values.get(2);
        infoFrameHeight=values.get(3);
        maxLevel=values.get(4);
        numberOfBricksX=values.get(5);
        numberOfBricksY=values.get(6);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,"Nie udalo sie polaczyc z serwerem, grasz offline");
            online=JOptionPane.NO_OPTION;
            parseConfigurationFile();
        }
    }
}
