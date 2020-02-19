/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketbooking.ui.mainui.admin;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author bose4
 */
public class HomeController implements Initializable {

    @FXML
    private ImageView gear1;
    @FXML
    private ImageView gear2;
    @FXML
    private ImageView gear3;
    void rotategears()
    {
        RotateTransition rg1=new RotateTransition(Duration.seconds(5),gear1);
        rg1.setFromAngle(0);
        rg1.setToAngle(360);
        RotateTransition rg2=new RotateTransition(Duration.seconds(5),gear2);
        rg2.setFromAngle(360);
        rg2.setToAngle(0);
        RotateTransition rg3=new RotateTransition(Duration.seconds(5),gear3);
        rg3.setFromAngle(0);
        rg3.setToAngle(360);
        ParallelTransition pt=new ParallelTransition(rg1,rg2,rg3);
        pt.setCycleCount(ParallelTransition.INDEFINITE);
        pt.play();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rotategears();
    }    
    
}
