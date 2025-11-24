package udpEquation;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class EquationServer {
    public static void main(String[] args) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(5002);
            byte[] receiveBuffer = new byte[1024];

            System.out.println("UDP Equation Solver Server is running on port 5002...");

            while (true) {
                // Receive request from client
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                serverSocket.receive(receivePacket);

                String request = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received: " + request);

                // Request format: "a:b" (values for a and b)
                String[] parts = request.trim().split(":");
                double a = Double.parseDouble(parts[0]);
                double b = Double.parseDouble(parts[1]);

                // Solve (a-b)^2 = a^2 - 2ab + b^2
                double result = (a * a) - (2 * a * b) + (b * b);

                String response = "Result of (a-b)^2 = " + result;
                byte[] sendBuffer = response.getBytes();

                // Send result back to client
                DatagramPacket sendPacket = new DatagramPacket(
                        sendBuffer, sendBuffer.length,
                        receivePacket.getAddress(), receivePacket.getPort()
                );
                serverSocket.send(sendPacket);

                System.out.println("Sent: " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
