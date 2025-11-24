package udpDateTime;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class DateTimeClient {

	public static void main(String[] args) {
		try {
			DatagramSocket clientSocket = new DatagramSocket();
			byte[] sendBuffer = "getDateTime".getBytes();
			InetAddress serverAddress = InetAddress.getByName("localhost");
			DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length,serverAddress,5000);
			clientSocket.send(sendPacket);
			byte[] receiveBuffer = new byte[1024];
			
			DatagramPacket recivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
			clientSocket.receive(recivePacket);
			String dateTime = new String(recivePacket.getData(),0,recivePacket.getLength());
			System.out.println("Received from server : "+ dateTime);
			clientSocket.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}