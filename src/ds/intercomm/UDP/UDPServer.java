package ds.intercomm.UDP;
import java.net.*;
import java.io.*;

public class UDPServer {
    public static void main(String args[]) {
       DatagramSocket aSocket = null;
       try {
           aSocket = new DatagramSocket(6789);
           byte[] buffer = new byte[1000];
           while(true) {
               DatagramPacket request = new DatagramPacket(buffer, buffer.length);
               aSocket.receive(request);
               DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), request.getAddress(), request.getPort());
               aSocket.send(reply);
           }
       } catch(SocketException e) {
           e.printStackTrace();
       } catch(IOException e) {
           e.printStackTrace();
       } finally {
           if(aSocket != null)
               aSocket.close();
       }
    }
}
