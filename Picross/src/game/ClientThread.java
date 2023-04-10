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
    private String host;
    private Model model;
    private int port;
    private boolean downloading = false;
    private byte[] downloadedData;
    private volatile int state;
    private final int STATE_IDLE = 0;
    private final int STATE_CONNECTING = 1;
    private final int STATE_CONNECTED = 2;
    private final int STATE_TERMINATE = 3;
    private final byte OP_SEND = (byte) 1;
    private final byte OP_BROADCAST = (byte) 2;
    private final byte OP_SENDGAME = (byte) 3;
    private final byte OP_DOWNLOADGAME = (byte) 4;
    private final byte OP_NOP = (byte) 0xFE;
    private final byte OP_EXIT = (byte) 0xFF;

    public ClientThread(){
        this.messageClientHeader = null;
    }

    public ClientThread(String host, int port){
        this.messageClientHeader = null;
        this.host = host;
        this.port = port;
    }

    public void setModel(Model model){
        this.model = model;
    }

    public void setHost(String host){
        this.host = host;
    }

    public void setPort(int port){
        this.port = port;
    }

    private void log(String message){
        if(this.model==null) return;
        model.log(message);
    }

    public void sendGame(int dim, int solution){
        byte[] data = new byte[5];
        byte[] solutionBytes = intToBytes(solution);
        data[0] = (byte) dim;
        data[1] = solutionBytes[0];
        data[2] = solutionBytes[1];
        data[3] = solutionBytes[2];
        data[4] = solutionBytes[3];
        sendMessage(packInstruction(OP_SENDGAME, data));
    }

    public int[] downloadGame(){
        int[] res = new int[2];
        downloading = true;
        sendMessage(packInstruction(OP_DOWNLOADGAME));
        while(downloading){};
        byte[] solutionBytes = {downloadedData[1],downloadedData[2],downloadedData[3],downloadedData[4]};
        log("downloaded: "+downloadedData[0]+" "+downloadedData[1]+","+downloadedData[2]+","+downloadedData[3]+","+downloadedData[4]);
        res[0] = (int) downloadedData[0];
        res[1] = bytesToInt(solutionBytes);
        return res;
    }

    private int bytesToInt(byte[] bytes){
		int num = 0;
		num |= (int) bytes[0]&0xFF;
		num<<=8;
		num |= (int) bytes[1]&0xFF;
		num<<=8;
		num |= (int) bytes[2]&0xFF;
		num<<=8;
		num |= (int) bytes[3]&0xFF;
		return num;
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

        for(i=0; i<data.length; i++){
            res[i+5] = data[i];
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
        if(messageString == null || this.state!=STATE_CONNECTED) return;
        sendMessage(generateMessage(messageString));
    }

    public void sendMessage(byte[] data){
        //byte[] message = new byte[messageClientHeader.length + data.length];
        //for(int i=0; i<messageClientHeader.length; i++) message[i] = messageClientHeader[i];
        //for(int i=0; i<data.length; i++) message[messageClientHeader.length+i] = data[i];
        try{
            writer.write(data);
        }catch(IOException e){
            log("Failed to send message");
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
                log("Connected!");
                break;
            case OP_DOWNLOADGAME:
                downloadedData = data;
                downloading = false;
                log("Downloaded");
                //System.out.println(data[0]);
                //model.setDim(dim);
                //model.newGame(bytesToInt(solutionBytes));
                break;
            case OP_EXIT:
                this.state = STATE_IDLE;
                break;
            default:
                log("Received invalid instruction");
        }
    }

    public void listen(){
        try{
            this.socket = new Socket(host,port);
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            this.writer = output;
            //this.writer = new PrintWriter(output,true);
            this.state = STATE_CONNECTED;
            byte opcode;
            int dataLen;
            byte[] bytes;
            int temp;

            log("Connecting to server...");
            while(state==STATE_CONNECTED){
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
            input.close();
            output.close();
            output.flush();
            socket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void connect(){
        this.state = STATE_CONNECTING;
    }

    public void disconnect(){
        sendMessage(packInstruction(OP_EXIT));
    }

    public void exit(){
        this.state = STATE_TERMINATE;
    }

    public void run(){
        state = STATE_IDLE;
        while(state!=STATE_TERMINATE){
            if(state==STATE_CONNECTING) listen();
        }
    }
}
