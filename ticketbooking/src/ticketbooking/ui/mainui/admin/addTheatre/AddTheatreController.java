/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketbooking.ui.mainui.admin.addTheatre;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
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
import ticketbooking.util.database.DatabaseHandler;

/**
 * FXML Controller class
 *
 * @author ArKadius
 */
public class AddTheatreController implements Initializable {

    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField name;
    @FXML
    private JFXButton submit;
    @FXML
    private JFXButton back;
    @FXML
    private JFXTextField halls;
    @FXML
    private JFXTextField platinum;
    @FXML
    private JFXTextField gold;
    @FXML
    private JFXTextField silver;
    @FXML
    private JFXTextArea address;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    private void closeStage() {
        ((Stage) id.getScene().getWindow()).close();
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
    private void SubmitDetails(ActionEvent event) {
        String sql="insert into theatre values("+
                id.getText()+",'"+
                name.getText()+"','"+
                address.getText()+"',"+
                halls.getText()+","+
                platinum.getText()+","+
                gold.getText()+","+
                silver.getText()+")";
        int val=DatabaseHandler.insert_record(sql);
        if(val!=0)
            AlertMaker.showNotification("Success","Theatre Details Successfully Registered",AlertMaker.image_movie_3dglasses2);
        else
            AlertMaker.showNotification("Failed","There is a problem with your input,Try Again.",AlertMaker.image_cross);
    }

    @FXML
    private void goBack(ActionEvent event) {
        closeStage();
    }
    
}
