package personalprimtakip.Model;

import personalprimtakip.Helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Gorusme {
    private int id;
    private int user_id;
    private int prim_id;
    private String name;
    private String tarih;
    private String gorusme_konu;
    private String gorusme_durum;
    Prim prim;
    User asistan;

    public Gorusme(int id, int user_id, int prim_id, String name, String tarih, String gorusme_konu, String gorusme_durum) {
        this.id = id;
        this.user_id = user_id;
        this.prim_id = prim_id;
        this.name = name;
        this.tarih = tarih;
        this.gorusme_konu = gorusme_konu;
        this.gorusme_durum = gorusme_durum;
        this.prim = Prim.getFetch(prim_id);
        this.asistan = User.getFetch(user_id);
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

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getGorusme_konu() {
        return gorusme_konu;
    }

    public void setGorusme_konu(String gorusme_konu) {
        this.gorusme_konu = gorusme_konu;
    }

    public String getGorusme_durum() {
        return gorusme_durum;
    }

    public void setGorusme_durum(String gorusme_durum) {
        this.gorusme_durum = gorusme_durum;
    }
    public static ArrayList<Gorusme> getList(){
        ArrayList<Gorusme> gorusmeList = new ArrayList<>();

        Gorusme obj;

        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM public.gorusme");
            while (rs.next()){
                int id = rs.getInt("id");
                int user_id = rs.getInt("user_id");
                int prim_id = rs.getInt("prim_id");
                String name = rs.getString("name");
                String tarih = rs.getString("tarih");
                String gorusme_konu = rs.getString("gorusme_konu");
                String gorusme_durum = rs.getString("gorusme_durum");

                obj = new Gorusme(id,user_id,prim_id,name,tarih,gorusme_konu,gorusme_durum);
                gorusmeList.add(obj);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return gorusmeList;
    }

    public static boolean add(int user_id, int prim_id, String name, String tarih, String gorusme_konu, String gorusme_durum) {
        String query = "INSERT INTO public.gorusme (user_id, prim_id, name, tarih, gorusme_konu, gorusme_durum) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, user_id);
            pr.setInt(2, prim_id);
            pr.setString(3, name);
            pr.setString(4, tarih);
            pr.setString(5, gorusme_konu);
            pr.setString(6, gorusme_durum);
            return pr.executeUpdate() != -1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

}
