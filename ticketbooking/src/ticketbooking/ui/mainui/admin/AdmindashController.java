/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketbooking.ui.mainui.admin;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import ticketbooking.alert.AlertMaker;

/**
 * FXML Controller class
 *
 * @author ArKadius
 */
public class AdmindashController implements Initializable {
    public static String username;
    @FXML
    private JFXButton home;
    @FXML
    private JFXButton movies;
    @FXML
    private JFXButton settings;
    @FXML
    private JFXButton logout;
    @FXML
    private AnchorPane holderPane;
    @FXML
    private Label adminid;
    @FXML
    private JFXButton theatre;
    @FXML
    private JFXButton show;
    AnchorPane mov,theat,showp,homep,settingsp;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        adminid.setText(username);
        try {
            homep=FXMLLoader.load(getClass().getResource("Home.fxml"));
             mov=FXMLLoader.load(getClass().getResource("Movies.fxml"));
             theat=FXMLLoader.load(getClass().getResource("Theatre.fxml"));
             showp=FXMLLoader.load(getClass().getResource("Show.fxml"));
             settingsp=FXMLLoader.load(getClass().getResource("Settings.fxml"));
        } catch (IOException ex) {
            AlertMaker.showErrorMessage(ex);
        }
        setNode(homep);
    }
    private void setNode(Node node) {
        holderPane.getChildren().clear();
        holderPane.getChildren().add((Node) node);

        FadeTransition ft = new FadeTransition(Duration.millis(1500));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }
    private void closeStage() {
        ((Stage) adminid.getScene().getWindow()).close();
    }
    void loadWindow(String loc, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException ex) {
            AlertMaker.showErrorMessage(ex);
        }
    }
    @FXML
    private void goToHome(ActionEvent event) {
        setNode(homep);
    }
    @FXML
    private void goToMovies(ActionEvent event) {
        setNode(mov);
    }



    @FXML
    private void showSettings(ActionEvent event) {
        setNode(settingsp);
    }

    @FXML
    private void goToLogin(ActionEvent event) {
        closeStage();
        loadWindow("/ticketbooking/ui/login/admin/adminlogin.fxml","Aurora");
    }

    @FXML
    private void goToTheatre(ActionEvent event) {
        setNode(theat);
    }

    @FXML
    private void goToShow(ActionEvent event) {
        setNode(showp);
    }
    
}
