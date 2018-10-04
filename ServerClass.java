package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Klasa reprezentuj¹ca serwer gry
 */
public class ServerClass implements Runnable {

    /**
     * Gniazdko komunikacyjne serwera
     */
    private ServerSocket serverSocket;

    /**
     * Konstruktor klasy Serwera
     */
    public ServerClass()
    {
        try {
            serverSocket = new ServerSocket(60000);
            new Thread(this).start();
        } catch (IOException e) {
            System.err.println("Error starting IBServer.");
            System.exit(1);

        }

    }

    /**
     * Metoda watku serwera
     */
    public void run()
    {
        while (true)
            try {
                System.out.println("Server is up! Waiting for connections...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client Connected !!");
               new Thread(new Service(clientSocket,this)).start();
            } catch (IOException e) {
                System.err.println("Error accepting connection. "
                        + "Client will not be served...");
            }
    }
    public static void main(String[] args) {
        new ServerClass();
    }
}
