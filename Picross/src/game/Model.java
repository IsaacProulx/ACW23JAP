package game;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javafx.scene.paint.Color;

public class Model {
    private View view;
    private ControllableTimer timer;
    private ClientThread gameClient;
    private int dim;
    private byte[][] board;
    private boolean mark;
    private int mode;
    private int solution;
    private int colHints[][];
    private int rowHints[][];
    private int points;
    private int numErrors;
    private String baseFileDir;
    private String language;
    private String host;
    private int port;
    private Color color;
    private Color markColor;
    private Color wrongColor;
    private Color correctColor;
    private Color clearColor;
    private final int TILE_EMPTY = 0;
    private final int TILE_CORRECT = 1;
    private final int TILE_MARK = 2;
    private final int TILE_WRONG = 4;

    public Model(){
        baseFileDir = null;
        language = "en";
        numErrors = 0;
        points = 0;
        dim = 5;
        mode = 0;
        mark = false;
        board = new byte[dim][dim];
        color = new Color(1,1,1,1);
        this.markColor = new Color(1, 1, 0, 1);
        this.wrongColor = new Color(1, 0, 0, 1);
        this.correctColor = new Color(0, 1, 0, 1);
        this.clearColor = new Color(1,1,1,1);
        generateSolution();
        generateRowHints();
        generateColHints();
    }

    public void setView(View view){
        this.view = view;
    }

    public void setTimer(ControllableTimer timer){
        this.timer = timer;
    }

    public void setGameClient(ClientThread gameClient){
        this.gameClient = gameClient;
        this.gameClient.setModel(this);
    }

    public void setLanguage(String language){
        this.language = language;
        view.setLanguage(language);
    }

    public void setColor(Color color){
        this.color = color;
        view.setColor(color);
        view.hideColorPicker();
    }

    public void setMarkColor(Color color){
        this.markColor = color;
        //view.setColor(col);
        for(int row=0; row<dim; row++){
            for(int col=0; col<dim; col++){
                if(board[row][col]==TILE_MARK) view.setTile(row*dim + col, markColor.toString().substring(2));
            }
        }
        view.hideColorPicker();
    }

    public void setWrongColor(Color color){
        this.wrongColor = color;
        //view.setColor(col);
        for(int row=0; row<dim; row++){
            for(int col=0; col<dim; col++){
                if(board[row][col]==TILE_WRONG) view.setTile(row*dim + col, wrongColor.toString().substring(2));
            }
        }
        view.hideColorPicker();
    }

    public void setCorrectColor(Color color){
        this.correctColor = color;
        //view.setColor(col);
        for(int row=0; row<dim; row++){
            for(int col=0; col<dim; col++){
                if(board[row][col]==TILE_CORRECT) view.setTile(row*dim + col, correctColor.toString().substring(2));
            }
        }
        view.hideColorPicker();
    }

    public void setDim(int dim){
        this.dim = dim;
    }

    public void setBaseFileDir(String baseFileDir) {
        this.baseFileDir = baseFileDir;
    }

    public void setHost(String host){
        this.host = host;
        gameClient.setHost(this.host);
    }

    public void setPort(String portString){
        this.port = Integer.parseInt(portString);
        gameClient.setPort(this.port);
    }
    
    public int getDim(){
        return dim;
    }

    public Color getColor(){
        return this.color;
    }

    public Color getMarkColor(){
        return this.markColor;
    }

    public Color getWrongColor(){
        return this.wrongColor;
    }

    public Color getCorrectColor(){
        return this.correctColor;
    }

    public String getLanguage(){
        return language;
    }

    public void connect(){
        gameClient.connect();
    }

    public void disconnect(){
        gameClient.disconnect();
    }

    public void exit(){
        System.out.println("Exit");
        view.exit();
        //just in case
        timer.setStatus(ControllableTimer.STOP);
        timer.setStatus(ControllableTimer.TERMINATE);
    }

    private int getSolutionForTile(int row, int col){
        return (solution>>((dim)*(dim-row)-1-col))&1;
    }

    private void checkPlayerWin(){
        for(int i=0; i<dim; i++) for(int j=0; j<dim; j++) if(board[i][j] != 1 && getSolutionForTile(i, j)==1) return;
        timer.setStatus(ControllableTimer.STOP);
        view.showEnd(numErrors == 0);
    }

    public void updateTile(int row, int col, boolean rightClick){
        Color color = null;
        if(mark || rightClick){
            if(board[row][col]==TILE_MARK){
                board[row][col] = TILE_EMPTY;
                color = this.clearColor;
            }else if(board[row][col]==TILE_EMPTY){
                board[row][col] = TILE_MARK;
                color = this.markColor;
            }
        }else if(board[row][col]==TILE_EMPTY){
            if(getSolutionForTile(row,col) != TILE_CORRECT){
                color = this.wrongColor;
                board[row][col] = TILE_WRONG;
                points--;
                numErrors++;
            }else{
                color = this.correctColor;
                board[row][col] = TILE_CORRECT;
                points++;
                checkPlayerWin();
            }
            view.setPoints(points);
        }
        if(color==null) return;
        //view.setTile(row*dim + col, board[row][col]);
        view.setTile(row*dim + col, color.toString().substring(2));
    }

