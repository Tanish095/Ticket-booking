/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketbooking.ui.mainui.user.bookshow;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
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
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import ticketbooking.alert.AlertMaker;
import ticketbooking.ui.mainui.user.UserdashController;
import ticketbooking.util.database.DatabaseHandler;

/**
 * FXML Controller class
 *
 * @author ArKadius
 */
public class BookShowController implements Initializable {
    public static int movieid;
    public static int theatreid;
    public static String showtime;
    public static  String date;
    @FXML
    private JFXTextField tickets;
    @FXML
    private JFXRadioButton Platinum;
    @FXML
    private ToggleGroup seat;
    @FXML
    private JFXRadioButton Gold;
    @FXML
    private JFXRadioButton Silver;
    @FXML
    private Label moviename;
    @FXML
    private Label theatrename;
    @FXML
    private Label platinum_seat;
    @FXML
    private Label plat_price;
    @FXML
    private Label gold_seat;
    @FXML
    private Label gold_price;
    @FXML
    private Label silver_seat;
    @FXML
    private Label sliver_price;
    @FXML
    private Label time;
    @FXML
    private Label hall;
    @FXML
    private JFXButton purchase;
    @FXML
    private JFXButton back;

    private void closeStage() {
        ((Stage) back.getScene().getWindow()).close();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        time.setText(showtime);
        String sql="SELECT s.theatrename,s.moviename,s.hall,s.platinumseat,s.goldseat,s.silverseat,"
                + "t.platinum,t.gold,t.silver from theatre as t,"+
                "show as s where s.theatreid=? and s.movieid=? and s.time=? and s.theatreid=t.id and s.date=?";
        ResultSet rs=DatabaseHandler.getTicketDetails(sql,String.valueOf(movieid),String.valueOf(theatreid), showtime,date);
        try {
            rs.next();
            moviename.setText(rs.getString("moviename"));
            theatrename.setText(rs.getString("theatrename"));
            hall.setText(rs.getString("hall"));
            platinum_seat.setText(rs.getString("platinumseat"));
            gold_seat.setText(rs.getString("goldseat"));
            silver_seat.setText(rs.getString("silverseat"));
            plat_price.setText(rs.getString("platinum"));
            gold_price.setText(rs.getString("gold"));
            sliver_price.setText(rs.getString("silver"));
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(BookShowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    void create_new_order(String seats,int price)
    {
        int total=price*Integer.parseInt(tickets.getText());
        String sql1="update show set "+seats+"seat="+seats+"seat-"+tickets.getText()+
                " where movieid="+movieid+" and theatreid="+theatreid+" and "+
                "time='"+showtime+"' and date='"+date+"'";
        String sql2="INSERT INTO [order] (username,movieid,theatreid,date,time,seats,"
                + "category,total,ordertime)VALUES ('"
                + UserdashController.username+"',"
                + movieid+ ","
                + theatreid+",'"
                + date+"','"
                + showtime+ "',"
                + tickets.getText()+ ",'"
                + seats+ "',"
                + total+ ",datetime('now'))";
        if(DatabaseHandler.order_transaction(sql1, sql2))
        {
            AlertMaker.showNotification("Purchase Successful","Thankyou for using Aurora,Your seats are booked.",AlertMaker.image_movie_tickets);
            int val;
            switch(seats)
            {
                case "platinum":
                    val=Integer.parseInt(platinum_seat.getText())-Integer.parseInt(tickets.getText());
                    platinum_seat.setText(String.valueOf(val));
                break;
                case "gold":
                    val=Integer.parseInt(gold_seat.getText())-Integer.parseInt(tickets.getText());
                    gold_seat.setText(String.valueOf(val));
                break;
                case "silver":
                    val=Integer.parseInt(silver_seat.getText())-Integer.parseInt(tickets.getText());
                    silver_seat.setText(String.valueOf(val));
                break;
            }                
        }
        else
            AlertMaker.showNotification("Transaction Failed","Please try again",AlertMaker.image_warning);
    }

    @FXML
    private void purchaseticket(ActionEvent event) {
        String seattype;
        int price;
        boolean flag=true;
        try{

        if(seat.getSelectedToggle()==Platinum)
        {
            seattype="platinum";
            price=Integer.parseInt(plat_price.getText());
            if(Integer.parseInt(platinum_seat.getText())-Integer.parseInt(tickets.getText())<0)
                flag=false;
        }  
        else if(seat.getSelectedToggle()==Gold)
        {
            seattype="gold";
            price=Integer.parseInt(gold_price.getText());
            if(Integer.parseInt(gold_seat.getText())-Integer.parseInt(tickets.getText())<0)
                flag=false;
        }
        else
        {
            seattype="silver";
            price=Integer.parseInt(sliver_price.getText());
            if(Integer.parseInt(silver_seat.getText())-Integer.parseInt(tickets.getText())<0)
                flag=false;
        }
        if(flag&&(Integer.parseInt(tickets.getText())>0))
            create_new_order(seattype,price);
        else
            AlertMaker.showNotification("Wrong Input","Please check seat availability or ticket input",AlertMaker.image_movie_forbidden);
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            ex.getStackTrace();
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        closeStage();
    }
    
}
