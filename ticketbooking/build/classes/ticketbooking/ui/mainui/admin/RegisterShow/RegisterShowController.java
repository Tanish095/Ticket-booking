/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketbooking.ui.mainui.admin.RegisterShow;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
import ticketbooking.util.database.DatabaseHandler;

/**
 * FXML Controller class
 *
 * @author ArKadius
 */
public class RegisterShowController implements Initializable {
    boolean flag1;
    boolean flag2;

    @FXML
    private JFXTextField movieid;
    @FXML
    private JFXTextField theatreid;
    @FXML
    private JFXButton ms;
    @FXML
    private JFXButton ts;
    @FXML
    private JFXDatePicker start;
    @FXML
    private JFXDatePicker end;
    @FXML
    private JFXTimePicker time;
    @FXML
    private JFXButton register;
    @FXML
    private JFXButton back;
    @FXML
    private JFXTextArea moviename;
    @FXML
    private JFXTextArea theatrename;
    @FXML
    private JFXTextField platinum;
    @FXML
    private JFXTextField gold;
    @FXML
    private JFXTextField silver;
    @FXML
    private JFXTextField hall;
    private void closeStage() {
        ((Stage) register.getScene().getWindow()).close();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void movieSearch(ActionEvent event) {
        try{
        String sql="Select id,name from movie where id="+movieid.getText();
        ResultSet rs=DatabaseHandler.executeQuery(sql);
        rs.next();
        moviename.setText(rs.getString("name"));
        rs.close();
        DatabaseHandler.disconnect();
        flag1=true;
        }catch(SQLException|NullPointerException ex)
        {
            flag1=false;
            moviename.setText("");
            AlertMaker.showNotification("ID Error","Check the movie ID",AlertMaker.image_warning2);
        }
    }

    @FXML
    private void theatreSearch(ActionEvent event){
        if(theatreid.getText()!=null)
        {
        try{
        String sql="Select id,name,hall from theatre where id="+theatreid.getText();
        ResultSet rs=DatabaseHandler.executeQuery(sql);
        rs.next();
        theatrename.setText(rs.getString("name"));
        hall.setText(rs.getString("hall"));
        rs.close();
        DatabaseHandler.disconnect();
        flag2=true;
        }catch(NullPointerException | SQLException ex)
        {
            flag2=false;
            theatrename.setText("");
            hall.setText("");
            AlertMaker.showNotification("ID Error","Check the Theatre ID",AlertMaker.image_warning2);
        }
        }
    }
    private void insertShow()
    {
        int i=0,j=0;
        LocalDate enddate=end.getValue().plusDays(1);
        try
        {
            DatabaseHandler.connect();
            for (LocalDate date = start.getValue(); date.isBefore(enddate); date = date.plusDays(1))
            {
                i++;
                String sql="insert into show values("+
                movieid.getText()+","+
                theatreid.getText()+",'"+
                date.toString()+"','"+
                time.getValue().toString()+"','"+
                moviename.getText()+"','"+
                theatrename.getText()+"',"+
                hall.getText()+","+
                platinum.getText()+","+
                gold.getText()+","+
                silver.getText()+")";
                if(!DatabaseHandler.insert_show(sql))
                {
                    AlertMaker.showNotification("Sorry","This time slot is not available:\n"+date+"\t"+time.getValue().toString()+"\nAnother movie is already running.",AlertMaker.image_warning);
                    j++;
                }
            }
        DatabaseHandler.disconnect();
        if(j==0)
            AlertMaker.showNotification("Successful","The Show has been created",AlertMaker.image_movie_popcorn);
        else if(i==j)
            AlertMaker.showNotification("Error","None of your entries checks out. \nMovie/s is/are scheduled torun. ",AlertMaker.image_cross);
         else
            AlertMaker.showNotification("Sorry","It seems some date and timeslots are reserved for other movies. ",AlertMaker.image_warning);
            
            
        }
        catch(NullPointerException ex)
        {
            AlertMaker.showErrorMessage(ex);
        }
    }
    @FXML
    private void RegisterShow(ActionEvent event) {
        try{
        if(flag1&&flag2&&start.getValue().isBefore(end.getValue()))
        {
            insertShow();
        }
        else
        {
            AlertMaker.showNotification("Warning!","You need to Provide correct data",AlertMaker.image_warning2);
        }
        }
        catch(Exception ex)
        {
            AlertMaker.showWarning(ex);
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        closeStage();
    }
    
}
