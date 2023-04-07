package components;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import game.DialogParser;
import game.Controller;

public class ControlPanel extends VBox{
    final private String DIALOG_PATH = Constants.DIALOG_PATH;
    final private String IMAGE_PATH = Constants.IMAGE_PATH;
    private double tileSize;
    private String language;
    private double width;
    private double height;
    private TextArea historyArea;
    private Counter timer;
    private Counter scoreCounter;
    private CheckBox markBox;
    private Controller controller;
    private Button resetButton;

    public void setColor(Color col){
        this.styleProperty().set("-fx-background-color: #"+col.darker().darker().toString().substring(2)+";");
    }

    /**
     * Generates an Insets to horizontally center the child relative to the parent
     * @param insets The Insets to center based off
     * @param parentWidth The width of the parent to center within
     * @param childWidth The width of the child to center
     * @return The new Insets
     */
    private Insets horizontallyCenter(Insets insets, double parentWidth, double childWidth){
        double margin = (parentWidth-childWidth)/2.0;
        return new Insets(insets.getTop(),margin,insets.getBottom(),margin); 
    }

    public TextArea getHistoryArea(){
        return this.historyArea;
    }

    public Counter getTimer(){
        return this.timer;
    }

    public Counter getScoreCounter(){
        return this.scoreCounter;
    }

    public void setLanguage(String language){
        DialogParser dp = new DialogParser();
        dp.parse(DIALOG_PATH+"/control-panel-"+language+".txt");
        String timeText = dp.get("time");
        String pointsText = dp.get("points");
        String resetText = dp.get("reset");
        String markText = dp.get("mark");
        this.resetButton.setText(resetText);
        this.markBox.setText(markText);
        this.timer.setText(timeText);
        this.scoreCounter.setText(pointsText);
    }

    public void updateHistoryArea(){

    }
    
    /**
     * Generates the control panel on the right
     * @param dim The board dimension
     * @param topPanelHeight The current height of the top panel
     */
    public ControlPanel(int dim, double tileSize, String language, Controller controller){
        this.controller = controller;
        this.tileSize = tileSize;
        this.language = language;
        this.width = tileSize*3;
        //final double controlPanelHeight = TILE_SIZE*dim + topPanelHeight;
        this.height = tileSize*dim + tileSize*Math.ceil(dim/2.0);

        this.setPrefSize(this.width, this.width);
        this.setStyle("-fx-background-color: #A0A0A0;");
        VBox.setVgrow(this, Priority.ALWAYS);

        DialogParser dp = new DialogParser();
        dp.parse(DIALOG_PATH+"/control-panel-"+language+".txt");
        String timeText = dp.get("time");
        String pointsText = dp.get("points");
        String resetText = dp.get("reset");
        String markText = dp.get("mark");

        this.timer = new Counter(width*0.8, this.getPrefHeight(), tileSize, timeText, "0s");
        VBox.setMargin(timer, horizontallyCenter(new Insets(this.height*0.01),this.width,timer.getPrefWidth()));
        
        this.scoreCounter = new Counter(this.width*0.8, this.getPrefHeight(), tileSize, pointsText, "0");
        VBox.setMargin(scoreCounter, horizontallyCenter(new Insets(this.height*0.01),this.width,scoreCounter.getPrefWidth()));

        this.historyArea = createHistoryArea(this.width*0.8,this.height*0.8);
        VBox.setMargin(historyArea, horizontallyCenter(new Insets(this.getPrefHeight()*0.01),this.width,historyArea.getPrefWidth()));

        this.getChildren().addAll(
            createLogo(this.width, this.getPrefHeight()),
            timer,
            scoreCounter,
            historyArea,
            createButtonPanel(this.width,this.getPrefHeight(), resetText, markText)
        );
    }

    /**
     * Generates the history area in the control panel
     * @param width
     * @param height
     * @return The history area
     */
    private TextArea createHistoryArea(double width, double height){
        TextArea historyArea = new TextArea();
        historyArea.setPrefSize(width, height);
        historyArea.editableProperty().set(false);
        
        historyArea.getStyleClass().add(".historyArea");
        historyArea.idProperty().set("history");
        return historyArea;
    }

    /**
     * Generates the logo in the control panel
     * @param controlPanelWidth The width of the control panel
     * @param controlPanelHeight The height of the control panel
     * @return The logo
     */
    private ImageView createLogo(double controlPanelWidth, double controlPanelHeight){
        ImageView picrossLogo = new ImageView(IMAGE_PATH+"/picrossLogo.png");
        VBox.setMargin(picrossLogo, horizontallyCenter(new Insets(controlPanelHeight*0.02), controlPanelWidth, picrossLogo.getFitWidth()));
        return picrossLogo;
    }

    /**
     * Generates the reset button inside the control panel
     * @return The reset button
     */
    private Button createResetButton(String resetText){
        Button resetButton = new Button(resetText);
        resetButton.idProperty().set("resetButton");
        resetButton.addEventHandler(ActionEvent.ACTION, controller);
        this.resetButton = resetButton;
        return resetButton;
    }

    /**
     * Generates the mark box inside the control panel
     * @return The mark box
     */
    private CheckBox createMarkBox(String markText){
        CheckBox markBox = new CheckBox(markText);
        markBox.idProperty().set("markBox");
        markBox.addEventHandler(ActionEvent.ACTION, controller);
        this.markBox = markBox;
        return markBox;
    }

    /**
     * Generates a box containing the reset button and mark box inside the control panel
     * @param controlPanelWidth The width of the control panel
     * @param controlPanelHeight The height of the control panel
     * @return The button panel
     */
    private HBox createButtonPanel(double controlPanelWidth, double controlPanelHeight, String resetText, String markText){
        final double buttonPanelWidth = controlPanelWidth*0.8;
        final double buttonPanelHeight = controlPanelHeight*0.2;

        HBox buttonPanel = new HBox();
        buttonPanel.setPrefSize(buttonPanelWidth, buttonPanelHeight);
        VBox.setMargin(buttonPanel, horizontallyCenter(new Insets(controlPanelHeight*0.01),controlPanelWidth,buttonPanel.getWidth()));

        DialogParser dp = new DialogParser();
        dp.parse(DIALOG_PATH+"/control-panel-"+language+".txt");

        buttonPanel.getChildren().addAll(
            createResetButton(resetText),
            createMarkBox(markText)
        );

        return buttonPanel;
    }
}
