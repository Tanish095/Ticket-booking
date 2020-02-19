/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketbooking.ui.mainui.user;

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
public class UserdashController implements Initializable {
    public static String username;
    @FXML
    private AnchorPane holderPane;
    @FXML
    private JFXButton home;
    @FXML
    private JFXButton movies;
    @FXML
    private JFXButton history;
    @FXML
    private JFXButton about;
    @FXML
    private JFXButton profile;
    @FXML
    private JFXButton settings;
    @FXML
    private JFXButton logout;
    @FXML
    private Label userlabel;
    AnchorPane showp,historyp,aboutp,settingsp,homep,profilep;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userlabel.setText(username);
        try {
             showp=FXMLLoader.load(getClass().getResource("Show.fxml"));
             historyp=FXMLLoader.load(getClass().getResource("OrderHistory.fxml"));
             aboutp=FXMLLoader.load(getClass().getResource("About.fxml"));
             settingsp=FXMLLoader.load(getClass().getResource("Settings.fxml"));
             homep=FXMLLoader.load(getClass().getResource("Home.fxml"));
             profilep=FXMLLoader.load(getClass().getResource("Profile.fxml"));
        } catch (IOException ex) {
            AlertMaker.showErrorMessage(ex);
        }
        setNode(homep);
    }    
    //Set selected node to a content holder
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
        setNode(showp);
    }

    @FXML
    private void goToHistory(ActionEvent event) {
        setNode(historyp);
    }

    @FXML
    private void showAbout(ActionEvent event) {
        setNode(aboutp);
    }

    @FXML
    private void showProfile(ActionEvent event) {
        setNode(profilep);
        
    }

    @FXML
    private void showSettings(ActionEvent event) {
        setNode(settingsp);
    }
    private void closeStage() {
    ((Stage) logout.getScene().getWindow()).close();
    }
    @FXML
    private void goToLogin(ActionEvent event) {
        loadWindow("/ticketbooking/ui/login/login.fxml","Aurora");
        closeStage();
    }
    
}
