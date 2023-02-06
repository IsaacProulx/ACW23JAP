package game;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;

/**
 * Handles the GUI controller
 * @author Isaac Proulx - 041007853
 */
public class Controller implements EventHandler<Event> {
    private Game view;
    
    /**
     * Parameterized constructor
     * @param view A reference to the view (Game) object
     */
    public Controller(Game view) {
        this.view = view;
    }

    /**
     * Handles events on menu items
     * @param event The event object
     * @param item The menu item that triggered the event
     */
    private void handleMenuItem(ActionEvent event, MenuItem item){

        view.addHistory("Selected menu item: "+item.getText());
        switch(item.getText()){
            case "Language":{
                view.addHistory("Not implemented, would give language prompt.\n");
                break;
            }
            case "New Game":{
                view.addHistory("Not implemented, would give new game prompt.\n");
                break;
            }
            case "Test":{
                view.addHistory("Just a test option\n");
                break;
            }
        }
    }

    /**
     * Handle events
     * @param event The event to handle
     */
    @Override
    public void handle(Event event){
        //System.out.println(event.getEventType());
        if(event.getSource().getClass() == MenuItem.class) {
            handleMenuItem((ActionEvent) event, (MenuItem) event.getSource());
            return;
        }
        Node target = (Node) event.getSource();
        String id = target.idProperty().get();
        EventType<? extends Event> eventType = event.getEventType();
        //view.addHistory(id);
        if(eventType == MouseEvent.MOUSE_CLICKED){
            view.addHistory("Clicked "+id+"\n");
            return;
        }
        if(eventType == ActionEvent.ACTION){
            switch(id){
                case "resetButton":
                    view.addHistory("Reset pressed\n");
                    break;
                case "markBox":
                    view.addHistory(view.markSelected()?"Mark set\n":"Mark unset\n");
                    break;
                default:
                    System.out.println(event);
            }
            return;
        }
    }
}
