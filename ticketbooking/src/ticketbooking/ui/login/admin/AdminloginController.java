/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketbooking.ui.login.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ticketbooking.alert.AlertMaker;
import ticketbooking.models.Admin;
import ticketbooking.models.User;
import ticketbooking.ui.mainui.admin.AdmindashController;
import ticketbooking.util.database.DatabaseHandler;

/**
 * FXML Controller class
 *
 * @author ArKadius
 */
public class AdminloginController implements Initializable {

    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXButton signin;
    @FXML
    private JFXButton exit;
    @FXML
    private JFXButton user;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public boolean ValidateUsernamePassword() throws SQLException, NoSuchAlgorithmException
    {
        boolean valid=false;
        ResultSet Myres=DatabaseHandler.getUserNamePassword_admin();
        while(Myres.next())
        {
            if(Myres.getString("username").equals(username.getText()))
                if(Myres.getString("password").equals(Admin.Sha1(password.getText())))
                {    valid=true;
                     AdmindashController.username=Myres.getString("username");
                }
        }
        return valid;
    }
    @FXML
    private void SignInAdmin(ActionEvent event) {
        try {
            if(ValidateUsernamePassword())
            {
                closeStage();
                loadWindow("/ticketbooking/ui/mainui/admin/AdminDashBoard.fxml","Aurora");
            }
            else
            {
                AlertMaker.showNotification("Login Failed!","Your Admin ID or Password is invalid", AlertMaker.image_cross);
            }
        } catch (SQLException | NoSuchAlgorithmException ex) {
            AlertMaker.showWarning(ex);
        }
    }

    @FXML
    private void exitToSystem(ActionEvent event) {
        System.exit(0);
    }

    private void closeStage() {
        ((Stage) username.getScene().getWindow()).close();
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
    private void SwitchtoUser(ActionEvent event) {
        closeStage();
        loadWindow("/ticketbooking/ui/login/login.fxml","Aurora");
        
    }
    
}
