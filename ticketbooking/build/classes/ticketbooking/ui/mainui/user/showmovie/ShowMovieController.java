/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketbooking.ui.mainui.user.showmovie;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ticketbooking.alert.AlertMaker;
import ticketbooking.util.database.DatabaseHandler;

/**
 * FXML Controller class
 *
 * @author ArKadius
 */
public class ShowMovieController implements Initializable {
    public static int id;
    @FXML
    private ImageView poster;
    @FXML
    private Label name;
    @FXML
    private Label rating;
    @FXML
    private TextArea details;
    @FXML
    private TextArea cast;
    @FXML
    private TextArea director;
    @FXML
    private JFXButton back;

    public void InitializeMovie() throws Exception
    {
        ResultSet rs=DatabaseHandler.getMovie(id);
        System.out.println("flag");
        rs.next();
        name.setText(rs.getString("name"));
        int rat=(int) (rs.getFloat("rating")*10);
        rating.setText(String.valueOf(rat)+" %");
        details.setText(rs.getString("details"));
        director.setText(rs.getString("director"));
        cast.setText(rs.getString("cast"));
        File filei = new File("poster.jpg");
        FileOutputStream fos = new FileOutputStream(filei);
        InputStream input = rs.getBinaryStream("poster");
        rs.close();
        DatabaseHandler.disconnect();
        byte[] buffer = new byte[1024];
        while (input.read(buffer) > 0)
        {
            fos.write(buffer);
        }
        fos.close();
        Image img=new Image(filei.toURI().toString());
        poster.setImage(img);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try{
        InitializeMovie();
        }catch(Exception ex){
            AlertMaker.showWarning(ex);
        }
    }    
private void closeStage() {
        ((Stage) details.getScene().getWindow()).close();
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
    private void goBack(ActionEvent event) {
        closeStage();
    }
    
}
