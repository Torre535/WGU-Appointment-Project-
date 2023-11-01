package torres.wguappointmentapp.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainReportsFormDisplayGUI implements Initializable {
    public AnchorPane mainReportsAnchorPane;
    public Text mainReportsTitleText;
    public ComboBox mainReportsSelectCustomerComboBox;
    public Text mainReportsSelectCustomerTitleText;
    public Button mainReportsBackToMainButton;
    public Button mainReportsLogoutButton;
    public TableView mainReportsContactAppTableView;
    public TableColumn mainReportsAppIDColumn;
    public TableColumn mainReportsTitleColumn;
    public TableColumn mainReportsDescriptionColumn;
    public TableColumn mainReportsLocationColumn;
    public TableColumn mainReportsContactIDColumn;
    public TableColumn mainReportsTypeColumn;
    public TableColumn mainReportsStartColumn;
    public TableColumn mainReportsEndColumn;
    public TableColumn mainReportsCustomerIDColumn;
    public TableColumn mainReportsUserIDColumn;
    public TableView mainReportsTotalByTypeAndMonthTableView;
    public TableColumn mainReportsByTypeAndMonthDateColumn;
    public TableColumn mainReportsByTypeAndMonthTypeColumn;
    public TableColumn mainReportsByTypeAndMonthTotalColumn;
    public TableView mainReportsTotalCustomerPastAppTableView;
    public TableColumn mainReportsTotalPastCustomerNameColumn;
    public TableColumn mainReportsTotalPastCustomerIDColumn;
    public TableColumn mainReportsTotalPastNumberOfAppColumn;
    public Text mainReportsTotalAppTitleText;
    public Text mainReportsTotalTypeAppTitleText;

    Parent scene;
    Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



    }

    public void onClickSelectCustomerComboBox(ActionEvent actionEvent) {
    }

    public void onClicksBackToMainButton(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/torres/wguappointmentapp/MainMenuDisplayGUI.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onClicksLogoutButton(ActionEvent actionEvent) {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
