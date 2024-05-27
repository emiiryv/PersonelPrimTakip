package personalprimtakip.Model;

import personalprimtakip.Helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Prim {
    private int id;
    private String name;
    private double dailySalary;
    private double monthlySalary;

    public Prim(int id, String name, double dailySalary, double monthlySalary) {
        this.id = id;
        this.name = name;
        this.dailySalary = dailySalary;
        this.monthlySalary = monthlySalary;
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

    public double getDailySalary() {
        return dailySalary;
    }

    public void setDailySalary(double dailySalary) {
        this.dailySalary = dailySalary;
    }

    public double getMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(double monthlySalary) {
        this.monthlySalary = monthlySalary;
    }

    @Override
    public String toString() {
        return name;
    }


    public static ArrayList<Prim> getList() {
        ArrayList<Prim> primList = new ArrayList<>();
        String query = "SELECT * FROM prim_list_view";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double dailySalary = rs.getDouble("dailySalary");
                double monthlySalary = rs.getDouble("monthlySalary");
                Prim prim = new Prim(id, name, dailySalary, monthlySalary);
                primList.add(prim);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return primList;
    }



    public static boolean add(String name, double dailySalary, double monthlySalary) {
        String query = "INSERT INTO public.primlistesi (name, dailySalary, monthlySalary) VALUES (?, ?, ?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, name);
            pr.setDouble(2, dailySalary);
            pr.setDouble(3, monthlySalary);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

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

    public static boolean update(int id, String name, double dailySalary, double monthlySalary) {
        String query = "UPDATE public.primlistesi SET name=?, dailySalary=?, monthlySalary=? WHERE id=?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, name);
            pr.setDouble(2, dailySalary);
            pr.setDouble(3, monthlySalary);
            pr.setInt(4, id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static double calculateDailyPrim(int primID) {
        String query = "SELECT calculate_daily_prim(?)"; // SQL'de oluşturduğumuz günlük prim hesaplama fonksiyonunu çağırır
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, primID);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static double calculateMonthlyPrim(int primID) {
        String query = "SELECT calculate_monthly_prim(?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, primID);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static Prim getFetch(int id) {
        String query = "SELECT * FROM public.primlistesi WHERE id=?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                double dailySalary = rs.getDouble("dailySalary");
                double monthlySalary = rs.getDouble("monthlySalary");
                return new Prim(id, name, dailySalary, monthlySalary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
