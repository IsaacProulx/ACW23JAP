package game;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.text.Text;
public class Game extends Application{
    public Game(){
        //launch();
    }

    private Parent createContent(){
        GridPane mainPane = new GridPane();

        mainPane.add(new Text("Hello World"), 0, 0, 1, 1);

        return mainPane;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    public static void main(String[] args){
        launch();
    }

    private class Controller{

    }
}