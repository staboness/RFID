/**
 * Created by Rodin Maxim on May, 2019
 */
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
public class SqlConnect {
    public static void main(String[] args) throws Exception {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getConnection();
                } catch (Exception ex){
                    System.out.println(ex);
                }
            }
        });
        th.start();
        LoginLayout login = new LoginLayout();
    }
    //Handles login operation
    public static boolean loginHandler(String login, String password) throws Exception {
        try {
            Connection connection = getConnection();
            PreparedStatement getQuery = connection.prepareStatement("SELECT login, password FROM login_system");
            ResultSet result = getQuery.executeQuery();
            ArrayList<String> array = new ArrayList<>();
            while (result.next()) {
                array.add(result.getString("login"));
                array.add(result.getString("password"));
            }
            if (array.contains(login) && array.contains(password)){
                MainGUI gui = MainGUI.getInstance();
                connection.close();
                getQuery.close();
                result.close();
                System.out.println("Connection closed at loginHandler");
                return true;
            } else {
                ShowError("Логин и/или пароль введен не верно!");
                return false;
            }
        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
    }
    //Handles user exist check
    public static ArrayList<String> checkifExists () throws Exception {
        try {
            Connection connection = getConnection();
            PreparedStatement getQuery = connection.prepareStatement("SELECT firstname, secondname, patronymic, photo, rfid FROM users");
            ResultSet result = getQuery.executeQuery();
            ArrayList<String> array = new ArrayList<>();
            while(result.next()){
                array.add(result.getString("firstname"));
                array.add(result.getString("secondname"));
                array.add(result.getString("patronymic"));
                array.add(result.getString("photo"));
                array.add(result.getString("rfid"));
            }
            connection.close();
            result.close();
            getQuery.close();
            System.out.println("Connection closed at checkifExists");
            return array;
        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        }
    }
    //Post user to database
    public static void postUser(String firstname, String secname, String patron, String photopath, String position, String accesslevel, String rfid) throws Exception {
        try {
            Connection connection = getConnection();
            PreparedStatement getQuery = connection.prepareStatement("SELECT MAX(id) maxid FROM users");
            ResultSet result = getQuery.executeQuery();
            result.next();
            int lastrow = result.getInt("maxid");
            lastrow++;
            String fixedPath = photopath.replace("\\", "\\\\");
            ArrayList<String> array;
            array = checkifExists();
            if (!array.contains(firstname) || !array.contains(secname) || !array.contains(patron) || !array.contains(photopath) && !array.contains(rfid)) {
                PreparedStatement post = connection.prepareStatement("INSERT INTO users (id, firstname, secondname, patronymic, photo, rfid, accesslevel, position) " +
                        "VALUES ('" + lastrow + "', '" + firstname + "','" + secname + "','" + patron + "','" + fixedPath + "','" + rfid +"', '"+ accesslevel + "', '" + position + "')");
                System.out.println("Connection closed at postUser");
                post.executeUpdate();
                post.close();
                connection.close();
            } else {
                ShowError("Пользователь уже существует!");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    //Connection handler
    public static Connection getConnection() throws Exception {
        try {
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/rfid?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            String login = "root";
            String password = "root";
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, login, password);
            System.out.println("Connected successfully");
            return connection;
        } catch (Exception e) {
            System.out.print(e);
            ShowError("Нет соединения с базой данных!");
            return null;
        }
    }

    public static void updateUsers(int id, String firstname, String secname, String patron, String photopath, String rfid, String accesslevel, String position) throws Exception {
        try{
            Connection con = getConnection();
            String fixedPath = photopath.replace("\\", "\\\\");
            PreparedStatement post = con.prepareStatement("UPDATE users SET " +
                    "firstname = '" + firstname + "', secondname = '" + secname + "' , patronymic = '" + patron + "', photo = '" + fixedPath + "', rfid = '" + rfid + "', " +
                    "accesslevel = '" + accesslevel + "', position = '" + position + "' WHERE id = '" + id + "'");
            post.executeUpdate();
            post.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Exception in updateUsers");
        }
    }

    public ArrayList<Users> getUsers() throws Exception{
        ArrayList<Users> users = new ArrayList<Users>();
        Connection con = getConnection();
        Statement st = null;
        ResultSet result = null;
        try {
            Users u;
            st = con.createStatement();
            result = st.executeQuery("SELECT id, firstname, secondname, patronymic, photo, rfid, accesslevel, position FROM users");
            while(result.next()){
                u = new Users(
                        result.getInt("id"),
                        result.getString("firstname"),
                        result.getString("secondname"),
                        result.getString("patronymic"),
                        result.getString("photo"),
                        result.getString("rfid"),
                        result.getInt("accesslevel"),
                        result.getString("position")
                );
                users.add(u);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        con.close();
        st.close();
        result.close();
        System.out.println("Connection at getUsers() closed");
        return users;
    }

    class Users{
        private int Id;
        private String Fname;
        private String Sname;
        private String Lname;
        private String Photo;
        private String Rfid;
        private int Access;
        private String Position;

        public Users(int id, String fname, String sname, String lname, String photo, String rfid, int access, String position){
            this.Id = id;
            this.Fname = fname;
            this.Sname = sname;
            this.Lname = lname;
            this.Photo = photo;
            this.Rfid = rfid;
            this.Access = access;
            this.Position = position;
        }

        public int getId(){
            return this.Id;
        }

        public String getFname(){
            return this.Fname;
        }

        public String getSname(){
            return this.Sname;
        }

        public String getLname(){
            return this.Lname;
        }

        public String getPhoto(){
            return this.Photo;
        }

        public String getRfid() { return this.Rfid; }

        public int getAccess(){
            return this.Access;
        }

        public String getPosition(){
            return this.Position;
        }
    }

    public static void ShowError(String errorMsg) {
        JOptionPane.showMessageDialog(null, errorMsg, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }
}
