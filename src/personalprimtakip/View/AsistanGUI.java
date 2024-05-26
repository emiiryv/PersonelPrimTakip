package personalprimtakip.View;

import personalprimtakip.Helper.Config;
import personalprimtakip.Helper.Helper;
import personalprimtakip.Model.Gorusme;
import personalprimtakip.Model.Itiraz;
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
    private JComboBox<Prim> cmb_prim_itiraz_listesi; // Primler için itiraz JComboBox
    private JTextField fld_prim_itiraz_aciklama;
    private JButton btn_gonder;
    private JTable tbl_itirazlarim; // Kullanıcının itirazlarını göstermek için tablo

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
        loadPrimTable(); // Prim tablosunu yükle
        loadGorusmeTable(); // Görüşme listesini yükle
        loadItirazTable(); // Kullanıcının itirazlarını yükle

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
            }
        });

        btn_gonder.addActionListener(e -> {
            Prim selectedPrim = (Prim) cmb_prim_itiraz_listesi.getSelectedItem();
            String aciklama = fld_prim_itiraz_aciklama.getText();
            if (selectedPrim != null && !aciklama.isEmpty()) {
                System.out.println("Itiraz Edilen Prim: " + selectedPrim.getName());
                if (Itiraz.add(currentUser.getId(), selectedPrim.getId(), selectedPrim.getName(), "Beklemede", aciklama)) {
                    Helper.showMsg("done");
                    loadItirazTable(); // Itiraz listesini güncelle
                    fld_prim_itiraz_aciklama.setText(null); // Alanı temizle
                } else {
                    Helper.showMsg("error");
                }
            } else {
                Helper.showMsg("fill");
            }
        });

        tbl_gorusme_listesi.addComponentListener(new ComponentAdapter() {
        });

        loadPrimComboBox(); // Combobox'ı doldurmak için metodu çağır
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
        cmb_prim_listesi.removeAllItems(); // Önce mevcut öğeleri temizle
        ArrayList<Prim> primList = Prim.getList(); // Tüm primleri al

        for (Prim prim : primList) {
            cmb_prim_itiraz_listesi.addItem(prim); // Combobox'a her bir primi ekle
            cmb_prim_listesi.addItem(prim); // Combobox'a her bir primi ekle
        }
    }

    private void loadPrimTable() {
        DefaultTableModel primModel = new DefaultTableModel();
        primModel.addColumn("ID");
        primModel.addColumn("Ad");

        ArrayList<Prim> primList = Prim.getList();
        for (Prim prim : primList) {
            primModel.addRow(new Object[]{
                    prim.getId(),
                    prim.getName()
            });
        }

        tbl_prim_list.setModel(primModel);
    }

    private void loadItirazTable() {
        DefaultTableModel itirazModel = new DefaultTableModel();
        itirazModel.addColumn("ID");
        itirazModel.addColumn("Prim Adı");
        itirazModel.addColumn("Durum");
        itirazModel.addColumn("Açıklama");
        ArrayList<Itiraz> itirazList = Itiraz.getListByUser(currentUser.getId());
        for (Itiraz itiraz : itirazList) {
            int primId = itiraz.getPrim_id();
            Prim prim = Prim.getFetch(primId); // Veritabanından ilgili Prim nesnesini al
            String primName = prim != null ? prim.getName() : ""; // Prim nesnesi varsa ismini al, yoksa boş string ata
            itirazModel.addRow(new Object[]{
                    itiraz.getId(), // getId() metodu buradan erişilebilir
                    primName,
                    itiraz.getStatus(),
                    itiraz.getAciklama()
            });
        }


        tbl_itirazlarim.setModel(itirazModel);
    }

    public static void main(String[] args) {
        Helper.setLayout();
        new AsistanGUI();
    }

    private void createUIComponents() {
        cmb_prim_listesi = new JComboBox<>(); // JComboBox'ı oluşturun
        cmb_prim_itiraz_listesi = new JComboBox<>(); // JComboBox'ı oluşturun
        tbl_itirazlarim = new JTable(); // JTable'ı oluşturun
    }
}

