import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Server server = new Server(8080);
		Scanner scanner = new Scanner(System.in);
		while(true) {
			System.out.print("Enter choice: ");
			String option = scanner.next();
			switch(option) {
				case "s":{
					server.start();
					break;
				}
				case "c":{
					try(Socket socket = new Socket("localhost",8080)){
						InputStream input = socket.getInputStream();
			            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			 
			            String line = reader.readLine();
			 
			            System.out.println(line);
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
				case "q":{
					server.close();
					System.out.println("goodbye");
					return;
				}
			}
		}
	}
}
