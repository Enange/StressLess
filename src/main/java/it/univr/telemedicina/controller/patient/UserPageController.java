package it.univr.telemedicina.controller.patient;

import it.univr.telemedicina.MainApplication;
import it.univr.telemedicina.models.users.Patient;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class UserPageController implements Initializable{

    @FXML
    public Label lblTime;
    @FXML
    public Button buttonHome;
    @FXML
    public Button buttonEditPressure;
    @FXML
    public Button buttonEditDrugs;
    @FXML
    public Button buttonEditProfile;
    @FXML
    public AnchorPane homeScene;
    @FXML
    public AnchorPane pressureScene;
    @FXML
    public AnchorPane drugsScene;
    @FXML
    public AnchorPane editProfileScene;
    @FXML
    private Label lblName;
    @FXML
    public Button logoutButton;

    private final MainApplication newScene = new MainApplication();
    private static Patient patient;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set scene to be visible
        homeScene.setVisible(true);
        pressureScene.setVisible(false);
        editProfileScene.setVisible(false);
        drugsScene.setVisible(false);

        // Edit button style to show the clicked one
        buttonHome.setStyle("-fx-background-color: #2A7878");

        lblName.setText(patient.getName());

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                lblTime.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
        };
        timer.start();
    }

    /**
     * Method that handle button click and use the type of the button chosen to change scene
     */
    public void handleChangeScene(ActionEvent actionEvent) throws IOException {
        if (actionEvent.getSource() == buttonHome) {  // Home button click
            homeScene.setVisible(true);
            pressureScene.setVisible(false);
            editProfileScene.setVisible(false);
            drugsScene.setVisible(false);

            // Edit button style to show the clicked one
            buttonHome.setStyle("-fx-background-color: #2A7878");
            buttonEditPressure.setStyle("-fx-background-color: #0000");
            buttonEditProfile.setStyle("-fx-background-color: #0000");
            buttonEditDrugs.setStyle("-fx-background-color: #0000");
        }
        else if(actionEvent.getSource() == buttonEditPressure){   // Pressure button click
            homeScene.setVisible(false);
            pressureScene.setVisible(true);
            editProfileScene.setVisible(false);
            drugsScene.setVisible(false);

            // Edit button style to show the clicked one
            buttonHome.setStyle("-fx-background-color: #0000");
            buttonEditPressure.setStyle("-fx-background-color: #2A7878");
            buttonEditProfile.setStyle("-fx-background-color: #0000");
            buttonEditDrugs.setStyle("-fx-background-color: #0000");
        }
        else if(actionEvent.getSource() == buttonEditDrugs){
            homeScene.setVisible(false);
            pressureScene.setVisible(false);
            editProfileScene.setVisible(false);
            drugsScene.setVisible(true);

            // Edit button style to show the clicked one
            buttonHome.setStyle("-fx-background-color: #0000");
            buttonEditPressure.setStyle("-fx-background-color: #0000");
            buttonEditProfile.setStyle("-fx-background-color: #0000");
            buttonEditDrugs.setStyle("-fx-background-color: #2A7878");
        }
        else if(actionEvent.getSource() == logoutButton){     // LogOut button click
            newScene.changeScene("Login.fxml", "Paziente", actionEvent);
        }
        else {      // Edit profile button click
            homeScene.setVisible(false);
            pressureScene.setVisible(false);
            editProfileScene.setVisible(true);
            drugsScene.setVisible(false);

            // Edit button style to show the clicked one
            buttonHome.setStyle("-fx-background-color: #0000");
            buttonEditPressure.setStyle("-fx-background-color: #0000");
            buttonEditProfile.setStyle("-fx-background-color: #2A7878");
            buttonEditDrugs.setStyle("-fx-background-color: #0000");
        }
    }

    /**
     * method to take Patient Data
     * @param patient
     */
    public void setPatient(Patient patient){
        UserPageController.patient = patient;
    }
}


