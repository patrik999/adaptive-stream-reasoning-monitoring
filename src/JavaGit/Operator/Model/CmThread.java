package Model;
import java.io.*;
import java.net.*;

public class CmThread extends Thread {
	protected Socket socket;

    public CmThread(Socket clientSocket) {
        this.socket = clientSocket;
        System.out.println(java.time.LocalTime.now()+": "+"Communication manager connected");
    }

    public void run() {
        InputStream inp = null;
        BufferedReader brinp = null;
        DataOutputStream out = null;
        try {
            inp = socket.getInputStream();
            brinp = new BufferedReader(new InputStreamReader(inp));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return;
        }
        String line;
        while (true) {
            try {
                line = brinp.readLine();
                if ((line == null) || line.equalsIgnoreCase("QUIT")) {
                    socket.close();
                    return;
                } else {
                	System.out.println("Communication Manager says: "+line);
                    out.writeBytes(line + "\n");
                    out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
    public void pushMessage(String message) {
        try {
        	DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        	out.writeBytes(message + "\n");
            out.flush();
        } catch (IOException e) {
        	e.printStackTrace();
        }

    }
}
