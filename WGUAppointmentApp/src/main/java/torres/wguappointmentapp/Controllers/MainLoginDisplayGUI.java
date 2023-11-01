package torres.wguappointmentapp.Controllers;

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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainLoginDisplayGUI implements Initializable {

    public Button loginButton;
    @FXML
    private Button exitLoginScreen;
    @FXML
    private Text UserloginUserAppTitleText;
    @FXML
    private AnchorPane loginMainUserAnchorPane;
    @FXML
    private TextField loginUserNameTextField;
    @FXML
    private Button userClicksLoginButton;
    @FXML
    private PasswordField loginUserPasswordTextField;
    @FXML
    private Label loginUserNameLabel;
    @FXML
    private Label loginUserPasswordLabel;
    @FXML
    private Text loginUserAppTitleText;
    @FXML
    private Text loginRegionTitleText;
    @FXML
    private Text loginRegionDisplayTextToUser;

    private Stage stage;

    private Parent scene;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupUI();
    }

    private void setupUI() {
        String lang = getLocaleData();
        translateGUIForUser(lang);
        displayZoneIDToUser();
    }

    private String getLocaleData() {
        Locale locale = Locale.getDefault();
        return locale.getDisplayLanguage();
    }

    private void translateGUIForUser(String lang) {
        if ("français".equalsIgnoreCase(lang)) {
            translateLanguageToFrench();
        } else if ("English".equalsIgnoreCase(lang)) {

        } else {
        }
    }

    private void translateLanguageToFrench() {
        System.out.println("Your system settings are in French.");
        loginButton.setText("Connexion");
        loginUserAppTitleText.setText("Connexion");
        loginUserPasswordLabel.setText("Mot de passe");
        loginUserNameLabel.setText("Nom d'utilisateur");
        loginRegionTitleText.setText("Région:");
    }
    private void displayZoneIDToUser() {
        ZoneId zoneId = ZoneId.systemDefault();
        System.out.println("ZoneId: " + zoneId);
        loginRegionDisplayTextToUser.setText(String.valueOf(zoneId));
    }


    public void onUserClicksTheLoginButton(ActionEvent actionEvent) {
        String username = loginUserNameTextField.getText();
        String password = loginUserPasswordTextField.getText();

        if (isValidLoginAttempt(username, password)) {
            handleSuccessfulLoginAttempt();
        } else {
            handleFailedLoginAttempt();
        }
    }

    private boolean isValidLoginAttempt(String username, String password) {
        String validUsername = "USER_NAME";
        String validPassword = "PASSWORD";

        // Used this trim method because I was always getting wrong password or login because i would leave a space.
        username = username.trim();
        password = password.trim();

        return validUsername.equals(username) && validPassword.equals(password);
    }

    private void handleSuccessfulLoginAttempt() {
        System.out.println("LOGIN SUCCESSFUL!");

        userLoginTrackerFile(true);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/torres/wguappointmentapp/MainMenuDisplayGUI.fxml"));
            Parent mainMenuParent = loader.load();
            Scene mainMenuScene = new Scene(mainMenuParent);


            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.setScene(mainMenuScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleFailedLoginAttempt() {
        // Handle failed login
        System.out.println("LOGIN UNSUCCESSFUL!");

        userLoginTrackerFile(false);

        showLoginErrorMessageDisplay(getLocaleData());
    }

    private void userLoginTrackerFile(boolean isSuccessful) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        String status = isSuccessful ? "SUCCESSFUL" : "UNSUCCESSFUL";
        String username = loginUserNameTextField.getText();
        String logEntry = String.format("%s - Login %s - Username: %s%n", formattedDateTime, status, username);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("login_activity.txt", true))) {
            writer.write(logEntry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showLoginErrorMessageDisplay(String lang) {
        String title = "ERROR";
        String headerText = "Login Error Attempt";
        String contentText = "LOGIN FAILED. WRONG USER NAME OR PASSWORD OR BOTH. Please Try Again!";

        if ("français".equalsIgnoreCase(lang)) {
            title = "ERREUR";
            headerText = "Tentative d'erreur de connexion";
            contentText = "LA CONNEXION A ÉCHOUÉ. NOM D'UTILISATEUR OU MOT DE PASSE MAUVAIS, OU LES DEUX. Veuillez réessayer!";
        }

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public void onExitLoginScreen(ActionEvent actionEvent) {
        String lang = getLocaleData();

        String title;
        String headerText;
        String contentText;

        if ("français".equalsIgnoreCase(lang)) {
            title = "La Confirmation";
            headerText = "Êtes-vous sûr de vouloir quitter ?";
            contentText = "Si vous sélectionnez 'Oui', l'application se fermera.";
        } else {
            title = "Confirmation";
            headerText = "Are you sure you want to exit?";
            contentText = "If you select 'Yes', the application will close.";
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

       // If the user clicks Ok then the system closes
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }

    }

}


