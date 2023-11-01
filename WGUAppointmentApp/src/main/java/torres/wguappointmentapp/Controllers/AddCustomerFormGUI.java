package torres.wguappointmentapp.Controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import torres.wguappointmentapp.DatabaseServer.CustomerDataAccessObject;
import torres.wguappointmentapp.Helpers.JDBC;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class AddCustomerFormGUI implements Initializable {
    @FXML
    private AnchorPane addCustomerRecordAnchorPane;
    @FXML
    private Text addCustomerRecordTitleText;
    @FXML
    private Label addCustomerRecordCustomerIdLabel;
    @FXML
    private Label addCustomerRecordNameLabel;
    @FXML
    private Label addCustomerRecordAddress;
    @FXML
    private Label addCustomerRecordPhoneLabel;
    @FXML
    private Label addCustomerRecordPostalCodeLabel;
    @FXML
    private TextField addCustomerRecordIdAutoGenTextField;
    @FXML
    private TextField addCustomerRecordNameTextField;
    @FXML
    private TextField addCustomerRecordAddressTextField;
    @FXML
    private TextField addCustomerRecordPhoneTextField;
    @FXML
    private TextField addCustomerRecordPostalCodeTextField;
    @FXML
    private Label addCustomerRecordCountryLabel;
    @FXML
    private Label addCustomerRecordStateProvLabel;
    @FXML
    private ComboBox<String> addCustomerRecordCountryComboBox;
    @FXML
    private ComboBox<String> addCustomerRecordDivisionComboBox;
    @FXML
    private Button addCustomerRecordSaveButton;
    @FXML
    private Button addCustomerRecordCancelButton;

    Stage stage;
    Parent scene;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            addCustomerRecordCountryComboBox.setItems(CustomerDataAccessObject.getAllCountries());

            // Add an event listener to the Country ComboBox
            addCustomerRecordCountryComboBox.setOnAction(this::onClicksCountryComboBox);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void onClicksSaveButton(ActionEvent actionEvent) {
        // Check if any text fields are empty
        if (addCustomerRecordNameTextField.getText().isEmpty() ||
                addCustomerRecordAddressTextField.getText().isEmpty() ||
                addCustomerRecordPhoneTextField.getText().isEmpty() ||
                addCustomerRecordPostalCodeTextField.getText().isEmpty() ||
                addCustomerRecordCountryComboBox.getSelectionModel().isEmpty() ||
                addCustomerRecordDivisionComboBox.getSelectionModel().isEmpty()) {
            // Show an error alert for missing information
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Missing Information");
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
        } else {
            try {
                // Get the selected division name
                String selectedDivisionName = addCustomerRecordDivisionComboBox.getValue();

                // Get the division ID using the division name
                int selectedDivisionID = CustomerDataAccessObject.getDivisionID(selectedDivisionName);

                // Insert the new customer data into the database
                insertCustomerToDatabase(selectedDivisionID);

                // Show a success alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Data Added Successfully");
                alert.setContentText("The data has been added successfully.");
                alert.showAndWait();

                // Close the current window
                Stage currentStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                currentStage.close();
            } catch (SQLException e) {
                // Handle any database insertion errors
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Database Error");
                alert.setContentText("An error occurred while inserting data into the database.");
                alert.showAndWait();
            }
        }
    }


    private void insertCustomerToDatabase(int divisionID) throws SQLException {


        String SQL = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL)) {
            ps.setInt(1, CustomerDataAccessObject.getNewCustomerID());
            ps.setString(2, addCustomerRecordNameTextField.getText());
            ps.setString(3, addCustomerRecordAddressTextField.getText());
            ps.setString(4, addCustomerRecordPostalCodeTextField.getText());
            ps.setString(5, addCustomerRecordPhoneTextField.getText());
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(7, "admin");
            ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(9, "admin");
            ps.setInt(10, divisionID);
            ps.execute();
        }
    }


    public void onClicksCancelButton(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }


    public void onClicksCountryComboBox(ActionEvent actionEvent) {
        String selectedCountry = addCustomerRecordCountryComboBox.getSelectionModel().getSelectedItem();

        try {
            // Use the selected country to fetch the corresponding divisions
            ObservableList<String> divisions = CustomerDataAccessObject.getFirstLevelDivisions(selectedCountry);

            // Populate the Division ComboBox with the retrieved divisions
            addCustomerRecordDivisionComboBox.setItems(divisions);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }


    public void onClicksDivComboBox(ActionEvent actionEvent) {
    }
}
