package torres.wguappointmentapp.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import torres.wguappointmentapp.Helpers.JDBC;
import torres.wguappointmentapp.Model.Customer;

import torres.wguappointmentapp.DatabaseServer.AppointmentDataAccessObject;
import torres.wguappointmentapp.DatabaseServer.CustomerDataAccessObject;
import torres.wguappointmentapp.Model.Users;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class AddCustomerAppointmentDisplayGUI implements Initializable {

    @FXML
    private TextField createAppStartTimeAndDateTextField;
    @FXML
    private TextField createAppEndTimeaAndDateTextField;
    @FXML
    private TextField createAppStartTimeTextField;
    @FXML
    private TextField createAppEndTimeTextField;
    @FXML
    private AnchorPane createAppAnchorPane;
    @FXML
    private Text createNewAppText;
    @FXML
    private Label createAppDescriptionLabel;
    @FXML
    private Label createAppTypeLabel;
    @FXML
    private Label createAppLocationLabel;
    @FXML
    private TextField createAppAppIDTextField;
    @FXML
    private TextField createAppDescriptionTextField;
    @FXML
    private TextField createAppTypeTextField;
    @FXML
    private TextField createAppLocationTextField;
    @FXML
    private Label createAppCustomerIDLabel;
    @FXML
    private Label createAppUserIDLabel;
    @FXML
    private ComboBox<Customer> createAppCustomerIDComboBox;
    @FXML
    private ComboBox<Users> createAppUserIDComboBox;
    @FXML
    private Button createAppSaveButton;
    @FXML
    private Button createAppCancelButton;
    @FXML
    private TextField createAppTitleTextField;
    @FXML
    private Label createAppTitleLabel;
    @FXML
    private Label createAppStartTimeDateLabel;
    @FXML
    private Label createAppEndDateTimeLabel;
    @FXML
    private Label createAppContactIDLabel;
    @FXML
    private ComboBox<String> createAppContactIDComboBox;
    @FXML
    private Label createAppIDLabel;

    Stage stage;

    Parent scene;

    private static final DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter specialDateTimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ZoneId localZoneID = ZoneId.systemDefault();
    private static final ZoneId utcZoneID = ZoneId.of("UTC");
    private static final ZoneId estZoneID = ZoneId.of("US/Eastern");

     public void onCreateNewAppSaveButton(ActionEvent event) throws IOException, SQLException {
         try {
             // Check for empty fields
             if (checkForEmptyFields()) {
                 showAlert("ERROR", "Field(s) Empty", "Please fill in all required fields.");
                 return;
             }

             // Validate date/time format
             String appointmentStartDateTime = createAppStartTimeAndDateTextField.getText();
             String appointmentEndDateTime = createAppEndTimeaAndDateTextField.getText();

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
             String contactName = String.valueOf(createAppContactIDComboBox.getSelectionModel().getSelectedItem());
             int selectedContactID = AppointmentDataAccessObject.getContactID(contactName);
             int selectedUserID = Integer.parseInt(String.valueOf(createAppUserIDComboBox.getSelectionModel().getSelectedItem()));
             int selectedCustomerID = createAppCustomerIDComboBox.getValue().getCustomerId();

             String UTCAppointmentStartDateTime = utcStartDT.atZone(localZoneID).withZoneSameInstant(utcZoneID).format(datetimeDTF);
             String UTCAppointmentEndDateTime = utcEndDT.atZone(localZoneID).withZoneSameInstant(utcZoneID).format(datetimeDTF);

             String SQL = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
             PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL);
             ps.setInt(1, AppointmentDataAccessObject.getNewAppointmentIDAutoGen());
             ps.setString(2, createAppTitleTextField.getText());
             ps.setString(3, createAppDescriptionTextField.getText());
             ps.setString(4, createAppLocationTextField.getText());
             ps.setString(5, createAppTypeTextField.getText());
             ps.setString(6, UTCAppointmentStartDateTime);
             ps.setString(7, UTCAppointmentEndDateTime);
             ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
             ps.setString(9, "admin");
             ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
             ps.setString(11, "admin");
             ps.setInt(12, selectedCustomerID);
             ps.setInt(13, selectedUserID);
             ps.setInt(14, selectedContactID);
             ps.execute();

             stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
             stage.close();
         } catch (Exception ex) {
             ex.printStackTrace();
             showAlert("ERROR", "An error occurred", "Please check your input and try again.");
         }
     }

    // Helper method to show an alert dialog
    private void showAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
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

    private boolean checkForEmptyFields() {
        // Define an array of text fields and comboboxes that you want to check for emptiness
        Node[] fieldsToCheck = {
                createAppTitleTextField,
                createAppDescriptionTextField,
                createAppLocationTextField,
                createAppTypeTextField,
                createAppStartTimeAndDateTextField,
                createAppEndTimeaAndDateTextField,
                createAppCustomerIDComboBox,
                createAppUserIDComboBox,
                createAppContactIDComboBox
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

    public void onCreateNewAppCancelButton(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();

    }

    public void initialize(URL url, ResourceBundle rb) {

        try {
            createAppContactIDComboBox.setItems(AppointmentDataAccessObject.getAllContacts());
            createAppUserIDComboBox.setItems(AppointmentDataAccessObject.getAllUsersId());
            createAppCustomerIDComboBox.setItems(CustomerDataAccessObject.getAllCustomers());
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }


    }
}

