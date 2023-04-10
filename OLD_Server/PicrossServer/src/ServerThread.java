import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerThread extends Thread {
	private Socket socket;
	private int ID;
	private Server server;
	private boolean running;
	private OutputStream output;
	private final byte OP_SEND = (byte) 1;
    private final byte OP_BROADCAST = (byte) 2;
    private final byte OP_NOP = (byte) 0xFE;
    private final byte OP_EXIT = (byte) 0xFF;

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

	private byte[] intToBytes(int num){
        byte[] bytes = {(byte)((num>>24)&0xFF),(byte)((num>>16)&0xFF),(byte)((num>>8)&0xFF),(byte)(num&0xFF)};
        return bytes;
    }

	private byte[] packInstruction(byte op, byte[] data){
        byte[] res = new byte[1 + data.length + 4];
        int i=0;
        res[i++] = op;

        byte[] temp = intToBytes(data.length);
        res[i++] = temp[0];
        res[i++] = temp[1];
        res[i++] = temp[2];
        res[i++] = temp[3];

        for(; i<data.length; i++){
            res[i] = data[i-1];
        }
        return res;
    }

    private byte[] packInstruction(byte op){
        byte[] data = {0};
        return packInstruction(op,data);
    }

	public void sendMessage(byte[] data){
		//System.out.println("Sending Message...");
        try{
            output.write(data);
			//System.out.println("Success!");
        }catch(IOException e){
            System.out.println("Failed to send message");
        }
    }

	private void processInstruction(byte op, int datalen, byte[] data) {
		switch(op) {
		case OP_SEND:
			//if(send(data)==0) res="Sent message successfully.";
			//else res = "Error sending message.";
			break;
		case OP_BROADCAST:
			//if(broadcast(data)==0) res="Sent message successfully.";
			//else res = "Error sending message.";
			break;
		case OP_EXIT:
			System.out.println("Disconnecting client: "+ID+"...");
			sendMessage(packInstruction(OP_EXIT));
			this.server.disconnectClient(ID);
			this.running = false;
			break;
		case OP_NOP:
			//do nothing
		default:
			//return "Unknown instruction.".getBytes();
		}
	}
	
	public void run() {
		try {
			InputStream input = socket.getInputStream();
			//BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			
			this.output = socket.getOutputStream();
	        //PrintWriter writer = new PrintWriter(output, true);
			byte[] b = {0,0,0,0,1,0};
			output.write(b);

			int clientIDLen;
			byte[] clientID;
	        byte opcode;
            int dataLen;
            byte[] bytes;
            int temp;
			running = true;
	        while(running) {
				/*temp = input.read();
                System.out.println(temp);
                clientIDLen = temp;

				clientID = new byte[clientIDLen];
				input.read(clientID,0,clientIDLen);*/

	        	temp = input.read();
                //System.out.println(temp);
                opcode = (byte) temp;

                temp = input.read();
                dataLen = temp;
                dataLen <<=8;
                temp = input.read();
                dataLen |= temp;
                dataLen <<=8;
                temp = input.read();
                dataLen |= temp;
                dataLen <<=8;
                temp = input.read();
                dataLen |= temp;
                
                //System.out.println(dataLen);

                bytes = new byte[dataLen];
                input.read(bytes, 0, dataLen);
                //System.out.println(bytes);
                processInstruction(opcode,dataLen,bytes);
	        }
			//input.close();
			//output.close();
	        //socket.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
