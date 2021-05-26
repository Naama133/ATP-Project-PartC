package Model;

import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;

import java.util.Observable;
import java.util.Observer;

public class MyModel extends Observable implements IModel {
    private Maze modelMaze;
    private int rowChar;
    private int colChar;

    public MyModel() {
        this.modelMaze = null;
        this.rowChar = 0;
        this.colChar = 0;
    }

    public Maze getMaze(){
        return modelMaze;
    }

    public int getPlayerRow() {
        return rowChar;
    }

    public int getPlayerCol() {
        return colChar;
    }

    @Override
    public void solveMaze(Maze maze) { //todo
        //Solving Maze
        setChanged();
        notifyObservers();
    }

    @Override
    public void getSolution() { //todo
        //return solution
    }

    public void generateMaze(int rows, int cols){ //create Maze
        IMazeGenerator generator = new MyMazeGenerator();
        Maze maze = null;
        try {
            maze = generator.generate(rows,cols);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.modelMaze  = maze;
        setChanged();
        notifyObservers();

    }

    public void UpdatePlayerPosition(int direction){ //Update Location of the character is the Maze
         /** direction 1 -> UP;
            direction 2 -> DOWN;
            direction 1 -> LEFT;
            direction 1 -> RIGHT;
          **/
        switch (direction) {//todo : need to limit the bound
            case 1 -> rowChar--; //UP
            case 2 -> rowChar++; //DOWN
            case 3 -> colChar--; //LEFT
            case 4 -> colChar++; //RIGHT
        }
        setChanged();
        notifyObservers();
    }

    @Override
    public void assignObserver(Observer O) {
        this.addObserver(O);
    }

}
