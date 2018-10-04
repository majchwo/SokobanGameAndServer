
package Application;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Klasa opisujaca komponent wyswietlajacy plansze gry
 */
public class GameArea extends JComponent implements MoveListener {

    /**
     * Zmienna przechowująca informację czy gra została ukończona
     */
    private boolean GameWon;

    /**
     * numer granego poziomu
     */
    private int level = 1;
//public String player;

    /**
     * Zmienna ustalajaca ilosc klatek animacji
     */

    int max = 20;
    /**
     * Zmienna ustalajaca nujmer aktualnego poziomu
     */
    int n = 1;
    /**
     * Zmienna ustalajaca ilosc zdobytych punktow
     */
    private int punkty = 0;
    /**
     * Zmienna pomocnicza przechowująca informacje o punktach z poprzedniego poziomu
     */
    private int currentPoints = 0;
    /**
     * Zmienna pomocnicza przechowująca informacje o ruchach z poprzedniego poziomu
     */
    private int currentMoves = 0;
    /**
     * Zmienna ustalajaca ilosc wykonanych ruchow w danym poziomie
     */
    private int ruchy = 0;
    /**
     * Liczba żyć gracza
     */
    private int zycia = 3;
    /**
     * Zmienna określający czy pauza jest włączona
     */
    private boolean pause = false;
    /**
     * Zmienna zliczajaca postep animacji ruchu
     */
    static int count = 0;
    /**
     * Zmienna ktora mowi czy juz zakonczyla sie animacja ruchu
     */
    private boolean drawed = true;
    /**
     * Zmienna ktora miwi czy juz zakonczyla sie animacja ruchu
     */
    private boolean ciagnij = false;


    /**
     * Zmienna ktora miwi czy juz zakonczyla sie animacja zniszczenia
     */
    private boolean destroyed = true;
    /**
     * Zmienna wskazujaca kierunek ruchu aktualnej animacji
     */
    private int direction = 0;
    /**
     * Zmienna wskazujaca numer paczki poruszajacej sie w trwajacej animacji
     */
    private int nbag = -1;
    /**
     * Zmienna wskazujaca numer paczki ciagnietej sie w trwajacej animacji
     */
    private int cbag = -1;
    /**
     * Stala okreslajaca kierunek w lewo
     */
    private final int LEFT_COLLISION = 1;
    /**
     * Stala okreslajaca kierunek w prawo
     */
    private final int RIGHT_COLLISION = 2;
    /**
     * Stala okreslajaca kierunek w gore
     */
    private final int TOP_COLLISION = 3;
    /**
     * Stala okreslajaca kierunek w dol
     */
    private final int BOTTOM_COLLISION = 4;
    /**
     * Tablica przechowujaca stan wczytanego poziomu
     */
    private char[][] bricks = new char[Parameters.numberOfBricksX][Parameters.numberOfBricksY];
    /**
     * Lista przechowujace wczystkie wyswietlane klocki
     */
    private ArrayList allbrick = new ArrayList();
    /**
     * Lista przechowujace wczystkie wyswietlane klocki Wall
     */
    private ArrayList walls = new ArrayList();
    /**
     * Lista przechowujace wczystkie wyswietlane klocki Package
     */
    private ArrayList packs = new ArrayList();
    /**
     * Lista przechowujace wczystkie wyswietlane klocki Destination
     */
    private ArrayList dest = new ArrayList();
    /**
     * Lista przechowujace wczystkie sciany do zniszczenia
     */
    private ArrayList destroylist = new ArrayList();

    /**
     * Obiekt gracza Sokoban
     */
    private SokobanBrick sokoban;

    /**
     *Nazwy najlepszych graczy
     */
    private ArrayList<String> bestScores = new ArrayList();
    /**
     * Punkty Najlepszych graczy
     */
    private ArrayList<Integer> bestScoresPoints = new ArrayList();


    /**
     * Adapter do obslugi przyciskow sterowania
     */
    AAdapter adapt = new AAdapter();

    /**
     * Maksymalny Poziom gry
     */
    private int maxlevel;

    /**
     * Nazwa gracza
     */
    private String playerName;


