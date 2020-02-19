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
public class MoviesController implements Initializable {
    ObservableList<Movie> list = FXCollections.observableArrayList();
    @FXML
    private TableView<Movie> table;
    @FXML
    private TableColumn<Movie, Integer> idCol;
    @FXML
    private TableColumn<Movie, String> nameCol;
    @FXML
    private TableColumn<Movie, String> directorCol;
    @FXML
    private TableColumn<Movie, String> castCol;
    @FXML
    private TableColumn<Movie, String> detailsCol;
    @FXML
    private TableColumn<Movie, Float> ratingCol;
    @FXML
    private JFXButton refresh;
    @FXML
    private JFXButton but_movie;
    @FXML
    private JFXButton but_remove;
    @FXML
    private JFXButton mov;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCols();
        loadtabledata();
    }
    private void initCols() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        castCol.setCellValueFactory(new PropertyValueFactory<>("cast"));
        directorCol.setCellValueFactory(new PropertyValueFactory<>("director"));
        detailsCol.setCellValueFactory(new PropertyValueFactory<>("details"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
    }
    public void loadtabledata()
    {
        String sql = "SELECT * FROM movie";
        ResultSet rs = DatabaseHandler.executeQuery(sql);
        try {
            while (rs.next()) {
                int id =rs.getInt("id");
                String name = rs.getString("name");
                String cast = rs.getString("cast");
                String director = rs.getString("director");
                String details = rs.getString("details");
                float rating= rs.getFloat("rating");
                list.add(new Movie(id,name,director,cast,details,rating));
               
            }
        } catch (SQLException ex) {
            AlertMaker.showWarning(ex);
        }
        table.getItems().setAll(list);
    }
    private void refresh()
    {
        table.getItems().clear();
        list.removeAll(list);
        loadtabledata();
    }
    @FXML
    private void refreshTable(ActionEvent event) {
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
    private void AddMovie(ActionEvent event) {
        loadWindow("/ticketbooking/ui/mainui/admin/addmovie/addMovie.fxml","Aurora");
    }

    @FXML
    private void removeMovie(ActionEvent event) {
        try{
            Movie movie= table.getSelectionModel().getSelectedItem();
            String sql="delete from movie where id="+movie.getId();
            if(DatabaseHandler.insert_record(sql)>0)
            {
                AlertMaker.showNotification("Successful", "Movie Deleted",AlertMaker.image_movie_frame);
                refresh();
            }
            else
                AlertMaker.showNotification("Error","Movie is already scheduled to run", AlertMaker.image_cross);
        }
        catch(Exception ex)
        {
            AlertMaker.showNotification("Error","Not Selected movie", AlertMaker.image_cross);
        }
    }

    @FXML
    private void movdetails(ActionEvent event) {
        try{
            Movie movie= table.getSelectionModel().getSelectedItem();
            ticketbooking.ui.mainui.user.showmovie.ShowMovieController.id=movie.getId();
           loadWindow("/ticketbooking/ui/mainui/user/showmovie/showMovie.fxml","Aurora");
        }
        catch(Exception ex)
        {
            AlertMaker.showNotification("Error","Not Selected movie", AlertMaker.image_cross);
        }
    }
    public static class Movie {

        private final SimpleIntegerProperty id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty director;
        private final SimpleStringProperty cast;
        private final SimpleStringProperty details;
        private final SimpleFloatProperty rating;

        public Movie(int id, String name, String director, String cast, String details, float rating) {
            this.id =new SimpleIntegerProperty(id);
            this.name =new SimpleStringProperty(name);
            this.director =new SimpleStringProperty(director);
            this.cast = new SimpleStringProperty(cast);
            this.details = new SimpleStringProperty(details);
            this.rating = new SimpleFloatProperty(rating);
        }

    public int getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public String getDirector() {
        return director.get();
    }

    public String getCast() {
        return cast.get();
    }

    public String getDetails() {
        return details.get();
    }

    public float getRating() {
        return rating.get();
    }
    }
}
