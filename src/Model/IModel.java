package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import java.util.Observer;

public interface IModel {
    public void generateMaze(int rows, int cols);//create maze
    public void UpdatePlayerPosition(int direction); //update location char
    public void assignObserver(Observer O);
    public Maze getMaze();
    public int getPlayerRow();
    public int getPlayerCol();
    public void solveMaze();
    public Solution getSolution();
    public void deleteSolution();
    public void shutDownServers();
    public void initServers();
    public void setLoadedMaze(Maze loadedMaze);
}
