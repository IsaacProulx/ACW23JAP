package game;

import javax.swing.Icon;
import javax.swing.plaf.IconUIResource;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Handles the GUI view
 * @author Isaac Proulx - 041007853
 */
public class View extends GridPane {
    private Controller controller;
    private Model model;
    private TilePane gamePanel;
    private VBox controlPanel;
    private TextArea historyArea;
    private VBox leftPanel;
    private HBox topPanel;
    private CheckBox markBox;
    private VBox optionsPanel;
    private MenuBar menuBar;
    private TextField timerTime;
    private TextField scoreCount;
    private String RESOURCES_PATH = "/resources";
    private String IMAGE_PATH = RESOURCES_PATH + "/images";
    private String DIALOG_PATH = "resources/dialog";
    private StackPane popupPanel;
    final double HINT_DISPLAY_SIZE_FACTOR = 0.70;
    final double TILE_SIZE = 75;

    public void setLanguage(int lang){
        this.getChildren().remove(menuBar);
        this.add(creatMenuBar((lang==0)?"menu-en.txt":"menu-fr.txt"),0,0,3,1);
    }
        
    public void setTile(int idx, int state){
        StackPane tile = (StackPane) gamePanel.getChildren().get(idx);
        switch(state){
            case 0:
                tile.getStyleClass().set(1,"");
                break;
            case 1:
                //tile.getStyleClass().set(1,"filled");
                tile.setStyle("-fx-background-color:#555555;");
                break;
            case 2:
                tile.getStyleClass().set(1,"marked");
                break;
            case 3:
                tile.getStyleClass().set(1,"markedWrong");
                break;
        }
    }

    public void setPoints(int points){
        scoreCount.setText(""+points);
    }

    public void setTime(int time){
        timerTime.setText(time+"s");
    }

    /**
     * Adds a new entry to the history area
     * @param entry The entry to add
     */
    public void addHistory(String entry){
        historyArea.appendText(String.format("%s%n", entry));
    }

    /**
     * Calculates the necessary left/right (can be used for either) margin
     * needed to horizontally center the child node in the specified parent node.
     * @param parentWidth The parent node to center within
     * @param childWidth The child node to be centered
     * @return The left/right margin
     */
    private double calculateHCenter(double parentWidth, double childWidth){
        return ((parentWidth-childWidth)/2);
    }

