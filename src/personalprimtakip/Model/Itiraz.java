package personalprimtakip.Model;

import personalprimtakip.Helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Itiraz {
    private int id;
    private int user_id;
    private int prim_id;
    private String name;
    private String status;

    private Prim prim;
    private User operator;

    public Itiraz(int id, int user_id, int prim_id, String name, String status) {
        this.id = id;
        this.user_id = user_id;
        this.prim_id = prim_id;
        this.name = name;
        this.status = status;
        this.prim = Prim.getFetch(prim_id);
        this.operator = User.getFetch(user_id);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPrim_id() {
        return prim_id;
    }

    public void setPrim_id(int prim_id) {
        this.prim_id = prim_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Prim getPrim() {
        return prim;
    }

    public void setPrim(Prim prim) {
        this.prim = prim;
    }

    public User getOperator() {
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }

    public static ArrayList<Itiraz> getList(){
        ArrayList<Itiraz> itirazList = new ArrayList<>();

        Itiraz obj;

        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM public.itiraz");
            while (rs.next()){
                int id = rs.getInt("id");
                int user_id = rs.getInt("user_id");
                int prim_id = rs.getInt("prim_id");
                String name = rs.getString("name");
                String status = rs.getString("status");
                obj = new Itiraz(id,user_id,prim_id,name,status);
                itirazList.add(obj);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return itirazList;
    }

    public static boolean add(int user_id,int prim_id,String name,String status){
        String query = "INSERT INTO public.itiraz (user_id,prim_id,name,status) VALUES (?,?,?,?)";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,user_id);
            pr.setInt(2,prim_id);
            pr.setString(3,name);
            pr.setString(4,status);
            return pr.executeUpdate() != -1;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }
    public static ArrayList<Itiraz> getListByUser(int user_id){
        ArrayList<Itiraz> itirazList = new ArrayList<>();
        Itiraz obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM public.itiraz WHERE user_id = " + user_id);
            while (rs.next()) {
                int id = rs.getInt("id");
                int userID = rs.getInt("user_id");
                int prim_id = rs.getInt("prim_id");
                String name = rs.getString("name");
                String status = rs.getString("status");
                obj = new Itiraz(id,userID,prim_id,name,status);
                itirazList.add(obj);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return itirazList;
    }
    public static boolean delete(int id){
        String query = "DELETE FROM public.itiraz WHERE id = ?";
        ArrayList<Itiraz> itirazList = Itiraz.getListByUser(id);

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);

            return pr.executeUpdate() != -1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

}