    /**
     * Konstruktor klasy GameArea sluzacy do tworzenia obiektow
     */
    GameArea(String name) {
//Dimension d=new Dimension(this.getMaximumSize().width,this.getPreferredSize().height);
//d.height=150;
//d.width=200;

        playerName = new String(name);
        if(Parameters.online==JOptionPane.NO_OPTION){
        setupBricks();}
        else
        {
            getLvlFromServer(1);
        }
        inicjalizuj();

        this.setFocusable(true);
        this.requestFocus();
        addKeyListener(adapt);
        adapt.addListener(this);
        // this.setMinimumSize(new Dimension(300, 200));
        GameWon = false;
        setupBestScores();
        maxlevel=Parameters.maxLevel;
    }

    /**
     * Metoda sluzaca stworzenia obiektow klockow na podstawie wczytanej tablicy poziomu
     */
    public final void inicjalizuj() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (bricks[i][j] == '#') {

                    boolean s;
                    if (i == 14 || i == 0 || j == 14 || j == 0) {
                        s = true;
                    } else {
                        s = false;
                    }
                    WallBrick b = new WallBrick(20 * j, 20 * i, s);

                    walls.add(b);

                }
                if (bricks[i][j] == '@') {

                    SokobanBrick z = new SokobanBrick(20 * j, 20 * i);
                    sokoban = z;

                }
                if (bricks[i][j] == '.') {
                    DestinationBrick x = new DestinationBrick(20 * j, 20 * i);
                    dest.add(x);

                }
                if (bricks[i][j] == '$') {
                    PackageBrick f = new PackageBrick(20 * j, 20 * i);
                    packs.add(f);

                }


            }


        }

        allbrick.addAll(walls);
        allbrick.addAll(dest);
        allbrick.addAll(packs);
        allbrick.add(sokoban);
    }

    /**
     * Metoda sluzaca rysowniu planszy rozgrywkji
     *
     * @param g grafika
     */
    public void paintComponent(Graphics g) {

        //super.paintComponent(g);
        int add = 0;
        int add2 = 0;
        Dimension size = getSize();
        int d = Math.min((size.width) / 20, (size.height) / 15);
        // d=d/15;
        if (size.height / 15 > size.width / 20) {
            add = (size.height - d * 15) / 2;
        } else {
            add2 = (size.width - d * 20) / 2;
        }

        for (int i = 0; i < allbrick.size(); i++) {
            Brick b = (Brick) allbrick.get(i);
            g.setColor(b.brickColor);
            g.fillRect(add2 + (int) ((float) b.brickX / (float) (20) * d) + (int) ((1.0 - b.multiplier) * 0.5 * d), add + (int) ((float) b.brickY / (float) (20) * d) + (int) ((1.0 - b.multiplier) * 0.5 * d), (int) (d * b.multiplier), (int) (d * b.multiplier));
            g.drawRect(add2 + (int) ((float) b.brickX / (float) (20) * d) + (int) ((1.0 - b.multiplier) * 0.5 * d), add + (int) ((float) b.brickY / (float) (20) * d) + (int) ((1.0 - b.multiplier) * 0.5 * d), (int) (d * b.multiplier), (int) (d * b.multiplier));

        }
        if (drawed == false) {
            if (pause == false) {
                animujsp();

            }
        }
        if (destroyed == false) {
            if (pause == false) {

                niszcz();
            }
            int x = 0;
        }
        g.setColor(Color.BLACK);
        int c = Math.min((size.width - 15 * d + 10) / 15, 15 * d / 7);
        c = 5 * d / 6;

        g.setFont(new Font("SansSerif", Font.BOLD, c));
//String s="Punkty: "+punkty+ System.lineSeparator()+"Ruchy: "+ruchy+"\nŻycia: "+zycia+"\nPULL "+ciagnij;
        //     g.drawString("Gracz: "+player, add2+d*15+5,add+g.getFontMetrics().getHeight());
        g.drawString("Punkty: " + punkty, add2 + d * 15 + 5, add + g.getFontMetrics().getHeight());
        g.drawString("Ruchy: " + ruchy, add2 + d * 15 + 5, add + 2 * g.getFontMetrics().getHeight());
        g.drawString("Zycia: " + zycia, add2 + d * 15 + 5, add + 3 * g.getFontMetrics().getHeight());
        g.drawString("Poziom " + level, add2 + d * 15 + 5, add + 4 * g.getFontMetrics().getHeight());
        g.drawString("PULL " + ciagnij, add2 + d * 15 + 5, add + 5 * g.getFontMetrics().getHeight());
        if (pause) {
            g.drawString("PAUZA", add2 + d * 15 + 5, 20 + add + 6 * g.getFontMetrics().getHeight());
        }
    }

    /**
     * Metoda sluzaca do obslugi zdarzenia ruchu
     *
     * @param e obiekt zdarzenia MoveEvent
     */

    public void domove(MoveEvent e) {
        if (e.direction == LEFT_COLLISION) {
            if (checkWallCollision(sokoban,
                    LEFT_COLLISION)) {
                return;
            }

            if (checkBagCollision(LEFT_COLLISION)) {
                return;
            }
            int u = givebagcollided(LEFT_COLLISION);
            int c = givebagcollided(RIGHT_COLLISION);
            drawed = false;
            direction = LEFT_COLLISION;
            nbag = u;
            cbag = c;
        } else if (e.direction == RIGHT_COLLISION) {
            if (checkWallCollision(sokoban,
                    RIGHT_COLLISION)) {
                return;
            }

            if (checkBagCollision(RIGHT_COLLISION)) {
                return;
            }

            int u = givebagcollided(RIGHT_COLLISION);
            int c = givebagcollided(LEFT_COLLISION);
            drawed = false;
            direction = RIGHT_COLLISION;
            nbag = u;
            cbag = c;
        } else if (e.direction == TOP_COLLISION) {
            if (checkWallCollision(sokoban,
                    TOP_COLLISION)) {
                return;
            }

            if (checkBagCollision(TOP_COLLISION)) {
                return;
            }

            int u = givebagcollided(TOP_COLLISION);
            int c = givebagcollided(BOTTOM_COLLISION);
            drawed = false;
            direction = TOP_COLLISION;
            nbag = u;
            cbag = c;
        } else if (e.direction == BOTTOM_COLLISION) {
            if (checkWallCollision(sokoban,
                    BOTTOM_COLLISION)) {
                return;
            }

            if (checkBagCollision(BOTTOM_COLLISION)) {
                return;
            }

            int u = givebagcollided(BOTTOM_COLLISION);
            int c = givebagcollided(TOP_COLLISION);
            drawed = false;
            direction = BOTTOM_COLLISION;
            nbag = u;
            cbag = c;

        }


    }

    /**
     * Metoda sluzaca wczytaniu poziomu z pliku konfiguracyjnego xml
     */
    public void setupBricks() {

        try {
            File fXmlFile = new File("level_1.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("L");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String tmp = eElement.getTextContent();
                    for (int i = 0; i < 15; i++) {
                        bricks[temp][i] = tmp.charAt(i);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Blad wczytywania pliku");
        }
    }

    /**
     * Metoda sluzaca do zmiany układu klocków w kolejnych klatkach animacji zniszczenia
     */
    private void niszcz() {
        if (destroyed == false) {

            for (int i = 0; i < destroylist.size(); i++) {
                int t = (int) destroylist.get(i);
                WallBrick wall = (WallBrick) walls.get(t);
                wall.multiplier = wall.multiplier - 0.05;


            }


            count++;
            if (count == max) {
                destroyed = true;
                count = 0;
                for (int j = 0; j < destroylist.size(); j++) {
                    allbrick.remove((int) destroylist.get(j));
                    walls.remove((int) destroylist.get(j));


                }
                destroylist.clear();
                ruchy += 100;


            }


        }
    }

    /**
     * Metoda sluzaca do zmiany układu klocków w kolejnych klatkach animacji ruchu
     */
    private void animujsp() {

        if (drawed == false) {
            if (direction == LEFT_COLLISION) {

                if (nbag == -1) {


                    sokoban.move(-1, 0);


                } else {

                    PackageBrick bag = (PackageBrick) packs.get(nbag);


                    bag.move(-1, 0);
                    sokoban.move(-1, 0);


                }

                if (ciagnij && cbag != -1) {
                    PackageBrick ccbag = (PackageBrick) packs.get(cbag);
                    ccbag.move(-1, 0);
                }


            } else if (direction == RIGHT_COLLISION) {

                if (nbag == -1) {


                    sokoban.move(1, 0);


                } else {

                    PackageBrick bag = (PackageBrick) packs.get(nbag);


                    bag.move(1, 0);
                    sokoban.move(1, 0);


                }
                if (ciagnij && cbag != -1) {
                    PackageBrick ccbag = (PackageBrick) packs.get(cbag);
                    ccbag.move(1, 0);
                }


            } else if (direction == TOP_COLLISION) {

                if (nbag == -1) {


                    sokoban.move(0, -1);


                } else {

                    PackageBrick bag = (PackageBrick) packs.get(nbag);


                    bag.move(0, -1);
                    sokoban.move(0, -1);


                }
                if (ciagnij && cbag != -1) {
                    PackageBrick ccbag = (PackageBrick) packs.get(cbag);
                    ccbag.move(0, -1);
                }

            } else if (direction == BOTTOM_COLLISION) {

                if (nbag == -1) {


                    sokoban.move(0, 1);


                } else {

                    PackageBrick bag = (PackageBrick) packs.get(nbag);


                    bag.move(0, 1);
                    sokoban.move(0, 1);


                }
                if (ciagnij && cbag != -1) {
                    PackageBrick ccbag = (PackageBrick) packs.get(cbag);
                    ccbag.move(0, 1);
                }


            }


        }


        count++;
        if (count == max) {
            drawed = true;
            count = 0;
            nbag = -1;
            if (ciagnij && cbag != -1) {
                ruchy += 9;
            }
            cbag = -1;
            direction = 0;
            if (iscompleted()) {
                zakonczpoziom();
            }

        }

    }

    /**
     * Metoda zakanczajaca rozgrywke poziomu
     */
    private void zakonczpoziom() {

        int bonus = n * 10000 - 10 * ruchy;
        if (bonus < 0) {
            bonus = 0;
        }

        punkty = punkty + n * 100 + bonus;

        n++;

        JOptionPane.showMessageDialog(null, "Wygrales poziom !!!!");
        level++;
        if(Parameters.online==JOptionPane.NO_OPTION)
        loadNewLevel();
        else {
            if(level<=Parameters.maxLevel){
            getLvlFromServer(level);
            currentMoves = ruchy;
            currentPoints = punkty;
            clearArea();
            inicjalizuj();}
            else
                endGame();
        }


    }

    /**
     * Metoda sluzaca do sprawdzenia czy wszystkie paczki zostaly umieszczone w celach
     *
     * @return czy wszystkie paczki zostaly umieszczone w celach
     */
    private boolean iscompleted() {
        // int destinations=dest.size();
        int count = 0;
        for (int i = 0; i < dest.size(); i++) {
            DestinationBrick d = (DestinationBrick) dest.get(i);
            for (int j = 0; j < packs.size(); j++) {
                PackageBrick p = (PackageBrick) packs.get(j);
                if (d.equalplace(p)) {
                    count++;
                }
            }


        }

        if (count == dest.size()) {
            return true;
        } else
            return false;


    }

    /**
     * Klasa sluzaca do obsługi przycisków sterowania
     */
    public class AAdapter extends KeyAdapter {


        private ArrayList<MoveListener> listeners = new ArrayList<MoveListener>();

        public void addListener(MoveListener toAdd) {
            listeners.add(toAdd);

        }

        public void makemove(MoveEvent e) {

            // Notify everybody that may be interested.
            for (MoveListener hl : listeners)
                hl.domove(e);
        }


        @Override
        public void keyReleased(KeyEvent e) {
            char z = e.getKeyChar();
            if (z == 'p' || z == 'P') {
                if (pause == true) {
                    pause = false;
                } else {
                    pause = true;
                }


            } else if (z == 'c' || z == 'C') {

                if (ciagnij == true) {
                    ciagnij = false;
                } else {
                    ciagnij = true;
                }


            }


        }


        public void keyPressed(KeyEvent e) {

            if (drawed == true && pause == false && destroyed == true) {
                int key = e.getKeyCode();

                char z = e.getKeyChar();
                if (key == KeyEvent.VK_LEFT) {
                    MoveEvent ev = new MoveEvent(LEFT_COLLISION);
                    makemove(ev);
                    ruchy++;


                } else if (key == KeyEvent.VK_RIGHT) {

                    ruchy++;
                    MoveEvent ev = new MoveEvent(RIGHT_COLLISION);
                    makemove(ev);


                } else if (key == KeyEvent.VK_UP) {
                    ruchy++;
                    MoveEvent ev = new MoveEvent(TOP_COLLISION);
                    makemove(ev);


                } else if (key == KeyEvent.VK_DOWN) {
                    ruchy++;
                    MoveEvent ev = new MoveEvent(BOTTOM_COLLISION);
                    makemove(ev);


                } else if (z == 'd' || z == 'D') {
                    for (int i = 0; i < walls.size(); i++) {
                        WallBrick wall = (WallBrick) walls.get(i);
                        if (wall.skrajny == false) {
                            if (sokoban.dolkolizja(wall) || sokoban.gorakolizja(wall) || sokoban.lewakolizja(wall) || sokoban.prawakolizja(wall) || sokoban.anglekolizja(wall)) {
                                destroylist.add(i);
                            }

                        }


                    }

                    destroyed = false;
                    Collections.reverse(destroylist);


                }


            }
        }

    }

    /**
     * Metoda sluzaca do sprawdzenia kolizji ze sciana WallBrick
     *
     * @param actor obiekt klocka ktorego kolizje spawdzamy
     * @param type  kierunek sprawdzanej kolizji
     * @return czy zaszla kolizja
     */
    private boolean checkWallCollision(Brick actor, int type) {

        if (type == LEFT_COLLISION) {

            for (int i = 0; i < walls.size(); i++) {
                WallBrick wall = (WallBrick) walls.get(i);
                if (actor.lewakolizja(wall)) {
                    return true;
                }
            }
            return false;

        } else if (type == RIGHT_COLLISION) {

            for (int i = 0; i < walls.size(); i++) {
                WallBrick wall = (WallBrick) walls.get(i);
                if (actor.prawakolizja(wall)) {
                    return true;
                }
            }
            return false;

        } else if (type == TOP_COLLISION) {

            for (int i = 0; i < walls.size(); i++) {
                WallBrick wall = (WallBrick) walls.get(i);
                if (actor.gorakolizja(wall)) {
                    return true;
                }
            }
            return false;

        } else if (type == BOTTOM_COLLISION) {

            for (int i = 0; i < walls.size(); i++) {
                WallBrick wall = (WallBrick) walls.get(i);
                if (actor.dolkolizja(wall)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /**
     * Metoda zwracajaca numer paczki ktora koliduje
     *
     * @param type kierunek sprawdzanej kolizji
     * @return numer kolidujacej paczki
     */
    private int givebagcollided(int type) {
        if (type == LEFT_COLLISION) {

            for (int i = 0; i < packs.size(); i++) {

                PackageBrick bag = (PackageBrick) packs.get(i);
                if (sokoban.lewakolizja(bag)) {


                    //isCompleted();
                    return i;
                }
            }
            return -1;

        } else if (type == RIGHT_COLLISION) {

            for (int i = 0; i < packs.size(); i++) {

                PackageBrick bag = (PackageBrick) packs.get(i);
                if (sokoban.prawakolizja(bag)) {


                    return i;
                    //isCompleted();
                }
            }
            return -1;

        } else if (type == TOP_COLLISION) {

            for (int i = 0; i < packs.size(); i++) {

                PackageBrick bag = (PackageBrick) packs.get(i);
                if (sokoban.gorakolizja(bag)) {
                    return i;
                }


                //isCompleted();
            }


            return -1;
        } else if (type == BOTTOM_COLLISION) {

            for (int i = 0; i < packs.size(); i++) {

                PackageBrick bag = (PackageBrick) packs.get(i);
                if (sokoban.dolkolizja(bag)) {


                    return i;
                    //isCompleted();
                }
            }
        }

        return -1;


    }

    /**
     * Metoda sluzaca do sprawdzenia kolizji ze paczka PackageBrick
     *
     * @param type kierunek sprawdzanej kolizji
     * @return czy zaszla kolizja
     */
    private boolean checkBagCollision(int type) {

        if (type == LEFT_COLLISION) {

            for (int i = 0; i < packs.size(); i++) {

                PackageBrick bag = (PackageBrick) packs.get(i);
                if (sokoban.lewakolizja(bag)) {

                    for (int j = 0; j < packs.size(); j++) {
                        PackageBrick item = (PackageBrick) packs.get(j);
                        if (!bag.equals(item)) {
                            if (bag.lewakolizja(item)) {
                                return true;
                            }
                        }
                        if (checkWallCollision(bag,
                                LEFT_COLLISION)) {
                            return true;
                        }
                    }
                    // bag.move(-20, 0);
                    //isCompleted();
                }
            }
            return false;

        } else if (type == RIGHT_COLLISION) {

            for (int i = 0; i < packs.size(); i++) {

                PackageBrick bag = (PackageBrick) packs.get(i);
                if (sokoban.prawakolizja(bag)) {
                    for (int j = 0; j < packs.size(); j++) {

                        PackageBrick item = (PackageBrick) packs.get(j);
                        if (!bag.equals(item)) {
                            if (bag.prawakolizja(item)) {
                                return true;
                            }
                        }
                        if (checkWallCollision(bag,
                                RIGHT_COLLISION)) {
                            return true;
                        }
                    }
                    //     bag.move(20, 0);
                    //isCompleted();
                }
            }
            return false;

        } else if (type == TOP_COLLISION) {

            for (int i = 0; i < packs.size(); i++) {

                PackageBrick bag = (PackageBrick) packs.get(i);
                if (sokoban.gorakolizja(bag)) {
                    for (int j = 0; j < packs.size(); j++) {

                        PackageBrick item = (PackageBrick) packs.get(j);
                        if (!bag.equals(item)) {
                            if (bag.gorakolizja(item)) {
                                return true;
                            }
                        }
                        if (checkWallCollision(bag,
                                TOP_COLLISION)) {
                            return true;
                        }
                    }
                    //  bag.move(0, -20);
                    //isCompleted();
                }
            }

            return false;

        } else if (type == BOTTOM_COLLISION) {

            for (int i = 0; i < packs.size(); i++) {

                PackageBrick bag = (PackageBrick) packs.get(i);
                if (sokoban.dolkolizja(bag)) {
                    for (int j = 0; j < packs.size(); j++) {

                        PackageBrick item = (PackageBrick) packs.get(j);
                        if (!bag.equals(item)) {
                            if (bag.dolkolizja(item)) {
                                return true;
                            }
                        }
                        if (checkWallCollision(bag,
                                BOTTOM_COLLISION)) {
                            return true;
                        }
                    }
                    // bag.move(0, 20);
                    //isCompleted();
                }
            }
        }

        return false;
    }


    /**
     * Metoda wczytujaca nowy poziom
     */
    public void loadNewLevel() {
        currentMoves = ruchy;
        currentPoints = punkty;
        if(level<=maxlevel){

            try {

                File fXmlFile = new File("level_" + level + ".xml");
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("L");
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        String tmp = eElement.getTextContent();
                        for (int i = 0; i < 15; i++) {
                            bricks[temp][i] = tmp.charAt(i);
                        }
                    }
                }


            } catch (Exception e) {

            }

            clearArea();
            inicjalizuj();
        }
        else {
            endGame();
        }

    }

    /**
     * Funkcja kończąca grę
     */
    public void endGame()
    {
        JOptionPane.showMessageDialog(null, "Wygrales gre!!!, nie ma wiecej poziomow");
        GameWon = true;
        clearArea();
        if(Parameters.online==JOptionPane.NO_OPTION) {
        addBestScore(playerName,punkty);}
        else
            addScoresToFile();

        level--;
    }
    /**
     * Funkcja wczytująca poprzednio grany poziom z pliku xml
     */
    public void loadPreviousLevel() {
        if(Parameters.online==JOptionPane.NO_OPTION) {
            try {
                File fXmlFile = new File("level_" + (level) + ".xml");
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("L");
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        String tmp = eElement.getTextContent();
                        for (int i = 0; i < 15; i++) {
                            bricks[temp][i] = tmp.charAt(i);
                        }
                    }
                }
            } catch (Exception e) {


                clearArea();

            }
        }
        else{
            getLvlFromServer(level);
        }
        clearArea();
        inicjalizuj();


    }

    /**
     * Metoda usuwająca klocki z planszy
     */
    public void clearArea() {
        allbrick.clear();
        packs.clear();
        walls.clear();
        dest.clear();
        sokoban = null;
    }



    /**
     * Funkcja resetująca poziom
     */
    public void resetLevel() {
        punkty = currentPoints;
        ruchy = currentMoves;
        if(GameWon==false) {
            zycia--;
            if (zycia == 0) {
                JOptionPane.showMessageDialog(null, "Koniec Gry, brak zyc");
                clearArea();
            } else {

                loadPreviousLevel();
            }
        }
        else
            JOptionPane.showMessageDialog(null,"Zakonczyles juz gre, nie mozesz zresetowac poziomu");
    }


    /**
     * Funkcja odczytująca listę najlepszych wyników
     */
    public void setupBestScores() {
        try {
            File fXmlFile = new File("highscores.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("name");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String tmp = eElement.getTextContent();

                    if (tmp != null)
                        bestScores.add(tmp);
                    else
                        bestScores.add("brak wynikow ");


                }
            }
        } catch (Exception e) {


            System.out.println("Nie udało sie załadować pliku");

        }

        try {
            File fXmlFile = new File("highscores.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("score");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String tmp = eElement.getTextContent();

                    if (tmp != "")
                        bestScoresPoints.add(Integer.parseInt(tmp));
                    else
                        bestScoresPoints.add(0);

                }
            }
        } catch (Exception e) {


            System.out.println("Nie udało sie załadować pliku");

        }

    }


    /**
     * Funkcja dodająca wynik do listy najlepszych wyników
     * @param name nazwa gracza
     * @param score punkty gracza
     */
    public void addBestScore(String name, int score) {
        for (int i = 0; i < bestScoresPoints.size(); i++) {
            if (score >= bestScoresPoints.get(i)) {

                bestScores.add(i, name);
                bestScoresPoints.add(i, score);
                bestScoresPoints.remove(bestScoresPoints.size()-1);
                bestScores.remove(bestScores.size() - 1);
                addScoresToFile();
                JOptionPane.showMessageDialog(null,"Gratulacje, zostales wpisany na liste TOP 10 na miejscu "+(i+1));
                break;
            }

        }


    }


    /**
     * Funkcja tworzaca plik z najlepszymi wynikami lub wysyła komendę do serwera aby zapiać wynik w pliku
     */
    public void addScoresToFile() {
        if(Parameters.online==JOptionPane.NO_OPTION) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("highscores");
            doc.appendChild(rootElement);
            for (int i = 0; i < 10; i++) {
                if (bestScores.size() <= i)
                    break;
                Element ScoreID = doc.createElement("ScoreID");
                rootElement.appendChild(ScoreID);
                ScoreID.setAttribute("id", Integer.toString(i));
                Element name = doc.createElement("name");
                name.appendChild(doc.createTextNode(bestScores.get(i)));
                ScoreID.appendChild(name);
                Element score = doc.createElement("score");
                score.appendChild(doc.createTextNode(Integer.toString(bestScoresPoints.get(i))));
                ScoreID.appendChild(score);
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("highscores.xml"));
            transformer.transform(source, result);


        } catch (Exception e) {

        }}
        else
        {
            try{
            OutputStream os = Parameters.socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            pw.println("PUTSCORE_"+playerName+"_"+punkty);
                InputStream is = Parameters.socket.getInputStream();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is));
                String response = new String(br.readLine());
                String noOfLines=response.substring(0,response.indexOf("_"));
                String usableResponse=response.substring(response.indexOf("_")+1);
                JOptionPane.showMessageDialog(null,usableResponse);
            }
            catch (Exception e)
            {
                System.out.println("error");
            }
        }
    }

    /**
     * Funkcja zwracająca nazwę gracza o i-tym najlepszym wyniku
     * @param i  miejsce gracza na liscie
     * @return nazwa gracza
     */
   public String getBestScores(int i)
    {
        return bestScores.get(i);
    }

    /**
     * Funkcja zwracająca punkty gracza o i-tym najlepszym wyniku
     * @param i miejsce gracza na liscie
     * @return punkty gracza
     */
   public int getBestScoresPoints(int i)
    {
        return bestScoresPoints.get(i);
    }

    /**
     * Metoda służąca do pobrania poziomu z serwera
     * @param lvl numer poziomu
     */
    public void getLvlFromServer(int lvl)
    {
        try {
            int i;
            OutputStream os = Parameters.socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            pw.println("GETLEVEL_"+lvl);

            InputStream is = Parameters.socket.getInputStream();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is));
            String response = new String(br.readLine());
            String noOfLines=response.substring(0,response.indexOf("_"));
            String usableResponse=response.substring(response.indexOf("_")+1);
            i=Integer.parseInt(noOfLines);
            int x=i;
            while(i!=0){

                for (int j = 0; j < 15; j++) {
                    bricks[(x-i)][j] = usableResponse.charAt(j);
                }
                i--;
                if(i!=0)
                {
                    usableResponse = br.readLine();
                }
            }
        }catch (Exception e)
        {
            System.out.println("errror");
        }
    }

}





