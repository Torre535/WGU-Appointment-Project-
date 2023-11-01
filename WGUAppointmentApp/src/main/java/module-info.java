module torres.wguappointmentapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens torres.wguappointmentapp to javafx.fxml;
    exports torres.wguappointmentapp;
    exports torres.wguappointmentapp.Controllers;
    opens torres.wguappointmentapp.Controllers to javafx.fxml;
    opens torres.wguappointmentapp.Model;
}