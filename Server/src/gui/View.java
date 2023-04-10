package gui;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class View extends GridPane{
    private Controller controller;
    private String RESOURCES_PATH = "/resources";
    private String IMAGE_PATH = RESOURCES_PATH + "/images";
    //private String DIALOG_PATH = "resources/dialog";
    private Button startButton;
    private Button endButton;
    private Button resultsButton;
    private TextArea logArea;

    public View(Controller controller){
        this.controller = controller;
        ImageView logo = new ImageView(IMAGE_PATH+"/piccorssLogoServer.png");
        
        this.add(logo,0,0,1,1);
        this.add(createControlPane(),0,1,1,1);
        this.add(createLogArea(),0,2,1,1);
    }

    public void log(String message){
        this.logArea.appendText(message+"\n");
    }
    
    public TextArea createLogArea(){
        this.logArea = new TextArea();
        logArea.editableProperty().set(false);
        return logArea;
    }

    public HBox createControlPane(){
        HBox controlPane = new HBox();

        Label portLabel = new Label("Port:");
        TextField portText = new TextField();

        this.startButton = new Button("Start");
        startButton.idProperty().set("startButton");
        startButton.setUserData(portText);
        startButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this.controller);

        this.resultsButton = new Button("Results");
        resultsButton.idProperty().set("resultsButton");
        resultsButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this.controller);
        //resultsButton.setDisable(true);

        CheckBox finalizeBox = new CheckBox("Finalize");
        finalizeBox.idProperty().set("finalizeBox");
        finalizeBox.addEventHandler(ActionEvent.ACTION, this.controller);

        this.endButton = new Button("End");
        endButton.idProperty().set("endButton");
        endButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this.controller);
        endButton.setDisable(true);

        controlPane.getChildren().addAll(
            portLabel, portText,
            startButton,
            resultsButton,
            finalizeBox,
            endButton
        );

        return controlPane;
    }

    public void enableEndButton(){
        this.endButton.setDisable(false);
    }

    public void disableEndButton(){
        this.endButton.setDisable(true);
    }

    public void enableStartButton(){
        this.startButton.setDisable(false);
    }

    public void disableStartButton(){
        this.startButton.setDisable(true);
    }

    public void enableResultsButton(){
        this.resultsButton.setDisable(false);
    }

    public void disableResultsButton(){
        this.resultsButton.setDisable(true);
    }
}