    /**
     * Generates an Insets to vertically center the child relative to the parent
     * @param insets The Insets to center based off
     * @param parentHeight The height of the parent to center within
     * @param childHeight The height of the child to center
     * @return The new Insets
     */
    private Insets verticallyCenter(Insets insets, double parentHeight, double childHeight){
        double margin = (parentHeight-childHeight)/2.0;
        return new Insets(margin,insets.getLeft(),margin,insets.getRight()); 
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

    /**
     * Generates the game panel containing the tiles
     * @param dim The board dimensions
     * @return The game panel
     */
    private TilePane createGamePanel(int dim){
        TilePane gamePanel = new TilePane();
        gamePanel.setPrefSize(dim*TILE_SIZE+1, dim*TILE_SIZE+1);
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
                gamePanel.getChildren().add(tile);
            }    
        }
        return gamePanel;
    }

    /**
     * Generates the history area in the control panel
     * @param controlPanelWidth The width of the control panel
     * @param controlPanelHeight The height of the control panel
     * @return
     */
    private TextArea createHistoryArea(double controlPanelWidth, double controlPanelHeight){
        final double historyAreaWidth = controlPanelWidth*0.8;
        final double historyAreaHeight = controlPanelHeight*0.8;

        TextArea historyArea = new TextArea();
        historyArea.setPrefSize(historyAreaWidth, historyAreaHeight);
        historyArea.editableProperty().set(false);
        VBox.setMargin(historyArea, horizontallyCenter(new Insets(controlPanelHeight*0.01),controlPanelWidth,historyAreaWidth));
        historyArea.getStyleClass().add(".historyArea");
        historyArea.idProperty().set("history");
        this.historyArea = historyArea;

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
     * Generates the timer label
     * @param scorePanelWidth The width of the timer panel
     * @param scorePanelHeight The height of the timer panel
     * @return The timer label
     */
    private Label createTimerLabel(double width, double timerPanelHeight){
        final double timerLabelWidth = width;
        final double timerLabelHeight = timerPanelHeight;

        Label timerLabel = new Label("Time:");
        timerLabel.setPrefSize(timerLabelWidth, timerLabelHeight);
        return timerLabel;
    }

    /**
     * Generates the timer time
     * @param scorePanelWidth The width of the timer panel
     * @param scorePanelHeight The height of the timer panel
     * @return The timer time
     */
    private TextField createTimerTime(double width, double timerPanelHeight){
        final double timerTimeWidth = width;
        final double timerTimeHeight = timerPanelHeight;
        TextField timerTime = new TextField("0s");
        timerTime.setPrefSize(timerTimeWidth, timerTimeHeight);
        timerTime.editableProperty().set(false);
        timerTime.focusTraversableProperty().set(false);
        this.timerTime = timerTime;
        //HBox.setMargin(timerTime, new Insets(marginTop,marginLeft,marginBottom,hCenterOffset));
        return timerTime;
    }

    /**
     * Generates a box containing the timer label and timer time inside the control panel
     * @param controlPanelWidth The width of the control panel
     * @param controlPanelHeight The height of the control panel
     * @return The timer panel
     */
    private HBox createTimerPanel(double controlPanelWidth, double controlPanelHeight){
        final double timerPanelWidth = controlPanelWidth*0.8;
        final double timerPanelHeight = TILE_SIZE/4;
        double centerMargin;
        HBox timerPanel = new HBox();
        timerPanel.setPrefSize(timerPanelWidth, timerPanelHeight);
        VBox.setMargin(timerPanel, horizontallyCenter(new Insets(controlPanelHeight*0.01),controlPanelWidth,timerPanelWidth));

        Label timerLabel = createTimerLabel(timerPanelWidth*0.4,timerPanelHeight);
        TextField timerTime = createTimerTime(timerPanelWidth*0.5,timerPanelHeight);
        centerMargin = calculateHCenter(timerPanelWidth, timerLabel.getWidth()+timerTime.getWidth());
        HBox.setMargin(timerLabel, new Insets(0,0,0,centerMargin));
        HBox.setMargin(timerTime, new Insets(0,centerMargin,0,0));
        //timerPanel.setStyle("-fx-background-color: #000000;");

        timerPanel.getChildren().addAll(
            timerLabel,
            timerTime
        );

        return timerPanel;
    }

    /**
     * Generates the score label
     * @param scorePanelWidth The width of the score panel
     * @param scorePanelHeight The height of the score panel
     * @return The score label
     */
    private Label createScoreLabel(double scorePanelWidth, double scorePanelHeight){
        final double width = scorePanelWidth*0.4;
        final double height = scorePanelHeight;

        Label scoreLabel = new Label("Points:");
        scoreLabel.setPrefSize(width, height);
        return scoreLabel;
    }

    /**
     * Generates the score counter
     * @param scorePanelWidth The width of the score panel
     * @param scorePanelHeight The height of the score panel
     * @return The score counter
     */
    private TextField createScoreCount(double scorePanelWidth, double scorePanelHeight){
        final double width = scorePanelWidth*0.5;
        final double height = scorePanelHeight;
        this.scoreCount = new TextField("0");
        scoreCount.setPrefSize(width, height);
        scoreCount.editableProperty().set(false);
        scoreCount.focusTraversableProperty().set(false);
        //HBox.setMargin(timerTime, new Insets(marginTop,marginLeft,marginBottom,hCenterOffset));
        return scoreCount;
    }

    /**
     * Generates a box containing the score label and score count inside the control panel
     * @param controlPanelWidth The width of the control panel
     * @param controlPanelHeight The height of the control panel
     * @return The score panel
     */
    private HBox createScorePanel(double controlPanelWidth, double controlPanelHeight){
        final double width = controlPanelWidth*0.8;
        final double height = TILE_SIZE/4;
        double centerMargin;
        HBox scorePanel = new HBox();
        scorePanel.setPrefSize(width, height);
        VBox.setMargin(scorePanel, horizontallyCenter(new Insets(controlPanelHeight*0.01),controlPanelWidth,width));

        Label scoreLabel = createScoreLabel(width,height);
        TextField scoreCount = createScoreCount(width,height);
        centerMargin = calculateHCenter(width, scoreLabel.getWidth()+scoreCount.getWidth());
        HBox.setMargin(scoreLabel, new Insets(0,0,0,centerMargin));
        HBox.setMargin(scoreCount, new Insets(0,centerMargin,0,0));
        //timerPanel.setStyle("-fx-background-color: #000000;");

        scorePanel.getChildren().addAll(
            scoreLabel,
            scoreCount
        );

        return scorePanel;
    }

    /**
     * Generates the reset button inside the control panel
     * @return The reset button
     */
    private Button createResetButton(){
        Button resetButton = new Button("Reset");
        resetButton.idProperty().set("resetButton");
        resetButton.addEventHandler(ActionEvent.ACTION, controller);
        return resetButton;
    }

    /**
     * Generates the mark box inside the control panel
     * @return The mark box
     */
    private CheckBox createMarkBox(){
        CheckBox markBox = new CheckBox("Mark");
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
    private HBox createButtonPanel(double controlPanelWidth, double controlPanelHeight){
        final double buttonPanelWidth = controlPanelWidth*0.8;
        final double buttonPanelHeight = controlPanelHeight*0.2;

        HBox buttonPanel = new HBox();
        buttonPanel.setPrefSize(buttonPanelWidth, buttonPanelHeight);
        VBox.setMargin(buttonPanel, horizontallyCenter(new Insets(controlPanelHeight*0.01),controlPanelWidth,buttonPanel.getWidth()));

        buttonPanel.getChildren().addAll(
            createResetButton(),
            createMarkBox()
        );

        return buttonPanel;
    }

    /**
     * Generates the control panel on the right
     * @param dim The board dimension
     * @param topPanelHeight The current height of the top panel
     * @return The control panel
     */
    private VBox createControlPanel(int dim, double topPanelHeight){
        final double controlPanelWidth = TILE_SIZE*3;
        final double controlPanelHeight = TILE_SIZE*dim + topPanelHeight;
        //System.out.println(topPanelHeight);

        VBox controlPanel = new VBox();
        controlPanel.setPrefSize(controlPanelWidth, controlPanelHeight);
        controlPanel.setStyle("-fx-background-color: #A0A0A0;");
        VBox.setVgrow(controlPanel, Priority.ALWAYS);

        controlPanel.getChildren().addAll(
            createLogo(controlPanelWidth, controlPanel.getPrefHeight()),
            createTimerPanel(controlPanelWidth, controlPanel.getPrefHeight()),
            createScorePanel(controlPanelWidth, controlPanel.getPrefHeight()),
            createHistoryArea(controlPanelWidth,controlPanel.getPrefHeight()),
            createButtonPanel(controlPanelWidth,controlPanel.getPrefHeight())
        );

        return controlPanel;
    }

    /**
     * Generates the left panel which contains the hints for the row
     * @param dim The board dimension
     * @return The left panel
     */
    private VBox createLeftPanel(int dim){
        int mostHintsPerRow = 0;
        int hintColSize = (int) Math.ceil(dim/2.0);

        VBox leftPanel = new VBox();
        leftPanel.setPrefSize(TILE_SIZE*hintColSize, dim*TILE_SIZE);
        
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
            leftPanel.getChildren().add(rowCount);
        }
        //if(Math.ceil(dim/2.0) > mostHintsPerRow) leftPanel.setPrefWidth(Math.floor(dim/2.0)*tileSize);

        return leftPanel;
    }

    /**
     * Generates the top panel which contains the hints for the column
     * @param dim The board dimension
     * @return The top panel
     */
    private HBox createTopPanel(int dim){
        int mostHintsPerCol = 0;
        int hintColSize = (int) Math.ceil(dim/2.0);
        
        HBox topPanel = new HBox();
        topPanel.setPrefSize(dim*TILE_SIZE, TILE_SIZE*hintColSize);

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
            topPanel.getChildren().add(colCount);
        }
        //topPanel.setPrefHeight(mostHintsPerCol*TILE_SIZE);

        return topPanel;
    }

    /**
     * Generates the options panel in the top left corner
     * @param width The width of the panel
     * @param height The height of the panel
     * @return The options panel
     */
    private VBox createOptionsPanel(double width, double height){
        VBox optionsPanel = new VBox();
        optionsPanel.setPrefSize(width, height);
        optionsPanel.setStyle("-fx-background-color: #A0A0A0;");

        MenuButton optionsMenu = new MenuButton("Options");
        optionsMenu.setPrefSize(width, TILE_SIZE);
        optionsMenu.setStyle("-fx-font: 24 arial");

        MenuItem language = new MenuItem("Language");
        MenuItem newGame = new MenuItem("New Game");
        MenuItem test = new MenuItem("Test");

        language.addEventHandler(Event.ANY, controller);
        newGame.addEventHandler(Event.ANY, controller);
        test.addEventHandler(Event.ANY, controller);

        optionsMenu.getItems().addAll(
            language,
            newGame,
            test
        );

        /*optionsPanel.getChildren().addAll(
            optionsMenu
        );*/

        return optionsPanel;
    }

    public void hideDimension(){
        this.add(menuBar,0,0,3,1);
    }

    public void showColorPicker(){
        System.out.println("color picker");
        StackPane endPane = new StackPane();
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.idProperty().set("colorPicker");
        colorPicker.addEventHandler(ActionEvent.ACTION, this.controller);
        endPane.getChildren().addAll(
            colorPicker
        );
        this.add(endPane,0,0,3,3);
    }

    public void showDimension(){
        System.out.println("test");
        StackPane dimPane = new StackPane();
        dimPane.getStyleClass().add("dimensionScreen");
        dimPane.getChildren().addAll(
            new Text("Hello World")
        );
        this.add(dimPane,0,0,3,3);
    }

    public void showEnd(boolean won){
        ImageView endImg = new ImageView(IMAGE_PATH+(won?"/gamewinner.png":"/gameend.png"));
        StackPane endPane = new StackPane();
        Button test = new Button("New Game");
        test.idProperty().set("newGame");
        test.addEventHandler(MouseEvent.MOUSE_CLICKED, this.controller);
        endPane.getChildren().addAll(
            endImg,
            test
        );
        this.add(endPane,0,0,3,3);
    }

    private MenuItem createMenuItem(String text, String id, Node graphic){
        MenuItem item = (graphic==null)?new MenuItem(text):new MenuItem(text, graphic);
        item.idProperty().set(id);
        item.addEventHandler(Event.ANY, this.controller);
        return item;
    }

    private MenuItem createMenuItem(String text, String id){
        return createMenuItem(text, id, null);
    }

    private MenuBar creatMenuBar(String diagFile){
        DialogParser dp = new DialogParser();
        dp.parse(DIALOG_PATH+"/"+diagFile);
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu(dp.get("fileMenu"));
        Menu helpMenu = new Menu(dp.get("helpMenu"));
        Menu gameMenu = new Menu(dp.get("gameMenu"));
        Menu languageMenu = new Menu(dp.get("languageMenu"));
        Menu modeMenu = new Menu(dp.get("modeMenu"));

        MenuItem languageEnglish = createMenuItem(dp.get("languageEnglish"),"languageEnglish");
        MenuItem languageFrench = createMenuItem(dp.get("languageFrench"),"languageFrench");

        MenuItem fileSave = createMenuItem(dp.get("fileSave"),"fileSave");
        MenuItem fileLoad = createMenuItem(dp.get("fileLoad"),"fileLoad");

        MenuItem modePlay = createMenuItem(dp.get("modePlay"),"modePlay");
        MenuItem modeDesign = createMenuItem(dp.get("modeDesign"),"modeDesign");

        MenuItem gameDimension = createMenuItem(dp.get("gameDimension"),"gameDimension");
        MenuItem gameNewGame = createMenuItem(dp.get("gameNewGame"),"gameNewGame",new ImageView(IMAGE_PATH+"/piciconnew.gif"));
        MenuItem gameSolution = createMenuItem(dp.get("gameSolution"),"gameSolution",new ImageView(IMAGE_PATH+"/piciconsol.gif"));
        MenuItem gameReset = createMenuItem(dp.get("gameReset"),"gameReset");
        MenuItem gameExit = createMenuItem(dp.get("gameExit"),"gameExit",new ImageView(IMAGE_PATH+"/piciconext.gif"));

        ColorPicker colorPicker = new ColorPicker();
        colorPicker.idProperty().set("colorPicker");
        colorPicker.addEventHandler(ActionEvent.ACTION, this.controller);
        MenuItem helpColour = createMenuItem(dp.get("helpColour"),"helpColour",colorPicker);
        MenuItem helpAbout = createMenuItem(dp.get("helpAbout"),"helpAbout",new ImageView(IMAGE_PATH+"/piciconabt.gif"));

        languageMenu.getItems().addAll(
            languageEnglish,
            languageFrench
        );

        fileMenu.getItems().addAll(
            languageMenu,
            fileSave,
            fileLoad
        );

        modeMenu.getItems().addAll(
            modePlay,
            modeDesign
        );

        gameMenu.getItems().addAll(
            gameNewGame,
            gameSolution,
            gameExit,
            modeMenu,
            gameDimension,
            gameReset
        );

        helpMenu.getItems().addAll(
            helpColour,
            helpAbout
        );

        menuBar.getMenus().addAll(
            fileMenu,
            gameMenu,
            helpMenu
        );
        return menuBar;
    }

    public void newGame(int dim){
        this.getChildren().clear();
        this.gamePanel = createGamePanel(dim);
        this.leftPanel = createLeftPanel(dim);
        this.topPanel = createTopPanel(dim);
        this.controlPanel = createControlPanel(dim,topPanel.getPrefHeight());
        this.optionsPanel = createOptionsPanel(leftPanel.getPrefWidth(),topPanel.getPrefHeight());
        this.menuBar = creatMenuBar("menu-en.txt");
        //this.popupPanel = createPopupPanel();
        this.add(menuBar,0,0,3,1);
        this.add(optionsPanel, 0, 1, 1, 1);
        this.add(leftPanel, 0, 2, 1, 1);
        this.add(topPanel, 1, 1, 1, 1);
        this.add(gamePanel, 1, 2, 1, 1);
        this.add(controlPanel, 2, 1, 1, 2);
    }

    public void resetGame(int dim){
        this.getChildren().remove(gamePanel);
        this.gamePanel = createGamePanel(dim);
        this.add(gamePanel, 1, 2, 1, 1);
    }

    /**
     * Constructor generates the parent for the main scene
     * @return the parent node
     */
    public View(Controller controller, Model model){
        //final double TILE_SIZE = Screen.getPrimary().getBounds().getHeight()/(dim*2*HINT_DISPLAY_SIZE_FACTOR);
        //int dim = model.getDim();
        this.model = model;
        this.controller = controller;
        //int colHints[][] = {{5},{5},{4},{3},{1}};
        newGame(model.getDim());
    }
}
