package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TestProg2 {
	public static void main(String args[]) throws InterruptedException {
		Process p;
		Process killProcess;
		String s;
		String acthex = "~/.local/bin/acthex";
		String plugins = "/home/stefan/Desktop/install/adaptive-stream-reasoning-monitoring-master/stream-reasoner/plugins/";
		String programClient = "/home/stefan/Desktop/install/adaptive-stream-reasoning-monitoring-master/stream-reasoner/pgms/encoding_stream_ws.hex";
		String clienTest = "/home/stefan/Desktop/install/adaptive-stream-reasoning-monitoring-master/stream-reasoner/pgms/test.hex";

		String programServer = "~/Desktop/install/adaptive-stream-reasoning-monitoring-master/stream-reasoner/ws_server.py";
		String python = "~/miniconda3/bin/python3.6";
		int counter = 0;
		try {
			p = Runtime.getRuntime().exec("acthex" + " --flpcheck=none --pluginpath=" + plugins + " \n"
					+ "--plugin=stream-reasoning-plugin " + clienTest + " --hidemodels --sleep 2000");
			System.out.println(p.pid());
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while (true) {
				while ((s = br.readLine()) != null) {
					counter++;
					if (counter == 10) {
						killProcess=Runtime.getRuntime().exec("kill -SIGHUP "+p.pid());
					}
					System.out.println("line: " + s);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
