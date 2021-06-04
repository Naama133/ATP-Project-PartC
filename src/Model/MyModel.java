package Model;

import Client.Client;
import IO.MyDecompressorInputStream;
import Server.Server;
import algorithms.mazeGenerators.Maze;
import algorithms.search.*;
import javafx.scene.control.Alert;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import Client.IClientStrategy;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;
import Server.Configurations;

public class MyModel extends Observable implements IModel {
    private static MyModel ModelInstance;
    private Maze modelMaze;
    private int PlayerRow;
    private int PlayerCol;
    private Solution modelMazeSolution;
    private Server GenerateMazeServer;
    private Server SolveMazeServer;

    private MyModel() {
        modelMaze = null;
        PlayerRow = 0;
        PlayerCol = 0;
        modelMazeSolution = null;
        GenerateMazeServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        SolveMazeServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        GenerateMazeServer.start();
        SolveMazeServer.start();
        Configurations.setThreadPoolSize("3"); //todo: by user chose
        Configurations.setGenerator("MyMazeGenerator");
        Configurations.setSearchingAlgorithm("BestFirstSearch");
    }

    public void deleteSolution() {
        modelMazeSolution = null;
    }

    public static MyModel getInstance(){
        if(ModelInstance == null)
            ModelInstance = new MyModel();
        return ModelInstance;
    }

    public Maze getMaze(){
        return modelMaze;
    }

    public int getPlayerRow() {
        return PlayerRow;
    }

    public int getPlayerCol() {
        return PlayerCol;
    }

    @Override
    public void solveMaze() {
        //Solving Maze
        try{
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inputStream, OutputStream outputStream) {
                    try{
                        ObjectOutputStream toServer = new ObjectOutputStream(outputStream);
                        ObjectInputStream fromServer = new ObjectInputStream(inputStream);
                        toServer.flush();
                        toServer.writeObject(modelMaze); //send maze to server
                        toServer.flush();
                        modelMazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        setChanged(); //todo: add catch for no solution exception
        notifyObservers("ModelSolvedMaze");
    }

    @Override
    public Solution getSolution() {
        return modelMazeSolution;
    }


    public void generateMaze(int rows, int cols){ //create Maze
        String action = "ModelGenerateMaze"; //todo - add this check to the view!
        if (rows < 2 || cols < 2 || rows >1000 || cols > 1000)
            action = "InvalidMazeSize";
        else{
            try{
                Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                    public void clientStrategy(InputStream inputStream, OutputStream outputStream){
                        try{
                            ObjectOutputStream toServer = new ObjectOutputStream(outputStream);
                            ObjectInputStream fromServer = new ObjectInputStream(inputStream);
                            toServer.flush();
                            int[] mazeDimensions = new int[]{rows, cols};
                            toServer.writeObject(mazeDimensions);
                            toServer.flush();
                            byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                            InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                            byte[] decompressedMaze = new byte[mazeDimensions[0]*mazeDimensions[1]+12]; //allocating byte[] for the decompressed maze -
                            is.read(decompressedMaze); //Fill decompressedMaze with bytes
                            modelMaze = new Maze(decompressedMaze);
                            PlayerRow =modelMaze.getStartPosition().getRowIndex();
                            PlayerCol = modelMaze.getStartPosition().getColumnIndex();
                        }
                        catch(ClassNotFoundException | IOException e){
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                client.communicateWithServer();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        deleteSolution();
        setChanged();
        notifyObservers(action);
    }


    public void UpdatePlayerPosition(int direction){ //Update Location of the character is the Maze
        String ActionMessage = "ModelUpdatePlayerPosition";
        switch (direction) {
            case 0:
                PlayerRow =modelMaze.getStartPosition().getRowIndex();
                PlayerCol = modelMaze.getStartPosition().getColumnIndex();
                ActionMessage = "restartPlayerPosition";
                break;
            case 8:
                if(PlayerRow>0)
                    if(modelMaze.getMazeContent()[PlayerRow-1][PlayerCol]==0)
                        PlayerRow--; //UP
                    else{ActionMessage = "Wall";}
                else{ActionMessage = "BoundariesProblem";}
                break;
            case 2:
                if(PlayerRow<modelMaze.getRows()-1)
                    if(modelMaze.getMazeContent()[PlayerRow+1][PlayerCol]==0)
                        PlayerRow++; //DOWN
                    else{ActionMessage = "Wall";}
                else{ActionMessage = "BoundariesProblem";}
                break;
            case 4:
                if(PlayerCol>0)
                    if(modelMaze.getMazeContent()[PlayerRow][PlayerCol-1]==0)
                        PlayerCol--; //LEFT
                    else{ActionMessage = "Wall";}
                else{ActionMessage = "BoundariesProblem";}
                break;
            case 6:
                if(PlayerCol<modelMaze.getColumns()-1)
                    if(modelMaze.getMazeContent()[PlayerRow][PlayerCol+1]==0)
                        PlayerCol++; //RIGHT
                    else{ActionMessage = "Wall";}
                else{ActionMessage = "BoundariesProblem";}
                break;
            case 1:
                if((PlayerRow<modelMaze.getRows()-1)&&(PlayerCol>0)){
                    if(modelMaze.getMazeContent()[PlayerRow+1][PlayerCol-1]==0){
                        PlayerRow++; //DOWN
                        PlayerCol--; //LEFT
                    }
                    else{ActionMessage = "Wall";}
                }
                else{ActionMessage = "BoundariesProblem";}
                break;
            case 3:
                if((PlayerRow<modelMaze.getRows()-1)&&(PlayerCol<modelMaze.getColumns()-1)){
                    if(modelMaze.getMazeContent()[PlayerRow+1][PlayerCol+1]==0){
                        PlayerRow++; //DOWN
                        PlayerCol++; //RIGHT
                    }
                    else{ActionMessage = "Wall";}
                }
                else{ActionMessage = "BoundariesProblem";}
                break;
            case 7:
                if((PlayerRow>0)&&(PlayerCol>0)){
                    if(modelMaze.getMazeContent()[PlayerRow-1][PlayerCol-1]==0){
                        PlayerRow--; //UP
                        PlayerCol--; //LEFT
                    }
                    else{ActionMessage = "Wall";}
                }
                else{ActionMessage = "BoundariesProblem";}
                break;
            case 9:
                if((PlayerRow>0)&&(PlayerCol<modelMaze.getColumns()-1)){
                    if(modelMaze.getMazeContent()[PlayerRow-1][PlayerCol+1]==0){
                        PlayerRow--; //UP
                        PlayerCol++; //RIGHT
                    }
                    else{ActionMessage = "Wall";}
                }
                else{ActionMessage = "BoundariesProblem";}
                break;
        }
        String currPlayerPosition = "{" + PlayerRow + "," + PlayerCol + "}";
        if(currPlayerPosition.equals(modelMaze.getGoalPosition().toString())) //todo: solve message again and again
            ActionMessage = "UserSolvedTheMaze";
        setChanged();
        notifyObservers(ActionMessage);
    }


    //todo : checkBox to choose the theme of the game (and music?)

    @Override
    public void assignObserver(Observer O) {
        this.addObserver(O);
    }

    public void shutDownServers(){
        //todo dar - to set new servers - 25:00 in recording of rotem
        GenerateMazeServer.stop();
        SolveMazeServer.stop();
    }

}
