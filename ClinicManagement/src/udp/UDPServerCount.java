package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import servers.records.RecordManager;

public class UDPServerCount extends Thread {
    
    private  RecordManager database;
    private int serverPort;
    
    public UDPServerCount(int serverPort, RecordManager database) {
        this.database = database;
        this.serverPort = serverPort;
        System.out.println(serverPort);
    }
    
    public void run() {
        DatagramSocket serverSocket = null;
        DatagramPacket receivePacket;
        DatagramPacket sendPacket;
        try {
            while (true) {
                
                serverSocket = new DatagramSocket(serverPort);
                byte[] receiveData = new byte[1024];
                byte[] sendData = new byte[1024];
                while(true) {
                      receivePacket = new DatagramPacket(receiveData, receiveData.length);
                      serverSocket.receive(receivePacket);
                      
                      String sentence = new String( receivePacket.getData());
                      System.out.println("RECEIVED: " + sentence);
                      InetAddress IPAddress = receivePacket.getAddress();
                      int port = receivePacket.getPort();
                      String message = Integer.toString(database.getRecordCounts());  
                      sendData = message.getBytes();
                      sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                      serverSocket.send(sendPacket);
                }
            }
        } catch (SocketException e){
            System.out.println("Socket: " + e.getMessage());
            
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }
}