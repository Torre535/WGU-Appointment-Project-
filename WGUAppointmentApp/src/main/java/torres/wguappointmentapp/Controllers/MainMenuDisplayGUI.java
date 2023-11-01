package torres.wguappointmentapp.Controllers;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import torres.wguappointmentapp.DatabaseServer.AppointmentDataAccessObject;
import torres.wguappointmentapp.DatabaseServer.CustomerDataAccessObject;
import torres.wguappointmentapp.Model.Appointments;
import torres.wguappointmentapp.Model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

import static torres.wguappointmentapp.DatabaseServer.AppointmentDataAccessObject.deleteAppointmentFromTableView;

public class MainMenuDisplayGUI implements Initializable {
    @FXML
    private TableColumn mainMenuCustomerCountryColumn;
    //Appointments Table
    @FXML
    private AnchorPane mainMenuLoginPane;
    @FXML
    private Text mainAppTitleText;
    @FXML
    private Text mainMenuCustomerTitle;
    @FXML
    private TableView<Appointments> mainMenuAppTableView;
    @FXML
    private TableColumn<Appointments, Integer> mainMenuAppIdColumn;
    @FXML
    private TableColumn<Appointments, String> mainMenuAppTitleColumn;
    @FXML
    private TableColumn<Appointments, String> mainMenuAppDescriptionColumn;
    @FXML
    private TableColumn<Appointments, String> mainMenuAppLocationColumn;
    @FXML
    private TableColumn<Appointments, Integer> mainMenuAppContactIdColumn;
    @FXML
    private TableColumn<Appointments, String> mainMenuAppTypeColumn;
    @FXML
    private TableColumn<Appointments, String> mainMenuAppStartColumn;
    @FXML
    private TableColumn<Appointments, String> mainMenuAppEndColumn;
    @FXML
    private TableColumn<Appointments, Integer> mainMenuAppCustomerIdColumn;
    @FXML
    private TableColumn<Appointments, Integer> mainMenuAppUserIdColumn;

    // Customers Record Table
    @FXML
    private TableView<Customer> mainMenuCustomerTableView;
    @FXML
    private TableColumn<Customer, Integer> mainMenuCustomerIDColumn;
    @FXML
    private TableColumn<Customer, String> mainMenuCustomerFullNameColumn;
    @FXML
    private TableColumn<Customer, String> mainMenuCustomerAddressColumn;
    @FXML
    private TableColumn<Customer, String> mainMenuCustomerStateProvidenceColumn;
    @FXML
    private TableColumn<Customer, Integer> mainMenuCustomerPostalCodeColumn;
    @FXML
    private TableColumn<Customer, String> mainMenuCustomerPhoneNumberColumn;
    @FXML
    private TableColumn<Customer, String> mainMenuCustomerDivisionColumn;

    // Appointments Table Buttons for ADD, MOD, DELETE
    @FXML
    private Button mainMenuAppAddButton;
    @FXML
    private Button mainMenuModifyAppButton;
    @FXML
    private Button mainMenuDeleteAppButton;
    @FXML

    // Appointments Table Radio Buttons for Filtering
    private RadioButton allAppButton;
    @FXML
    private RadioButton allCurrentWeekAppButton;
    @FXML
    private RadioButton allCurrentMonthAppButton;

    private ToggleGroup AppointmentFilterToggleGroup;

    //General EXIT and Reports Button
    @FXML
    private Button mainMenuExitButton;
    @FXML
    private Button mainMenuReportsButton;
    @FXML

    //Customers Record Table ADD,MOD,DELETE buttons
    private Button mainMenuCustomerAddButton;
    @FXML
    private Button mainMenuCustomerModifyButton;
    @FXML
    private Button mainMenuCustomerDeleteButton;

    Stage stage;
    Parent scene;

    private ZoneId userTimeZone;

    //Loading the Appointments and Customer Records from database
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        userTimeZone = ZoneId.systemDefault();

