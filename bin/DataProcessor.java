
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;

public class DataProcessor {
	static HashMap<String, String> properties = new HashMap<String, String>(16);
	static int _totalCharts = 0;
	
	public static void main(String a[]) throws Exception{
		DataInputStream dis = new DataInputStream(new FileInputStream(new File(a[0])));
		//loading all properties
		while (dis.available()>0){
			String line = dis.readLine();
			if (line.startsWith("#") ||!line.contains("="))
				continue;
			String[] propToken = line.split("=");
			properties.put(propToken[0], propToken[1]);
		}
		_totalCharts = Integer.parseInt(properties.get("chart.count"));
               //System.out.println("Total charts  = "+_totalCharts);
		for (int i=0; i<_totalCharts; i++){
			DataReceiverThread t = new DataReceiverThread("DataReceiverThread:"+i);
			t.port = properties.get("chart."+i+".receiveport");
                        //t.maxLineCount = Integer.parseInt(properties.get("chart."+i+".maxdata"));
			t.dos = new DataOutputStream(new FileOutputStream(new File(properties.get("chart."+i+".rawdata"))));
                        t.start();
			//t.join();
			
		}
	}
}

class DataReceiverThread extends Thread {
	public String port = null;
	public DataOutputStream dos = null;
	boolean init = false;
	private DatagramChannel datagramChannel;
	private DatagramSocket dsocket;
        byte[] buffer = new byte[2048];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
	
	DataReceiverThread(String name){
		super(name);
	}
	
	private void init(){
		try {
			dsocket = new DatagramSocket(Integer.parseInt(port));
		}catch(Exception e){
			//loled out.
			e.printStackTrace();
		}

	}
	public void run(){
		if (!init){
			init();
		}
		try {
                        while(true){
                      	   dsocket.receive(packet);
			   String msg = new String(buffer, 0, packet.getLength());
                           dos.writeBytes(msg);
                           dos.flush();
                           //dos.close();
                           //linecount++;
                           //if (i>maxLineCount){
                             //   dos.close();
                                
                           //}
                        }

		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}


