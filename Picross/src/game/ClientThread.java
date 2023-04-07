package game;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket socket;
    //private HashMap<Integer, String> responses;
    private byte[] messageClientHeader;
    private OutputStream writer;
    private boolean running;
    private String host;
    private int port;
    private final byte OP_SEND = (byte) 1;
    private final byte OP_BROADCAST = (byte) 2;
    private final byte OP_NOP = (byte) 0xFE;
    private final byte OP_EXIT = (byte) 0xFF;

    public ClientThread(){
        this.running = false;
        this.messageClientHeader = null;
    }

    public ClientThread(String host, int port){
        this.running = false;
        this.messageClientHeader = null;
        this.host = host;
        this.port = port;
    }

    public void setHost(String host){
        this.host = host;
    }

    public void setPort(int port){
        this.port = port;
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

    private byte[] generateMessage(String messageString) {
        int spaceIdx = 0;
        String op = "";
        String data = "";

        spaceIdx = messageString.indexOf(" ");
        data = "";
        if(spaceIdx == -1) spaceIdx = messageString.length();
        else data = messageString.substring(spaceIdx+1);
        op = messageString.substring(0, spaceIdx);
        

		switch(op) {
		case "send":
			return packInstruction(OP_SEND, data.getBytes());
		case "broadcast":
            return packInstruction(OP_BROADCAST, data.getBytes());
		case "exit":
            return packInstruction(OP_EXIT);
		default:
            return packInstruction(OP_NOP);
		}
	}

    public void sendMessage(String messageString){
        if(messageString == null || this.running==false) return;
        sendMessage(generateMessage(messageString));
    }

    public void sendMessage(byte[] data){
        //byte[] message = new byte[messageClientHeader.length + data.length];
        //for(int i=0; i<messageClientHeader.length; i++) message[i] = messageClientHeader[i];
        //for(int i=0; i<data.length; i++) message[messageClientHeader.length+i] = data[i];
        try{
            writer.write(data);
        }catch(IOException e){
            System.out.println("Failed to send message");
        }
    }

    public void processInstruction(byte opcode, int dataLen, byte[] data) throws IOException {
        // System.out.println("--------");
        // System.out.println(opcode);
        // System.out.println(dataLen);
        // System.out.println(data);
        // System.out.println("--------");
        switch(opcode){
            case 0x00:
                this.messageClientHeader = data;
                System.out.println("Connected!");
                break;
            case OP_EXIT:
                this.running = false;
                break;
            default:
                System.out.println("Received invalid instruction");
        }
    }

    public void run(){
        try{
            this.socket = new Socket(host,port);
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            this.writer = output;
            //this.writer = new PrintWriter(output,true);
            this.running = true;
            byte opcode;
            int dataLen;
            byte[] bytes;
            int temp;

            System.out.println("Connecting to server...");
            while(running){
                temp = input.read();
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

                bytes = new byte[dataLen];
                input.read(bytes, 0, dataLen);
                processInstruction(opcode,dataLen,bytes);
            }
            System.out.println("Press enter to exit...");
            input.close();
            output.close();
            socket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
