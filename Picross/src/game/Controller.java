package game;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * Handles the GUI controller
 * @author Isaac Proulx - 041007853
 */
public class Controller implements EventHandler<Event> {
    private View view;
    private Model model;
    
    /**
     * Default constructor
     */
    public Controller() {
    }
    
    /**
     * Sets the view object
     * @param view A reference to the view (Game) object
     */
    public void setView(View view){
        this.view = view;
    }

    public void setModel(Model model){
        this.model = model;
    }

    private void saveGame(){

    }

    private void loadGame(){

    }

    private void setLanguage(String lang){

    }

    private void setGameMode(int mode){

    }

    private void showSolution(){

    }

    /**
     * Handles events on menu items
     * @param event The event object
     * @param item The menu item that triggered the event
     */
    private void handleMenuItem(MenuItem item){
        String id = item.idProperty().get();
        //view.addHistory("Selected menu item: "+item.getText());
        switch(id){
            case "languageEnglish":{
                view.setLanguage(0);
                break;
            }
            case "languageFrench":{
                view.setLanguage(1);
                break;
            }
            case "fileSave":{
                saveGame();
                break;
            }
            case "fileLoad":{
                loadGame();
                break;
            }
            case "modePlay":{
                setGameMode(0);
                break;
            }
            case "modeDesign":{
                setGameMode(1);
                break;
            }
            case "gameDimension":{
                view.showDimension();
                break;
            }
            case "gameNewGame":{
                model.newGame();
                break;
            }
            case "gameSolution":{
                showSolution();
                break;
            }
            case "gameReset":{
                model.resetGame();
                break;
            }
            case "helpColour":{
                //view.showColorPicker();
                break;
            }
        }
    }

    private void handleTile(StackPane tile, MouseButton button){
        int col = (int) tile.getProperties().get("col");
        int row = (int) tile.getProperties().get("row");
        view.addHistory("Clicked tile: ("+col+","+row+")\n");
        model.updateTile(row, col, button == MouseButton.SECONDARY);
    }

    private void handleButton(Button button){
        String id = button.idProperty().get();
        switch(id){
            case "resetButton":
                model.resetGame();
                break;
            case "newGame":
                model.newGame();
                break;
        }
    }

    private void handleCheckBox(CheckBox checkBox){
        String id = checkBox.idProperty().get();
        if(id=="markBox"){
            model.toggleMark();
            return;
        }
    }

    /**
     * Handle events
     * @param event The event to handle
     */
    @Override
    public void handle(Event e){
        //System.out.println(event.getEventType());
        Object eventSource = e.getSource();
        Class<? extends Object> sourceClass = eventSource.getClass();
        if(sourceClass == MenuItem.class) {
            handleMenuItem((MenuItem) eventSource);
            return;
        }
        if(sourceClass == StackPane.class){
            //the only clickable StackPanes should be tiles
            handleTile((StackPane) eventSource, ((MouseEvent) e).getButton());
            return;
        }
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
