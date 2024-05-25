package personalprimtakip.View;

import personalprimtakip.Helper.Config;
import personalprimtakip.Helper.DBConnector;
import personalprimtakip.Helper.Helper;
import personalprimtakip.Model.Prim;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdatePrimGUI extends JFrame {
    private JPanel wrapper;
    private JTextField fld_prim_name;
    private JButton btn_update;
    private Prim prim;
    private Connection conn;

    public UpdatePrimGUI(Prim prim){
        this.prim = prim;
        add(wrapper);
        setSize(300,150);
        setLocation(Helper.screenCenter("x",getSize()),Helper.screenCenter("y",getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);

        fld_prim_name.setText(prim.getName());
        btn_update.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_prim_name)){
                Helper.showMsg("fill");
            } else {
                if (updatePrim(prim.getId(), fld_prim_name.getText())) {
                    Helper.showMsg("done");
                }
                dispose();
            }
        });

        getMonthlyPrimList();
        getItirazListesi();

    }

    private boolean updatePrim(int primId, String primName) {
        conn = DBConnector.getInstance(); // Assuming you have a DatabaseConnection class
        String query = "UPDATE primlistesi SET name = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, primName);
            preparedStatement.setInt(2, primId);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Bu fonksiyon, itirazlarını listeleyen view'dan veri almayı sağlar
    private void getItirazListesi() {
        conn = DBConnector.getInstance(); // Assuming you have a DatabaseConnection class
        String query = "SELECT * FROM itiraz_listesi_view WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, prim.getId()); // Assuming prim.getId() returns user_id
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // Itiraz listesi verilerini işle
                int id = resultSet.getInt("id");
                int primId = resultSet.getInt("prim_id");
                String userName = resultSet.getString("user_name");
                String itirazDurumu = resultSet.getString("itiraz_durumu");
                // Bu verileri kullanarak gerekli işlemleri yapabilirsin
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Bu fonksiyon, aylık prim listesini alır
    private void getMonthlyPrimList() {
        conn = DBConnector.getInstance(); // Assuming you have a DatabaseConnection class
        String query = "SELECT * FROM monthly_prim_view WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, prim.getId()); // Assuming prim.getId() returns user_id
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // Aylık prim listesi verilerini işle
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
