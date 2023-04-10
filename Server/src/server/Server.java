package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;

import gui.Model;

public class Server extends Thread{
	private Model model;
	private ServerSocket serverSocket;
	private HashMap<Integer,ServerThread> serverThreads;
	private int port;
	private int count;
	//compiler optimizations caused hours of pain...
	private volatile int state;
	public final int STATE_IDLE = 0;
	public final int STATE_HOST = 1;
	private final int STATE_HOSTING = 2;
	public final int STATE_TERMINATE = 3;

	public Server() {
		this.port = 3000;
		this.state = STATE_IDLE;
		serverSocket = null;
		serverThreads = null;
		count = 0;
	}

	public void setModel(Model model){
		this.model = model;
	}

	public void setPort(int port){
		this.port = port;
	}

	public void log(int ID, String message){
		model.log("Server thread ["+ID+"]: "+message);
	}

	public void setGame(byte[] gameData){
		model.setGameData(gameData);
	}

	public byte[] getGame(){
		return model.getGame();
	}

	public void broadCast(int senderID, byte[] message) {
		serverThreads.forEach((receiverID, serverThread)->{
			if(receiverID==senderID) return;
			serverThread.sendMessage(message);
		});
	}

	public void removeThread(int clientID){
		serverThreads.remove(clientID);
		if(this.serverThreads.size()==0) model.allClientsDCd();
	}
	
	private void __startup(){
		//don't use log in this method
		try {
			serverSocket = new ServerSocket(this.port);
			serverThreads = new HashMap<Integer,ServerThread>();
			this.state = STATE_HOSTING;
			while(this.state==STATE_HOSTING) {
				ServerThread thread = new ServerThread(this,serverSocket.accept(),count);
				serverThreads.put(count++,thread);
				thread.start();
			}
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void startServer(){
		this.state = STATE_HOST;
		log(-1,("Server running on port "+this.port));
	}

	public void stopServer(){
		if(this.serverSocket == null) return;
		this.state = STATE_IDLE;
		ArrayList<Integer> keys = new ArrayList<>();
		keys.addAll(serverThreads.keySet());
		int size = serverThreads.size();
		for(int i=0; i<size; i++){
			this.serverThreads.get(keys.get(i)).terminate();
		}
		try {
			log(-1,"closing...");
			this.serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.state = STATE_IDLE;
	}

	public void terminateServer(){
		stopServer();
		this.state = STATE_TERMINATE;
	}
	
	@Override
	public void run() {
		this.state=STATE_IDLE;
		while(this.state!=STATE_TERMINATE){
			if(this.state==STATE_HOST) __startup();
		}
	}
}
