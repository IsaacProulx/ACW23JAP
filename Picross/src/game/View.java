package game;

import java.io.File;

import components.ControlPanel;
import components.GamePanel;
import components.LeftPanel;
import components.TopPanel;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Handles the GUI view
 * @author Isaac Proulx - 041007853
 */
public class View extends GridPane {
    private Controller controller;
    private Model model;
    private GamePanel gamePanel;
    private ControlPanel controlPanel;
    private LeftPanel leftPanel;
    private TopPanel topPanel;
    private ColorPicker colorPicker;
    private VBox optionsPanel;
    private MenuBar menuBar;
    private String RESOURCES_PATH = "/resources";
    private String IMAGE_PATH = RESOURCES_PATH + "/images";
    private String DIALOG_PATH = "resources/dialog";
    private StackPane endPane;
    private StackPane popUpPane;
    private StackPane errorPane;
    private Stage stage;
    final double HINT_DISPLAY_SIZE_FACTOR = 0.70;
    double TILE_SIZE = 75;

    public File chooseFile(String text, String baseDir){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(text);
        if(baseDir!=null) fileChooser.setInitialDirectory(new File(baseDir));
        return fileChooser.showOpenDialog(stage);
    }

    public void showError(String msg){
        ((Text) this.errorPane.getChildren().get(0)).setText(msg);
        this.add(errorPane,0,0,3,3);
    }

    public void hideError(){
        this.getChildren().remove(this.errorPane);
    }

    public void setColor(Color col){
        this.menuBar.styleProperty().set("-fx-background-color: #"+col.toString().substring(2)+";");
        this.leftPanel.setColor(col);
        this.topPanel.setColor(col);
        this.gamePanel.setColor(col);
        this.controlPanel.setColor(col);
        this.optionsPanel.styleProperty().set("-fx-background-color: #"+col.darker().darker().toString().substring(2)+";");
    }

    public void setLanguage(String language){
        model.getLanguage();
        this.getChildren().removeAll(
            menuBar
        );
        this.add(creatMenuBar(),0,0,3,1);
        this.controlPanel.setLanguage(language);
    }
        
    public void OLD_setTile(int idx, int state){
        Color col = model.getColor();
        StackPane tile = (StackPane) gamePanel.getChildren().get(idx);
        switch(state){
            case 0:
                tile.getStyleClass().set(1,"");
                tile.setStyle("-fx-background-color:#"+col.toString().substring(2)+";");
                break;
            case 1:
                tile.getStyleClass().set(1,"filled");
                tile.setStyle("-fx-background-color:#"+col.darker().darker().toString().substring(2)+";");
                break;
            case 2:
                tile.getStyleClass().set(1,"marked");
                tile.setStyle("-fx-background-color:#"+col.darker().toString().substring(2)+";");
                break;
            case 3:
                tile.getStyleClass().set(1,"markedWrong");
                break;
        }
    }

    public void setTile(int idx, String color){
        StackPane tile = (StackPane) gamePanel.getChildren().get(idx);
        tile.setStyle("-fx-background-color:#"+color+";");
    }

    public void setPoints(int points){
        this.controlPanel.getScoreCounter().setCount(""+points);
    }

    public void setTime(int time){
        //timerTime.setText(time+"s");
        this.controlPanel.getTimer().setCount(time+"s");
    }

    /**
     * Adds a new entry to the history area
     * @param entry The entry to add
     */
    public void addHistory(String entry){
        this.controlPanel.getHistoryArea().appendText(String.format("%s%n", entry));
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

    public void hidePopUp(){
        this.getChildren().remove(this.popUpPane);
        this.popUpPane.getChildren().clear();
    }

    public void showPopUp(Parent content){
        StackPane container = new StackPane();
        container.getStyleClass().add("tile");
        //prefSize won't work for some reason
        container.setMinSize(stage.getWidth()/2,stage.getHeight()/2);
        container.setMaxSize(stage.getWidth()/2,stage.getHeight()/2);
        container.getChildren().addAll(content);
        popUpPane.getChildren().addAll(
            container
        );
        this.add(popUpPane,0,0,3,3);
    }

    public void showHostSelect(){
        VBox container = new VBox();
        
        TextField hostInput = new TextField();

        Button cancelButton = new Button("Cancel");
        cancelButton.idProperty().set("hostCancelButton");
        cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this.controller);

        Button confirmButton = new Button("Confirm");
        confirmButton.idProperty().set("hostConfirmButton");
        confirmButton.setUserData(hostInput);
        confirmButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this.controller);

