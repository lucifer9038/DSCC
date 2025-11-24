package udpcalc;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class CalculatorServer {

    public static void main(String[] args) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(5000);
            byte[] receiveBuffer = new byte[1024];

            System.out.println("UDP Calculator Server is running on port 5000...");

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                serverSocket.receive(receivePacket);

                String request = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received: " + request);

                // Parse request: "add:5:3"
                String[] parts = request.trim().split(":");
                String operation = parts[0].toLowerCase();
                double num1 = Double.parseDouble(parts[1]);
                double num2 = Double.parseDouble(parts[2]);
                double result = 0;

                switch (operation) {
                    case "add": result = num1 + num2; break;
                    case "sub": result = num1 - num2; break;
                    case "mul": result = num1 * num2; break;
                    case "div": result = (num2 != 0) ? num1 / num2 : Double.NaN; break;
                    default: result = Double.NaN;
                }

                String response = "Result:" + result;
                byte[] sendBuffer = response.getBytes();

                DatagramPacket sendPacket = new DatagramPacket(
                        sendBuffer,
                        sendBuffer.length,
                        receivePacket.getAddress(),
                        receivePacket.getPort()
                );
                serverSocket.send(sendPacket);

                System.out.println("Sent to client: " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
