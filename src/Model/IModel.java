package Model;

import algorithms.mazeGenerators.Maze;

import java.util.Observer;

public interface IModel {
    public void generateMaze(int rows, int cols);//create maze

    public void UpdatePlayerPosition(int direction); //update location char
    public void assignObserver(Observer O);

    public Maze getMaze();
    public int getPlayerRow();
    public int getPlayerCol();

    public void solveMaze(Maze maze);
    public void getSolution();

}
