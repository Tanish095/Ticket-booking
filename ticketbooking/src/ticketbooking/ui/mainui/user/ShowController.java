/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketbooking.ui.mainui.user;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ticketbooking.alert.AlertMaker;
import ticketbooking.ui.mainui.user.bookshow.BookShowController;
import ticketbooking.ui.mainui.user.showmovie.ShowMovieController;
import ticketbooking.util.database.DatabaseHandler;

/**
 * FXML Controller class
 *
 * @author ArKadius
 */
public class ShowController implements Initializable {
    String where="";
    ObservableList<Show> list = FXCollections.observableArrayList();
    @FXML
    private TableView<Show> table;
    @FXML
    private TableColumn<Show, Integer> movieidCol;
    @FXML
    private TableColumn<Show, Integer> theatreidCol;
    @FXML
    private TableColumn<Show, String> timeCol;
    @FXML
    private TableColumn<Show, String> movienameCol;
    @FXML
    private TableColumn<Show, String> theatrenameCol;
    @FXML
    private TableColumn<Show, Integer> hallcol;
    @FXML
    private TableColumn<Show, Integer> platinumcol;
    @FXML
    private TableColumn<Show, Integer> goldcol;
    @FXML
    private TableColumn<Show, Integer> silvercol;
    @FXML
    private JFXButton refresh;
    @FXML
    private JFXButton details;
    @FXML
    private JFXButton book;
    @FXML
    private TableColumn<Show, String> date;
    @FXML
    private JFXDatePicker datefilter;

     private void initCols() {
        movieidCol.setCellValueFactory(new PropertyValueFactory<>("movieid"));
        theatreidCol.setCellValueFactory(new PropertyValueFactory<>("theatreid"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        movienameCol.setCellValueFactory(new PropertyValueFactory<>("moviename"));
        theatrenameCol.setCellValueFactory(new PropertyValueFactory<>("theatrename"));
        hallcol.setCellValueFactory(new PropertyValueFactory<>("hall"));
        platinumcol.setCellValueFactory(new PropertyValueFactory<>("platinum"));
        goldcol.setCellValueFactory(new PropertyValueFactory<>("gold"));
        silvercol.setCellValueFactory(new PropertyValueFactory<>("silver"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
    }
     public void loadtabledata()
    {
        String sql = "select * from show where platinumseat+goldseat+silverseat>0"+where;
        ResultSet rs = DatabaseHandler.executeQuery(sql);
        try {
            while (rs.next()) {
                int movieid =rs.getInt("movieid");
                int theatreid =rs.getInt("theatreid");
                String time =rs.getString("time");
                String moviename = rs.getString("moviename");
                String theatrename = rs.getString("theatrename");
                int hall =rs.getInt("hall");
                int platinum =rs.getInt("platinumseat");
                int gold =rs.getInt("goldseat");
                int silver =rs.getInt("silverseat");
                String date1 = rs.getString("date");
                list.add(new Show(movieid,theatreid,time,hall,moviename,theatrename,platinum,gold,silver,date1));               
            }
            rs.close();
        } catch (SQLException ex) {
            
            AlertMaker.showWarning(ex);
        }
        table.getItems().setAll(list);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCols();
        loadtabledata();
    }    
    public void refresh()
    {
        
        table.getItems().clear();
        list.removeAll(list);
        loadtabledata();
    }
    @FXML
    private void refreshtable(ActionEvent event) {
        where="";
        refresh();
    }
    void loadWindow(String loc, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.initOwner(((Stage) refresh.getScene().getWindow()));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (IOException ex) {
            AlertMaker.showErrorMessage(ex);
        }
    }
    @FXML
    private void showDetails(ActionEvent event) {
        try{
        Show show= table.getSelectionModel().getSelectedItem();
        ShowMovieController.id=show.getMovieid();
        loadWindow("/ticketbooking/ui/mainui/user/showmovie/showMovie.fxml","Aurora");
        }
        catch(Exception ex)
        {
            AlertMaker.showNotification("Sorry","You have not Selected the movie",AlertMaker.image_cross);
        }
    }

    @FXML
    private void BookShow(ActionEvent event) {
        try{
        Show show= table.getSelectionModel().getSelectedItem();
        BookShowController.movieid=show.getMovieid();
        BookShowController.theatreid=show.getTheatreid();
        BookShowController.showtime=show.getTime();
        BookShowController.date=show.getDate();
        loadWindow("/ticketbooking/ui/mainui/user/bookshow/bookShow.fxml","Aurora");
        }
        catch(Exception ex)
        {
            AlertMaker.showNotification("Sorry","You have not Selected the movie",AlertMaker.image_cross);
        }
    }

    @FXML
    private void datefilterquery(ActionEvent event) {
        where=" and date='"+datefilter.getValue()+"'";
        refresh();
    }
    public static class Show {

        private final SimpleIntegerProperty movieid;
        private final SimpleIntegerProperty theatreid;
        private final SimpleStringProperty time;
        private final SimpleIntegerProperty hall;
        private final SimpleStringProperty moviename;
        private final SimpleStringProperty theatrename;
        private final SimpleIntegerProperty platinum;
        private final SimpleIntegerProperty gold;
        private final SimpleIntegerProperty silver;
        private final SimpleStringProperty date;

        public Show(int movieid, int theatreid, String time, int hall, String moviename, String theatrename, int platinum, int gold, int silver, String date){ 
            this.movieid = new SimpleIntegerProperty(movieid);
            this.theatreid = new SimpleIntegerProperty(theatreid);
            this.time = new SimpleStringProperty(time);
            this.hall = new SimpleIntegerProperty(hall);
            this.moviename = new SimpleStringProperty(moviename);
            this.theatrename = new SimpleStringProperty(theatrename);
            this.platinum = new SimpleIntegerProperty(platinum);
            this.gold = new SimpleIntegerProperty(gold);
            this.silver =new SimpleIntegerProperty(silver);
            this.date = new SimpleStringProperty(date);
        }

        public int getMovieid() {
            return movieid.get();
        }

        public int getTheatreid() {
            return theatreid.get();
        }

        public String getTime() {
            return time.get();
        }

        public int getHall() {
            return hall.get();
        }

        public String getMoviename() {
            return moviename.get();
        }

        public String getTheatrename() {
            return theatrename.get();
        }

        public int getPlatinum() {
            return platinum.get();
        }

        public int getGold() {
            return gold.get();
        }

        public int getSilver() {
            return silver.get();
        }

        public String getDate() {
            return date.get();
        }
    }
}
