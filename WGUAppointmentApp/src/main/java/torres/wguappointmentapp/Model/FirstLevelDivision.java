package torres.wguappointmentapp.Model;

import java.time.LocalDateTime;

public class FirstLevelDivision {


    // Fields from the ERD data that are within the database table
    private int divisionId;
    private String division;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private int countryId;
    private int divisionID;


    // Constructor for the First Level Division using fields from database table
    public FirstLevelDivision(String division)
    {
        this.divisionId = divisionId;
        this.division = division;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.countryId = countryId;
    }
    // Public Getter and Setter methods


    // Division ID
    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {

        this.divisionId = divisionId;
    }

    //Division
    public String getDivision() {

        return division;
    }

    public void setDivision(String division) {

        this.division = division;
    }


    //Create Date
    public LocalDateTime getCreateDate() {

        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {

        this.createDate = createDate;
    }

    //Created By
    public String getCreatedBy() {

        return createdBy;
    }

    public void setCreatedBy(String createdBy) {

        this.createdBy = createdBy;
    }

    //Last Updated
    public LocalDateTime getLastUpdate() {

        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {

        this.lastUpdate = lastUpdate;
    }

    //Last Updated By

    public String getLastUpdatedBy() {

        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {

        this.lastUpdatedBy = lastUpdatedBy;
    }

    //Country ID
    public int getCountryId() {
        return countryId;
    }


    public void setCountryId(int countryId) {

        this.countryId = countryId;
    }

    public int getDivisionID() {
        return this.divisionID;
    }

    @Override
    public String toString() {
        return division; //
    }
}

