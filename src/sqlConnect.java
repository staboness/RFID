import java.sql.*;
import java.util.ArrayList;

public class sqlConnect {
    public static void main(String[] args) throws Exception {
        loginFrame login = new loginFrame();
        getConnection();
        login.setVisible(true);
    }
    public void changeLayout(int layout){
        journalFrame journal = new journalFrame();
        addUser mainpanel = new addUser();
        if (layout == 1) {
            journal.setVisible(true);
        } else if (layout == 2) {
            mainpanel.setVisible(true);
        } else {
            System.out.println("Error");
        }
    }
    //Handles login operation
    public static boolean loginHandler(String login, String password) throws Exception {
        addUser mainpanel = new addUser();
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
                mainpanel.setVisible(true);
                return true;
            } else {
                mainpanel.ShowError("Логин и/или пароль введен не верно!");
                return false;
            }
        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
    }
    //Handles user exist check
    public static ArrayList<String> checkifExists (String firstname, String secname, String patron, String photopath) throws Exception {
        try {
            Connection connection = getConnection();
            PreparedStatement getQuery = connection.prepareStatement("SELECT firstname, secondname, patronymic, photo FROM users");
            ResultSet result = getQuery.executeQuery();
            ArrayList<String> array = new ArrayList<>();
            while(result.next()){
                array.add(result.getString("firstname"));
                array.add(result.getString("secondname"));
                array.add(result.getString("patronymic"));
                array.add(result.getString("photo"));
            }
            return array;
        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        }
    }
    //Post user to database
    public static void postUser(String firstname, String secname, String patron, String photopath, String position, String accesslevel) throws Exception {
        addUser addUser = new addUser();
        try {
            Connection connection = getConnection();
            String fixedPath = photopath.replace("\\", "\\\\");
            ArrayList<String> array;
            array = checkifExists(firstname, secname, patron, photopath);
            if (!array.contains(firstname) && !array.contains(secname) && !array.contains(patron) && !array.contains(photopath)) {
                PreparedStatement post = connection.prepareStatement("INSERT INTO users (firstname, secondname, patronymic, photo, accesslevel, position) " +
                        "VALUES ('" + firstname + "','" + secname + "','" + patron + "','" + fixedPath + "','" + accesslevel + "', '" + position + "')");
                post.executeUpdate();
            } else {
                addUser.ShowError("Пользователь уже существует!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    //Connection handler
    public static Connection getConnection() throws Exception {
        addUser addUser = new addUser();
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
            addUser.ShowError("Нет соединения с базой данных!");
            return null;
        }
    }
    public ArrayList<Users> getUsers(){
        ArrayList<Users> users = new ArrayList<Users>();
        try {
        Connection con = getConnection();
        Statement st;
        ResultSet result;
        Users u;
            st = con.createStatement();
            result = st.executeQuery("SELECT id, firstname, secondname, patronymic, photo, accesslevel, position FROM users");
            while(result.next()){
                u = new Users(
                        result.getInt("id"),
                        result.getString("firstname"),
                        result.getString("secondname"),
                        result.getString("patronymic"),
                        result.getString("photo"),
                        result.getInt("accesslevel"),
                        result.getString("position")
                );
                users.add(u);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return users;
    }

    class Users{
        private int Id;
        private String Fname;
        private String Sname;
        private String Lname;
        private String Photo;
        private int Access;
        private String Position;

        public Users(int id, String fname, String sname, String lname, String photo, int access, String position){
            this.Id = id;
            this.Fname = fname;
            this.Sname = sname;
            this.Lname = lname;
            this.Photo = photo;
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

        public int getAccess(){
            return this.Access;
        }

        public String getPosition(){
            return this.Position;
        }
    }
}