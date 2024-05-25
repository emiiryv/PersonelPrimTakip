package personalprimtakip.View;

import personalprimtakip.Helper.Config;
import personalprimtakip.Helper.Helper;
import personalprimtakip.Model.Operator;
import personalprimtakip.Model.User;

import javax.swing.*;

public class LoginGUI extends JFrame {
    private JPanel wrapper;
    private JPanel wtop;
    private JPanel wbottom;
    private JTextField fld_user_uname;
    private JPasswordField fld_user_pass;
    private JButton btn_login;
    private static User currentUser;  // Oturum açan kullanıcıyı saklamak için statik değişken

    public LoginGUI() {
        add(wrapper);
        setSize(400, 400);
        setLocation(Helper.screenCenter("x", getSize()), Helper.screenCenter("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);
        btn_login.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_user_uname) || Helper.isFieldEmpty(fld_user_pass)) {
                Helper.showMsg("fill");
            } else {
                User u = User.getFetch(fld_user_uname.getText(), new String(fld_user_pass.getPassword()));
                if (u == null) {
                    Helper.showMsg("Kullanıcı bulunamadı.");
                } else {
                    currentUser = u;  // Oturum açan kullanıcıyı sakla
                    switch (u.getType()) {
                        case "operator":
                            OperatorGUI opGUI = new OperatorGUI((Operator) u);
                            break;
                        case "asistan":
                            AsistanGUI aGUI = new AsistanGUI();
                            break;
                    }
                    dispose();
                }
            }
        });
    }

    public static User getCurrentUser() {
        return currentUser;  // Oturum açan kullanıcıyı döndür
    }

    public static void main(String[] args) {
        Helper.setLayout();
        LoginGUI login = new LoginGUI();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
