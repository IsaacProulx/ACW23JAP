package components;

import game.Model;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class LeftPanel extends VBox {
    private Model model;
    private double TILE_SIZE;
    
    public void setColor(Color col){
        String colString = col.toString().substring(2);
        this.setStyle("-fx-background-color: #"+colString+";");
        this.getChildren().forEach(child->{
            child.setStyle("-fx-background-color: #"+colString+";");
        });
    }

    public void newGame(int dim){
        this.getChildren().clear();
        
        int mostHintsPerRow = 0;
        int hintColSize = (int) Math.ceil(dim/2.0);
        this.setPrefSize(TILE_SIZE*hintColSize, dim*TILE_SIZE);
        
        HBox rowCount;
        for(int i=0; i<dim; i++){
            rowCount = new HBox();
            rowCount.setPrefSize(TILE_SIZE*hintColSize,TILE_SIZE);
            rowCount.getStyleClass().add("hintDisplay");
            
            Pane spacer = new Pane();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            rowCount.getChildren().add(spacer);

            for(int j=0; j<hintColSize; j++){
                //if(hints[i].length > mostHintsPerRow) mostHintsPerRow = hints[i].length;

                StackPane hintText = new StackPane(new Text(""+model.getRowHint(i,j)));
                hintText.setPrefSize(TILE_SIZE, TILE_SIZE);
                rowCount.getChildren().add(hintText);
            }
            this.getChildren().add(rowCount);
            //if(Math.ceil(dim/2.0) > mostHintsPerRow) leftPanel.setPrefWidth(Math.floor(dim/2.0)*tileSize);
        }
        this.setColor(model.getColor());
    }

    /**
     * Generates the left panel which contains the hints for the row
     * @param dim The board dimension
     * @return The left panel
     */
    public LeftPanel(int dim, double tileSize, Model model){
        this.model = model;
        this.TILE_SIZE = tileSize;

        newGame(dim);
        
    }
}
