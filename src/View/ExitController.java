package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class ExitController implements Initializable {
    @Override

    public void initialize(URL url, ResourceBundle resourceBundle) {
        //todo

    }

    public void continueExit(ActionEvent actionEvent) {
        MyViewModel viewModel = MyViewModel.getInstance();
        viewModel.exitGame();
        System.exit(0);
    }

    public void abortExit(ActionEvent actionEvent) {
       //todo
        actionEvent.consume();
    }
}