        container.getChildren().addAll(
            new Label("Enter Server Host Address"),
            hostInput,
            confirmButton,
            cancelButton
        );
        showPopUp(container);
    }

    public void showPortSelect(){
        VBox container = new VBox();
        
        TextField portInput = new TextField();

        Button cancelButton = new Button("Cancel");
        cancelButton.idProperty().set("portCancelButton");
        cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this.controller);

        Button confirmButton = new Button("Confirm");
        confirmButton.idProperty().set("portConfirmButton");
        confirmButton.setUserData(portInput);
        confirmButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this.controller);

        container.getChildren().addAll(
            new Label("Enter Server Port"),
            portInput,
            confirmButton,
            cancelButton
        );
        showPopUp(container);
    }

    public void hideDimension(){
        this.getChildren().remove(this.endPane);
        this.endPane.getChildren().clear();
    }

    public void showDimension(){
        VBox container = new VBox();
        container.getStyleClass().add("tile");
        //prefSize won't work for some reason
        container.setMinSize(stage.getWidth()/2,stage.getHeight()/2);
        container.setMaxSize(stage.getWidth()/2,stage.getHeight()/2);

        TextField dimInput = new TextField();

        Button cancelButton = new Button("Cancel");
        cancelButton.idProperty().set("dimensionCancelButton");
        cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this.controller);

        Button confirmButton = new Button("Confirm");
        confirmButton.idProperty().set("dimensionConfirmButton");
        confirmButton.setUserData(dimInput);
        confirmButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this.controller);

        container.getChildren().addAll(
            new Label("Enter Dimension (game will be DIMxDIM)"),
            dimInput,
            confirmButton,
            cancelButton
        );

        endPane.getChildren().addAll(
            container
        );
        this.add(endPane,0,0,3,3);
    }

    public void showNetworkConfigure(){
        VBox container = new VBox();
        
        TextField portInput = new TextField();

        Button cancelButton = new Button("Cancel");
        cancelButton.idProperty().set("networkConfigureCancelButton");
        cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this.controller);

        Button confirmButton = new Button("Confirm");
        confirmButton.idProperty().set("networkConfigureConfirmButton");
        confirmButton.setUserData(portInput);
        confirmButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this.controller);

        container.getChildren().addAll(
            new Label("Enter Server Port"),
            portInput,
            confirmButton,
            cancelButton
        );
        showPopUp(container);
    }

    public void hideColorPicker(){
        this.endPane.getChildren().clear();
        this.getChildren().remove(this.endPane);
    }
    
    public void showColorPicker(){
        ColorPicker colorPickerMark = new ColorPicker(model.getMarkColor());
        colorPickerMark.idProperty().set("colorPickerMark");
        colorPickerMark.addEventHandler(ActionEvent.ACTION, this.controller);

        ColorPicker colorPickerWrong = new ColorPicker(model.getWrongColor());
        colorPickerWrong.idProperty().set("colorPickerWrong");
        colorPickerWrong.addEventHandler(ActionEvent.ACTION, this.controller);

        ColorPicker colorPickerCorrect = new ColorPicker(model.getCorrectColor());
        colorPickerCorrect.idProperty().set("colorPickerCorrect");
        colorPickerCorrect.addEventHandler(ActionEvent.ACTION, this.controller);

        VBox container = new VBox();
        container.getStyleClass().add("tile");
        //prefSize won't work for some reason
        container.setMinSize(stage.getWidth()/2,stage.getHeight()/2);
        container.setMaxSize(stage.getWidth()/2,stage.getHeight()/2);

        Button doneButton = new Button("Done");
        doneButton.idProperty().set("colorPickerDone");
        doneButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this.controller);

        container.getChildren().addAll(
            new HBox(
                new VBox(
                    new Label("Mark Color:"),
                    colorPickerMark
                ),
                new VBox(
                    new Label("Wrong Color:"),
                    colorPickerWrong
                ),
                new VBox(
                    new Label("Correct Color:"),
                    colorPickerCorrect
                )   
            ),
            doneButton
        );

        endPane.getChildren().addAll(
            container
        );
        this.add(endPane,0,0,3,3);
        //colorPicker.show();
    }

    public void hideEnd(){
        if(!this.getChildren().contains(this.endPane)) return;
        this.endPane.getChildren().clear();
        this.getChildren().remove(this.endPane);
    }

    public void showEnd(boolean won){
        ImageView endImg = new ImageView(IMAGE_PATH+(won?"/gamewinner.png":"/gameend.png"));
        this.endPane = new StackPane();
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

    private MenuBar creatMenuBar(){
        DialogParser dp = new DialogParser();
        dp.parse(DIALOG_PATH+"/menu-"+model.getLanguage()+".txt");
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu(dp.get("fileMenu"));
        Menu helpMenu = new Menu(dp.get("helpMenu"));
        Menu gameMenu = new Menu(dp.get("gameMenu"));
        Menu languageMenu = new Menu(dp.get("languageMenu"));
        Menu modeMenu = new Menu(dp.get("modeMenu"));
        Menu networkMenu = new Menu(dp.get("networkMenu"));

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

        /*this.colorPicker = new ColorPicker();
        colorPicker.idProperty().set("colorPicker");
        colorPicker.addEventHandler(ActionEvent.ACTION, this.controller);*/
        MenuItem helpColour = createMenuItem(dp.get("helpColour"),"helpColour",new ImageView(IMAGE_PATH+"/piciconcol.gif"));
        MenuItem helpAbout = createMenuItem(dp.get("helpAbout"),"helpAbout",new ImageView(IMAGE_PATH+"/piciconabt.gif"));

        MenuItem networkPort = createMenuItem(dp.get("networkPort"), "networkPort");
        MenuItem networkHost = createMenuItem(dp.get("networkHost"), "networkHost");
        MenuItem networkConnect = createMenuItem(dp.get("networkConnect"), "networkConnect");
        MenuItem networkDisconnect = createMenuItem(dp.get("networkDisconnect"), "networkDisconnect");
        MenuItem networkUploadGame = createMenuItem(dp.get("networkUploadGame"), "networkUploadGame");
        MenuItem networkDownloadGame = createMenuItem(dp.get("networkDownloadGame"), "networkDownloadGame");
        MenuItem networkConfigure = createMenuItem(dp.get("networkConfigure"), "networkConfigure");

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

        networkMenu.getItems().addAll(
            networkHost,
            networkPort,
            networkConnect,
            networkDisconnect,
            networkUploadGame,
            networkDownloadGame,
            networkConfigure
        );

        menuBar.getMenus().addAll(
            fileMenu,
            gameMenu,
            helpMenu,
            networkMenu
        );
        return menuBar;
    }

    public void newGame(int dim){
        //this.getChildren().clear();
        if(this.getChildren().contains(endPane)) this.getChildren().remove(endPane);
        setTime(0);
        setPoints(0);
        this.gamePanel.newGame(dim, model.getColor());
        this.topPanel.newGame(dim);
        this.leftPanel.newGame(dim);
        //this.add(controlPanel, 2, 1, 1, 2);
    }

    public void resetGame(int dim){
        this.getChildren().remove(gamePanel);
        this.gamePanel.newGame(dim, model.getColor());
        this.add(gamePanel, 1, 2, 1, 1);
    }

    public void exit(){
        //maybe Model should be the one to own the stage?
        stage.close();
    }

    /**
     * Constructor generates the parent for the main scene
     * @return the parent node
     */
    public View(Controller controller, Model model, Stage stage){
        //final double TILE_SIZE = Screen.getPrimary().getBounds().getHeight()/(dim*2*HINT_DISPLAY_SIZE_FACTOR);
        int dim = model.getDim();
        this.model = model;
        this.controller = controller;
        this.stage = stage;

        this.endPane = new StackPane();
        this.popUpPane = new StackPane();
        /*(Button okButton = new Button("OK");
        okButton.idProperty().set("errorOK");
        okButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this.controller);
        this.endPane.getChildren().addAll(
            new VBox(new Text(), okButton)
        );*/

        this.gamePanel = new GamePanel(dim, TILE_SIZE, model.getColor(), controller); //createGamePanel(dim);
        this.leftPanel = new LeftPanel(dim, TILE_SIZE, model); //createLeftPanel(dim);
        this.topPanel = new TopPanel(dim, TILE_SIZE, model); //createTopPanel(dim);
        //this.controlPanel = createControlPanel(dim,topPanel.getPrefHeight());
        this.controlPanel = new ControlPanel(dim, TILE_SIZE, model.getLanguage(), controller);
        this.optionsPanel = createOptionsPanel(leftPanel.getPrefWidth(),topPanel.getPrefHeight());
        this.menuBar = creatMenuBar();
        //this.popupPanel = createPopupPanel();
        this.add(menuBar,0,0,3,1);
        this.add(optionsPanel, 0, 1, 1, 1);
        this.add(leftPanel, 0, 2, 1, 1);
        this.add(topPanel, 1, 1, 1, 1);
        this.add(gamePanel, 1, 2, 1, 1);
        this.add(controlPanel, 2, 1, 1, 2);
        newGame(model.getDim());
    }
}
