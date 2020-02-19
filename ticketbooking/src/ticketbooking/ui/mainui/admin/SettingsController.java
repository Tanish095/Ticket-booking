/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketbooking.ui.mainui.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    @FXML
    private void UpdatePassword(ActionEvent event) {
        try {
            String sql="select * from admin where username='"+AdmindashController.username+"'";
            ResultSet rs=DatabaseHandler.executeQuery(sql);
            rs.next();
            password=rs.getString("password");
            rs.close();
            DatabaseHandler.disconnect();
        } catch (SQLException ex) {
            ex.getMessage();
        }
        String hashedpass="";
        try {
                    hashedpass=Admin.Sha1(new_password.getText());
                    } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
        }
        String sql="update admin set password='"+hashedpass+"' where username='"+AdmindashController.username+"'";
        if(!validation1())
            AlertMaker.showNotification("Failed", "Please Check your current password", AlertMaker.image_warning);
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
