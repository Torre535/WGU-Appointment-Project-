package torres.wguappointmentapp.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import torres.wguappointmentapp.DatabaseServer.AppointmentDataAccessObject;
import torres.wguappointmentapp.DatabaseServer.CustomerDataAccessObject;
import torres.wguappointmentapp.Helpers.JDBC;
import torres.wguappointmentapp.Model.Appointments;
import torres.wguappointmentapp.Model.Customer;
import torres.wguappointmentapp.Model.Users;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class ModifyCustomerAppointmentDisplayGUI implements Initializable {
    @FXML
    private Label modifyAppDescriptionLabel;
    @FXML
    private Label modifyAppTypeLabel;
    @FXML
    private Label modifyAppLocationLabel;
    @FXML
    protected TextField modifyAppIdAutoGenTextField;
    @FXML
    protected TextField modifyAppDescriptionTextField;
    @FXML
    protected TextField modifyAppTypeTextField;
    @FXML
    protected TextField modifyAppLocationTextField;
    @FXML
    private Label modifyAppCustomersIdLabel;
    @FXML
    private Label modifyAppUserIdLabel;
    @FXML
    public ComboBox<Customer> modifyAppCustomersIdComboBox;
    @FXML
    public ComboBox<Users> modifyAppUserIdComboBox;
    @FXML
    private Button modifyAppSaveButton;
    @FXML
    private Button modifyAppCancelButton;
    @FXML
    protected TextField modifyAppTitleTextField;
    @FXML
    private Label modifyAppTitleLabel;
    @FXML
    private Label modifyAppStartDateTimeLabel;
    @FXML
    private Label modifyAppEndDateTimeLabel;
    @FXML
    private Label modifyAppContactIdLabel;
    @FXML
    protected ComboBox<String>  modifyAppContactIdComboBox;
    @FXML
    private Label modifyAppIDLabel;
    @FXML
    private ComboBox modifyAppStartTimeComboBox;
    @FXML
    private ComboBox modifyAppEndTimeComboBox;
    @FXML
    protected TextField modifyAppStartTimeAndDateTextField;
    @FXML
    protected TextField modifyAppEndTimeAndDateTextField;

    Stage stage;
    Parent scene;

    private static final DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter specialDateTimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ZoneId localZoneID = ZoneId.systemDefault();
    private static final ZoneId utcZoneID = ZoneId.of("UTC");
    private static final ZoneId estZoneID = ZoneId.of("US/Eastern");


    @Override
    public void initialize(URL url, ResourceBundle rb) {

            try {
                modifyAppContactIdComboBox.setItems(AppointmentDataAccessObject.getAllContacts());
                modifyAppUserIdComboBox.setItems(AppointmentDataAccessObject.getAllUsersId());
                modifyAppCustomersIdComboBox.setItems(CustomerDataAccessObject.getAllCustomers());
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }

        }

    public void autoFillModAppInfo (Appointments selectedAppointment) throws SQLException {
        // Set the appointment information in the ModifyCustomerAppointmentDisplayGUI

    }

    public void onClicksSaveButton(ActionEvent event) throws IOException, SQLException {
        try {
          /*  System.out.println(modifyAppIdAutoGenTextField.getText());
            System.out.println("Title: " + modifyAppTitleTextField.getText());
            System.out.println("Description: " + modifyAppDescriptionTextField.getText());
            System.out.println(modifyAppTypeTextField.getText());
            System.out.println(modifyAppLocationTextField.getText());
            System.out.println(modifyAppStartTimeAndDateTextField.getText());
            System.out.println(modifyAppEndTimeAndDateTextField.getText());
            System.out.println(modifyAppContactIdComboBox.getValue());
            System.out.println(modifyAppCustomersIdComboBox.getValue());
            System.out.println(modifyAppUserIdComboBox.getValue()); */


            // Check for empty fields
            if (checkForEmptyFields()) {
                showAlert("ERROR", "Field(s) Empty", "Please fill in all required fields.");
                return;
            }

            // Validate date/time format
            String appointmentStartDateTime = modifyAppStartTimeAndDateTextField.getText();
            String appointmentEndDateTime = modifyAppEndTimeAndDateTextField.getText();

            if (!isDateTimeValid(appointmentStartDateTime) || !isDateTimeValid(appointmentEndDateTime)) {
                showAlert("ERROR", "DATE/TIME FORMAT ERROR",
                        "24 hour clock format (1am = 01:00, 1pm =13:00) . Only One space between Data and Time, no space at end. Please make sure your start date/time AND end date/time are in the correct formats.\nDATE = 'yyyy-MM-dd'\nTIME = 'HH:mm'");
                return;
            }

            // Parse date/time
            LocalDateTime utcStartDT = LocalDateTime.parse(appointmentStartDateTime, datetimeDTF);
            LocalDateTime utcEndDT = LocalDateTime.parse(appointmentEndDateTime, datetimeDTF);

            // Check for overlap
            if (isAppointmentOverlap(utcStartDT, utcEndDT, -1)) {
                showAlert("ERROR", "Appointment Overlap", "The appointment time overlaps with an existing appointment.");
                return;
            }

            // Check if it's within business hours
            if (!isWithinBusinessHours(utcStartDT, utcEndDT)) {
                showAlert("ERROR", "Outside Business Hours", "The appointment is outside of business hours.");
                return;
            }

            // All validation passed, insert appointment
            String contactName = String.valueOf(modifyAppContactIdComboBox.getSelectionModel().getSelectedItem());
            int selectedContactID = AppointmentDataAccessObject.getContactID(contactName);
            int selectedUserID = Integer.parseInt(String.valueOf(modifyAppUserIdComboBox.getSelectionModel().getSelectedItem()));
            int selectedCustomerID = modifyAppCustomersIdComboBox.getValue().getCustomerId();


            String UTCAppointmentStartDateTime = utcStartDT.atZone(localZoneID).withZoneSameInstant(utcZoneID).format(datetimeDTF);
            String UTCAppointmentEndDateTime = utcEndDT.atZone(localZoneID).withZoneSameInstant(utcZoneID).format(datetimeDTF);

            String SQL = "UPDATE APPOINTMENTS SET TITLE = ?, DESCRIPTION = ?, LOCATION = ?, TYPE = ?, START = ?, END = ?, CREATE_DATE = ?, CREATED_BY = ?, LAST_UPDATE = ?, LAST_UPDATED_BY = ?, CUSTOMER_ID = ?, USER_ID = ?, CONTACT_ID = ? WHERE Appointment_ID = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL);
            ps.setString(1, modifyAppTitleTextField.getText());
            ps.setString(2, modifyAppDescriptionTextField.getText());
            ps.setString(3, modifyAppLocationTextField.getText());
            ps.setString(4, modifyAppTypeTextField.getText());
            ps.setString(5, UTCAppointmentStartDateTime);
            ps.setString(6, UTCAppointmentEndDateTime);
            ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(8, "admin");
            ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(10, "admin");
            ps.setInt(11, selectedCustomerID);
            ps.setInt(12, selectedUserID);
            ps.setInt(13, selectedContactID);
            ps.setString(14, modifyAppIdAutoGenTextField.getText());

            ps.execute();



            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("ERROR", "An error occurred", "Please check your input and try again.");
        }

    }


    private boolean checkForEmptyFields() {
        // Define an array of text fields and comboboxes that you want to check for emptiness
        Node[] fieldsToCheck = {
                modifyAppTitleTextField,
                modifyAppDescriptionTextField,
                modifyAppLocationTextField,
                modifyAppTypeTextField,
                modifyAppStartTimeAndDateTextField,
                modifyAppEndTimeAndDateTextField,
                modifyAppCustomersIdComboBox,
                modifyAppUserIdComboBox,
                modifyAppContactIdComboBox,
        };

        for (Node field : fieldsToCheck) {
            if (field instanceof TextField) {
                if (((TextField) field).getText().isEmpty()) {
                    return true; // At least one field is empty
                }
            } else if (field instanceof ComboBox) {
                if (((ComboBox<?>) field).getSelectionModel().isEmpty()) {
                    return true; // At least one ComboBox is empty
                }
            }
        }

        return false; // No empty fields found
    }

    private boolean isDateTimeValid(String dateTime) {
        String pattern = "yyyy-MM-dd HH:mm";
        try {
            LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(pattern));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isAppointmentOverlap(LocalDateTime newStart, LocalDateTime newEnd, int appointmentIdToExclude) throws SQLException {
        String SQL = "SELECT * FROM appointments WHERE ((? BETWEEN Start AND End) OR (? BETWEEN Start AND End) OR (Start BETWEEN ? AND ?)) AND Appointment_ID != ?";

        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL)) {
            ps.setTimestamp(1, Timestamp.valueOf(newStart));
            ps.setTimestamp(2, Timestamp.valueOf(newEnd));
            ps.setTimestamp(3, Timestamp.valueOf(newStart));
            ps.setTimestamp(4, Timestamp.valueOf(newEnd));
            ps.setInt(5, appointmentIdToExclude);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public static boolean isWithinBusinessHours(LocalDateTime start, LocalDateTime end) {
        // Define business hours for EST (Monday to Friday, 8 AM to 10 PM)
        LocalTime businessStartTime = LocalTime.of(8, 0);
        LocalTime businessEndTime = LocalTime.of(22, 0);

        // Check if the day is a weekend (Saturday or Sunday)
        boolean isWeekend = start.getDayOfWeek() == DayOfWeek.SATURDAY || start.getDayOfWeek() == DayOfWeek.SUNDAY;

        // Check if the appointment is within business hours, including weekends
        return !isWeekend || (start.toLocalTime().isAfter(businessStartTime) && end.toLocalTime().isBefore(businessEndTime));
    }
    // Helper method to show an alert dialog
    private void showAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public void onClicksCancelButton(ActionEvent actionEvent) throws IOException {

        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    // methods for clicking combo boxes
    public void onClicksContactIdComboBox(ActionEvent actionEvent) {
    }
    public void onClicksCustomersIdComboBox(ActionEvent actionEvent) {
    }
    public void onClicksUserIdComboBox(ActionEvent actionEvent) {
    }


}
