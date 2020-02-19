/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketbooking;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ticketbooking.ui.mainui.user.showmovie.ShowMovieController;
import ticketbooking.util.database.DatabaseHandler;
/**
 *
 * @author ArKadius
 */
public class Ticketbooking extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/ticketbooking/ui/login/login.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Aurora");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        new Thread(()->{
            DatabaseHandler.create_all_tables();
        }).start();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
