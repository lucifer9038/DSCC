package tcpDateTime;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeServer {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5001)) {
            System.out.println("Date Time TCP Server is running on port 5000...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client Connected!");

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); // auto-flush

                String request = in.readLine();  // expecting "getDateTime"
                System.out.println("Request received: " + request);

                if ("getDateTime".equalsIgnoreCase(request)) {
                    String currentDateTime = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
                    out.println("Current Date & Time: " + currentDateTime);
                } else {
                    out.println("Invalid Request");
                }

                clientSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
