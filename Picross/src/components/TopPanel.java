package components;

import game.Model;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class TopPanel extends HBox {
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

        int mostHintsPerCol = 0;
        int hintColSize = (int) Math.ceil(dim/2.0);
        this.setPrefSize(dim*TILE_SIZE, TILE_SIZE*hintColSize);

        VBox colCount;
        for(int i=0; i<dim; i++){
            colCount = new VBox();
            colCount.setPrefSize(TILE_SIZE,(double) hintColSize);
            colCount.getStyleClass().add("hintDisplay");

            Pane spacer = new Pane();
            VBox.setVgrow(spacer, Priority.ALWAYS);
            colCount.getChildren().add(spacer);

            for(int j=0; j<hintColSize; j++){
                //if(hints[i].length > mostHintsPerCol) mostHintsPerCol = hints[i].length;
                StackPane hintText = new StackPane(new Text(""+model.getColHint(i,j)));
                hintText.setPrefSize(TILE_SIZE, TILE_SIZE);
                colCount.getChildren().add(hintText);
            }
            this.getChildren().add(colCount);
        }
        this.setColor(model.getColor());
    }

    /**
     * Generates the top panel which contains the hints for the column
     * @param dim The board dimension
     * @return The top panel
     */
    public TopPanel(int dim, double tileSize, Model model){
        this.model = model;
        this.TILE_SIZE = tileSize;

        newGame(dim);
    }
}
