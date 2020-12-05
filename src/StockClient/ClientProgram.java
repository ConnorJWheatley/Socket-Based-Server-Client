package StockClient;

import java.util.Scanner;

public class ClientProgram {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            try (Client client = new Client()) {
//                Scanner serverUpdates = new Scanner(client.socket.getInputStream());
                while (true) {
                    //String serverUpdate = serverUpdates.nextLine();
                    //System.out.println(serverUpdate);
                    String line = scanner.nextLine();
                    String[] substrings = line.split(" ");
                    switch (substrings[0].toLowerCase()) {
//                        case "hello":
//                            client.helloTest();
//                            break;
                        case "traders": // test
                            client.getTraders();
                            break;
                        case "stock_check": // test
                            client.stockCheck();
                            break;
                        case "trade":
                            client.trade(substrings[1]);
                            break;
                        default:
                            throw new Exception("Unknown command: " + substrings[0]);
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}