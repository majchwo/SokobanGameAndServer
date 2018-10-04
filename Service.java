package Server;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Klasa reprezentujaca obsługę żądań klienta
 */
public class Service implements  Runnable {

    /**
     * Zmienna reprezentująca server który obsługuje klienta
     */
    private ServerClass server;

    /**
     * Gniazdko komunikacyjne klienta
     */
    private Socket clientSocket;

    /**
     * Bufor wejściowy
     */
    private BufferedReader input;

    /**
     * Strumien wyjsciowy
     */
    private PrintWriter output;

    /**
     * Konstruktor klasy
     * @param clientSocket Gniazdko klienta
     * @param server serwer
     */
    public Service(Socket clientSocket, ServerClass server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }


    /**
     * Metoda watku obslugi zadania klienta
     */
    @Override
    public void run() {
        while (true) {
            try {
                InputStream inputStream = clientSocket.getInputStream();
                input = new BufferedReader(new InputStreamReader(inputStream));
                OutputStream os = clientSocket.getOutputStream();
                output = new PrintWriter(os, true);
                String fromClient = input.readLine();

                if (fromClient != null) {
                    System.out.println("FROM_CLIENT: " + fromClient);
                    if(fromClient.equals("GETFRAMEWIDTH")|| fromClient.equals("GETFRAMEHEIGHT") || fromClient.equals("GETINFOFRAMEWIDTH")|| fromClient.equals("GETINFOFRAMEHEIGHT" )|| fromClient.equals("GETMAXLEVEL")||fromClient.equals("GETNUMOFBRICKSX")||fromClient.equals("GETNUMOFBRICKSY"))
                    {
                    String serverMsg = ServiceCommands.clientRequest(fromClient);
                    output.println(1+"_"+serverMsg);
                    output.flush();
                    System.out.println("TO_CLIENT: " + serverMsg);}
                    if (fromClient.equals("GETHELP") || fromClient.equals("GETRULES") || fromClient.contains("GETLEVEL")){
                        ArrayList<String> serverMsg= ServiceCommands.clientTxtRequest(fromClient);
                        output.println(serverMsg.size()+"_"+serverMsg.get(0));
                        output.flush();
                        for (int i=1;i<serverMsg.size();i++)
                        {
                            output.println(serverMsg.get(i));
                            output.flush();
                        }
                    }
                    if(fromClient.equals("GETHIGHSCORES"))
                    {
                        ArrayList<String> Names= ServiceCommands.clientHighscoresNamesRequest();
                        ArrayList<Integer> Points= ServiceCommands.clientHighscoresPointsRequest();

                        output.println(Names.size()+"_1. "+Names.get(0)+"  "+Points.get(0));
                        output.flush();
                        for (int i=1;i<Names.size();i++)
                        {
                            output.println((i+1)+". "+Names.get(i)+"  "+Points.get(i));
                            output.flush();
                        }
                    }
                    if(fromClient.contains("PUTSCORE"))
                    {
                        StringTokenizer st = new StringTokenizer(fromClient,"_");
                        st.nextToken();
                        int i=ServiceCommands.addBestScore(st.nextToken(),Integer.parseInt(st.nextToken()));
                        if(i>0){
                            output.println(1+"_Gratulacje, twoj wynik został zapisany na liscie top 10 Serwera na miejscu "+ i);
                        output.flush();}
                        else {
                            output.println(1+"_Niestety nie znalazłeś sie na liscie top 10");
                        output.flush();}

                    }
                }


            } catch (Exception e) {

            }
        }


    }
}
