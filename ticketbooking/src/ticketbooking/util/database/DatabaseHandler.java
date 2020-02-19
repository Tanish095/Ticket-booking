package ticketbooking.util.database;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JRViewer;
import org.sqlite.SQLiteConfig;

import ticketbooking.alert.AlertMaker;
import ticketbooking.models.*;
public class DatabaseHandler {
    private static final String Jdbc_driver="org.sqlite.JDBC";
    private static final String Connection_string="jdbc:sqlite:Aurora.db";
    public static final String users_table="CREATE TABLE if not exists users(" +
    "username VARCHAR(20) PRIMARY KEY NOT NULL,"+
    "fullname varchar(40) NOT NULL,"+
    "email varchar(30) NOT NULL,"+
    "password varchar(60) NOT NULL,"+
    "phone INT(10) NOT NULL)";
    public static final String admin_table="CREATE TABLE if not exists admin(" +
    "username VARCHAR(20) PRIMARY KEY NOT NULL,"+
    "password VARCHAR(60) NOT NULL)";
    public static final String movie_table="CREATE TABLE if not exists movie("+
    "id INT(5) PRIMARY KEY NOT NULL,"+
    "name VARCHAR UNIQUE NOT NULL,"+
    "director varchar NOT NULL,"+
    "cast varchar NOT NULL,"+
    "details varchar NOT NULL,"+
    "rating DECIMAL NOT NULL,"+
    "poster BLOB NOT NULL)";
    public static final String theatre_table="CREATE TABLE if not exists theatre("+
    "id INT(5) PRIMARY KEY NOT NULL,"+
    "name VARCHAR NOT NULL,"+
    "address VARCHAR NOT NULL,"+
    "hall INT(1) NOT NULL,"+
    "platinum DECIMAL NOT NULL,"+
    "gold DECIMAL NOT NULL,"+
    "silver DECIMAL NOT NULL,"+
    "UNIQUE(name,hall))";
    public static final String show_table="CREATE TABLE if not exists show(" +
    "movieid INT(5)," +
    "theatreid INT(5)," +
    "date DATE," +
    "time TIME," +
    "moviename VARCHAR," +
    "theatrename VARCHAR," +
    "hall INT(1)," +
    "platinumseat INT," +
    "goldseat INT," +
    "silverseat INT," +
    "FOREIGN KEY (movieid)" +
    "REFERENCES movie (id)," +
    "FOREIGN KEY (theatreid)" +
    "REFERENCES theatre (id)," +
    "PRIMARY KEY (theatreid,date,time))";
    public static final String order_table="CREATE TABLE if not exists [order] "
            + "(orderid INTEGER PRIMARY KEY AUTOINCREMENT,username VARCHAR (20) REFERENCES users (username)"
            + ",movieid INT(5),theatreid INT(5),date DATE,time TIME,seats INT,category VARCHAR,total INT"
            + ",ordertime DATETIME,FOREIGN KEY (theatreid,date,time)REFERENCES show (theatreid,date,time));";
    static Connection con=null;
    static Statement stmt=null;
    //This function connects the App with the database
    public static void connect()
    {
        try {
            Class.forName(Jdbc_driver);
            SQLiteConfig config = new SQLiteConfig();  
            config.enforceForeignKeys(true);
           con=DriverManager.getConnection(Connection_string,config.toProperties());
            stmt=con.createStatement();
            } 
        catch (ClassNotFoundException | SQLException ex) 
            {
                AlertMaker.showNotification("Database Error","Connection not Established",AlertMaker.image_link);
            }
    }
    public static void disconnect()
    {
        if(con!=null)
            try {
                stmt.close();
                con.close();
        } catch (SQLException ex) {
            AlertMaker.showNotification("Database Error","Could not disconnect!",AlertMaker.image_cross);
        }
    }
    //Modules to create tables for database ------------------------------------
    public static void create_table(String create_table)
    {
        try {
         stmt.executeUpdate(create_table);
      } catch ( SQLException e ) {
         AlertMaker.showErrorMessage(e);
      }
   }
    