        AppointmentFilterToggleGroup = new ToggleGroup();
        allAppButton.setToggleGroup(AppointmentFilterToggleGroup);
        allCurrentWeekAppButton.setToggleGroup(AppointmentFilterToggleGroup);
        allCurrentMonthAppButton.setToggleGroup(AppointmentFilterToggleGroup);

        try {

            // Initializing the App Table with data from database
            ObservableList<Appointments> appointments = AppointmentDataAccessObject.getTotalAppointments();


            mainMenuAppTableView.setItems(appointments);

            mainMenuAppIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
            mainMenuAppTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            mainMenuAppDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            mainMenuAppLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
            mainMenuAppContactIdColumn.setCellValueFactory(new PropertyValueFactory<>("contactId"));
            mainMenuAppTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            mainMenuAppStartColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
            mainMenuAppEndColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
            mainMenuAppCustomerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            mainMenuAppUserIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));




            //Initializing Customer Record table with data from the database
            ObservableList<Customer> customers = CustomerDataAccessObject.getAllCustomers();
            mainMenuCustomerTableView.setItems(customers);

            mainMenuCustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            mainMenuCustomerFullNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            mainMenuCustomerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
            mainMenuCustomerCountryColumn.setCellValueFactory(new PropertyValueFactory<>("customerCountry"));
            mainMenuCustomerPostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            mainMenuCustomerPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
            mainMenuCustomerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("divisionId"));





        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }


    // Add a method to convert division ID to name
    private String convertIdToName(Integer divisionId) {
        // Implement the logic to convert division ID to name here
        // You may need to retrieve the name from your data source or wherever it's stored
        return "Division Name"; // Replace with your actual implementation
    }


    // Appointments table
    public void onActionMainMenuAddAppButton(ActionEvent actionEvent) {
        try {
            // Load the FXML file for the appointment form
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/torres/wguappointmentapp/AddCustomerAppointmentDisplayGUI.fxml"));
            Parent addNewAppointmentFormGUI = loader.load();

            // Create a new stage for the appointment form
            Stage addAppointmentStageNew = new Stage();
            addAppointmentStageNew.setTitle("Add New Appointment Form");
            addAppointmentStageNew.setScene(new Scene(addNewAppointmentFormGUI));

            // Show the appointment form as a modal dialog (blocks interaction with the main window)
            addAppointmentStageNew.initModality(Modality.APPLICATION_MODAL);
            addAppointmentStageNew.showAndWait();

            // After the form is closed, you can refresh the main table to display the newly added appointment.
            refreshAppointmentsTableAfterUpdatedData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Add a method to refresh the appointments table after a new appointment is added.
    private void refreshAppointmentsTableAfterUpdatedData() {
        try {
            ObservableList<Appointments> appointments = AppointmentDataAccessObject.getTotalAppointments();
            mainMenuAppTableView.setItems(appointments);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onActionMainMenuModifyButton(ActionEvent actionEvent)throws SQLException {
        try {
            // Check if an appointment is selected in the table
            Appointments selectedAppointment = mainMenuAppTableView.getSelectionModel().getSelectedItem();
            if (selectedAppointment == null) {
                // No appointment selected, show an alert
                showInformationAlert("No Appointment Selected", "Please select an appointment to modify.");
                return;
            }

            // Load the FXML file for modifying the appointment record
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/torres/wguappointmentapp/ModifyCustomerAppointmentDisplayGUI.fxml"));
            Parent modifyAppointmentFormGUI = loader.load();

            // Access the controller for the ModifyCustomerAppointmentDisplayGUI
            ModifyCustomerAppointmentDisplayGUI modifyAppointmentController = loader.getController();

            // Set the appointment information in the ModifyCustomerAppointmentDisplayGUI

            modifyAppointmentController.modifyAppIdAutoGenTextField.setText(String.valueOf(selectedAppointment.getAppointmentId()));
            modifyAppointmentController.modifyAppTitleTextField.setText(selectedAppointment.getTitle());
            modifyAppointmentController.modifyAppDescriptionTextField.setText(selectedAppointment.getDescription());
            modifyAppointmentController.modifyAppLocationTextField.setText(selectedAppointment.getLocation());
            modifyAppointmentController.modifyAppContactIdComboBox.setValue(AppointmentDataAccessObject.getContactName(selectedAppointment.getContactId()));
            modifyAppointmentController.modifyAppTypeTextField.setText(selectedAppointment.getType());
            modifyAppointmentController.modifyAppCustomersIdComboBox.setValue(CustomerDataAccessObject.getAllCustomers().get(selectedAppointment.getCustomerId()));
            modifyAppointmentController.modifyAppUserIdComboBox.setValue(AppointmentDataAccessObject.getAllUsersId().get(selectedAppointment.getUserId()));


            // Previous methods that gave time in yyyy-MM-dd HH:mm:ss when user clicked modify.
          //  modifyAppointmentController.modifyAppStartTimeAndDateTextField.setText(String.valueOf(selectedAppointment.getStart()));
            // modifyAppointmentController.modifyAppEndTimeAndDateTextField.setText(String.valueOf(selectedAppointment.getEnd()));

            modifyAppointmentController.modifyAppStartTimeAndDateTextField.setText(convertDateTimeForModDisplay(String.valueOf(selectedAppointment.getStart())));
            modifyAppointmentController.modifyAppEndTimeAndDateTextField.setText(convertDateTimeForModDisplay(String.valueOf(selectedAppointment.getEnd())));



            // Create a new stage for the appointment form
            Stage modifyAppointmentStage = new Stage();
            modifyAppointmentStage.setTitle("Modify Appointment");
            modifyAppointmentStage.setScene(new Scene(modifyAppointmentFormGUI));

            // Show the appointment form as a modal dialog (blocks interaction with the main window)
            modifyAppointmentStage.initModality(Modality.APPLICATION_MODAL);
            modifyAppointmentStage.showAndWait();

            // After the form is closed, you can refresh the main table to display the updated appointment information.
            refreshAppointmentsTableAfterModifiedInput2();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshAppointmentsTableAfterModifiedInput2() {
        try {
            ObservableList<Appointments> appointments = AppointmentDataAccessObject.getTotalAppointments();
            mainMenuAppTableView.setItems(appointments);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void onActionMainMenuDeleteAppButton(ActionEvent actionEvent) {
        // Check if an appointment is selected in the table
        Appointments selectedAppointment = mainMenuAppTableView.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            // No appointment selected, show an error alert
            showInformationAlert("No Appointment Selected", "Please select an appointment to delete.");
            return;
        }

        // Confirm deletion with a confirmation dialog
        showConfirmationAlertToUser("Delete Appointment Confirmation",
                "Are you sure you want to delete the selected appointment?",
                "Click 'OK' to confirm deletion or 'Cancel' to go back.",
                () -> {
                    // User confirmed deletion, proceed with deletion
                    try {
                        deleteAppointmentFromTableView(selectedAppointment.getAppointmentId());

                        // Show a success message
                        showInformationAlert("Appointment Deleted",
                                "The selected appointment has been successfully deleted.");

                        // Refresh the appointments table to reflect the deletion
                        refreshAppointmentsTableAfterDeletedInputByUser();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        // Handle any potential database errors here
                    }
                });

    }

    private void refreshAppointmentsTableAfterDeletedInputByUser() {
        try {
            ObservableList<Appointments> appointments = AppointmentDataAccessObject.getTotalAppointments();
            mainMenuAppTableView.setItems(appointments);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential database errors here
        }
    }


    // These are Radio Buttons for filtering data
    public void onActionMainMenuAllAppButton(ActionEvent actionEvent) throws SQLException {
        mainMenuAppTableView.setItems(AppointmentDataAccessObject.getTotalAppointments());
    }

    public void onActionMainMenuWeekAppButton(ActionEvent actionEvent) throws SQLException {
        mainMenuAppTableView.setItems(AppointmentDataAccessObject.getAllAppointmentsByWeek());
    }

    public void onActionMainMenuMonthsAppButton(ActionEvent actionEvent) throws SQLException {
        mainMenuAppTableView.setItems(AppointmentDataAccessObject.getAllAppointmentsByMonth());
    }


    //General Buttons for Page (EXIT and REPORTS)
    public void onActionMainMenuExitButton(ActionEvent actionEvent) {
        showConfirmationAlertToUser("EXIT APP CONFIRMATION", "Are you postive you want to close applicaiton?",
                "Click 'OK' to exit or 'Cancel' to return", () -> Platform.exit());
    }

    public void onActionMainMenuReportsButton(ActionEvent actionEvent) {
        try {
            switchToNewScene("/torres/wguappointmentapp/MainReportsDisplayGUI.fxml"); //
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Helpers for Scene to completely switch to new scene from current scene.
    private void switchToNewScene(String fxmlPath) throws IOException {
        stage = (Stage) mainMenuAppTableView.getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(fxmlPath));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    // This method uses FXML as a file path parameter. For an already loaded FXML file
    private void switchToSceneLoader(FXMLLoader loader) {
        stage = (Stage) mainMenuAppTableView.getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.showAndWait();
    }

    private FXMLLoader loadFXML(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        loader.load();
        return loader;
    }

    // This is an alert method to warn uses about an action they are taking and to confirm that action.
    public static void showConfirmationAlertToUser(String title, String headerText, String contentText, Runnable
            onConfirm) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (onConfirm != null) {
                onConfirm.run();
            }
        }
    }

    // Customer Reports Methods (ADD, DELETE, MOD)

    public void onActionMainMenuCustomerAddButton(ActionEvent actionEvent) {
        try {
            // Load the FXML file for the customer form
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/torres/wguappointmentapp/AddCustomerRecordDisplayGUI.fxml"));
            Parent addCustomerFormGUI = loader.load();

            // Create a new stage for the customer form
            Stage addCustomerNewStage = new Stage();
            addCustomerNewStage.setTitle("Add New Customer");
            addCustomerNewStage.setScene(new Scene(addCustomerFormGUI));

            // Show the customer form as a modal dialog (blocks interaction with the main window)
            addCustomerNewStage.initModality(Modality.APPLICATION_MODAL);
            addCustomerNewStage.showAndWait();

            // After the form is closed, you can refresh the main table to display the newly added customer.
            refreshCustomersTableAfterNewInput();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Refresh is used so when the table is updated it will show the new data
    private void refreshCustomersTableAfterNewInput() {
        try {
            ObservableList<Customer> customers = CustomerDataAccessObject.getAllCustomers();
            mainMenuCustomerTableView.setItems(customers);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onActionMainMenuModifyCustomerButton(ActionEvent actionEvent) {
        try {
            // Check if a customer is selected in the table
            Customer selectedCustomer = mainMenuCustomerTableView.getSelectionModel().getSelectedItem();
            if (selectedCustomer == null) {
                // No customer selected, show an alert
                showInformationAlert("No Customer Selected", "Please select a customer to modify.");
                return;
            }

            // Load the FXML file for modifying the customer record
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/torres/wguappointmentapp/ModifyCustomerRecordDisplayGUI.fxml"));
            Parent modifyCustomerFormGUI = loader.load();

            // Access the controller for the ModifyCustomerRecordDisplayGUI
            ModifyCustomerRecordDisplayGUI modifyCustomerController = loader.getController();

            // Set the customer information in the ModifyCustomerRecordDisplayGUI
            modifyCustomerController.modifyCustomerRecordIdAutoGen.setText(String.valueOf(selectedCustomer.getCustomerId()));
            modifyCustomerController.modifyCustomerRecordNameTextField.setText(selectedCustomer.getCustomerName());
            modifyCustomerController.modifyCustomerRecordAddressTextField.setText(selectedCustomer.getAddress());
            modifyCustomerController.modifyCustomerRecordPhoneNumberTextField.setText(selectedCustomer.getPhone());
            modifyCustomerController.modifyCustomerRecordPostalCodeTextField.setText(selectedCustomer.getPostalCode());
            //modifyCustomerController.modifyCustomerRecordCountryComboBox.setValue(selectedCustomer.getCustomerCountry());
            modifyCustomerController.modifyCustomerRecordDivisionComboBox.setValue(String.valueOf(selectedCustomer.getDivisionId()));

            // This was used to make sure that the State/Providence showed up and not the actual ID number for them
            String divisionName = CustomerDataAccessObject.getFirstLevelDivisionName2(selectedCustomer.getDivisionId());
            modifyCustomerController.modifyCustomerRecordDivisionComboBox.setValue(divisionName);

            // Was having issue where the Divisions Info was not populating with the County already populated before hand 
            String selectedCountry = selectedCustomer.getCustomerCountry();
            if (selectedCountry != null) {
                modifyCustomerController.modifyCustomerRecordCountryComboBox.setValue(selectedCountry);
                modifyCustomerController.onClicksCountryComboBox(new ActionEvent(modifyCustomerController.modifyCustomerRecordCountryComboBox, null));
            }

            // Create a new stage for the customer form
            Stage modifyCustomerStage = new Stage();
            modifyCustomerStage.setTitle("Modify Customer");
            modifyCustomerStage.setScene(new Scene(modifyCustomerFormGUI));

            // Show the customer form as a modal dialog (blocks interaction with the main window)
            modifyCustomerStage.initModality(Modality.APPLICATION_MODAL);
            modifyCustomerStage.showAndWait();

            // After the form is closed, you can refresh the main table to display the updated customer information.
            refreshCustomersTableAfterModifiedInput();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

    }

    public static String convertDateTimeForModDisplay(String dateTimeStr) {
        // Define the input and output date time formatters
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Parse the input string to LocalDateTime using the input formatter
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, inputFormatter);

        // Format the LocalDateTime to the desired output format
        String formattedDateTime = dateTime.format(outputFormatter);

        return formattedDateTime;
    }


    // Generic Helper for an alert to users
    private void showInformationAlert(String title, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    // Helper Method to update the table when a user modifies a customer
    private void refreshCustomersTableAfterModifiedInput() {

        try {
            ObservableList<Customer> customers = CustomerDataAccessObject.getAllCustomers();
            mainMenuCustomerTableView.setItems(customers);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void onActionMainMenuDeleteCustomerButton(ActionEvent actionEvent) {
        // Check if a customer is selected in the table
        Customer selectedCustomer = mainMenuCustomerTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            // No customer selected, show an error alert
            showInformationAlert("No Customer Selected", "Please select a customer to delete.");
            return;
        }

        // Check if the selected customer has associated appointments
        try {
            if (CustomerDataAccessObject.hasAssociatedAppointments(selectedCustomer.getCustomerId())) {
                // Customer has associated appointments, show a warning
                showInformationAlert("Cannot Delete Customer", "This customer has associated appointments. Delete the appointments first.");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any database errors here
            return;
        }

        // Confirm deletion with a confirmation dialog
        showConfirmationAlertToUser("Delete Customer Confirmation",
                "Are you sure you want to delete the selected customer?",
                "Click 'OK' to confirm deletion or 'Cancel' to go back.",
                () -> {
                    // User confirmed deletion, proceed with deletion
                    try {
                        CustomerDataAccessObject.deleteCustomer(selectedCustomer.getCustomerId());

                        // Show a success message
                        showInformationAlert("Customer Deleted", "The selected customer has been successfully deleted.");

                        // Refresh the customers table to reflect the deletion
                        refreshCustomersTableAfterDeletedInputByUser();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        // Handle any potential database errors here
                    }
                });
    }

    // Add a method to refresh the customers table after deletion
    private void refreshCustomersTableAfterDeletedInputByUser() {
        try {
            ObservableList<Customer> customers = CustomerDataAccessObject.getAllCustomers();
            mainMenuCustomerTableView.setItems(customers);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential database errors here
        }
    }
}
