/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketbooking.ui.mainui.user;

import ticketbooking.ui.mainui.admin.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import ticketbooking.alert.AlertMaker;
import ticketbooking.models.Admin;
import ticketbooking.util.database.DatabaseHandler;

/**
 * FXML Controller class
 *
 * @author bose4
 */
public class SettingsController implements Initializable {

    @FXML
    private JFXPasswordField current_password;
    @FXML
    private JFXPasswordField new_password;
    @FXML
    private JFXButton btnu;
    String password;
    private static final String  PASSWORD_PATTERN="(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}"; 
    private static final String PASSWORD_NORMS="Password must contain :\nA Digit\nA Lower Case Letter\n"
            + "An Upper Case Letter\nA Special Character\nNo White Space\nAt least 8 characters";
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    public boolean validation1()
    {
        String cur_pass="";
        try {
            cur_pass=Admin.Sha1(current_password.getText());
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
        }
        if(password.equals(cur_pass))
            return true;
        return false;
    }
    public boolean validation2()
    {
                return Pattern.matches(PASSWORD_PATTERN,new_password.getText());
    }
    @FXML
    private void UpdatePassword(ActionEvent event) {
        try {
            String sql="select * from users where username='"+UserdashController.username+"'";
            ResultSet rs=DatabaseHandler.executeQuery(sql);
            rs.next();
            password=rs.getString("password");
            rs.close();
        } catch (SQLException ex) {
            ex.getMessage();
        }
        DatabaseHandler.disconnect();
        String hashedpass="";
        try {
                    hashedpass=Admin.Sha1(new_password.getText());
                    } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
        }
        String sql="update users set password='"+hashedpass+"' where username='"+UserdashController.username+"'";
        if(!validation1())
            AlertMaker.showNotification("Failed", "Please Check your current password", AlertMaker.image_warning);
        else if(!validation2())
            AlertMaker.showNotification("Failed", "New password has to follow norms.", AlertMaker.image_warning);
        else if(DatabaseHandler.insert_record(sql)!=0)
        {
            new_password.setText("");
            current_password.setText("");
            AlertMaker.showNotification("Success", "Your password has been updated.", AlertMaker.image_password_key);
        }
        else
            AlertMaker.showNotification("Failed", "Sorry it is our problem.We'll solve the issue.",AlertMaker.image_cross);
    }
    
}
