/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketbooking.ui.mainui.admin;

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
import ticketbooking.util.database.DatabaseHandler;

/**
 * FXML Controller class
 *
 * @author ArKadius
 */
public class ShowController implements Initializable {
    ObservableList<Show> list = FXCollections.observableArrayList();
    @FXML
    private TableView<Show> table;
    @FXML
    private TableColumn<Show, Integer> movieidCol;
    @FXML
    private TableColumn<Show, Integer> theatreidCol;
    @FXML
    private TableColumn<Show,String> dateCol;
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
    private JFXButton but_show;
    @FXML
    private JFXButton but_remove;
    @FXML
    private JFXDatePicker datefilter;
    public String where="";
     private void initCols() {
        movieidCol.setCellValueFactory(new PropertyValueFactory<>("movieid"));
        theatreidCol.setCellValueFactory(new PropertyValueFactory<>("theatreid"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        movienameCol.setCellValueFactory(new PropertyValueFactory<>("moviename"));
        theatrenameCol.setCellValueFactory(new PropertyValueFactory<>("theatrename"));
        hallcol.setCellValueFactory(new PropertyValueFactory<>("hall"));
        platinumcol.setCellValueFactory(new PropertyValueFactory<>("platinum"));
        goldcol.setCellValueFactory(new PropertyValueFactory<>("gold"));
        silvercol.setCellValueFactory(new PropertyValueFactory<>("silver"));
    }
     public void loadtabledata()
    {
        String sql = "SELECT * FROM show"+where;
        ResultSet rs = DatabaseHandler.executeQuery(sql);
        try {
            while (rs.next()) {
                int movieid =rs.getInt("movieid");
                int theatreid =rs.getInt("theatreid");
                String date1 = rs.getString("date");
                String time =rs.getString("time");
                String moviename = rs.getString("moviename");
                String theatrename = rs.getString("theatrename");
                int hall =rs.getInt("hall");
                int platinum =rs.getInt("platinumseat");
                int gold =rs.getInt("goldseat");
                int silver =rs.getInt("silverseat");
                list.add(new Show(movieid,theatreid,date1,time,hall,moviename,theatrename,platinum,gold,silver));               
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
    private void addShow(ActionEvent event) {
        loadWindow("/ticketbooking/ui/mainui/admin/RegisterShow/RegisterShow.fxml","Aurora");
    }
    @FXML
    private void removeShow(ActionEvent event) {
        try{
            Show show= table.getSelectionModel().getSelectedItem();
            String sql="delete from show where movieid="+show.getMovieid()+" and theatreid="+show.getTheatreid()+
                    " and date='"+show.getDate()+"' and time='"+show.getTime()+"'";
            if(DatabaseHandler.insert_record(sql)>0)
            {
                AlertMaker.showNotification("Successful", "Show Cancelled",AlertMaker.image_movie_popcorn);
                refresh();
            }
            else
                AlertMaker.showNotification("Error","Tickets have already been bought", AlertMaker.image_cross);
        }
        catch(Exception ex)
        {
            AlertMaker.showNotification("Error","Not Selected Show", AlertMaker.image_cross);
        }
    }

    @FXML
    private void datefilterquery(ActionEvent event) {
        where=" where date='"+datefilter.getValue()+"'";
        refresh();
    }

    public static class Show {

        private final SimpleIntegerProperty movieid;
        private final SimpleIntegerProperty theatreid;
        private final SimpleStringProperty date;
        private final SimpleStringProperty time;
        private final SimpleIntegerProperty hall;
        private final SimpleStringProperty moviename;
        private final SimpleStringProperty theatrename;
        private final SimpleIntegerProperty platinum;
        private final SimpleIntegerProperty gold;
        private final SimpleIntegerProperty silver;

        public Show(int movieid, int theatreid, String date, String time, int hall, String moviename, String theatrename, int platinum, int gold, int silver){ 
            this.movieid = new SimpleIntegerProperty(movieid);
            this.theatreid = new SimpleIntegerProperty(theatreid);
            this.date = new SimpleStringProperty(date);
            this.time = new SimpleStringProperty(time);
            this.hall = new SimpleIntegerProperty(hall);
            this.moviename = new SimpleStringProperty(moviename);
            this.theatrename = new SimpleStringProperty(theatrename);
            this.platinum = new SimpleIntegerProperty(platinum);
            this.gold = new SimpleIntegerProperty(gold);
            this.silver =new SimpleIntegerProperty(silver);
        }

        public int getMovieid() {
            return movieid.get();
        }

        public int getTheatreid() {
            return theatreid.get();
        }
        public String getDate() {
            return date.get();
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
    }
}
