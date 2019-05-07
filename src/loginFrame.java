import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class loginFrame extends JFrame {
    private JLabel loginlabel = new JLabel("Логин:");
    private JLabel passlabel = new JLabel("Пароль:");
    private JTextField logintext = new JTextField("",8);
    private JTextField passtext = new JTextField("", 8);
    private JButton loginbtn = new JButton("Войти");
    sqlConnect sql;


    loginFrame() {
        super("Login Frame");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLayout(new FlowLayout());
        this.setBounds(dim.width/3-this.getSize().width/3, dim.height/4-this.getSize().height/4, 640, 480);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = this.getContentPane();
        container.setLayout(null);
        this.setResizable(false);
        container.add(loginlabel);
        container.add(passlabel);
        container.add(logintext);
        container.add(passtext);
        container.add(loginbtn);
        loginlabel.setBounds(185,160,250,25);
        logintext.setBounds(185,180,250,25);
        passlabel.setBounds(185,205,250,25);
        passtext.setBounds(185,225,250,25);
        loginbtn.setBounds(185,265,250,25);
        loginbtn.addActionListener(new loginListener());
    }

    class loginListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                if (sql.getConnection().isValid(3)){
                    if (sql.loginHandler(logintext.getText().trim(), passtext.getText().trim())) {
                        dispose();
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }
}
