package personalprimtakip.View;

import personalprimtakip.Helper.Config;
import personalprimtakip.Helper.Helper;
import personalprimtakip.Model.Prim;

import javax.swing.*;

public class UpdatePrimGUI extends JFrame {
    private JPanel wrapper;
    private JTextField fld_prim_name;
    private JButton btn_update;
    private Prim prim;

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
            }else {
                if (Prim.update(prim.getId(),fld_prim_name.getText())){
                    Helper.showMsg("done");
                }
                dispose();
            }
        });
    }




    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
