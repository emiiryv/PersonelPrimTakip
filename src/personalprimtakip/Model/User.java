package personalprimtakip.Model;

import personalprimtakip.Helper.DBConnector;
import personalprimtakip.Helper.Helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class User {
    private int id;
    private String name;
    private String uname;
    private String pass;
    private String type;

    public User() {
    }

    public User(int id, String name, String uname, String pass, String type) {
        this.id = id;
        this.name = name;
        this.uname = uname;
        this.pass = pass;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static ArrayList<User> getList() {
        ArrayList<User> userList = new ArrayList<>();
        String query = "SELECT * FROM all_users_view";
        User obj;
        Statement st = null;
        ResultSet rs = null;
        try {
            st = DBConnector.getInstance().createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                obj = new User();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUname(rs.getString("uname"));
                obj.setPass(rs.getString("pass"));
                obj.setType(rs.getString("type"));
                userList.add(obj);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userList;
    }


    public static boolean add(String name, String uname, String pass, String type) {
        String query = "INSERT INTO public.user (name, uname, pass, type) VALUES (?, ?, ?, ?::user_type)";
        User findUser = User.getFetch(uname);
        if (findUser != null){
            Helper.showMsg("Bu kullanıcı adı daha önceden eklenmiş. Lütfen farklı bir kullanıcı adı giriniz.");
            return false;
        }
        PreparedStatement pr = null;
        try {
            pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, name);
            pr.setString(2, uname);
            pr.setString(3, pass);
            pr.setString(4, type);

            int response = pr.executeUpdate();

            if (response == -1){
                Helper.showMsg("error");
            }

            return response != -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (pr != null) {
                    pr.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }



    public static User getFetch(String uname){
        User obj = null;
        String query = "SELECT * FROM public.user WHERE uname = ?";
        PreparedStatement pr = null;
        ResultSet rs = null;
        try {
            pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, uname);
            rs = pr.executeQuery();
            while (rs.next()){
                obj = new User();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUname(rs.getString("uname"));
                obj.setPass(rs.getString("pass"));
                obj.setType(rs.getString("type"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (pr != null) {
                    pr.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

    public static User getFetch(int id){
        User obj = null;
        String query = "SELECT * FROM public.user WHERE id = ?";
        PreparedStatement pr = null;
        ResultSet rs = null;
        try {
            pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            rs = pr.executeQuery();
            while (rs.next()){
                obj = new User();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUname(rs.getString("uname"));
                obj.setPass(rs.getString("pass"));
                obj.setType(rs.getString("type"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (pr != null) {
                    pr.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

    public static User getFetch(String uname, String  pass){
        User obj = null;
        String query = "SELECT * FROM public.user WHERE uname = ? AND pass = ?";
        PreparedStatement pr = null;
        ResultSet rs = null;
        try {
            pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, uname);
            pr.setString(2, pass);
            rs = pr.executeQuery();
            if (rs.next()){
                switch (rs.getString("type")){
                    case "operator":
                        obj = new Operator();
                        break;
                    default:
                        obj = new User();
                }
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUname(rs.getString("uname"));
                obj.setPass(rs.getString("pass"));
                obj.setType(rs.getString("type"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (pr != null) {
                    pr.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }


    public static boolean delete(int id){
        String query = "CALL delete_user(?)";
        ArrayList<Itiraz> itirazList = Itiraz.getListByUser(id);
        for (Itiraz i : itirazList){
            Itiraz.delete(i.getId());
        }
        PreparedStatement pr = null;
        try {
            pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);

            return pr.executeUpdate() != -1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (pr != null) {
                    pr.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static boolean update(int id, String name, String uname, String pass, String type){
        String query = "CALL update_user(?, ?, ?, ?, ?)";
        User findUser = User.getFetch(uname);
        if (findUser != null && findUser.getId() != id){
            Helper.showMsg("Bu kullanıcı adı daha önce eklenmiş.");
            return false;
        }
        PreparedStatement pr = null;
        try {
            pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            pr.setString(2, name);
            pr.setString(3, uname);
            pr.setString(4, pass);
            pr.setString(5, type);

            return pr.executeUpdate() != -1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (pr != null) {
                    pr.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static ArrayList<User> searchUserList(String query) {
        ArrayList<User> userList = new ArrayList<>();
        User obj;
        Statement st = null;
        ResultSet rs = null;
        try {
            st = DBConnector.getInstance().createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                obj = new User();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUname(rs.getString("uname"));
                obj.setPass(rs.getString("pass"));
                obj.setType(rs.getString("type"));
                userList.add(obj);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userList;
    }

    public static String searchQuery(String name, String uname, String type) {
        String query = "SELECT * FROM user_search_view WHERE 1=1";
        if (!uname.isEmpty()) {
            query += " AND uname LIKE '%" + uname + "%'";
        }
        if (!name.isEmpty()) {
            query += " AND name LIKE '%" + name + "%'";
        }
        if (!type.isEmpty()) {
            query += " AND type = '" + type + "'";
        }

        System.out.println(query);
        return query;
    }

    public static ArrayList<User> getListOnlyOperator() {
        ArrayList<User> operatorList = new ArrayList<>();
        String query = "SELECT * FROM operator_view";
        PreparedStatement pr = null;
        ResultSet rs = null;
        try {
            pr = DBConnector.getInstance().prepareStatement(query);
            rs = pr.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String uname = rs.getString("uname");
                String pass = rs.getString("pass");
                String type = rs.getString("type");
                User obj = new User(id, name, uname, pass, type);
                operatorList.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (pr != null) {
                    pr.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return operatorList;
    }


}
