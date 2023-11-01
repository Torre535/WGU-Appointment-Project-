package torres.wguappointmentapp.Model;

import java.time.LocalDateTime;

public class Users {

    // Fields from the ERD diagram that are within the database table
    private int userId;
    private String userName;
    private String password;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;

    // Constructor for Users base on database table
    public Users(int userId, String userName) {
        this.userId = userId;
      this.userName = userName;
        this.password = password;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    // Getters and Setters methods for Users

    //User ID
    public int getUserId() {

        return userId;
    }

    public void setUserId(int userId) {

        this.userId = userId;
    }

    //User Name
    public String getUserName() {

        return userName;
    }

    public void setUserName(String userName) {

        this.userName = userName;
    }

    //Password
    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
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

    // Last Updated By
    public String getLastUpdatedBy() {

        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {

        this.lastUpdatedBy = lastUpdatedBy;
    }


    @Override
    public String toString() {
        return String.valueOf(userId);

    }


}
