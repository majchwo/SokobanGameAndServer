package Application;



import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;

/**
 * Klasa opisujaca glowne okno w ktorym rysowana jest plansza do gry i panel informacyjny
 */
public class mainWindow extends JFrame {

    /**
     * Lista zawierajaca tekst pomocy w sterowaniu
     */
    private ArrayList<String> help = new ArrayList();
    /**
     * Lista zawierajaca zasady gry
     */
    private ArrayList<String> rules = new ArrayList();
    /**
     * Pomocnicza Zmienna tekstowa przechowujaca znak nowej linii
     */
    private final static String newline = "\n";

    /**
     * okno wyswietlajace zasady gry
     */
    JFrame RulesFrame = new JFrame("    ZASADY GRY  ");
    /**
     * okno wyswietlajace pomoc w sterowaniu
     */
    JFrame HelpFrame = new JFrame("     POMOC       ");
    /**
     * Okno wyswietlajace informacje o rozgrywce gracza
     */
    JFrame InfoFrame = new JFrame("NAJLEPSZE WYNIKI");


    /**
     * Nazwa Gracza
     */
    String Name;

    /**
     * Pole gry
     */
    GameArea  newGame;



    /**
     * Konstrukor klasy mainWindow
     */
    public mainWindow() {





        setupRules();
        setupHelp();
        setSize(Parameters.mainWindowFrameWidth, Parameters.mainWindowFrameHeight);
        this.setMinimumSize(new Dimension(369, 303));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        JMenuBar menu = new JMenuBar();
        setJMenuBar(menu);
        JMenu x = new JMenu("Menu");
        menu.add(x);
        JMenuItem newgame = new JMenuItem("Nowa Gra");
        x.add(newgame);
        JMenuItem bestScores = new JMenuItem("Najlepsze wyniki");
        x.add(bestScores);
        JMenuItem help = new JMenuItem("Pomoc");
        menu.add(help);
        JMenuItem rules = new JMenuItem("Zasady gry");
        x.add(rules);
        JMenuItem reset = new JMenuItem("Restartuj poziom");
        x.add(reset);
        rules.addActionListener(new mainWindowRulesEvent());
        newgame.addActionListener(new MainWindowNewGameEvent());
        help.addActionListener(new mainWindowHelpEvent());
        reset.addActionListener(new MainWindowLevelRestartEvent());
        bestScores.addActionListener(new bestScoresWindowEvent());

        newPlayer();
        newGame=new GameArea(Name);






        add(newGame);


    }

    /**
     * Metoda restartujaca gre
     */
    public void restartGame() {
        getContentPane().removeAll();



        newGame=new GameArea(Name);
        add(newGame);
        validate();

    }

