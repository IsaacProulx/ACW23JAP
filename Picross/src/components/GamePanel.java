package components;

import javax.management.modelmbean.ModelMBeanNotificationBroadcaster;

import game.Controller;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;

public class GamePanel extends TilePane {
    private double TILE_SIZE;
    private Controller controller;

    public void setColor(Color col){
        String colString = col.toString().substring(2);
        this.setStyle("-fx-background-color: #"+colString+";");
        this.getChildren().forEach(child->{
            child.setStyle("-fx-background-color: #"+colString+";");
        });
    }

    public void newGame(int dim, Color col){
        this.getChildren().clear();
        this.setPrefSize(dim*TILE_SIZE+1+1, dim*TILE_SIZE+1+1);
        for(int i=0; i<dim; i++){
            for(int j=0; j<dim; j++){
                StackPane tile = new StackPane();
                tile.getStyleClass().add("tile");
                tile.getStyleClass().add("");
                tile.setPrefSize(TILE_SIZE, TILE_SIZE);
                tile.getProperties().put("row",i);
                tile.getProperties().put("col",j);
                tile.idProperty().set(String.format("tile-%d-%d",j,i));
                tile.addEventHandler(MouseEvent.MOUSE_CLICKED, this.controller);
                this.getChildren().add(tile);
            }    
        }
        this.setColor(col);
    }

    /**
     * Generates the game panel containing the tiles
     * @param dim The board dimensions
     * @return The game panel
     */
    public GamePanel(int dim, double tileSize, Color col, Controller controller){
        this.controller = controller;
        this.TILE_SIZE = tileSize;
        newGame(dim, col);
    }
}
