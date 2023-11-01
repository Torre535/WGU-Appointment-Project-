package torres.wguappointmentapp.DatabaseServer;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import torres.wguappointmentapp.Helpers.JDBC;
import torres.wguappointmentapp.Model.Appointments;
import torres.wguappointmentapp.Model.Users;

import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;

import static java.lang.String.valueOf;


public class AppointmentDataAccessObject {

    /* DateTimeFormatter is used for formatting and parsing date and time values. ZoneID represents the systems time
    * The UTC zone is the one where the database is stored in */
    private static final DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ZoneId localZoneID = ZoneId.systemDefault();
    private static final ZoneId utcZoneID = ZoneId.of("UTC");


    private static final ObservableList<Appointments> getUserId = FXCollections.observableArrayList();


    //This method gets the maximum App ID and then adds 1 to it to make sure we have a unique ID.
    public static int getNewAppointmentIDAutoGen() throws SQLException {
        int appointmentID = 1;
        String SQL = "SELECT MAX(APPOINTMENT_ID) FROM APPOINTMENTS";

        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                appointmentID = rs.getInt(1) + 1;
            }
        }
        return appointmentID;
    }



    //This method gets the total appointments from the SQL database set and does an exchange in time for the users local time from UTC time.
    public static ObservableList<Appointments> getTotalAppointments() throws SQLException {
        ObservableList<Appointments> totalAppointments = FXCollections.observableArrayList();
        String SQL = "SELECT * FROM APPOINTMENTS ORDER BY CAST(APPOINTMENT_ID AS unsigned)";
        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Appointments newAppointment = createAppointmentFromResult(rs);
                totalAppointments.add(newAppointment);
            }
        }
        return totalAppointments;
    }


    public static ObservableList<Appointments> getAllAppointmentsByContact(int selectedContact) throws SQLException {
        ObservableList<Appointments> allAppointmentsContact = FXCollections.observableArrayList();
        String SQL = "SELECT * FROM APPOINTMENTS WHERE CONTACT_ID = ?";
        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL)) {
            ps.setInt(1, selectedContact);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Appointments newAppointment = createAppointmentFromResult(rs);
                    allAppointmentsContact.add(newAppointment);
                }
            }
        }
        return allAppointmentsContact;
    }


    public static ObservableList<Appointments> getAllAppointmentsByCustomer(String selectedCustomer) throws SQLException {
        ObservableList<Appointments> allAppointmentsCustomer = FXCollections.observableArrayList();
        String SQL = "SELECT * FROM APPOINTMENTS WHERE CUSTOMER_ID = ?";
        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL)) {
            ps.setString(1, selectedCustomer);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Appointments newAppointment = createAppointmentFromResult(rs);
                    allAppointmentsCustomer.add(newAppointment);
                }
            }
        }
        return allAppointmentsCustomer;
    }

    public static ObservableList<Appointments> getAllAppointmentsByMonth() throws SQLException {
        ObservableList<Appointments> allAppointmentsMonth = FXCollections.observableArrayList();
        LocalDate currentDate = LocalDate.now();
        int currentMonthNumber = currentDate.getMonth().getValue();
        String SQL = "SELECT * FROM APPOINTMENTS WHERE MONTH(Start) = ?";
        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL)) {
            ps.setInt(1, currentMonthNumber);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Appointments newAppointment = createAppointmentFromResult(rs);
                    allAppointmentsMonth.add(newAppointment);
                }
            }
        }
        return allAppointmentsMonth;
    }
    public static ObservableList<Appointments> getAllAppointmentsByWeek() throws SQLException {
        ObservableList<Appointments> allAppointmentsWeek = FXCollections.observableArrayList();
        LocalDate currentDate = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int currentYear = currentDate.getYear();
        int currentWeekNumber = currentDate.get(weekFields.weekOfWeekBasedYear());

        String SQL = "SELECT * FROM APPOINTMENTS WHERE YEAR(Start) = ? AND WEEK(Start, ?) = ?";

        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL)) {
            ps.setInt(1, currentYear);
            ps.setInt(2, currentWeekNumber);
            ps.setInt(3, currentWeekNumber);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Appointments newAppointment = createAppointmentFromResult(rs);
                    allAppointmentsWeek.add(newAppointment);
                }
            }
        }

        return allAppointmentsWeek;
    }

    public static ObservableList<Appointments> getAllAppointmentsWeek() throws SQLException {
        ObservableList<Appointments> allAppointmentsWeek = FXCollections.observableArrayList();
        LocalDate currentDate = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int currentMonthNumber = currentDate.getMonth().getValue();
        int currentWeekNumber = currentDate.get(weekFields.weekOfWeekBasedYear()) - 1;

        String SQL = "SELECT * FROM APPOINTMENTS WHERE MONTH(Start) = ? AND WEEK(Start, 1) = ?";

        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL)) {
            ps.setInt(1, currentMonthNumber);
            ps.setInt(2, currentWeekNumber);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Appointments newAppointment = createAppointmentFromResult(rs);
                    allAppointmentsWeek.add(newAppointment);
                }
            }
        }

        return allAppointmentsWeek;
    }
    public static ObservableList<Appointments> getAllPastAppointmentsForCustomer(String selectedCustomer) throws SQLException {
        ObservableList<Appointments> pastAppointments = FXCollections.observableArrayList();
        LocalDate currentDate = LocalDate.now();
        String SQL = "SELECT * FROM APPOINTMENTS WHERE CUSTOMER_ID = ? AND Start < ?";
        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL)) {
            ps.setString(1, selectedCustomer);
            ps.setObject(2, currentDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Appointments newAppointment = createAppointmentFromResult(rs);
                    pastAppointments.add(newAppointment);
                }
            }
        }
        return pastAppointments;
    }

    public static ObservableList<String> getAllContacts() throws SQLException {

        ObservableList<String> allContacts = FXCollections.observableArrayList();

        String SQL = "SELECT CONTACT_NAME, CONTACT_ID, EMAIL FROM CONTACTS";
        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String contactName = rs.getString("CONTACT_NAME"); // Get the contact name from the result set
                allContacts.add(valueOf(contactName)); // Add the contact name (a string) to the list
            }
        }
        return allContacts;
    }

    public static ObservableList<String> getAllUsers() throws SQLException {
        ObservableList<String> allUsers = FXCollections.observableArrayList();
        String SQL = "SELECT USER_NAME FROM USERS"; // Assuming you want to fetch the usernames

        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String userName = rs.getString("USER_NAME"); // Get the username from the result set
                allUsers.add(userName); // Add the username (a string) to the list
            }
        }
        return allUsers;
    }
   /* public static ObservableList<Users> getAllUsers1() throws SQLException {
        String SQL = "SELECT * FROM USERS"; // Assuming you have a table named USERS for user records
        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            ObservableList<Users> allUsers = FXCollections.observableArrayList();

            while (rs.next()) {
                Users users = createUsersFromResultSet(rs);
                allUsers.add(users);
            }

            return allUsers;
        }
    } */

  /*  private static Users createUsersFromResultSet(ResultSet rs) throws SQLException {
        int userId = rs.getInt("USER_ID");
        Users user = new Users(userId, userName);

        return user;
    }

*/


    public static ObservableList<Users> getAllUsersId() throws SQLException {
        ObservableList<Users> allUsers = FXCollections.observableArrayList();
        String SQL = "SELECT USER_ID, USER_NAME FROM USERS"; // Assuming USER_ID is the User ID field
        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int userId = rs.getInt("USER_ID");
                String userName = rs.getString("USER_NAME"); // Retrieve the user name
                Users user = new Users(userId, userName); // Pass both user ID and user name
                allUsers.add(user);
            }
        }
        return allUsers;
    }

    public static ObservableList<Users> getAllUsersIdOnly() throws SQLException {
        ObservableList<Users> allUsers = FXCollections.observableArrayList();
        String SQL = "SELECT USER_ID, USER_NAME FROM USERS"; // Assuming USER_ID is the User ID field

        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int userId = rs.getInt("USER_ID");
                String userName = rs.getString("USER_NAME");
                Users user = new Users(userId, userName);
                allUsers.add(user);
            }
        }

        return allUsers;
    }


    public static ObservableList<Integer> getAllUsersInt() throws SQLException {
        ObservableList<Integer> allUsers = FXCollections.observableArrayList();
        String SQL = "SELECT USER_ID FROM USERS";
        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int userId = rs.getInt("USER_ID");
                allUsers.add(userId);
            }
        }
        return allUsers;
    }


    public static int getContactID(String contactName) throws SQLException {
        int contactID = 0;
        String SQL = "SELECT CONTACT_ID FROM CONTACTS WHERE CONTACT_NAME = ?";
        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL)) {
            ps.setString(1, contactName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    contactID = rs.getInt(1);
                }
            }
        }
        return contactID;
    }

    public static String getContactName(int contactID) throws SQLException {
        String contactName = null;
        String SQL = "SELECT CONTACT_NAME FROM CONTACTS WHERE CONTACT_ID = ?";
        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL)) {
            ps.setInt(1, contactID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    contactName = rs.getString(1);
                }
            }
        }
        return contactName;
    }

    //

    private static Appointments createAppointmentFromResult(ResultSet rs) throws SQLException {
        int appointmentID = rs.getInt(1);
        String appointmentTitle = rs.getString(2);
        String appointmentDescription = rs.getString(3);
        String appointmentLocation = rs.getString(4);
        String appointmentType = rs.getString(5);
        String start = rs.getString(6);
        String end = rs.getString(7);
        int customerID = rs.getInt(12);
        int userID = rs.getInt(13);
        int contactID = rs.getInt(14);

        LocalDateTime utcStartDT = LocalDateTime.parse(start, datetimeDTF);
        LocalDateTime utcEndDT = LocalDateTime.parse(end, datetimeDTF);

        ZonedDateTime localZoneStart = utcStartDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);
        ZonedDateTime localZoneEnd = utcEndDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);

        String localStart = localZoneStart.format(datetimeDTF);
        String localEnd = localZoneEnd.format(datetimeDTF);


        return new Appointments(appointmentID, appointmentTitle, appointmentDescription,
                appointmentLocation, appointmentType, localStart, localEnd,
                customerID, userID, contactID);
    }


    public static void deleteAppointmentFromTableView(int appointmentId) throws SQLException {
        String SQL = "DELETE FROM APPOINTMENTS WHERE APPOINTMENT_ID = ?";
        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL)) {
            ps.setInt(1, appointmentId);
            ps.executeUpdate();
        }
    }

    // checks to see if appointment trying to make is outside of EST business hours, returns true if outside business hours and FALSE if inside hours.
    public static boolean isOutsideBusinessHours(LocalDateTime proposedStart, LocalDateTime proposedEnd) {
        // Define business hours (8 am to 10 pm EST)
        LocalTime businessHoursStart = LocalTime.of(8, 0);
        LocalTime businessHoursEnd = LocalTime.of(22, 0);

        // Convert the proposed appointment times to EST (UTC-5)
        ZoneId estZone = ZoneId.of("America/New_York"); // Eastern Standard Time (EST)
        ZonedDateTime estStart = proposedStart.atZone(estZone);
        ZonedDateTime estEnd = proposedEnd.atZone(estZone);

        // Check if the appointment starts or ends before business hours or after
        return estStart.toLocalTime().isBefore(businessHoursStart) ||
                estEnd.toLocalTime().isAfter(businessHoursEnd);
    }
}