package game;

import java.util.Random;

public class Model {
    private View view;
    private ControllableTimer timer;
    private int dim;
    private char[][] board;
    private boolean mark;
    private int solution;
    private int colHints[][];
    private int rowHints[][];
    private int points;
    private int numErrors;

    public Model(){
        numErrors = 0;
        points = 0;
        dim = 5;
        mark = false;
        board = new char[dim][dim];
        generateSolution();
    }

    public void setView(View view){
        this.view = view;
    }

    public void setTimer(ControllableTimer timer){
        this.timer = timer;
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
        if(mark || rightClick){
            if(board[row][col]==2) board[row][col] = 0;
            else if(board[row][col]==0) board[row][col] = 2;
        }else if(board[row][col]==0){
            if(getSolutionForTile(row,col) != 1){
                board[row][col] = 3;
                points--;
                numErrors++;
            }else{
                board[row][col] = 1;
                points++;
                checkPlayerWin();
            }
            view.setPoints(points);
        }
        view.setTile(row*dim + col, board[row][col]);
    }

    public void toggleMark(){
        this.mark = !mark;
        view.addHistory(mark?"Mark set\n":"Mark unset\n");
    }

    int getColHint(int col, int idx){
        return colHints[col][idx];
    }

    int getRowHint(int row, int idx){
        return rowHints[row][idx];
    }

    private void generateSolution(){
        solution = new Random().nextInt((int) Math.pow(2,(dim*dim - 1)))+2;
        int maxHintsPerCol = (int) Math.ceil(dim/2.0);
        System.out.println(maxHintsPerCol);
        colHints = new int[dim][maxHintsPerCol];
        rowHints = new int[dim][maxHintsPerCol];
        System.out.println(solution);
        for(int i=0; i<dim; i++){
            for(int j=0; j<dim; j++){
               // System.out.print((solution>>((dim)*(dim-i)-1-j))&1);
            }
            //System.out.println("");
        }

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
            System.out.println("");
        }
    }

    /**
     * Generates a new random game
     * @param dim
     */
    public void newGame(){
        points = 0;
        numErrors = 0;
        mark = false;
        board = new char[dim][dim];
        generateSolution();
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
        board = new char[dim][dim];
        timer.setStatus(ControllableTimer.STOP);
        timer.setStatus(ControllableTimer.RESET);
        timer.setStatus(ControllableTimer.START);
        view.resetGame(dim);
        view.setPoints(points);
        view.addHistory("Reset game\n");
    }

    public int getDim(){
        return dim;
    }

    public void setDim(int dim){
        this.dim = dim;
    }

}
