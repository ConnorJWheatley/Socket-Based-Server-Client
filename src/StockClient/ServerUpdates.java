package StockClient;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class ServerUpdates implements Runnable {

    private final Socket socket;

    public ServerUpdates(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(socket.getInputStream());
            try {
                while (true) {
                    String line = scanner.nextLine();
                    String[] substrings = line.split(" ");
                    System.out.println(substrings[0]);
                    switch (substrings[0].toLowerCase()) {
                        case "joined":
                        case "left":
                            System.out.println(Arrays.toString(substrings));
                            break;
                        default:
                            throw new Exception("Unknown server update: " + substrings[0]);
                    }
                }
            } catch (Exception e) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
