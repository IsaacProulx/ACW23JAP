package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.Server;

public class Main extends Application {
    private Model model;
	public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Server server = new Server();
        server.setDaemon(true);

        this.model = new Model(server);
        Controller controller = new Controller(model);
        View view = new View(controller);
        model.setView(view);
        
        stage.setScene(new Scene(view));
        stage.show();
    }

    @Override
    public void stop(){
        this.model.terminateServer();
    }
}
