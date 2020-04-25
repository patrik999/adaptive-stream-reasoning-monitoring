package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestProg {
	public static void main(String args[]) throws InterruptedException {
		Process p;
		String s;
		String acthex = "~/.local/bin/acthex";
		String plugins = "~/Desktop/install/adaptive-stream-reasoning-monitoring-master/stream-reasoner/plugins/";
		String programClient = "~/Desktop/install/adaptive-stream-reasoning-monitoring-master/stream-reasoner/pgms/encoding_stream_ws.hex";
		String programServer = "/home/stefan/Desktop/install/adaptive-stream-reasoning-monitoring-master/stream-reasoner/ws_server.py";
		String python = "~/miniconda3/bin/python3.6";
		try {
			p = Runtime.getRuntime().exec("python", new String[] {programServer});
			System.out.println(p.pid());
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			p.waitFor();
			if(p.exitValue()!=0) {
				try (final BufferedReader b = new BufferedReader(new InputStreamReader(p.getErrorStream()))) {
	                String line;
	                if ((line = b.readLine()) != null)
	                    System.out.println(line);
	            } catch (final IOException e) {
	                e.printStackTrace();
	            }    
			}
			p.waitFor();
	        System.out.println(p.exitValue());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}