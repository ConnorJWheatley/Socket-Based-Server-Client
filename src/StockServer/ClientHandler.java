package StockServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private long traderID;
    private StockMarket stockMarket;

    public ClientHandler(Socket socket, long traderID, StockMarket stockMarket) {
        this.socket = socket;
        this.traderID = traderID;
        this.stockMarket = stockMarket;
    }

    public void traderJoined(long newTraderID) throws IOException {
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        writer.println("Joined: Trader " + newTraderID + " has joined the market.");
    }

//    public void traderLeft(long leftTraderID) throws IOException {
//        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
//        writer.println("Trader " + leftTraderID + " has left the market.");
//    }

    @Override
    public void run() {
        try (
                Scanner scanner = new Scanner(socket.getInputStream());
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
            try {
                System.out.println("New connection; trader ID " + traderID);
                writer.println("SUCCESS");
                if(stockMarket.getListOfTraders().isEmpty()){
                    stockMarket.addTrader(traderID, 1);
                }
                else {
                    stockMarket.addTrader(traderID, 0);
                }
                System.out.println("Current list of traders: " + stockMarket.getListOfTraders());
                writer.println(traderID);
                writer.println(stockMarket.getListOfTraders());
                writer.println(stockMarket.traderWithStock());

                while (true) {
//                    writer.println("Your trader ID: " + traderID);
//                    writer.println("Current traders in the market: " + stockMarket.getListOfTraders());
                    // print out who has the stock
                    String line = scanner.nextLine();
                    String[] substrings = line.split(" ");
                    switch (substrings[0].toLowerCase()) {
                        case "traders": // test
                            List<Long> traders = stockMarket.getListOfTraders();
                            writer.println(traders);
                            break;
                        case "stock_check": // test
                            Trader trader = stockMarket.getTrader(traderID);
                            writer.println(trader.getHasStock());
                            break;

                        case "trade":
                            long fromTrader = traderID;
                            long toTrader = Long.parseLong(substrings[1]);
                            if(stockMarket.getTrader(toTrader) == null){
                                writer.println("That trader is not in the market.");
                                break;
                            }

                            if(stockMarket.moveStock(fromTrader, toTrader)){
                                writer.println("TRADE SUCCESSFUL");
                                System.out.println("Trader " + traderID + " gave the stock to " + toTrader);
                            }
                            else {
                                writer.println("You have no stock to trade to " + toTrader);
                            }
                            break;

                        default:
                            throw new Exception("Unknown command: " + substrings[0]);
                    }
                }
            } catch (Exception e) {
                writer.println("ERROR " + e.getMessage());
                socket.close();
            }
        } catch (Exception e) {
        } finally {
            // removes the trader from the list that stores all of the current trader IDs and a Trader object with stores whether they have the stock or not
            Trader trader = stockMarket.getTrader(traderID);
            // if the client disconnecting has the stock, give it to another client connected at random
            if (trader.getHasStock() == 1 && stockMarket.getListOfTraders().size() > 1) {
                stockMarket.removeTrader(traderID);
                List<Long> traders = stockMarket.getListOfTraders();
                // want to pick a random trader connected to move the stock to
                int numOfTraders = stockMarket.getListOfTraders().size();
                int randNum = (int) (Math.random() * numOfTraders);
                long randomTraderID = traders.get(randNum);
                Trader traderToGetStock = stockMarket.getTrader(randomTraderID);
                traderToGetStock.setHasStock(1);
                System.out.println("Trader " + traderID + " disconnected and left the market with the stock. It has been given to trader " + randomTraderID);
            } else {
                System.out.println("Trader " + traderID + " disconnected.");
                stockMarket.removeTrader(traderID);
            }
            System.out.println("Current list of traders: " + stockMarket.getListOfTraders());

        }
    }
}

