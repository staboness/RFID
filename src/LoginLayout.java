import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LoginLayout {
    JLabel loginlabel;
    JLabel passlabel;
    JTextField logintext;
    JPasswordField passtext;
    JButton loginbtn;
    final JFrame frame;
    final JPanel pane;
    final JPanel logscreen;
    JPanel dynamicLabels;
    SqlConnect sql = new SqlConnect();
    public LoginLayout() {
            //Initializing swing
            loginlabel = new JLabel("Логин:");
            passlabel = new JLabel("Пароль:");
            logintext = new JTextField("",8);
            passtext = new JPasswordField("", 8);
            loginbtn = new JButton("Войти");
            frame = new JFrame("Login panel");
            pane  = new JPanel(new BorderLayout(5,5));
            logscreen = new JPanel(new GridBagLayout());
            dynamicLabels = new JPanel(new BorderLayout(1,1));
            //Frame etc.
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout(5,5));
            logintext.setPreferredSize(new Dimension(0,20));
            passtext.setPreferredSize(new Dimension(0,20));
            loginbtn.setPreferredSize(new Dimension(140,25));
            frame.add(pane);
            dynamicLabels.setBorder(new TitledBorder("Вход в учетную запись") );
            //Add borders
            pane.add(dynamicLabels, BorderLayout.CENTER);
            dynamicLabels.add(logscreen, BorderLayout.CENTER);
            logscreen.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(110, 200, 110, 200), new EtchedBorder(5)));
            //Add components to pane
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(4,4,0,0);
            c.anchor = GridBagConstraints.LINE_END;
            logscreen.add(loginlabel, c);
            c.gridy++;
            logscreen.add(passlabel, c);
            c.gridy++;
            c.gridwidth = 2;
            logscreen.add(loginbtn, c);
            c.gridx = 1;
            c.anchor = GridBagConstraints.LINE_START;
            c.gridy = 0;
            c.gridwidth = 1;
            logscreen.add(logintext, c);
            c.gridy++;
            logscreen.add(passtext, c);
            //Finish frame operations
            frame.pack();
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
            //Login btn listener
            loginbtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        if (sql.getConnection().isValid(3)){
                            if (sql.loginHandler(logintext.getText().trim(), passtext.getText().trim())) {
                                frame.dispose();
                            }
                        }
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            });
        }
}
