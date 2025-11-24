package udpDateTime;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			DatagramSocket serverSocket = new DatagramSocket(5000);
			byte[] reciveBuffer = new byte[1024];
			
			System.out.println("UDP date time server is running on port 5000...");
			
			while(true)
			{
				DatagramPacket recivePacket = new DatagramPacket(reciveBuffer, reciveBuffer.length);
				serverSocket.receive(recivePacket);
				
				String cureentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
				
				byte[] sendBuffer = cureentDateTime.getBytes();
				
				InetAddress clientAddress = recivePacket.getAddress();
				int clientPort = recivePacket.getPort();
				DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length,clientAddress ,clientPort);
				serverSocket.send(sendPacket);
				System.out.println("sent date/time to client: "+clientAddress+":"+clientPort);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}