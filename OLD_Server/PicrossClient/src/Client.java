import java.io.Console;

public class Client{
    private Console console;
    private boolean running;
    private ClientThread ct;
    public static void main(String[] args){
        if(args.length < 2) return;
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        new Client().start(host,port);
    }

    public void close(){
        System.out.println("Stopping...");
        this.running = false;
    }

    public void start(String host, int port){
        this.ct = new ClientThread(this,host,port);
        this.ct.start();
        this.running = true;
        console = System.console();
        while(this.running==true){
            ct.sendMessage(console.readLine(">"));
        }
    }
}