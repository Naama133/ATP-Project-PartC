package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {
    private IModel model;
    private Maze maze;
    private int playerRow;
    private int playerCol;

    //viewModel is observer of model
    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this);
        this.maze = null;

    }
    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }

    public Maze getMaze() {
        return maze;
    }

    @Override
    public void update(Observable o, Object arg) { //create maze or move character
        String action = arg.toString();
        if (o instanceof IModel) {
            if (action.equals("ModelGenerateMaze")) {
                this.maze = model.getMaze();
                this.playerRow = model.getPlayerRow();
                this.playerCol = model.getPlayerCol();
            }
            else if (action.equals("ModelUpdatePlayerPosition")) {
                this.playerRow = model.getPlayerRow();
                this.playerCol = model.getPlayerCol();
            }
            else if (action.equals("ModelSolvedMaze")) {
                model.getSolution();}
            else if(action.equals("BoundariesProblem") || action.equals("Wall")){
                //do nothing, pass the same message to the View
            }
            else if (action.equals("UserSolvedTheMaze")) {
                this.playerRow = model.getPlayerRow();
                this.playerCol = model.getPlayerCol();
            }
        }
        setChanged(); // let know the view we done
        notifyObservers(action);
    }

    public void generateMaze(int row, int col) {
        model.generateMaze(row, col);
    }

    public void movePlayer(KeyEvent keyEvent){ //we get from the model the user UP/DOWM.. and the Model knows 1 2 3...
        int direction = -1;
        switch (keyEvent.getCode()){
            //up, down, left, right
            case DIGIT8 -> direction = 8;
            case DIGIT2 -> direction = 2;
            case DIGIT4 -> direction = 4;
            case DIGIT6 -> direction = 6;
            //diagonals
            case DIGIT7 -> direction = 7;
            case DIGIT9 -> direction = 9;
            case DIGIT1 -> direction = 1;
            case DIGIT3 -> direction = 3;
        }
        model.UpdatePlayerPosition(direction);

    }

    public void solveMaze(Maze maze){
        model.solveMaze(maze);
    }

    public void getSolution (){
        model.getSolution();
    }


}
