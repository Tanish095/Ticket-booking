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
import ticketbooking.util.database.DatabaseHandler;

/**
 * FXML Controller class
 *
 * @author bose4
 */
public class OrderHistoryController implements Initializable {
    public static String username=UserdashController.username;
    public static String where="";
    public static String where2=" and username='"+username+"'";
    ObservableList<Order> list = FXCollections.observableArrayList();
    @FXML
    private TableView<Order> table;
    @FXML
    private TableColumn<Order,Integer> orderidCol;
    @FXML
    private TableColumn<Order, String> date;
    @FXML
    private TableColumn<Order, String> timeCol;
    @FXML
    private TableColumn<Order, String> movienameCol;
    @FXML
    private TableColumn<Order, String> theatrenameCol;
    @FXML
    private TableColumn<Order,Integer> hallcol;
    @FXML
    private TableColumn<Order, Integer> seatsCols;
    @FXML
    private TableColumn<Order, String> categorycol;
    @FXML
    private TableColumn<Order, String> booktimeCol;
    @FXML
    private JFXButton refresh;
    @FXML
    private JFXButton tickets;
    @FXML
    private JFXDatePicker datefilter;
    @FXML
    private TableColumn<Order, Integer> totalCol;

     private void initCols() {
        orderidCol.setCellValueFactory(new PropertyValueFactory<>("orderid"));
        seatsCols.setCellValueFactory(new PropertyValueFactory<>("seats"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        movienameCol.setCellValueFactory(new PropertyValueFactory<>("moviename"));
        theatrenameCol.setCellValueFactory(new PropertyValueFactory<>("theatrename"));
        hallcol.setCellValueFactory(new PropertyValueFactory<>("hall"));
        categorycol.setCellValueFactory(new PropertyValueFactory<>("category"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        booktimeCol.setCellValueFactory(new PropertyValueFactory<>("ordertimestamp"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
    }
     public void loadtabledata()
    {
        String sql = "SELECT orderid,username,movieid,theatreid,date,time,seats,category,total,ordertime,"
                + "movie.name as moviename,theatre.name as theatrename,hall FROM [order],movie,"
                + "theatre where [order].movieid=movie.id and [order].theatreid=theatre.id"+where+where2;
        ResultSet rs = DatabaseHandler.executeQuery(sql);
        try {
            while (rs.next()) {
                int orderid =rs.getInt("orderid");
                int seats =rs.getInt("seats");
                String time =rs.getString("time");
                String moviename = rs.getString("moviename");
                String theatrename = rs.getString("theatrename");
                int hall =rs.getInt("hall");
                String category =rs.getString("category");
                String ordertimestamp =rs.getString("ordertime");
                int total =rs.getInt("total");
                String date1 = rs.getString("date");
                list.add(new Order(orderid,date1,time,moviename,theatrename,hall,seats,category,total,ordertimestamp));               
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
    private void GenerateTickets(ActionEvent event) {
        try{
        Order order= table.getSelectionModel().getSelectedItem();
       try{
        DatabaseHandler.get_ticket(order.getOrderid());
       }
       catch(Exception ex )
       {
           System.out.println(ex.getMessage());
       }
        }
        catch(Exception ex)
        {
            AlertMaker.showNotification("Sorry","You have not Selected a movie",AlertMaker.image_cross);
        }
    }

    @FXML
    private void datefilterquery(ActionEvent event) {
        where=" and date='"+datefilter.getValue()+"'";
        refresh();
    }
    public static class Order {

        private final SimpleIntegerProperty orderid;
        private final SimpleStringProperty time;
        private final SimpleIntegerProperty hall;
        private final SimpleStringProperty moviename;
        private final SimpleStringProperty theatrename;
        private final SimpleIntegerProperty seats;
        private final SimpleStringProperty category;
        private final SimpleStringProperty ordertimestamp;
        private final SimpleIntegerProperty total;
        private final SimpleStringProperty date;

        public Order(int orderid, String date,String time, String moviename, String theatrename, int hall,int seats,String category,int total,String ordertimestamp){ 
            this.orderid = new SimpleIntegerProperty(orderid);
            this.time = new SimpleStringProperty(time);
            this.hall = new SimpleIntegerProperty(hall);
            this.moviename = new SimpleStringProperty(moviename);
            this.theatrename = new SimpleStringProperty(theatrename);
            this.seats = new SimpleIntegerProperty(seats);
            this.category= new SimpleStringProperty(category);
            this.total =new SimpleIntegerProperty(total);
            this.date = new SimpleStringProperty(date);
            this.ordertimestamp = new SimpleStringProperty(ordertimestamp);
        }

        public int getOrderid() {
            return orderid.get();
        }

        public int getTotal() {
            return total.get();
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

        public int getSeats() {
            return seats.get();
        }

        public String getCategory() {
            return category.get();
        }

        public String getOrdertimestamp() {
            return ordertimestamp.get();
        }

        public String getDate() {
            return date.get();
        }
    }
}
