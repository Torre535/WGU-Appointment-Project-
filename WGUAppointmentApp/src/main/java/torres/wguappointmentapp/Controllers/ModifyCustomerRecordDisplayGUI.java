package torres.wguappointmentapp.Controllers;

import javafx.collections.FXCollections;
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
import org.w3c.dom.events.MouseEvent;
import torres.wguappointmentapp.DatabaseServer.CustomerDataAccessObject;
import torres.wguappointmentapp.Helpers.JDBC;
import torres.wguappointmentapp.Model.Customer;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ModifyCustomerRecordDisplayGUI implements Initializable {
    @FXML
    private AnchorPane modifyCustomerRecordAnchorPane;
    @FXML
    private Text modifyCustomerRecordTitleText;
    @FXML
    private Label modifyCustomerRecordIdLabel;
    @FXML
    private Label modifyCustomerRecordNameLabel;
    @FXML
    private Label modifyCustomerRecordAddressLabel;
    @FXML
    private Label modifyCustomerRecordPhoneNumberLabel;
    @FXML
    private Label modifyCustomerRecordPostalCodeLabel;
    @FXML
    public TextField modifyCustomerRecordIdAutoGen;
    @FXML
    public TextField modifyCustomerRecordNameTextField;
    @FXML
    public TextField modifyCustomerRecordAddressTextField;
    @FXML
    public TextField modifyCustomerRecordPhoneNumberTextField;
    @FXML
    public TextField modifyCustomerRecordPostalCodeTextField;
    @FXML
    private Label modifyCustomerRecordCountryLabel;
    @FXML
    private Label modifyCustomerRecordStateProvLabel;
    @FXML
    public ComboBox<String> modifyCustomerRecordCountryComboBox;
    @FXML
    public ComboBox <String> modifyCustomerRecordDivisionComboBox;
    @FXML
    private Button modifyCustomerRecordSaveButton;
    @FXML
    private Button modifyCustomerRecordCancelButton;

    Parent scene;
    Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            try {
                // Populate the Country ComboBox
                modifyCustomerRecordCountryComboBox.setItems(CustomerDataAccessObject.getAllCountries());

                // Add an event listener to the Country ComboBox
                modifyCustomerRecordCountryComboBox.setOnAction(this::onClicksCountryComboBox);

                // Add an event listener to the Division ComboBox
                modifyCustomerRecordDivisionComboBox.setOnAction(this::onClicksDivComboBox);



            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        }
    public void onClicksCountryComboBox(ActionEvent actionEvent) {
        String selectedCountry = modifyCustomerRecordCountryComboBox.getValue();
        if (selectedCountry != null) {
            try {
                // Use the selected country to fetch the corresponding divisions
                ObservableList<String> divisions = CustomerDataAccessObject.getFirstLevelDivisions(selectedCountry);
                // Populate the Division ComboBox with the retrieved divisions
                modifyCustomerRecordDivisionComboBox.setItems(divisions);
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        }
    }


    @FXML
   public void onClicksSaveButton(ActionEvent actionEvent) {

        //Debug print statements
       /* System.out.println("Name: " + modifyCustomerRecordNameTextField.getText());
        System.out.println("Address: " + modifyCustomerRecordAddressTextField.getText());
        System.out.println("phone: " + modifyCustomerRecordPhoneNumberTextField.getText());
        System.out.println("Postal: " + modifyCustomerRecordPostalCodeTextField.getText());
        System.out.println("Country: " + modifyCustomerRecordCountryComboBox.getValue());
        System.out.println("Postal: " + modifyCustomerRecordDivisionComboBox.getValue()); */


            String newName = modifyCustomerRecordNameTextField.getText().trim();
            String address = modifyCustomerRecordAddressTextField.getText().trim();
            String phoneNumber = modifyCustomerRecordPhoneNumberTextField.getText().trim();
            String postalCode = modifyCustomerRecordPostalCodeTextField.getText().trim();
            String selectedCountry = modifyCustomerRecordCountryComboBox.getValue();
            String selectedDivisionName = modifyCustomerRecordDivisionComboBox.getValue();

            if (newName.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || postalCode.isEmpty()
                    || selectedCountry == null || selectedDivisionName == null) {
                // Show an error alert for missing information
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Missing Information");
                alert.setContentText("Please fill in all fields.");
                alert.showAndWait();
            } else {
                try {
                    // Get the division ID using the division name
                    int selectedDivisionID = CustomerDataAccessObject.getDivisionID(selectedDivisionName);
                    // Get the customer ID from the text field
                    int customerId = Integer.parseInt(modifyCustomerRecordIdAutoGen.getText());
                    // Update the customer data in the database
                    updateCustomerInDatabase(customerId, selectedDivisionID, newName, address, phoneNumber, postalCode);
                    // Show a success alert
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Data Updated Successfully");
                    alert.setContentText("The data has been updated successfully.");
                    alert.showAndWait();
                    // Close the current window
                    Stage currentStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    currentStage.close();
                } catch (SQLException | NumberFormatException e) {
                    // Handle any database update errors or parsing errors
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Database Error");
                    alert.setContentText("An error occurred while updating the customer data.");
                    alert.showAndWait();
                }
            }
        }


   private void updateCustomerInDatabase(int customerId, int divisionID, String newName, String address, String phoneNumber, String postalCode) throws SQLException {
        String SQL = "UPDATE customers " +
                "SET Customer_Name = ?, Address = ?, Postal_Code = ?, " +
                "Phone = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? " +
                "WHERE Customer_ID = ?";

        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL)) {
            ps.setString(1, modifyCustomerRecordNameTextField.getText());
            ps.setString(2, modifyCustomerRecordAddressTextField.getText());
            ps.setString(3, modifyCustomerRecordPostalCodeTextField.getText());
            ps.setString(4, modifyCustomerRecordPhoneNumberTextField.getText());
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(6, "admin"); // You can replace with the actual user
            ps.setInt(7, divisionID);
            ps.setInt(8, customerId);
            ps.executeUpdate();
        }
    }


    public void onClicksCancelButton(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }


    public void onClicksDivComboBox(ActionEvent actionEvent) {

    }
}
