import java.io.*;
import java.net.*;

public class UDPSend {
  public static void main(String arg[]) {
    try {
      String host = arg[0];
      int port = Integer.parseInt(arg[1]);
      String msg = arg[2];
      msg+="\n";
      byte[] message = msg.getBytes();

      // Get the internet address of the specified host
      InetAddress address = InetAddress.getByName(host);

      // Initialize a datagram packet with data and address
      DatagramPacket packet = new DatagramPacket(message, message.length,
          address, port);

      // Create a datagram socket, send the packet through it, close it.
      DatagramSocket dsocket = new DatagramSocket();
      dsocket.send(packet);
      dsocket.close();
    } catch (Exception e) {
      System.err.println(e);
    }
  }
}

