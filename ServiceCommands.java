package Server;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;

/**
 * Klasa pobierajaca dane konfiguracyjne z plikow oraz zawierajaca instrukcje postepowania serwera po otrzymaniu danego zadania od klienta
 */
public final  class ServiceCommands {

    /**
     * Zmienna określająca szerokość okna gry
     */
    public static int mainWindowFrameWidth;

    /**
     * Zmienna okreslajaca wysokosc okna gry
     */
    public static int mainWindowFrameHeight;

    /**
     * Ostatni poziom gry
     */
    public static int maxLevel;

    /**
     * Zmienna okreslajaca szerokos pola gry (w klockach)
     */
    public static int numberOfBricksX;

    /**
     * Zmienna okreslajaca szerokos pola gry (w klockach)
     */
    public static int numberOfBricksY;

    /**
     * Zmienna określająca szerokość okna informacyjnego
     */
    public static int infoFrameWidth;

    /**
     * Zmienna okreslajaca wysokosc okna informacyjnego
     */
    public static int infoFrameHeight;

    /**
     * Lista zawierajaca tekst pomocy w sterowaniu
     */
    private static ArrayList<String> help = new ArrayList();
    /**
     * Lista zawierajaca zasady gry
     */
    private static ArrayList<String> rules = new ArrayList();

    /**
     * Nazwy najlepszych graczy
     */
    private static ArrayList<String> bestScoresName = new ArrayList();
    /**
     * Punkty Najlepszych graczy
     */
    private static ArrayList<Integer> bestScoresPoints = new ArrayList();


    static {
        parseConfigurationFile();

///  wczytywanie pliku z zasadami gry
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
        }

///  wczytywanie pliku z pomocą
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
        }

///  wczytywanie pliku z najlepszymi wynikami
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
                        bestScoresName.add(tmp);
                    else
                        bestScoresName.add("brak wyników ");


                }
            }
        } catch (Exception e) {


            System.out.println("Nie udało sie załadować pliku");

        }
///  wczytywanie pliku z najlepszymi wynikami
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
     * Funkcja obsługująca żądanie pobrania przez klienta wartości zmiennych konfiguracyjnych
     * @param command komenda wyslana przez klienta
     * @return odpowiedz serwera
     */
    public static String clientRequest(String command) {


        String serverMessage = new String();
        switch (command) {

            case "GETFRAMEWIDTH":
                serverMessage = Integer.toString(mainWindowFrameWidth);
                break;
            case "GETFRAMEHEIGHT":
                serverMessage = Integer.toString(mainWindowFrameHeight);
                break;
            case "GETNUMOFBRICKSX":
                serverMessage = Integer.toString(numberOfBricksX);
                break;
            case "GETNUMOFBRICKSY":
                serverMessage = Integer.toString(numberOfBricksY);
                break;
            case "GETINFOFRAMEWIDTH":
                serverMessage = Integer.toString(infoFrameWidth);
                break;
            case "GETINFOFRAMEHEIGHT":
                serverMessage = Integer.toString(infoFrameHeight);
                break;

            case "GETMAXLEVEL":
                serverMessage = Integer.toString(maxLevel);
                break;
            default:
                serverMessage = "INVALID_COMMAND";
        }
        return serverMessage;
    }


    /**
     *Funkcja obsługująca żądanie pobrania przez klienta poziomu,instrukcji lub zasad gry
     * @param command komenda wyslana przez klienta
     * @return odpowiedz serwera
     */
    public static ArrayList<String> clientTxtRequest(String command) {
        ArrayList<String> serverMessage = new ArrayList<String>();
        int i=0;

        if(command.contains("GETLEVEL"))
        {
            i=Integer.parseInt(command.substring(command.indexOf("_")+1,command.length()));
            command="GETLEVEL";
        }
        switch (command) {
            case "GETHELP":
                serverMessage = help;
                break;
            case "GETRULES":
                serverMessage = rules;
                break;
            case "GETLEVEL":
                serverMessage=loadNewLevel(i);
                break;
            default:
                serverMessage = null;
        }
        return serverMessage;
    }

    /**
     * Funkcja ładująca z pliku xml następny poziom
     * @param level numer poziomu
     * @return odpowiedź serwera na żadanie klienta o nastepny poziom
     */
    public static ArrayList<String> loadNewLevel(int level) {

        ArrayList<String> bricks=new ArrayList<>();
        if (level <= maxLevel) {
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
                        bricks.add(tmp);

                    }
                }


            } catch (Exception e) {

            }

        }
        else
        {
            bricks.add("To był ostatni poziom");
        }
        return bricks;
    }


    /**
     * Funkcja zwracająca nazwy graczy z top 10
     * @return nazwy graczy z top 10
     */
    public static ArrayList<String> clientHighscoresNamesRequest()
    {
        return bestScoresName;
    }

    /**
     * Funkcja zwracająca punkty graczy z top 10
     * @return punkty graczy z top 10
     */
    public static ArrayList<Integer>clientHighscoresPointsRequest(){
        return bestScoresPoints;
    }

    /**
     * Funkcja dodająca nowy wynik do listy top 10
     * @param name nazwa gracza
     * @param score punkty gracza
     * @return miejsce na którym gracz został dodany (Jeśli -1 wtedy gracz nie znalazł się w top 10)
     */
    public static int addBestScore(String name, int score)
    {
        for (int i = 0; i < bestScoresPoints.size(); i++) {
            if (score >= bestScoresPoints.get(i)) {
                System.out.println("Score added on pos. "+(i+1));
                bestScoresName.add(i, name);
                bestScoresPoints.add(i, score);
                bestScoresPoints.remove(bestScoresPoints.size()-1);
                bestScoresName.remove(bestScoresName.size() - 1);
                addScoresToFile();
                return (i+1);
            }

        }

        return -1;
    }

    /**
     * Funkcja aktualizująca plik z najlepszymi wynikami
     */
    public static void addScoresToFile() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("highscores");
            doc.appendChild(rootElement);
            for (int i = 0; i < 10; i++) {
                if (bestScoresName.size() <= i)
                    break;
                Element ScoreID = doc.createElement("ScoreID");
                rootElement.appendChild(ScoreID);
                ScoreID.setAttribute("id", Integer.toString(i));
                Element name = doc.createElement("name");
                name.appendChild(doc.createTextNode(bestScoresName.get(i)));
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

        }
    }
}
