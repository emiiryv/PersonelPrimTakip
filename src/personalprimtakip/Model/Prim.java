package personalprimtakip.Model;

import personalprimtakip.Helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    // Primleri listeleme metodu
    public static ArrayList<Prim> getList() {
        ArrayList<Prim> primList = new ArrayList<>();
        String query = "SELECT * FROM public.primlistesi";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Prim prim = new Prim(id, name);
                primList.add(prim);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return primList;
    }

    // Prim ekleme metodu
    public static boolean add(String name) {
        String query = "INSERT INTO public.primlistesi (name) VALUES (?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, name);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Prim silme metodu
    public static boolean delete(int id) {
        String query = "DELETE FROM public.primlistesi WHERE id=?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static Prim getFetch(int id) {
        String query = "SELECT * FROM public.primlistesi WHERE id=?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                return new Prim(id, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Eğer bir hata oluşursa veya sonuç bulunamazsa null döndürülür
    }
    public static boolean update(int id, String newName) {
        String query = "UPDATE public.primlistesi SET name=? WHERE id=?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, newName);
            pr.setInt(2, id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // JComboBox'ta doğru bir şekilde görüntülenmesi için toString metodu
    @Override
    public String toString() {
        return this.name;
    }
}
