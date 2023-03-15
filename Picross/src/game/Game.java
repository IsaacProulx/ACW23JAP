package game;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Game extends Application{
    private String RESOURCES_PATH = "/resources";
    private String CSS_PATH = RESOURCES_PATH + "/css";
    ControllableTimer timer;
    /**
     * Default constructor
     */
    public Game(){
        //this.controller = new Controller(this);
    }

    /**
     * Builds the scene for the stage (this method is automatically called by JavaFX)
     * @param stage The stage to show
     */
    @Override
    public void start(Stage stage) throws Exception {
        Model model = new Model();
        Controller controller = new Controller();
        View view = new View(controller, model);
        this.timer = new ControllableTimer(view);
        controller.setView(view);
        controller.setModel(model);
        model.setView(view);
        model.setTimer(timer);
        Scene scene = new Scene(view);
        scene.getStylesheets().clear();
        scene.getStylesheets().add(CSS_PATH+"/Game.css");
        stage.setScene(scene);
        stage.show();
        //timer.setStatus(ControllableTimer.TERMINATE);
        timer.start();
    }

    @Override
    public void stop(){
        System.out.println("Stopping...");
        timer.setStatus(ControllableTimer.TERMINATE);
    }

    /**
     * Entry point for program execution
     * @param args Command line args (Not used)
     */
    public static void main(String[] args){
        launch();
    }
}