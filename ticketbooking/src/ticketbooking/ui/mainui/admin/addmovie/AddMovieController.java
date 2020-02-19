/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketbooking.ui.mainui.admin.addmovie;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ticketbooking.alert.AlertMaker;
import ticketbooking.util.database.DatabaseHandler;

/**
 * FXML Controller class
 *
 * @author ArKadius
 */
public class AddMovieController implements Initializable {
    File file;

    @FXML
    private ImageView poster;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField name;
    @FXML
    private JFXTextField director;
    @FXML
    private JFXTextField cast;
    @FXML
    private JFXButton submit;
    @FXML
    private JFXButton back;
    @FXML
    private JFXTextArea details;
    @FXML
    private JFXTextField rating;
    @FXML
    private JFXButton getimg;
    private JFXTextField price;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void getposter(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
              
            //Set extension filter
            FileChooser.ExtensionFilter extFilterJPG = 
                    new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");
            FileChooser.ExtensionFilter extFilterjpg = 
                    new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
            FileChooser.ExtensionFilter extFilterPNG = 
                    new FileChooser.ExtensionFilter("PNG files (*.PNG)", "*.PNG");
            FileChooser.ExtensionFilter extFilterpng = 
                    new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
            fileChooser.getExtensionFilters()
                    .addAll(extFilterJPG, extFilterjpg, extFilterPNG, extFilterpng);
            try
            {
            file = fileChooser.showOpenDialog(((Stage) id.getScene().getWindow()));
            Image image = new Image(file.toURI().toString());
            poster.setImage(image);
            }catch(Exception ex)
            {
            }
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
        String sql = "INSERT INTO movie VALUES("+
                id.getText()+",'"+
                name.getText()+"','"+
                director.getText()+"','"+
                cast.getText()+"','"+
                details.getText()+"',"+
                rating.getText()+",?)";
        int val;
        val=DatabaseHandler.insert_movie(sql,file);
        if(val!=0)
                AlertMaker.showNotification("Success","Movie Successfully inserted in database",AlertMaker.image_movie_frame);
    }
    @FXML
    private void goBack(ActionEvent event) {
        closeStage();
    }
}