    /**
     * Klasa Wewnetrzna definiujaca zdarzenie klikniecia przycisku Resetuj poziom
     */
    public class MainWindowLevelRestartEvent implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            newGame.resetLevel();
        }
    }
    /**
     * Klasa Wewnetrzna definiujaca zdarzenie klikniecia przycisku Nowa gra
     */
    public class MainWindowNewGameEvent implements ActionListener {


        public void actionPerformed(ActionEvent event) {
           newPlayer();
            restartGame();
        }

    }

    /**
     * Klasa Wewnetrzna definiujaca zdarzenie klikniecia przycisku Zasady gry
     */
    public class mainWindowRulesEvent implements ActionListener {


        public mainWindowRulesEvent() {



        }

        public void actionPerformed(ActionEvent event) {
            RulesFrame.dispatchEvent(new WindowEvent(RulesFrame, WindowEvent.WINDOW_CLOSING));


            JTextArea rulesTxt = new JTextArea();
            for (int i = 0; i < rules.size(); i++) {
                rulesTxt.append(rules.get(i) + newline + newline);
            }
            rulesTxt.setEditable(false);

            rulesTxt.setWrapStyleWord(true);
            RulesFrame.add(rulesTxt);
            RulesFrame.pack();
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            RulesFrame.setLocation(dim.width/2-RulesFrame.getSize().width/2, dim.height/2-RulesFrame.getSize().height/2);
            RulesFrame.setVisible(true);
        }

    }

    /**
     * Klasa Wewnetrzna definiujaca zdarzenie klikniecia przycisku Pomoc
     */
    public class mainWindowHelpEvent implements ActionListener {



        public mainWindowHelpEvent() {



        }

        public void actionPerformed(ActionEvent event) {
            HelpFrame.dispatchEvent(new WindowEvent( HelpFrame, WindowEvent.WINDOW_CLOSING));
            HelpFrame.getContentPane().removeAll();

            JTextArea helpTxt = new JTextArea();
            for (int i = 0; i < help.size(); i++) {
                helpTxt.append(help.get(i) + newline + newline);
            }
            helpTxt.setEditable(false);

            helpTxt.setWrapStyleWord(true);
            HelpFrame.add(helpTxt);
            HelpFrame.pack();
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            HelpFrame.setLocation(dim.width/2-HelpFrame.getSize().width/2, dim.height/2-HelpFrame.getSize().height/2);
            HelpFrame.setVisible(true);
        }

    }


    /**
     * Klasa Wewnetrzna definiujaca zdarzenie klikniecia przycisku Najlepsze wyniki
     */
    public class bestScoresWindowEvent implements ActionListener
    {

        public bestScoresWindowEvent(){

        }
        @Override
        public void actionPerformed(ActionEvent e) {
            InfoFrame.dispatchEvent(new WindowEvent(InfoFrame, WindowEvent.WINDOW_CLOSING));
            InfoFrame.getContentPane().removeAll();

            JTextArea helpTxt = new JTextArea();
            if(Parameters.online==JOptionPane.NO_OPTION){
            for (int i = 0; i < 10; i++) {
                helpTxt.append((i+1) +". "+ newGame.getBestScores(i)+ " "+ newGame.getBestScoresPoints(i)+ newline);
            }}
            else
            {
                try {
                    int i;
                    OutputStream os = Parameters.socket.getOutputStream();
                    PrintWriter pw = new PrintWriter(os, true);
                    pw.println("GETHIGHSCORES");

                    InputStream is = Parameters.socket.getInputStream();
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(is));
                    String response = new String(br.readLine());
                    String noOfLines=response.substring(0,response.indexOf("_"));
                    String usableResponse=response.substring(response.indexOf("_")+1);
                    i=Integer.parseInt(noOfLines);

                    while(i!=0){

                        helpTxt.append(usableResponse+newline);

                        i--;
                        if(i!=0)
                        {
                            usableResponse = br.readLine();
                        }}

                }catch (Exception x)
                {
                    System.out.println("errror");
                }

            }
            helpTxt.setEditable(false);

            helpTxt.setWrapStyleWord(true);
            InfoFrame.add(helpTxt);
            InfoFrame.setSize(Parameters.infoFrameWidth,Parameters.infoFrameHeight);


            InfoFrame.repaint();
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
           InfoFrame.setLocation(dim.width/2-InfoFrame.getSize().width/2, dim.height/2-InfoFrame.getSize().height/2);
            InfoFrame.setVisible(true);
        }
    }



    /**
     *  Metoda wczytujaca zasady gry
     */
    public void setupRules()
    {
        if(Parameters.online==JOptionPane.NO_OPTION){
        try {
            File fXmlFile = new File("rules.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("rules");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    //String tmp= eElement.getTextContent();
                    rules.add(eElement.getTextContent());


                }
            }
        } catch (Exception e) {
            System.out.println("Blad wczytywania pliku");
        }}
        else{
            getRulesFromServer();
        }

    }


    /**
     * Metoda wczytujaca instrukcje sterowania
     */
    public void setupHelp()
    {
        if(Parameters.online==JOptionPane.NO_OPTION){
        try {
            File fXmlFile = new File("Pomoc.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("X");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    //String tmp= eElement.getTextContent();
                    help.add(eElement.getTextContent());


                }
            }
        } catch (Exception e) {
            System.out.println("Blad wczytywania pliku");
        }}
        else
        {
            getHelpFromServer();
        }

    }

    /**
     * Funkcja tworząca nowego Gracza
     */
    public void newPlayer()
    {
         Name = null;
         Name= new String();

         Name = JOptionPane.showInputDialog("Wprowadz nazwe gracza\n\nNazwa nie moze zawierac znaku  '_'  !!");


    }

    /**
     * Funkcja, ktora pobiera Zasady gry z serwera
     */
    public void getRulesFromServer()
    {
        try {
            int i;
            OutputStream os = Parameters.socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            pw.println("GETRULES");

            InputStream is = Parameters.socket.getInputStream();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is));
            String response = new String(br.readLine());
            String noOfLines=response.substring(0,response.indexOf("_"));
            String usableResponse=response.substring(response.indexOf("_")+1);
            i=Integer.parseInt(noOfLines);

            while(i!=0){

                rules.add(usableResponse);

                i--;
                if(i!=0)
                {
                    usableResponse = br.readLine();
                }}

        }catch (Exception e)
        {
            System.out.println("errror");
        }

    }

    /**
     * Funkcja, która pobiera Pomoc z serwera
     */
    public void getHelpFromServer()
    {
        try {
            int i;
            OutputStream os = Parameters.socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            pw.println("GETHELP");

            InputStream is = Parameters.socket.getInputStream();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is));
            String response = new String(br.readLine());
            String noOfLines=response.substring(0,response.indexOf("_"));
            String usableResponse=response.substring(response.indexOf("_")+1);
            i=Integer.parseInt(noOfLines);

            while(i!=0){

                help.add(usableResponse);

            i--;
            if(i!=0)
            {
                usableResponse = br.readLine();
            }}
        }catch (Exception e)
        {
            System.out.println("errror");
        }

    }

    }

