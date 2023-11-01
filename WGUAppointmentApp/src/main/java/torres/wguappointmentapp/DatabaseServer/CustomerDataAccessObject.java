package torres.wguappointmentapp.DatabaseServer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import torres.wguappointmentapp.Helpers.JDBC;
import torres.wguappointmentapp.Model.Countries;
import torres.wguappointmentapp.Model.Customer;
import torres.wguappointmentapp.Model.FirstLevelDivision;
import torres.wguappointmentapp.Model.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class CustomerDataAccessObject {


    private static final ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    public static int getNewCustomerID() throws SQLException {
        int customerID = 1;
        String SQL = "SELECT CUSTOMER_ID FROM CUSTOMERS ORDER BY CAST(CUSTOMER_ID AS unsigned)";
        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                if (rs.getInt(1) == customerID) {
                    customerID++;
                } else if (rs.getInt(1) != customerID) {
                    break;
                }
            }
        }
        return customerID;
    }

    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        String SQL = "SELECT cu.CUSTOMER_ID, cu.CUSTOMER_NAME, cu.ADDRESS, cu.POSTAL_CODE, cu.PHONE, cu.DIVISION_ID, co.COUNTRY FROM customers cu " +
                "INNER JOIN FIRST_LEVEL_DIVISIONS fld ON cu.DIVISION_ID = fld.DIVISION_ID INNER JOIN COUNTRIES co ON fld.COUNTRY_ID = co.COUNTRY_ID ORDER BY CAST(CUSTOMER_ID AS unsigned)";
        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {
            ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
            while (rs.next()) {
                Customer newCustomer = createCustomerFromResult(rs);
                allCustomers.add(newCustomer);
            }
            return allCustomers;
        }
        }

    private static Customer createCustomerFromResult(ResultSet rs) throws SQLException {
        int customerID = rs.getInt(1);
        String customerName = rs.getString(2);
        String customerAddress = rs.getString(3);
        String customerPostalCode = rs.getString(4);
        String customerPhoneNumber = rs.getString(5);
        int customerDivisionId = rs.getInt(6);
        String customerCountry = rs.getString(7);

        return new Customer(customerID, customerName, customerAddress, customerPostalCode, customerPhoneNumber, customerDivisionId, customerCountry);
    }
    public static ObservableList<String> getAllCountries() throws SQLException {
        String SQL = "SELECT COUNTRY,COUNTRY_ID FROM COUNTRIES";
        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {
            ObservableList<String> allCountries = FXCollections.observableArrayList();
            while (rs.next()) {
                String countryName = rs.getString(1);
                Countries country = new Countries(countryName);
                allCountries.add(String.valueOf(country));
            }
            return allCountries;
        }
    }


    public static ObservableList<String> getFirstLevelDivisions(String selectedCountry) throws SQLException {
        String SQL = "SELECT DIVISION FROM FIRST_LEVEL_DIVISIONS WHERE COUNTRY_ID = ?";
        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL)) {
            int countryId = getCountryId(selectedCountry);
            ps.setInt(1, countryId);
            try (ResultSet rs = ps.executeQuery()) {
                ObservableList<String> allFirstLevelDivisions = FXCollections.observableArrayList();
                while (rs.next()) {
                    String firstLevelDivisionName = rs.getString(1);
                    FirstLevelDivision firstLevelDivisions = new FirstLevelDivision(firstLevelDivisionName);
                    allFirstLevelDivisions.add(String.valueOf(firstLevelDivisions));
                }
                return allFirstLevelDivisions;
            }
        }
    }
// Used in the auto populate for mod customer Division (State/Prov) not ID
    public static String getFirstLevelDivisionName2(int firstLevelDivisionNum) throws SQLException {
        String SQL = "SELECT DIVISION FROM FIRST_LEVEL_DIVISIONS WHERE DIVISION_ID = ?";
        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL)) {
            ps.setInt(1, firstLevelDivisionNum);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        return null;
    }


    public static int getDivisionID(String firstLevelDivisionName) throws SQLException {
        String SQL = "SELECT DIVISION_ID FROM FIRST_LEVEL_DIVISIONS WHERE DIVISION = ?";
        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL)) {
            ps.setString(1, firstLevelDivisionName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public static String getFirstLevelDivisionName(int firstLevelDivisionNum) throws SQLException {
        String SQL = "SELECT DIVISION FROM FIRST_LEVEL_DIVISIONS WHERE DIVISION_ID = ?";
        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL)) {
            ps.setInt(1, firstLevelDivisionNum);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        return null;
    }
    private static int getCountryId(String selectedCountry) throws SQLException {
        String SQL = "SELECT COUNTRY_ID FROM COUNTRIES WHERE COUNTRY = ?";
        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL)) {
            ps.setString(1, selectedCountry);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }



    // Used to delete customer
    public static void deleteCustomer(int customerId) throws SQLException {
        // Delete the customer from the database
        String deleteCustomerSQL = "DELETE FROM CUSTOMERS WHERE CUSTOMER_ID = ?";
        try (PreparedStatement deleteCustomerStmt = JDBC.getConnection().prepareStatement(deleteCustomerSQL)) {
            deleteCustomerStmt.setInt(1, customerId);
            deleteCustomerStmt.executeUpdate();
        }
    }
    // Checks to see if customer has associated appointments, used before deleteing
    public static boolean hasAssociatedAppointments(int customerId) throws SQLException {
        String checkAppointmentsSQL = "SELECT COUNT(*) FROM APPOINTMENTS WHERE CUSTOMER_ID = ?";
        try (PreparedStatement checkAppointmentsStmt = JDBC.getConnection().prepareStatement(checkAppointmentsSQL)) {
            checkAppointmentsStmt.setInt(1, customerId);
            try (ResultSet rs = checkAppointmentsStmt.executeQuery()) {
                if (rs.next()) {
                    int appointmentCount = rs.getInt(1);
                    return appointmentCount > 0;
                }
            }
        }
        return false;
    }

    public static ObservableList<String> getAllCustomerNames() throws SQLException {
        ObservableList<String> customerNames = FXCollections.observableArrayList();
        String SQL = "SELECT CUSTOMER_NAME FROM customers ORDER BY CAST(CUSTOMER_ID AS unsigned)";
        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String customerName = rs.getString("CUSTOMER_NAME");
                customerNames.add(customerName);
            }
        }
        return customerNames;
    }

    public static void updateCustomer(int customerId, String newName, String newAddress, String newPhone, String newPostalCode, String newCountry, String newDivision) throws SQLException {
        String SQL = "UPDATE CUSTOMERS SET CUSTOMER_NAME = ?, ADDRESS = ?, PHONE = ?, POSTAL_CODE = ?, DIVISION_ID = ? WHERE CUSTOMER_ID = ?";

        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL)) {
            ps.setString(1, newName);
            ps.setString(2, newAddress);
            ps.setString(3, newPhone);
            ps.setString(4, newPostalCode);
            int divisionId = getDivisionID(newDivision);
            ps.setInt(5, divisionId);
            ps.setInt(6, customerId);

            ps.executeUpdate();
        }
    }


}

