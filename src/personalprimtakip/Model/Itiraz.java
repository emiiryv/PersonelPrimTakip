package personalprimtakip.Model;

import personalprimtakip.Helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Itiraz {
    private int id;
    private int user_id; // Asistanın kim tarafından oluşturulduğunu belirten kullanıcı kimliği
    private int prim_id;
    private String name;
    private String status;
    private String aciklama;

    // Constructor güncellendi
    public Itiraz(int id, int user_id, int prim_id, String name, String status, String aciklama) {
        this.id = id;
        this.user_id = user_id;
        this.prim_id = prim_id;
        this.name = name;
        this.status = status;
        this.aciklama = aciklama;
    }

    // Getter ve setter metotları eklendi
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

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    // Veritabanından liste alırken user_id'yi dikkate alacak şekilde güncellendi
    public static ArrayList<Itiraz> getList() {
        ArrayList<Itiraz> itirazList = new ArrayList<>();

        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM public.itiraz");
            while (rs.next()) {
                int id = rs.getInt("id");
                int user_id = rs.getInt("user_id");
                int prim_id = rs.getInt("prim_id");
                String name = rs.getString("name");
                String status = rs.getString("status");
                String aciklama = rs.getString("aciklama"); // Açıklama alanını da oku
                Itiraz obj = new Itiraz(id, user_id, prim_id, name, status, aciklama);
                itirazList.add(obj);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return itirazList;
    }

    // Kullanıcıya göre listeleme yaparken user_id'yi dikkate alacak şekilde güncellendi
    public static ArrayList<Itiraz> getListByUser(int user_id) {
        ArrayList<Itiraz> itirazList = new ArrayList<>();

        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM public.itiraz WHERE user_id = " + user_id);
            while (rs.next()) {
                int id = rs.getInt("id");
                int userID = rs.getInt("user_id");
                int prim_id = rs.getInt("prim_id");
                String name = rs.getString("name");
                String status = rs.getString("status");
                String aciklama = rs.getString("aciklama"); // Açıklama alanını da oku
                Itiraz obj = new Itiraz(id, userID, prim_id, name, status, aciklama);
                itirazList.add(obj);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return itirazList;
    }

    // Yeni bir itiraz eklemek için metot eklendi
    public static boolean add(int user_id, int prim_id, String name, String status, String aciklama) {
        String query = "INSERT INTO public.itiraz (user_id, prim_id, name, status, aciklama) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, user_id);
            pr.setInt(2, prim_id);
            pr.setString(3, name);
            pr.setString(4, status);
            pr.setString(5, aciklama); // Açıklama alanını da ekle
            return pr.executeUpdate() != -1;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    // Verilen itiraz ID'sine göre itirazı silen metot eklendi
    public static boolean delete(int id) {
        String query = "DELETE FROM public.itiraz WHERE id = ?";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            return pr.executeUpdate() != -1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
    public static boolean cevapla(int itirazId, String cevap) {
        String query = "UPDATE public.itiraz SET cevap = ? WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, cevap);
            pr.setInt(2, itirazId);
            return pr.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
