import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server extends Thread{
	private int port;
	private ServerSocket serverSocket;
	private HashMap<Integer,Socket> clientSockets;
	private int count;
	public Server(int port) {
		this.port = port;
		serverSocket = null;
		clientSockets = null;
		count = 0;
	}
	
	public void broadCast(int senderID, String message) {
		clientSockets.forEach((receiverID, client)->{
			if(receiverID==senderID) return;
			try {
				OutputStream output = client.getOutputStream();
		        PrintWriter writer = new PrintWriter(output, true);
		        writer.println(message);
			}catch(IOException e) {
				e.printStackTrace();
			}
	        
		});
	}
	
	public void run() {
		try {
			serverSocket = new ServerSocket(this.port);
			clientSockets = new HashMap<Integer,Socket>();
			while(true) {
				Socket socket = serverSocket.accept();
				new ServerThread(this,socket,count).start();
				clientSockets.put(count++,socket);
                //count++;
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		if(serverSocket==null) return;
		try {
			serverSocket.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		clientSockets.clear();
	}
}
