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
    private JTextField txt_itiraz_id;
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
            }
            loadItirazModel();
        });

        // PrimList
        primMenu = new JPopupMenu();
        JMenuItem updateMenu = new JMenuItem("Güncelle");
        JMenuItem deleteMenu = new JMenuItem("Sil");
        primMenu.add(updateMenu);
        primMenu.add(deleteMenu);

        updateMenu.addActionListener(e -> {
            int select_id = Integer.parseInt(tbl_prim_list.getValueAt(tbl_prim_list.getSelectedRow(), 0).toString());
            UpdatePrimGUI updatePrimGUI = new UpdatePrimGUI(Prim.getFetch(select_id));
            updatePrimGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowOpened(WindowEvent e) {
                    loadPrimModel();
                    loadPrimCombo();
                    loadItirazModel();
                }
            });
        });

        deleteMenu.addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int select_id = Integer.parseInt(tbl_prim_list.getValueAt(tbl_prim_list.getSelectedRow(), 0).toString());
                if (Prim.delete(select_id)) {
                    Helper.showMsg("done");
                    loadPrimModel();
                    loadPrimCombo();
                    loadItirazModel();
                } else {
                    Helper.showMsg("error");
                }
            }
        });

        mdl_prim_list = new DefaultTableModel();
        Object[] col_prim_list = {"ID", "Prim Listesi"};
        mdl_prim_list.setColumnIdentifiers(col_prim_list);
        row_prim_list = new Object[col_prim_list.length];
        loadPrimModel();

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
        Object[] col_itirazList = {"ID", "Itiraz Adı", "Durumu", "Prim", "Operator"};
        mdl_itiraz_list.setColumnIdentifiers(col_itirazList);
        row_itiraz_list = new Object[col_itirazList.length];
        loadItirazModel();
        tbl_itiraz_list.setModel(mdl_itiraz_list);
        tbl_itiraz_list.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_itiraz_list.getTableHeader().setReorderingAllowed(false);

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
                if (Prim.add(fld_prim_name.getText())) {
                    Helper.showMsg("done");
                    loadPrimModel();
                    loadPrimCombo();
                    fld_prim_name.setText(null);
                } else {
                    Helper.showMsg("error");
                }
            }
        });




        tbl_itiraz_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selected_row = tbl_prim_list.rowAtPoint(point);
                tbl_itiraz_list.setRowSelectionInterval(selected_row, selected_row);
            }
        });








        btn_itiraz_cevap.addActionListener(e -> {

                System.out.println("Gönder butonuna tıklandı!");



            // txt_itiraz_id bileşeninden itirazın ID'sini al
            int itirazId = Integer.parseInt(txt_itiraz_id.getText());

            // Cevaplama işlemlerini burada gerçekleştir
            // Örneğin:
            String cevap = cmb_cevap.getSelectedItem().toString(); // cmb_cevap'ten seçilen cevabı al

            if (Itiraz.cevapla(itirazId, cevap)) {
                Helper.showMsg("Itiraz cevaplandı.");
                // Cevaplama başarılı olduysa, gerekli güncellemeleri yapabilirsiniz
            } else {
                Helper.showMsg("Itiraz cevaplanamadı.");
            }
        });


    }


    private void loadItirazModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_itiraz_list.getModel();
        clearModel.setRowCount(0);

        for (Itiraz obj : Itiraz.getList()) {
            int i = 0;
            row_itiraz_list[i++] = obj.getId();
            row_itiraz_list[i++] = obj.getName();
            row_itiraz_list[i++] = obj.getStatus();

            // Ilgili Prim'in adını almak için Prim ID'sini kullan
            Prim prim = Prim.getFetch(obj.getPrim_id());
            String primName = prim != null ? prim.getName() : ""; // Eğer prim null ise boş string ata

            // Ilgili Operatörün adını almak için Operator ID'sini kullan
            User operator = User.getFetch(obj.getUser_id());
            String operatorName = operator != null ? operator.getName() : ""; // Eğer operator null ise boş string ata

            row_itiraz_list[i++] = primName;
            row_itiraz_list[i++] = operatorName;

            mdl_itiraz_list.addRow(row_itiraz_list);
        }
    }


    private void loadPrimModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_prim_list.getModel();
        clearModel.setRowCount(0);
        int i = 0;
        for (Prim obj : Prim.getList()) {
            i = 0;
            row_prim_list[i++] = obj.getId();
            row_prim_list[i++] = obj.getName();
            mdl_prim_list.addRow(row_prim_list);
        }
    }

    public void loadUserModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);
        int i = 0;
        for (User obj : User.getList()) {
            i = 0;
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
        cmb_itiraz_prim.removeAllItems();
        for (Prim obj : Prim.getList()) {
            cmb_itiraz_prim.addItem(new Item(obj.getId(), obj.getName()));
        }
    }

    public void loadOperatorCombo() {
        cmb_itiraz_operator.removeAllItems();
        for (User obj : User.getListOnlyOperator()) {
            cmb_itiraz_operator.addItem(new Item(obj.getId(), obj.getName()));
        }
    }


    // Belirli bir isme sahip itirazı getiren metod
    private Itiraz getItirazByName(String itirazName) {
        // Örnek olarak, itirazların bulunduğu bir liste olduğunu varsayalım
        ArrayList<Itiraz> itirazList = Itiraz.getList();

        // Listeyi dönerek isme göre itirazı arayalım
        for (Itiraz itiraz : itirazList) {
            if (itiraz.getName().equals(itirazName)) {
                return itiraz; // İsim bulunduğunda itirazı döndür
            }
        }

        return null; // İsim bulunamadığında null döndür
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
        // TODO: place custom component creation code here
    }
}
