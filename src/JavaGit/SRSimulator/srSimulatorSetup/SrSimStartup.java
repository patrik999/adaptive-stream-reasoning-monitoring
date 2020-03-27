package srSimulatorSetup;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SrSimStartup {
	static final int SR_PORT = 1976;
	
	public static DataOutputStream out;
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		System.out.println(java.time.LocalTime.now()+": "+"Started SR Simulator");

		Socket socket = new Socket("localhost", SR_PORT);
		
		
		out = new DataOutputStream(socket.getOutputStream());
		test3();

		System.in.read();

		socket.close();
		System.out.println("Closed SR Socket");
		
		
	}
	public static void test1() throws IOException, InterruptedException {
		out.writeBytes("TrafficJam(U1,Karlsplatz)" + "\n");out.flush();
		out.writeBytes("TrafficJam(U3,Karlsplatz)" + "\n");out.flush();
		out.writeBytes("TrafficJam(U2,Karlsplatz)" + "\n");out.flush();
		Thread.sleep(1000);
		out.writeBytes("TrafficJam(U1,Karlsplatz)" + "\n");out.flush();
		out.writeBytes("TrafficJam(U3,Karlsplatz)" + "\n");out.flush();
		out.writeBytes("TrafficJam(U2,Karlsplatz)" + "\n");out.flush();
		out.writeBytes("TrafficJam(U2,Karlsplatz)" + "\n");out.flush();
		Thread.sleep(1000);
		out.writeBytes("TrafficJam(U1,Karlsplatz)" + "\n");out.flush();
		Thread.sleep(2000);
		out.writeBytes("TrafficJam(U2,Karlsplatz)" + "\n");out.flush();
		Thread.sleep(6000);
		out.writeBytes("TrafficJam(U2,Karlsplatz)" + "\n");out.flush();
	}
	public static void test2() throws InterruptedException, IOException {
		List<String> events= new ArrayList<String>();
		events.add("TrafficJam(U4,Karlsplatz)");
		events.add("TrafficJam(U0,Karlsplatz)");
		events.add("TrafficJam(U1,Karlsplatz)");
		events.add("TrafficJam(U2,Karlsplatz)");
		events.add("TrafficJam(U3,Karlsplatz)");
		events.add("TrafficJam(U3,Karlsplatz)");
		events.add("TrafficJam(U3,Karlsplatz)");
		events.add("TrafficJam(U3,Karlsplatz)");
		events.add("TrafficJam(U3,Karlsplatz)");
		events.add("TrafficJam(U3,Karlsplatz)");
		events.add("TrafficJam(U0,Karlsplatz)");
		events.add("TrafficJam(U0,Karlsplatz)");
		events.add("TrafficJam(U1,Karlsplatz)");
		events.add("TrafficJam(U2,Karlsplatz)");
		events.add("TrafficJam(U3,Karlsplatz)");

		Random r = new Random();
		int upperbound=10;
		
		while(true) {
			for(String s :events) {
		    	Thread.sleep(r.nextInt(upperbound)*1000);
		    	out.writeBytes(s + "\n");
		        out.flush();
			}
		}
	}
	public static void test3() throws IOException, InterruptedException {
		while(true) {
			out.writeBytes("TrafficJam(U4,Karlsplatz)" + "\n");out.flush();
			Thread.sleep(2000);
		}
	}

}
