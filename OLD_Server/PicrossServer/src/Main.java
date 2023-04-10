import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		boolean debug = true;
		int port = 8080;
		if(args.length<1 && !debug){
			System.out.println("\u001b[31mExpected Port\u001b[0m");
			return;
		}
		if(!debug) port = Integer.parseInt(args[0]);
		Server server = new Server(port);
		Scanner scanner = new Scanner(System.in);
		while(true) {
			System.out.print("Enter choice: ");
			String option = scanner.next();
			switch(option) {
				case "s":{
					System.out.println("Starting server on localhost:"+port);
					server.start();
					break;
				}
				case "q":{
					scanner.close();
					server.close();
					System.out.println("goodbye");
					return;
				}
			}
		}
	}
}
