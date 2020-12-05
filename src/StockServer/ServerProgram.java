package StockServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ServerProgram {

    private final static int port = 8888;
    private static long traderID = 0L;
    private static final StockMarket stockMarket = new StockMarket();
    private static final Vector<ClientHandler> clients = new Vector<>();

    private static void RunServer() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Waiting for incoming connections...");
            while (true) {
                Socket socket = serverSocket.accept();
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                ClientHandler client = new ClientHandler(socket, traderID, stockMarket);
                clients.add(client);
                // if someone new joins the market, tell all other traders who that is
                if(clients.size() > 1){
                    for (ClientHandler ch : clients){
                        ch.traderJoined(traderID);
                    }
                }
                new Thread(client).start();
                traderID++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        RunServer();
    }
}
