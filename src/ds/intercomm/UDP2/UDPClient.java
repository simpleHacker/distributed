package ds.intercomm.UDP2;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
public class UDPClient {
    public static void main(String args[]){
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket();
            InetAddress aHost = InetAddress.getByName(args[1]);
            System.out.println(aHost.getHostAddress());
            int serverPort = 6789;
            String[] messes = {"message one", "message two", "message three"};
            
            BufferedReader rd = new BufferedReader(new FileReader(args[0]));
            String mess;
            while((mess = rd.readLine()) != null) {
                
                // insert a byte to mark how many time tried to pass this message
                byte[] m = mess.getBytes();
                byte[] mm = new byte[m.length+1];
                System.arraycopy(m ,0, mm, 1, m.length);
                mm[0] = 0;
                
                DatagramPacket request = 
                    new DatagramPacket(mm, mess.length()+1, aHost, serverPort);
                aSocket.send(request);
                
                byte[] buffer = new byte[1000];
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                
                aSocket.setSoTimeout(100);
                int flag = 0; // flag to mark if it is succeed
                int try_times = 3;
                while(flag == 0)
                    try {
                        aSocket.receive(reply);
                        flag = 1;
                    } catch(SocketTimeoutException e) {
                        // add info into the mess to make clear how many times it try
                        if(try_times-- != 0)
                            System.out.println("Server not reply, re-send it again");
                        else {
                            System.err.println("Still not get reply, request failed!");
                            break;
                        }
                        mm[0] += 1;
                        DatagramPacket new_request = 
                                new DatagramPacket(mm, mess.length()+1, aHost, serverPort);
                        aSocket.send(new_request);
                    }
                System.out.println("Reply: " + reply.getLength() + ":" + new String(reply.getData()));
            }
            
            for(int i=0; i<3; ++i) {
                
                
            }
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if(aSocket != null) 
                aSocket.close();
        }
    }
}
