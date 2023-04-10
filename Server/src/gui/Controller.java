package gui;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class Controller implements EventHandler<Event> {
    private Model model;

    public Controller(Model model){
        this.model = model;
    }

    private void handleButton(Button button){
        String id = button.idProperty().get();
        switch(id){
            case "startButton":
                TextField portText = (TextField) button.getUserData();
                model.startServer(portText.getText());
                break;
            case "resultsButton":
                model.log("Showing Results");
                break;
            case "endButton":
                model.stopServer();
                break;
        }
    }

    private void handleCheckBox(CheckBox checkBox){
        String id = checkBox.idProperty().get();
        switch(id){
            case "finalizeBox":
                model.setFinalize(checkBox.selectedProperty().get());
                break;
        }
    }

    @Override
    public void handle(Event e) {
        Object eventSource = e.getSource();
        Class<? extends Object> sourceClass = eventSource.getClass();
        if(sourceClass == CheckBox.class){
            handleCheckBox((CheckBox) eventSource);
            return;
        }
        if(sourceClass == Button.class){
            handleButton((Button) eventSource);
            return;
        }
    }
    
}
