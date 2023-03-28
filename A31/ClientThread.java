import java.io.BufferedReader;
import java.io.Console;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

public class ClientThread extends Thread {
    private Socket socket;
    private Client client;
    private HashMap<Integer, String> responses;
    private Console console;

    public ClientThread(Client client, Socket socket, Console console){
        this.socket = socket;
        this.client = client;
        this.console = console;
    }

    public void run(){
        try{
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String text = "";
            while(true){
                text = reader.readLine();
                console.writer().println(text);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
