import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.Thread.currentThread;

public class LoginLayout {
    Thread r = new Thread(new Runnable() {
        @Override
        public void run() {
            System.out.println("Login Layout Thread: " + currentThread());
            JLabel loginlabel = new JLabel("Логин:");
            JLabel passlabel = new JLabel("Пароль:");
            JTextField logintext = new JTextField("",8);
            JPasswordField passtext = new JPasswordField("", 8);
            JButton loginbtn = new JButton("Войти");
            SqlConnect sql = new SqlConnect();
            final JFrame frame = new JFrame("Login panel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout(5,5));
            final JPanel pane = new JPanel(new BorderLayout(5,5));
            final JPanel logscreen = new JPanel(new GridBagLayout());
            logintext.setPreferredSize(new Dimension(0,20));
            passtext.setPreferredSize(new Dimension(0,20));
            loginbtn.setPreferredSize(new Dimension(140,25));
            frame.add(pane);
            JPanel dynamicLabels = new JPanel(new BorderLayout(1,1));
            dynamicLabels.setBorder(new TitledBorder("Вход в учетную запись") );
            pane.add(dynamicLabels, BorderLayout.CENTER);
            dynamicLabels.add(logscreen, BorderLayout.CENTER);
            logscreen.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(110, 200, 110, 200), new EtchedBorder(5)));
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

            frame.pack();
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);

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
    });
}
