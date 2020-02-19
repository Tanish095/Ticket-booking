package ticketbooking.ui.login.register;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import ticketbooking.alert.AlertMaker;
import ticketbooking.models.User;
import ticketbooking.util.database.DatabaseHandler;
public class RegisterController implements Initializable {
    private static final String  EMAIL_PATTERN=
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String  PASSWORD_PATTERN="(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
    private static final String PASSWORD_NORMS="Password must contain :\nA Digit\nA Lower Case Letter\n"
            + "An Upper Case Letter\nA Special Character\nNo White Space\nAt least 8 characters";
    @FXML
    private JFXTextField username;
    @FXML
    private JFXTextField fullname;
    @FXML
    private JFXTextField email;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXTextField phone;
    @FXML
    private JFXButton register;
    @FXML
    private JFXButton back;
    public void validators()
    {
        ValidationSupport validation=new ValidationSupport();
        validation.registerValidator(username, Validator.createEmptyValidator("Input Required", Severity.ERROR));
        ValidationSupport validation2=new ValidationSupport();
        validation2.registerValidator(fullname, Validator.createEmptyValidator("Input Required",Severity.ERROR));
        ValidationSupport validation3=new ValidationSupport();
        validation3.registerValidator(phone, Validator.createRegexValidator("Number must contain 10digits(Start with 9/8/7)","[789]{1}[0-9]{9}", Severity.ERROR));
        ValidationSupport validation4=new ValidationSupport();
        validation4.registerValidator(email, Validator.createRegexValidator("Provide correct Email",EMAIL_PATTERN, Severity.ERROR));
        ValidationSupport validation5=new ValidationSupport();
        validation5.registerValidator(password, Validator.createRegexValidator(PASSWORD_NORMS,PASSWORD_PATTERN, Severity.ERROR));
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        validators();
    }    
    private void closeStage() {
        ((Stage) username.getScene().getWindow()).close();
    }
    private User getdata()
    {
        User user=new User();
        user.setUsername(username.getText());
        user.setFullname(fullname.getText());
        try {
            user.setPassword(User.Sha1(password.getText()));
        } catch (NoSuchAlgorithmException ex) {
            AlertMaker.showWarning(ex);
        }
        user.setEmail(email.getText());
        user.setContact(phone.getText());
        return user;
    }
    private boolean validationcheck()
    {
        if("".equals(username.getText()))
            return false;
        if("".equals(fullname.getText()))
            return false;
        if(!Pattern.matches(EMAIL_PATTERN, email.getText()))
            return false;
        if(!Pattern.matches(PASSWORD_PATTERN,password.getText()))
            return false;
        if(!Pattern.matches("[789]{1}[0-9]{9}",phone.getText()))
            return false;
        return true;
    }
    @FXML
    private void RegisterUser(ActionEvent event) {
        if(validationcheck())
        {
           if(DatabaseHandler.insert_user(getdata())!=0)
                AlertMaker.showNotification("Success","You are now Registered.Please Login from the Main Menu",AlertMaker.image_checked);
           else 
               AlertMaker.showNotification("Fail","Username Exists",AlertMaker.image_movie_forbidden);
        }
        else
        {
            AlertMaker.showNotification("Improper Data", "Please enter all relevant data in proper manner",AlertMaker.image_warning);
        }
    }
    @FXML
    private void goBack(ActionEvent event) {
        closeStage();
    }
}
