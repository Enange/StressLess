package it.univr.telemedicina.controller.doctor;

import it.univr.telemedicina.MainApplication;
import it.univr.telemedicina.models.users.Doctor;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DoctorPageController implements Initializable {
    private final MainApplication newScene = new MainApplication();
    private static Doctor doctor;
    @FXML
    public AnchorPane DoctorHomeScene;
    @FXML
    public AnchorPane DoctorEditProfileScene;
    @FXML
    public AnchorPane DoctorStatisticsScene;
    @FXML
    public AnchorPane DoctorPatientAnalysisScene;
    @FXML
    public Button buttonLogOut;
    @FXML
    public Button buttonEditProfile;
    @FXML
    public Button buttonSearchPatient;
    @FXML
    public Button buttonHomeDoc;
    @FXML
    public Button buttonStatisticDoc;
    @FXML
    public Label lblDoctorName;
    @FXML
    public Label lblTime;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DoctorHomeScene.setVisible(true);
        buttonHomeDoc.setStyle("-fx-background-color: #3d5e49");
        DoctorStatisticsScene.setVisible(false);
        DoctorEditProfileScene.setVisible(false);
        DoctorPatientAnalysisScene.setVisible(false);

        lblDoctorName.setText("Dott. " + doctor.getName().charAt(0) + ". " + doctor.getSurname());
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                lblTime.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
        };
        timer.start();
    }

    /**
     * Handle Side button showing the right tab
     * @param actionEvent action
     * @throws IOException Exception
     */
    public void handleChangeScene(ActionEvent actionEvent) throws IOException {
        if (actionEvent.getSource() == buttonHomeDoc) {  // Home button click
            DoctorHomeScene.setVisible(true);
            DoctorStatisticsScene.setVisible(false);
            DoctorEditProfileScene.setVisible(false);
            DoctorPatientAnalysisScene.setVisible(false);

            // Edit button style to show the clicked one
            buttonHomeDoc.setStyle("-fx-background-color: #3d5e49");
            buttonStatisticDoc.setStyle("-fx-background-color: #0000");
            buttonEditProfile.setStyle("-fx-background-color: #0000");
            buttonSearchPatient.setStyle("-fx-background-color: #0000");
        } else if (actionEvent.getSource() == buttonStatisticDoc) {    // Statistic button click
            DoctorHomeScene.setVisible(false);
            DoctorStatisticsScene.setVisible(true);
            DoctorEditProfileScene.setVisible(false);
            DoctorPatientAnalysisScene.setVisible(false);

            // Edit button style to show the clicked one
            buttonHomeDoc.setStyle("-fx-background-color: #0000");
            buttonStatisticDoc.setStyle("-fx-background-color: #3d5e49");
            buttonEditProfile.setStyle("-fx-background-color: #0000");
            buttonSearchPatient.setStyle("-fx-background-color: #0000");
        }
        else if(actionEvent.getSource() == buttonLogOut) {     // LogOut button click
            newScene.changeScene("Login.fxml", "Paziente", actionEvent);
        }
        else if(actionEvent.getSource() == buttonEditProfile){
            DoctorHomeScene.setVisible(false);
            DoctorStatisticsScene.setVisible(false);
            DoctorEditProfileScene.setVisible(true);
            DoctorPatientAnalysisScene.setVisible(false);

            buttonHomeDoc.setStyle("-fx-background-color: #0000");
            buttonStatisticDoc.setStyle("-fx-background-color: #0000");
            buttonEditProfile.setStyle("-fx-background-color: #3d5e49");
            buttonSearchPatient.setStyle("-fx-background-color: #0000");
        }
        else if(actionEvent.getSource() == buttonSearchPatient) {
            DoctorHomeScene.setVisible(false);
            DoctorStatisticsScene.setVisible(false);
            DoctorEditProfileScene.setVisible(false);
            DoctorPatientAnalysisScene.setVisible(true);

            // Edit button style to show the clicked one
            buttonHomeDoc.setStyle("-fx-background-color: #0000");
            buttonStatisticDoc.setStyle("-fx-background-color: #0000");
            buttonEditProfile.setStyle("-fx-background-color: #0000");
            buttonSearchPatient.setStyle("-fx-background-color: #3d5e49");
        }
    }

    /**
     * Take the doctor Data
     * @param doctor doctor
     */
    public void setDoctor(Doctor doctor) {
        DoctorPageController.doctor = doctor;
    }
}
