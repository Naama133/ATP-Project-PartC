package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ExitController {
    public Button cancel_btn;
    public Button yes_Btn;

    public void continueExit(ActionEvent actionEvent) {
        MyViewModel viewModel = MyViewModel.getInstance();
        viewModel.exitGame();
        System.exit(0);
    }


}
