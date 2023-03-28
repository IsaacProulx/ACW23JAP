import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {
	private Socket socket;
	private int ID;
	private Server server;
	public ServerThread(Server server, Socket socket, int ID) {
		this.socket = socket;
		this.ID = ID;
		this.server = server;
	}
	
	private int send(String data) {
		
		return 0;
	}
	
	private int broadcast(String data) {
		this.server.broadCast(this.ID, data);
		return 0;
	}
	
	private String process(String op, String data) {
		String res = "";
		switch(op) {
		case "send":
			if(send(data)==0) res="Sent message successfully.";
			else res = "Error sending message.";
			break;
		case "broadcast":
			if(broadcast(data)==0) res="Sent message successfully.";
			else res = "Error sending message.";
			break;
		case "exit":
			res = "GoodBye.";
			break;
		default:
			res = "Unknown instruction.";
		}
		return res;
	}
	
	public void run() {
		try {
			InputStream input = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			
			OutputStream output = socket.getOutputStream();
	        PrintWriter writer = new PrintWriter(output, true);

	        String text = "";
	        int spaceIdx = 0;
	        String op = "";
	        String data = "";
	        while(!text.equals("exit")) {
	        	text = reader.readLine();
	        	spaceIdx = text.indexOf(" ");
	        	data = "";
	        	if(spaceIdx == -1) spaceIdx = text.length();
	        	else data = text.substring(spaceIdx+1);
	        	op = text.substring(0, spaceIdx);
	        	writer.println(process(op,data));
	        }
	        socket.close();
		}catch(IOException e) {
			
		}
	}
}
