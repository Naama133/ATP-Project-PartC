package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
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
        if (o instanceof IModel){
            if (maze == null){
                this.maze = model.getMaze();

            }
            else {
                Maze modelMaze = model.getMaze();// give me the current model maze
                //Not Generate maze
                if (modelMaze == this.maze) {//if the maze not change is a Move Character.
                    int pRow = model.getPlayerRow();
                    int pCol = model.getPlayerCol();
                    if (this.playerRow == pRow && this.playerCol == pCol) { // Solve Maze
                        model.getSolution();
                    }
                    else{
                        this.playerRow = pRow;
                        this.playerCol = pCol;
                    }
                }
                else
                    this.maze = modelMaze;
            }
            setChanged(); // let know the view we done
            notifyObservers();
        }

    }

    public void generateMaze(int row, int col) {
        model.generateMaze(row, col);
    }

    public void movePlayer(KeyEvent keyEvent){ //we get from the model the user UP/DOWM.. and the Model knows 1 2 3...
        int direction = -1;
        switch (keyEvent.getCode()){
            case UP -> direction = 1;
            case DOWN -> direction = 2;
            case LEFT -> direction = 3;
            case RIGHT -> direction = 4;
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
