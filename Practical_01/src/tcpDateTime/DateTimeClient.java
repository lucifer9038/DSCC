package tcpDateTime;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class DateTimeClient {

    public static void main(String[] args) {
        try (Socket s = new Socket("localhost", 5001)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true); // auto-flush

            // Send RPC request
            out.println("getDateTime");

            // Receive RPC response
            String response = in.readLine();
            System.out.println("Response from server: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