    public void toggleMark(){
        this.mark = !mark;
        view.addHistory(mark?"Mark set\n":"Mark unset\n");
    }

    public int getColHint(int col, int idx){
        return colHints[col][idx];
    }

    public int getRowHint(int row, int idx){
        return rowHints[row][idx];
    }

    private void generateColHints(){
        int maxHintsPerCol = (int) Math.ceil(dim/2.0);
        colHints = new int[dim][maxHintsPerCol];
        for(int i=0; i<dim; i++){
            int hintNum = 0;
            int hint = 0;
            for(int j=0; j<dim; j++){
                int currentNum = getSolutionForTile(j,i);
                if(currentNum == 1) hint++;
                else if(hint>0){
                    colHints[i][hintNum++] = hint;
                    hint=0;
                }
            }
            if(hint>0) colHints[i][hintNum] = hint;
        }
    }

    private void generateRowHints(){
        int maxHintsPerCol = (int) Math.ceil(dim/2.0);
        rowHints = new int[dim][maxHintsPerCol];
        for(int i=0; i<dim; i++){
            int hintNum = 0;
            int hint = 0;
            for(int j=0; j<dim; j++){
                int currentNum = getSolutionForTile(i,j);
                System.out.print(currentNum);
                if(currentNum == 1) hint++;
                else if(hint>0){
                    rowHints[i][hintNum++] = hint;
                    hint=0;
                }
            }
            if(hint>0) rowHints[i][hintNum] = hint;
            System.out.println(rowHints);
        }
    }

    private void generateSolution(){
        solution = new Random().nextInt((int) Math.pow(2,(dim*dim - 1)))+2;
        //generateColHints();
        //generateRowHints();

        System.out.println(solution);
        /*for(int i=0; i<dim; i++){
            for(int j=0; j<dim; j++){
               // System.out.print((solution>>((dim)*(dim-i)-1-j))&1);
            }
            //System.out.println("");
        }*/
    }

    /**
     * Generates a new random game
     * @param dim
     */
    public void newGame(){
        points = 0;
        numErrors = 0;
        mark = false;
        board = new byte[dim][dim];
        generateSolution();
        generateColHints();
        generateRowHints();
        timer.setStatus(ControllableTimer.STOP);
        timer.setStatus(ControllableTimer.RESET);
        timer.setStatus(ControllableTimer.START);
        view.newGame(dim);
    }

    /**
     * Generates a new pre-defined game
     * @param dim
     */
    public void newGame(int solution){
        points = 0;
        numErrors = 0;
        mark = false;
        board = new byte[dim][dim];
        this.solution = solution;
        generateColHints();
        generateRowHints();
        timer.setStatus(ControllableTimer.STOP);
        timer.setStatus(ControllableTimer.RESET);
        timer.setStatus(ControllableTimer.START);
        view.newGame(dim);
    }

    /**
     * Resets the current game
     * @param dim
     */
    public void resetGame(){
        points = 0;
        numErrors = 0;
        board = new byte[dim][dim];
        timer.setStatus(ControllableTimer.STOP);
        timer.setStatus(ControllableTimer.RESET);
        timer.setStatus(ControllableTimer.START);
        view.resetGame(dim);
        view.setPoints(points);
        view.addHistory("Reset game\n");
    }

    

    public void saveConfig(){

    }

    public void saveGame(){
        File saveFile = view.chooseFile("Save Game",baseFileDir);
        setBaseFileDir(saveFile.getParent());
        try(OutputStream oStream = new BufferedOutputStream(new FileOutputStream(saveFile.toPath().toString()))){
            //write current game dimension
            oStream.write(dim);
            for(int i=0; i<this.dim; i++){
                for(int j=0; j<this.dim; j++){
                    //write each index
                    oStream.write(this.board[i][j]);
                }
            }
            oStream.close();
        }catch(FileNotFoundException e){
            view.showError("File Not Found");
        }catch(IOException e){
            view.showError("Failed To Save Game");
        }
    }

    public void loadGame(){
        File gameFile = view.chooseFile("Load Game",baseFileDir);
        setBaseFileDir(gameFile.getParent());
        try{
            DataInputStream dataStream = new DataInputStream(new BufferedInputStream(new FileInputStream(gameFile)));
            //dataStream.readFully(bytes);
            this.dim = dataStream.readByte();
            this.board = new byte[dim][dim];
            int newSolution = 0;
            for(int row=0; row<this.dim; row++){
                for(int col=0; col<this.dim; col++){
                    //write each index
                    newSolution <<= 1;
                    newSolution |= dataStream.readByte()&1;
                    //board[row][col] = dataStream.readByte();
                    //view.setTile(row*dim + col, board[row][col]);
                    //view.setTile(row*dim + col, this.correctColor.toString().substring(2));
                }
            }
            dataStream.close();
            newGame(newSolution);
        }catch(IOException e){
            view.showError("Failed To Load Game");
        }
    }

    public void uploadGame(){
        gameClient.sendGame(dim, solution);
    }

    public void downloadGame(){
        int[] data = gameClient.downloadGame();
        System.out.println(data[0]);
        System.out.println(data[1]);
        setDim(data[0]);
        newGame(data[1]);
    }

    public void log(String message){
        view.addHistory(message);
    }
}
