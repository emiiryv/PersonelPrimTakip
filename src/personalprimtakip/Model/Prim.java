package personalprimtakip.Model;

import personalprimtakip.Helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Prim {
    private int id;
    private String name;

    public Prim(int id, String name) {
        this.id = id;
        this.name = name;
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

    public static ArrayList<Prim> getList(){
        ArrayList<Prim> primList = new ArrayList<>();
        Prim obj;

        try {
         Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM public.primlistesi");
            while (rs.next()){
                obj = new Prim(rs.getInt("id"),rs.getString("name"));
                primList.add(obj);
            }
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return primList;
    }

    public static boolean add(String name){
        String query = "INSERT INTO public.primlistesi (name) VALUES (?)";
        try{
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,name);
            return  pr.executeUpdate() != -1;
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return true;
    }
    public static boolean update(int id, String name) {
        String query = "UPDATE public.primlistesi SET name = ? WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, name);
            pr.setInt(2, id);
            return pr.executeUpdate() != -1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static boolean delete(int id) {
        String query = "DELETE FROM public.primlistesi WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            return pr.executeUpdate() != -1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public static Prim getFetch(int id){
        Prim obj = null;
        String query = "SELECT * FROM public.primlistesi WHERE id = ?";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);
            ResultSet rs = pr.executeQuery();
            if (rs.next()){
                obj = new Prim(rs.getInt("id"),rs.getString("name"));

            }
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return obj;
    }


}
