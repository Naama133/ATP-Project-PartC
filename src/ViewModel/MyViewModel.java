package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyEvent;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {
    private IModel model;
    private Maze maze;
    private int playerRow;
    private int playerCol;
    private Solution solution;



    //viewModel is observer of model
    public MyViewModel(IModel Model) {
        model = Model;
        model.assignObserver(this);
        maze = null;
        solution = null;

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
                solution = model.getSolution();
            }
            else if(action.equals("BoundariesProblem") || action.equals("Wall") || action.equals("InvalidMazeSize")){
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
            //todo:  check answer on forum
            case NUMPAD8, DIGIT8 -> direction = 8;
            case DIGIT2, NUMPAD2 -> direction = 2;
            case DIGIT4, NUMPAD4 -> direction = 4;
            case DIGIT6, NUMPAD6 -> direction = 6;
            //diagonals
            case DIGIT7, NUMPAD7 -> direction = 7;
            case DIGIT9, NUMPAD9 -> direction = 9;
            case DIGIT1, NUMPAD1 -> direction = 1;
            case DIGIT3, NUMPAD3 -> direction = 3;
        }
        model.UpdatePlayerPosition(direction);

    }

    public void solveMaze(){
        model.solveMaze();
    }

    public Solution getSolution (){
        return solution;
    }


}
