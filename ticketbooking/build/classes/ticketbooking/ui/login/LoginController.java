/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketbooking.ui.login;

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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ticketbooking.alert.AlertMaker;
import ticketbooking.models.User;
import ticketbooking.ui.mainui.user.UserdashController;
import ticketbooking.util.database.DatabaseHandler;

/**
 * FXML Controller class
 *
 * @author ArKadius
 */
public class LoginController implements Initializable {

    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXButton login;
    @FXML
    private JFXButton register;
    @FXML
    private JFXButton signin;
    @FXML
    private JFXButton exit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
            stage.initOwner(((Stage) username.getScene().getWindow()));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (IOException ex) {
            AlertMaker.showErrorMessage(ex);
        }
    }
    public boolean ValidateUsernamePassword() throws SQLException, NoSuchAlgorithmException
    {
        boolean valid=false;
        ResultSet Myres=DatabaseHandler.getUserNamePassword_users();
        while(Myres.next())
        {
            if(Myres.getString("username").equals(username.getText()))
                if(Myres.getString("password").equals(User.Sha1(password.getText())))
                    valid=true;
                    
        }
        return valid;
    }
    @FXML
    private void LoginMember(ActionEvent event) {
        try {
            if(ValidateUsernamePassword())
            {
                UserdashController.username=username.getText();
                closeStage();
                loadWindow("/ticketbooking/ui/mainui/user/UserDashBoard.fxml","Aurora");
            }
            else
            {
                AlertMaker.showNotification("Login Failed!","Your Username or Password is incorrect", AlertMaker.image_cross);
            }
        } catch (SQLException | NoSuchAlgorithmException ex) {
            AlertMaker.showWarning(ex);
        }
    }

    @FXML
    private void RegisterMember(ActionEvent event) {
        loadWindow("/ticketbooking/ui/login/register/register.fxml","Aurora");
    }

    @FXML
    private void SignInAdmin(ActionEvent event) {
        closeStage();
        loadWindow("/ticketbooking/ui/login/admin/adminlogin.fxml","Aurora");
    }

    @FXML
    private void ExittoSystem(ActionEvent event) {
        System.exit(0);
    }
    
}
