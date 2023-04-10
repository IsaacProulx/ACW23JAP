package gui;

import server.Server;

public class Model {
    private View view;
    private Server server;
    private boolean finalize;
    private byte[] gameData;

    /**
     * Parameterized constructor
     * @param server The Server object to manage connections
     */
    public Model(Server server){
        this.server = server;
        this.server.setModel(this);
        this.server.start();
        this.finalize = false;
        this.gameData = null;
    }

    public void setGameData(byte[] gameData){
        this.gameData = gameData;
    }

    public byte[] getGame(){
        return this.gameData;
    }

    /**
     * Sets the view property
     * @param view The View object being displayed
     */
    public void setView(View view){
        this.view = view;
    }

    public void setFinalize(boolean finalize){
        this.finalize = finalize;
    }

    /**
     * Wrapper for View.log
     * @param message The message to display
     */
    public void log(String message){
        this.view.log(message);
    }

    /**
     * Starts the game server
     * @param portString The port to run on
     */
    public void startServer(String portString){
        int port = 3000;
        try{
            port = Integer.parseInt(portString);
            if(port<3000 || port>65535){
                log("Port must be in range 3000 to 65535"); 
                return;
            }
        }catch(NumberFormatException e){
            log("Port must be a number");
            return;
        }
        this.view.disableStartButton();
        this.server.setPort(port);

        log("Starting Server...");
        this.server.startServer();
        this.view.enableEndButton();
    }

    /**
     * Stops the game server
     */
    public void stopServer(){
        this.view.disableEndButton();
        log("Stopping Server...");
        this.server.stopServer();
        this.view.enableStartButton();
    }

    public void allClientsDCd(){
        if(this.finalize) stopServer();
    }

    public void terminateServer(){
        this.view.disableEndButton();
        this.view.disableStartButton();
        log("Shutting Down...");
        this.server.terminateServer();
    }
}
