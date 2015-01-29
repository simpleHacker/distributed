package ds.intercomm.UDP;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer {
    public static void main(String args[]) {
       DatagramSocket aSocket = null;
       try {
           aSocket = new DatagramSocket(6789);
           byte[] buffer = new byte[1000];
           while(true) {
               DatagramPacket request = new DatagramPacket(buffer, buffer.length);
               aSocket.receive(request);
               byte[] m = request.getData();
               byte[] mm = new byte[m.length-1];
               System.arraycopy(m ,1, mm, 0, mm.length);
               // report any message missing!
               if(m[0] != 0)
                   System.err.println("Request from "+ request.getAddress() + " at port " + request.getPort() + " loss messages before " + m[0]);
               System.out.println("Request from "+ request.getAddress() + " at port " + request.getPort() + " message " + m[0]);
               DatagramPacket reply = new DatagramPacket(mm, request.getLength() - 1, request.getAddress(), request.getPort());
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
