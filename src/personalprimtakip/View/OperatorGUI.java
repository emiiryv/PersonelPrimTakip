package personalprimtakip.View;

import personalprimtakip.Helper.Config;
import personalprimtakip.Helper.Helper;
import personalprimtakip.Helper.Item;
import personalprimtakip.Model.Itiraz;
import personalprimtakip.Model.Operator;
import personalprimtakip.Model.Prim;
import personalprimtakip.Model.User;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class OperatorGUI extends JFrame {

    private JPanel wrapper;
    private JTabbedPane tab_operator;
    private JPanel pnl_user_list;
    private JLabel lbl_welcome;
    private JPanel pnl_top;
    private JButton btn_logout;
    private JScrollPane scrl_user_list;
    private JTable tbl_user_list;
    private JPanel pnl_user_form;
    private JTextField fld_user_name;
    private JTextField fld_user_uname;
    private JTextField fld_user_pass;
    private JComboBox<String> cbm_user_type;
    private JButton btn_user_add;
    private JTextField fld_user_id;
    private JButton btn_user_delete;
    private JTextField fld_sh_user_name;
    private JTextField fld_sh_user_uname;
    private JComboBox cmb_sh_user_type;
    private JButton btn_user_sh;
    private JPanel pnl_prim_add;
    private JScrollPane scrl_prim_list;
    private JTable tbl_prim_list;
    private JTextField fld_prim_name;
    private JButton btn_prim_add;
    private JPanel pnl_itiraz_list;
    private JScrollPane scrl_itiraz_list;
    private JTable tbl_itiraz_list;
    private JPanel pnl_itiraz_add;
    private JTextField fld_itiraz_name;
    private JComboBox<Item> cmb_itiraz_operator;
    private JButton btn_itiraz_cevap;
    private JComboBox<Item> cmb_itiraz_prim;
    private JComboBox cmb_cevap;
    private DefaultTableModel mdl_user_list;
    private Object[] row_user_list;
    private DefaultTableModel mdl_prim_list;
    private Object[] row_prim_list;
    private JPopupMenu primMenu;
    private DefaultTableModel mdl_itiraz_list;
    private Object[] row_itiraz_list;

    private final Operator operator;

    public OperatorGUI(Operator operator) {
        this.operator = operator;
        createUIComponents();
        add(wrapper);
        setSize(1000, 500);
        setLocation(Helper.screenCenter("x", getSize()), Helper.screenCenter("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);

        lbl_welcome.setText("Hoşgeldiniz " + operator.getName());



        // UserList
        mdl_user_list = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };
        Object[] col_user_list = {"ID", "Ad", "Kullanıcı Adı", "Şifre", "Üyelik Tipi"};
        mdl_user_list.setColumnIdentifiers(col_user_list);
        row_user_list = new Object[col_user_list.length];

        loadUserModel();

        tbl_user_list.setModel(mdl_user_list);
        tbl_user_list.getTableHeader().setReorderingAllowed(false);

        tbl_user_list.getSelectionModel().addListSelectionListener(e -> {
            try {
                String select_user_id = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 0).toString();
                fld_user_id.setText(select_user_id);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        tbl_user_list.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int user_id = Integer.parseInt(tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 0).toString());
                String user_name = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 1).toString();
                String user_uname = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 2).toString();
                String user_pass = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 3).toString();
                String user_type = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 4).toString();

                if (User.update(user_id, user_name, user_uname, user_pass, user_type)) {
                    Helper.showMsg("done");
                }
                loadUserModel();
                loadOperatorCombo();
                //loadPrimModel();
                loadItirazModel();
            }
            loadItirazModel();
        });


        tbl_prim_list.setModel(mdl_prim_list);
        tbl_prim_list.setComponentPopupMenu(primMenu);
        tbl_prim_list.getTableHeader().setReorderingAllowed(false);
        tbl_prim_list.getColumnModel().getColumn(0).setMaxWidth(75);

        tbl_prim_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selected_row = tbl_prim_list.rowAtPoint(point);
                tbl_prim_list.setRowSelectionInterval(selected_row, selected_row);
            }
        });

        // Itiraz List
        mdl_itiraz_list = new DefaultTableModel();
        Object[] col_itirazList = {"ID", "Durumu", "Prim", "Asistan", "Açıklama"};
        mdl_itiraz_list.setColumnIdentifiers(col_itirazList);
        row_itiraz_list = new Object[col_itirazList.length];


        tbl_itiraz_list.setModel(mdl_itiraz_list);
        tbl_itiraz_list.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_itiraz_list.getTableHeader().setReorderingAllowed(false);
        loadPrimTable();
        //loadPrimModel();

        loadItirazModel();
        loadPrimCombo();
        loadOperatorCombo();

        // Event Listeners
        btn_user_add.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_user_name) || Helper.isFieldEmpty(fld_user_uname) || Helper.isFieldEmpty(fld_user_pass)) {
                Helper.showMsg("fill");
            } else {
                String name = fld_user_name.getText();
                String uname = fld_user_uname.getText();
                String pass = fld_user_pass.getText();
                String type = cbm_user_type.getSelectedItem().toString();
                if (User.add(name, uname, pass, type)) {
                    Helper.showMsg("done");
                    loadUserModel();
                    loadOperatorCombo();
                    fld_user_name.setText(null); // Ekleme işlemi başarılı olduğunda, alanları temizleyin
                    fld_user_uname.setText(null);
                    fld_user_pass.setText(null);
                }
                loadUserModel();
            }
        });

        btn_user_delete.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_user_id)) {
                Helper.showMsg("fill");
            } else {
                if (Helper.confirm("sure")) {
                    int user_id = Integer.parseInt(fld_user_id.getText());
                    if (User.delete(user_id)) {
                        Helper.showMsg("done");
                        loadUserModel();
                        loadOperatorCombo();
                        loadItirazModel();
                        fld_user_id.setText(null);
                    } else {
                        Helper.showMsg("error");
                    }
                }
            }
        });

        btn_user_sh.addActionListener(e -> {
            String name = fld_sh_user_name.getText();
            String uname = fld_sh_user_uname.getText();
            String type = cmb_sh_user_type.getSelectedItem().toString();
            String query = User.searchQuery(name, uname, type);
            loadUserModel(User.searchUserList(query));
        });

        btn_logout.addActionListener(e -> {
            dispose();
            new LoginGUI();
        });

        btn_prim_add.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_prim_name)) {
                Helper.showMsg("fill");
            } else {
                try {
                    String primName = fld_prim_name.getText();
                    if (Prim.add(primName, 0, 0)) { // Günlük ve aylık maaş bilgisi 0 olarak atanıyor
                        Helper.showMsg("done");
                        loadPrimTable();
                        loadPrimCombo();
                        fld_prim_name.setText(null);
                    } else {
                        Helper.showMsg("error");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Helper.showMsg("An error occurred while adding the prim.");
                }
            }
        });


        tbl_itiraz_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selectedRow = tbl_itiraz_list.rowAtPoint(point);
                if (selectedRow != -1) { // Satır geçerli mi kontrol edin
                    tbl_itiraz_list.setRowSelectionInterval(selectedRow, selectedRow);
                }
            }
        });


        btn_itiraz_cevap.addActionListener(e -> {
            int selectedRow = tbl_itiraz_list.getSelectedRow();
            if (selectedRow == -1) {
                Helper.showMsg("Lütfen bir itiraz seçiniz.");
                return;
            }

            int itirazId = Integer.parseInt(tbl_itiraz_list.getValueAt(selectedRow, 0).toString());

            // Seçili öğelerin null olup olmadığını kontrol et
            Object selectedPrimItem = cmb_itiraz_prim.getSelectedItem();
            Object selectedOperatorItem = cmb_itiraz_operator.getSelectedItem();
            if (selectedPrimItem != null && selectedOperatorItem != null) {
                String cevap = cmb_cevap.getSelectedItem().toString(); // Seçilen değeri al

                if (Itiraz.cevapla(itirazId, cevap)) {
                    Helper.showMsg("done");
                    loadItirazModel(); // Tabloyu güncelle
                } else {
                    Helper.showMsg("error");
                }
            } else {
                Helper.showMsg("Lütfen bir prim ve bir operatör seçiniz."); // Öğelerden biri veya her ikisi de null ise kullanıcıya bir hata mesajı göster
            }
        });


    }

    private void loadItirazModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_itiraz_list.getModel();
        clearModel.setRowCount(0);
        for (Itiraz obj : Itiraz.getList()) {
            int i = 0;
            row_itiraz_list[i++] = obj.getId();
            row_itiraz_list[i++] = obj.getStatus();

            Prim prim = Prim.getFetch(obj.getPrim_id());
            String primName = prim != null ? prim.getName() : "";

            User assistant = User.getFetch(obj.getUser_id());
            String assistantName = assistant != null ? assistant.getName() : "";

            // Açıklama için Itiraz nesnesinden değeri alın
            String aciklama = obj.getAciklama();

            // Tabloya ekle
            row_itiraz_list[i++] = primName;
            row_itiraz_list[i++] = assistantName;
            row_itiraz_list[i++] = aciklama;

            mdl_itiraz_list.addRow(row_itiraz_list);
        }
    }


    /*
    public void loadPrimModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_prim_list.getModel();
        clearModel.setRowCount(0);
        for (Prim obj : Prim.getList()) {
            int i = 0;
            row_prim_list[i++] = obj.getId();
            row_prim_list[i++] = obj.getName();
            row_prim_list[i++] = obj.getDailySalary();
            row_prim_list[i++] = obj.getMonthlySalary();
            mdl_prim_list.addRow(row_prim_list);
        }
    }*/


    public void loadUserModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);
        for (User obj : User.getList()) {
            int i = 0;
            row_user_list[i++] = obj.getId();
            row_user_list[i++] = obj.getName();
            row_user_list[i++] = obj.getUname();
            row_user_list[i++] = obj.getPass();
            row_user_list[i++] = obj.getType();
            mdl_user_list.addRow(row_user_list);
        }
    }

    public void loadUserModel(ArrayList<User> list) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);
        for (User obj : list) {
            int i = 0;
            row_user_list[i++] = obj.getId();
            row_user_list[i++] = obj.getName();
            row_user_list[i++] = obj.getUname();
            row_user_list[i++] = obj.getPass();
            row_user_list[i++] = obj.getType();
            mdl_user_list.addRow(row_user_list);
        }
    }

    public void loadPrimCombo() {
        if (cmb_itiraz_prim != null) {
            cmb_itiraz_prim.removeAllItems();
            for (Prim obj : Prim.getList()) {
                cmb_itiraz_prim.addItem(new Item(obj.getId(), obj.getName()));
            }
        } else {
            System.err.println("cmb_itiraz_prim is null");
        }
    }

    public void loadOperatorCombo() {
        if (cmb_itiraz_operator != null) {
            cmb_itiraz_operator.removeAllItems();
        }
        for (User obj : User.getListOnlyOperator()) {
            cmb_itiraz_operator.addItem(new Item(obj.getId(), obj.getName()));
        }
    }
    private void loadPrimTable() {
        DefaultTableModel primModel = new DefaultTableModel();
        primModel.addColumn("ID");
        primModel.addColumn("Prim Adı");
        primModel.addColumn("Günlük Prim");
        primModel.addColumn("Aylık Prim");

        ArrayList<Prim> primList = Prim.getList();
        for (Prim prim : primList) {
            // Her bir prim için hesaplanan primleri al
            double dailyPrim = Prim.calculateDailyPrim(prim.getId());
            double monthlyPrim = Prim.calculateMonthlyPrim(prim.getId());

            primModel.addRow(new Object[]{
                    prim.getId(),
                    prim.getName(),
                    dailyPrim,
                    monthlyPrim
            });
        }

        tbl_prim_list.setModel(primModel); // Tablo modelini güncelle
    }



    private void searchUserByType(String type) {
        String name = fld_sh_user_name.getText();
        String uname = fld_sh_user_uname.getText();
        String query = User.searchQuery(name, uname, type);
        loadUserModel(User.searchUserList(query));
    }


    public static void main(String[] args) {
        Helper.setLayout();
        Operator op = new Operator();
        op.setId(1);
        op.setName("Emir");
        op.setPass("123");
        op.setUname("emo");
        new OperatorGUI(op);
    }

    private void createUIComponents() {
        //cbm_user_type = new JComboBox<>();
        //cmb_sh_user_type = new JComboBox<>();
        cmb_itiraz_operator = new JComboBox<>();
        cmb_itiraz_prim = new JComboBox<>();
        //cmb_cevap = new JComboBox<>(new String[]{"Onayla", "Reddet"});

        // tbl_prim_list için bir TableModel oluşturun
        mdl_prim_list = new DefaultTableModel();
        Object[] col_prim_list = {"ID", "Prim Adı", "Günlük Prim", "Aylık Prim"};
        mdl_prim_list.setColumnIdentifiers(col_prim_list);
        row_prim_list = new Object[col_prim_list.length];
    }

}
