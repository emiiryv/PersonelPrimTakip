package personalprimtakip.View;

import personalprimtakip.Helper.Config;
import personalprimtakip.Helper.Helper;
import personalprimtakip.Model.Gorusme;
import personalprimtakip.Model.Prim;
import personalprimtakip.Model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ComponentAdapter;
import java.util.ArrayList;

public class AsistanGUI extends JFrame {
    private JPanel wrapper;
    private JTabbedPane tabbedPane1;
    private JTextField fld_musteri_adi;
    private JTextField fld_gorusme_tarihi;
    private JComboBox<String> cmb_gorusme_durumu;
    private JButton btn_add_gorusme;
    private JTable tbl_gorusme_listesi;
    private JButton btn_cikis;
    private JComboBox<String> cmb_gorusme_konusu;
    private JComboBox<Prim> cmb_prim_listesi; // Primler için JComboBox
    private JTable tbl_prim_list;
    private JComboBox cmb_prim_itiraz_listesi;
    private JTextField fld_prim_itiraz_aciklama;
    private JButton btn_gonder;
    private JTabbedPane tabbedPane2;

    private User currentUser;

    public AsistanGUI() {
        add(wrapper);
        setSize(1000, 500);
        setLocation(Helper.screenCenter("x", getSize()), Helper.screenCenter("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);

        currentUser = LoginGUI.getCurrentUser(); // Oturum açan kullanıcıyı al

        loadPrimComboBox(); // Prim ComboBox'ı yükle
        loadGorusmeTable(); // Görüşme listesini yükle
        loadPrimTable(); // Itiraz edilecek prim listesini yükle

        btn_cikis.addActionListener(e -> {
            dispose();
            new LoginGUI();
        });

        btn_add_gorusme.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_musteri_adi) || Helper.isFieldEmpty(fld_gorusme_tarihi)) {
                Helper.showMsg("fill");
            } else {
                String name = fld_musteri_adi.getText();
                String tarih = fld_gorusme_tarihi.getText();
                String gorusme_konusu = cmb_gorusme_konusu.getSelectedItem().toString();
                String gorusme_durumu = cmb_gorusme_durumu.getSelectedItem().toString();

                int user_id = currentUser.getId(); // Oturum açan kullanıcının id'si
                Prim selectedPrim = (Prim) cmb_prim_listesi.getSelectedItem();
                int prim_id = selectedPrim != null ? selectedPrim.getId() : -1; // Prim id'sini alın

                if (prim_id == -1) {
                    Helper.showMsg("Prim seçiniz.");
                    return;
                }

                if (Gorusme.add(user_id, prim_id, name, tarih, gorusme_konusu, gorusme_durumu)) {
                    Helper.showMsg("done");
                    // Tablo ve diğer bileşenleri güncellemek için gerekli metotları çağırın
                    fld_musteri_adi.setText(null); // Ekleme işlemi başarılı olduğunda, alanları temizleyin
                    fld_gorusme_tarihi.setText(null);
                    loadGorusmeTable(); // Görüşme listesini güncelle
                } else {
                    Helper.showMsg("error");
                }
            }
        });

        cmb_prim_listesi.addActionListener(e -> {
            Prim selectedPrim = (Prim) cmb_prim_listesi.getSelectedItem();
            if (selectedPrim != null) {
                System.out.println("Seçilen Prim: " + selectedPrim.getName());
                // Seçilen prim ile ilgili başka işlemler yapmak isterseniz buraya ekleyebilirsiniz
            }
        });
        tbl_gorusme_listesi.addComponentListener(new ComponentAdapter() {
        });
        loadPrimComboBox();
    }


    private void loadGorusmeTable() {
        DefaultTableModel gorusmeModel = new DefaultTableModel();
        gorusmeModel.addColumn("ID");
        gorusmeModel.addColumn("Kullanıcı ID");
        gorusmeModel.addColumn("Prim ID");
        gorusmeModel.addColumn("Ad");
        gorusmeModel.addColumn("Tarih");
        gorusmeModel.addColumn("Konu");
        gorusmeModel.addColumn("Durum");

        ArrayList<Gorusme> gorusmeList = Gorusme.getList();
        for (Gorusme gorusme : gorusmeList) {
            gorusmeModel.addRow(new Object[]{
                    gorusme.getId(),
                    gorusme.getUser_id(),
                    gorusme.getPrim_id(),
                    gorusme.getName(),
                    gorusme.getTarih(),
                    gorusme.getGorusme_konu(),
                    gorusme.getGorusme_durum()
            });
        }

        tbl_gorusme_listesi.setModel(gorusmeModel);
    }

    private void loadPrimComboBox() {
        cmb_prim_itiraz_listesi.removeAllItems(); // Önce mevcut öğeleri temizle
        ArrayList<Prim> primList = Prim.getList(); // Tüm primleri al

        for (Prim prim : primList) {
            cmb_prim_itiraz_listesi.addItem(prim); // Combobox'a her bir primi ekle
        }
    }



    private void loadPrimTable() {
        DefaultTableModel primTableModel = new DefaultTableModel();
        primTableModel.addColumn("ID");
        primTableModel.addColumn("Ad");
        // Diğer sütunları ekleyin

        ArrayList<Prim> primList = Prim.getList();
        for (Prim prim : primList) {
            primTableModel.addRow(new Object[]{
                    prim.getId(),
                    prim.getName(),
                    // Diğer sütunları buraya ekleyin
            });
        }

        tbl_prim_list.setModel(primTableModel);
    }


    public static void main(String[] args) {
        Helper.setLayout();
        new AsistanGUI();
    }

    private void createUIComponents() {
        cmb_prim_listesi = new JComboBox<>(); // JComboBox'ı oluşturun
    }
}