    public static void create_users_table()
    {
        create_table(users_table);
    }
    public static void create_admin_table()
    {
        create_table(admin_table);
    }
    public static void create_movie_table()
    {
        create_table(movie_table);
    }
    public static void create_theatre_table()
    {
        create_table(theatre_table);
    }
    public static void create_show_table()
    {
        create_table(show_table);
    }
    public static void create_order_table()
    {
        create_table(order_table);
    }
    public static void create_all_tables()
    {
        connect();
        System.out.println("connected");
        create_users_table();
        create_admin_table();
        create_movie_table();
        create_theatre_table();
        create_show_table();
        create_order_table();
        insert_root_admin();
        disconnect();
    }
    //--------------------------------------------------------------------------
    //Modules to insert record in a table---------------------------------------
    public static void insert_root_admin()
    {
        String sql="insert into admin values('root123','cbfdac6008f9cab4083784cbd1874f76618d2a97')";
        try{
            stmt.executeUpdate(sql);
        }
        catch(SQLException ex)
        {
            System.out.println("Root admin already present.");
        }
    }
    public static int insert_record(String sql)
    {
        int val=0;
        try {
         connect();
         val=stmt.executeUpdate(sql);
      } catch ( SQLException e ) {
         System.out.println(e.getMessage());
      }
        return val;
    }
    public static boolean insert_show(String sql)
    {
        try{
            stmt.executeUpdate(sql);
            return true;
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    public static int insert_user(User user)
    {
        String sql = "INSERT INTO users VALUES('"+
                user.getUsername()+"','"+
                user.getFullname()+"','"+
                user.getEmail()+"','"+
                user.getPassword()+"','"+
                user.getContact()+"')";
                return insert_record(sql);
    }
    public static int insert_admin(Admin admin)
    {
        String sql = "INSERT INTO admin VALUES('"+
                admin.getUsername()+"','"+
                admin.getPassword()+"')";
          return insert_record(sql);
    }
    public static boolean order_transaction(String sql1,String sql2)
    {
        stmt=null;
        con=null;
        //disconnect();
        connect();
        try {
            con.setAutoCommit(false);
            stmt.executeUpdate(sql1);
            stmt.executeUpdate(sql2);
            con.commit();
            con.setAutoCommit(true);
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (SQLException ex1) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return false;
        }
    }
    public static int insert_movie(String sql,File file)
    {
        int val=0;
        try {
            
            connect();
            PreparedStatement ps=con.prepareStatement(sql);
            FileInputStream input=new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            for (int readNum; (readNum = input.read(buf)) != -1;){
                bos.write(buf, 0, readNum);
            }
            input.close();
            ps.setBytes(1, bos.toByteArray());
            val=ps.executeUpdate();
            disconnect();
      } catch ( SQLException ex) {
         AlertMaker.showWarning(ex);}
        catch(IOException ex){
         AlertMaker.showNotification("Error","Invalid Image",AlertMaker.image_warning);
        }
        return val;
    }
    //--------------------------------------------------------------------------
    // 
    public static ResultSet executeQuery(String sql)
    {
        connect();
        ResultSet rs=null;
        try {
            rs=stmt.executeQuery(sql);
        } catch (SQLException ex) {
            AlertMaker.showWarning(ex);
        }
        return rs;
    }
    public static ResultSet getTicketDetails(String sql,String movieid,String theatreid,String time,String date)
    {
        ResultSet rs=null;
        try {
            connect();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, theatreid);
            ps.setString(2, movieid);
            ps.setString(3, time);
            ps.setString(4, date);
            rs=ps.executeQuery();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return rs;   
    }
    public static ResultSet getMovie(int id)
    {
        String sql="select * from movie where id="+String.valueOf(id);
        return executeQuery(sql);
    }
    public static ResultSet getUserNamePassword_users()
    {
        String sql="Select username,password from users";
        return executeQuery(sql);
    }
    public static ResultSet getUserNamePassword_admin()
    {
        String sql="Select * from admin";
        return executeQuery(sql);
    }
    public static void get_ticket(int orderid)
    {
        connect();
        new Ticket().print_ticket(orderid);
    }
    public static  class Ticket extends JFrame
    {
    public void print_ticket(int orderid)
    {
        System.out.println("flag");
        String report="C:\\Users\\bose4\\Documents\\NetBeansProjects\\ticketbooking\\src\\ticketbooking\\util\\database\\Ticket.jrxml";
       Map parameter=new HashMap();
        parameter.put("orderid", orderid);
        try {
            JasperReport jr=JasperCompileManager.compileReport(report);
            JasperPrint jasperprint=JasperFillManager.fillReport(jr,parameter, con);
            JRViewer viewer =new JRViewer(jasperprint);
            viewer.setOpaque(true);
            viewer.setVisible(true);
            this.add(viewer);
            this.setSize(700, 500);
            this.setVisible(true);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());        }
    }
    }
}
