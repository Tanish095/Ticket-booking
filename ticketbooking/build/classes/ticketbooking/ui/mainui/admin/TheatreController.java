/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketbooking.ui.mainui.admin;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleFloatProperty;
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
public class TheatreController implements Initializable {
    ObservableList<Theatre> list = FXCollections.observableArrayList();
    @FXML
    private TableView<Theatre> table;
    @FXML
    private TableColumn<Theatre, Integer> idCol;
    @FXML
    private TableColumn<Theatre, String> nameCol;
    @FXML
    private TableColumn<Theatre, Integer> hallCol;
    @FXML
    private TableColumn<Theatre, String> addressCol;
    @FXML
    private TableColumn<Theatre, Float> platinumCol;
    @FXML
    private TableColumn<Theatre, Float> goldCol;
    @FXML
    private TableColumn<Theatre, Float> silverCol;
    @FXML
    private JFXButton but_theatre;
    @FXML
    private JFXButton refresh;
    @FXML
    private JFXButton but_remove;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCols();
        loadtabledata();
    }
    private void initCols() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        hallCol.setCellValueFactory(new PropertyValueFactory<>("hall"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        platinumCol.setCellValueFactory(new PropertyValueFactory<>("platinum"));
        goldCol.setCellValueFactory(new PropertyValueFactory<>("gold"));
        silverCol.setCellValueFactory(new PropertyValueFactory<>("silver"));
    }
    public void loadtabledata()
    {
        String sql = "SELECT * FROM theatre";
        ResultSet rs = DatabaseHandler.executeQuery(sql);
        try {
            while (rs.next()) {
                int id =rs.getInt("id");
                String name = rs.getString("name");
                int hall =rs.getInt("hall");
                String address = rs.getString("address");
                float platinum= rs.getFloat("platinum");
                float gold= rs.getFloat("gold");
                float silver= rs.getFloat("silver");
                list.add(new Theatre(id,name,hall,address,platinum,gold,silver));
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        table.getItems().setAll(list);
    }
    void loadWindow(String loc, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.initOwner(((Stage) but_theatre.getScene().getWindow()));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (IOException ex) {
            AlertMaker.showErrorMessage(ex);
        }
    }
    @FXML
    private void addTheatre(ActionEvent event) {
        loadWindow("/ticketbooking/ui/mainui/admin/addTheatre/addTheatre.fxml","Aurora");
    }

    @FXML
    private void refreshTable(ActionEvent event) {
        refresh();
    }
      public void refresh()
      {
         table.getItems().clear();
          list.removeAll(list);
          loadtabledata();
      }
    @FXML
    private void removeTheatre(ActionEvent event) {
        try{
            Theatre theatre= table.getSelectionModel().getSelectedItem();
            String sql="delete from theatre where id="+theatre.getId();
            if(DatabaseHandler.insert_record(sql)>0)
            {
                AlertMaker.showNotification("Successful", "Theatre Unregistered",AlertMaker.image_movie_3dglasses2);
                refresh();
            }
            else
                AlertMaker.showNotification("Error","Movie is already scheduled tor run in Theatre", AlertMaker.image_cross);
        }
        catch(Exception ex)
        {
            AlertMaker.showNotification("Error","Not Selected Theatre", AlertMaker.image_cross);
        }
    }
    public static class Theatre {

        private final SimpleIntegerProperty id;
        private final SimpleStringProperty name;
        private final SimpleIntegerProperty hall;
        private final SimpleStringProperty address;
        private final SimpleFloatProperty platinum;
        private final SimpleFloatProperty gold;
        private final SimpleFloatProperty silver;

        public Theatre(int id, String name,int hall, String address, float platinum,float gold,float silver) {
            this.id =new SimpleIntegerProperty(id);
            this.name =new SimpleStringProperty(name);
            this.hall =new SimpleIntegerProperty(hall);
            this.address = new SimpleStringProperty(address);
            this.platinum = new SimpleFloatProperty(platinum);
            this.gold = new SimpleFloatProperty(gold);
            this.silver = new SimpleFloatProperty(silver);
        }

        public int getId() {
            return id.get();
        }

        public String getName() {
            return name.get();
        }

        public int getHall() {
            return hall.get();
        }

        public String getAddress() {
            return address.get();
        }
        public float getPlatinum() {
            return platinum.get();
        }
        public float getGold() {
            return gold.get();
        }

        public float getSilver() {
            return silver.get();
        }
    }
}
