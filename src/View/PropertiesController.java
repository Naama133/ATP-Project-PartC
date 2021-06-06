package View;

import Server.Configurations;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.search.ISearchingAlgorithm;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class PropertiesController implements Initializable {
    public ChoiceBox generatorChoiceBox;
    public ChoiceBox AlgorithmChoiceBox;
    public Spinner<Integer> spinner;
    public int TreadsNumber;
    public Button OK_btn;
    SpinnerValueFactory<Integer> spinnerValueFactory;
    private MyViewModel viewModel = MyViewModel.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generatorChoiceBox.getItems().addAll("EmptyMazeGenerator", "SimpleMazeGenerator", "MyMazeGenerator");
        AlgorithmChoiceBox.getItems().addAll("DepthFirstSearch", "BreadthFirstSearch", "BestFirstSearch");

        IMazeGenerator generator = Configurations.getGeneratingAlgorithm();
        String genStr = catString(generator.getClass().toString());
        generatorChoiceBox.setValue(genStr);

        ISearchingAlgorithm searcher = Configurations.getSearchingAlgorithm();
        String serStr = catString(searcher.getClass().toString());
        AlgorithmChoiceBox.setValue(serStr);

        TreadsNumber = Configurations.getThreadsNumber();
        spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,100,TreadsNumber);
        spinner.setValueFactory(spinnerValueFactory);
    }

    private String catString(String str){
        int i = str.indexOf(".");
        str = str.substring(i+1);
        i = str.indexOf(".");
        return str.substring(i+1);
    }

    public void changeConfiguration(ActionEvent actionEvent) {
        String newGenerator = (String) generatorChoiceBox.getValue();
        String newAlgorithm = (String) AlgorithmChoiceBox.getValue();
        Integer newThreadsNumber = spinner.getValue();
        String serStr = catString(Configurations.getSearchingAlgorithm().getClass().toString());

        if(newThreadsNumber != Configurations.getThreadsNumber()){
            viewModel.exitGame();
            Configurations.setThreadPoolSize(String.valueOf(newThreadsNumber));
            viewModel.initGameServers();
        }
        String genStr = catString(Configurations.getGeneratingAlgorithm().getClass().toString());
        if(!newGenerator.equals(genStr)){
            Configurations.setGenerator(newGenerator);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("If you want to create a new maze problem, by using the new\ngenerator algorithm you choose, press again: \"Generate Maze\"");
            alert.show();
        }
        else if(!newAlgorithm.equals(serStr) ){
            Configurations.setSearchingAlgorithm(newAlgorithm);
        }
        actionEvent.consume();
    }


}
