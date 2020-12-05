package StockClient;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements AutoCloseable {
    final int port = 8888;

    private final Scanner reader;
    private final PrintWriter writer;
    public final Socket socket;
    private long traderID;

    public Client() throws Exception {
        // Connecting to the server and creating objects for communications
        socket = new Socket("localhost", port);
        reader = new Scanner(socket.getInputStream());

        // Automatically flushes the stream with every command
        writer = new PrintWriter(socket.getOutputStream(), true);

        // Parsing the response
        String line = reader.nextLine();
        if (line.trim().compareToIgnoreCase("success") != 0)
            throw new Exception(line);
        traderID = Long.parseLong(reader.nextLine());
        String listOfTraders = reader.nextLine();
        String traderWithStock = reader.nextLine();

        // Outputting information to user
        System.out.println("Logged in successfully.");
        System.out.println("Current traderID: " + traderID);
        System.out.println("Current list of traders: " + listOfTraders);
        System.out.println("Current trader with stock: " + traderWithStock);
//        System.out.println("Trader with stock: " + traderWithStock);
    }

    public void getTraders(){
        writer.println("TRADERS");

        String line = reader.nextLine();
        System.out.println(line);
//        int numOfTraders = Integer.parseInt(line);
//
//        int[] traders = new int[numOfTraders];
//        for (int i = 0; i < numOfTraders; i++) {
//            line = reader.nextLine();
//            traders[i] = Integer.parseInt(line);
//        }
//        return traders;
    }

    public long getTraderID() { return traderID; }

    public void helloTest(){
        writer.println("hello");
        String response = reader.nextLine();
        System.out.println(response);
    }

    public void stockCheck(){ // testing method
        writer.println("STOCK_CHECK");
        String response = reader.nextLine();
        System.out.println(response);
    }

//    public void newTraderJoined(long newTrader){
//        System.out.println("Trader " + newTrader + " has joined the market");
//    }

    public void trade(String toTraderID){
        String line = String.format("TRADE %s", toTraderID);
        writer.println(line);
        String response = reader.nextLine();
        System.out.println(response);
    }

    @Override
    public void close() throws Exception {
        reader.close();
        writer.close();
    }
}