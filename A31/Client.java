import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Client{
    private Console console;
    public static void main(String[] args){
        if(args.length < 2) return;
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        new Client().run(host, port);
    }

    public void handleMessage(String message){
        console.writer().println("\n"+message);
    }

    public void run(String host, int port){
        try(Socket socket = new Socket(host,port)){
            new ClientThread(this, socket, console).start();;
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output,true);

            console = System.console();
            String text = "";
            while(!text.equals("exit")){
                text = console.readLine(">");
                writer.println(text);
            }
            socket.close();
        }catch(IOException e){

        }
    }
}