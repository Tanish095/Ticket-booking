/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketbooking.ui.mainui.user;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import ticketbooking.util.database.DatabaseHandler;

/**
 * FXML Controller class
 *
 * @author bose4
 */
public class ProfileController implements Initializable {

    @FXML
    private Label name;
    @FXML
    private Label email;
    @FXML
    private Label phone;
    @FXML
    private Label count;
    @FXML
    private JFXButton refresh;

    /**
     * Initializes the controller class.
     */
    void refresh()
    {
        try {
            String sql1="select * from users";
            String sql2="select count(*) as rows from [order] where username='"+UserdashController.username+"'";
            ResultSet rs1=DatabaseHandler.executeQuery(sql2);
            rs1.next();
            count.setText(String.valueOf(rs1.getInt("rows")));
            rs1.close();
            ResultSet rs2=DatabaseHandler.executeQuery(sql1);
            rs2.next();
            email.setText(rs2.getString("email"));
            phone.setText(rs2.getString("phone"));
            name.setText(rs2.getString("fullname"));
            rs2.close();
            DatabaseHandler.disconnect();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refresh();
    }    

    @FXML
    private void sync(ActionEvent event) {
        refresh();
    }
}
