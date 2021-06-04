package ViewModel;

import Model.IModel;
import Model.MyModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {
    private static MyViewModel ViewModelInstance;
    private IModel model;
    private Maze maze;
    private int playerRow;
    private int playerCol;
    private Solution solution;

    //viewModel is observer of model
    private MyViewModel() {
        model = MyModel.getInstance();
        model.assignObserver(this);
        maze = null;
        solution = null;
    }

    public static MyViewModel getInstance() {
        if (ViewModelInstance == null){
            ViewModelInstance = new MyViewModel();
        }
        return ViewModelInstance;
    }

    public void deleteSolution() {
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
                deleteSolution();
            }
            else if (action.equals("ModelUpdatePlayerPosition") || action.equals("restartPlayerPosition")) {
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
            //todo:  check answer on forum - add to help section the valid keys (add keys button?)
            case CONTROL -> direction = -2;// ignore
            case NUMPAD8, UP -> direction = 8;
            case NUMPAD2, DOWN -> direction = 2;
            case NUMPAD4, LEFT -> direction = 4;
            case NUMPAD6, RIGHT -> direction = 6;
            //diagonals
            case NUMPAD7 -> direction = 7;
            case NUMPAD9 -> direction = 9;
            case NUMPAD1 -> direction = 1;
            case NUMPAD3 -> direction = 3;
            default -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Invalid Key, press only 1,2,3,4,6,7,8,9 to move");
                alert.show();
            }

        }
        if (direction > 0)
            model.UpdatePlayerPosition(direction);
    }

    public void restartPlayer(){
        model.UpdatePlayerPosition(0);
    }

    public void solveMaze(){
        model.solveMaze();
    }

    public Solution getSolution (){
        return solution;
    }

    public void exitGame(){model.shutDownServers();}
}
