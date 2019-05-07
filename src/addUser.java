import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

public class addUser extends JFrame {
    private JLabel imglabel = new JLabel();
    private JTextField firstname = new JTextField("", 8); private JLabel namelabel = new JLabel("Имя:");
    private JTextField secname = new JTextField("", 8); private JLabel secnamelabel = new JLabel("Фамилия:");
    private JTextField patron = new JTextField("", 10); private JLabel patronlabel = new JLabel("Отчество:");
    private JButton selectfile = new JButton("Выбрать фото");
    private JFileChooser filechooser = new JFileChooser();
    private JTextField position = new JTextField("", 10); private JLabel poslabel = new JLabel("Должность:");
    private JComboBox combobox; private JLabel accesslabel = new JLabel("Уровень доступа:");
    private JButton button = new JButton("Добавить");
    private JButton unclickable = new JButton("Добавление пользователя");
    private JButton journal = new JButton("Журнал пользователей");
    private final String[] accesslevel = {"1", "2"};
    sqlConnect sql;

    public addUser() {
        setTitle("RFID APP");
        setLayout(new FlowLayout());
        combobox = new JComboBox(accesslevel);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(dim.width/3-this.getSize().width/3, dim.height/4-this.getSize().height/4, 640, 480);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = this.getContentPane();
        container.setLayout(null);
        this.setResizable(false);
        firstname.setBounds(10,45,250,25);
        namelabel.setBounds(10,20,250,25);
        secname.setBounds(10,90,250,25);
        secnamelabel.setBounds(10,65,250,25);
        patron.setBounds(10,135,250,25);
        patronlabel.setBounds(10, 110, 250, 25);
        selectfile.setBounds(10,165,250,25);
        position.setBounds(10,215,250,25);
        poslabel.setBounds(10, 190, 250, 25);
        combobox.setBounds(10,260,250,25);
        accesslabel.setBounds(10,235,250,25);
        imglabel.setBounds(270, 45, 340, 270);
        button.setBounds(10, 290, 250, 25);
        unclickable.setBounds(10, 380, 300, 50);
        journal.setBounds( 310, 380, 300, 50);
        container.add(firstname); container.add(namelabel);
        container.add(secname); container.add(secnamelabel);
        container.add(patron); container.add(patronlabel);
        container.add(selectfile);
        container.add(position); container.add(poslabel);
        container.add(combobox); container.add(accesslabel);
        container.add(button);
        container.add(imglabel);
        container.add(unclickable);
        container.add(journal);
        unclickable.setEnabled(false);
        selectfile.addActionListener(new imageButton());
        button.addActionListener(new postButton());
        journal.addActionListener(new journal());

    }
    class journal implements ActionListener{
        public void actionPerformed (ActionEvent e){
            sqlConnect sql = new sqlConnect();
            try {
                dispose();
                sql.changeLayout(1);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }
    class imageButton implements ActionListener {
        public void actionPerformed (ActionEvent e){
            BufferedImage image;
            filechooser.setCurrentDirectory(new java.io.File("V:/HD wallpapers"));
            filechooser.setDialogTitle("Выберите фото сотрудника");
            filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (filechooser.showOpenDialog(selectfile) == JFileChooser.APPROVE_OPTION){
                try {
                    image = ImageIO.read(new File(filechooser.getSelectedFile().getAbsolutePath()));
                    Image dimimg = image.getScaledInstance(imglabel.getWidth(), imglabel.getHeight(),
                            Image.SCALE_SMOOTH);
                    imglabel.setIcon(new ImageIcon(dimimg));
                } catch (Exception ex) {
                    System.out.println(ex);
                    ShowError("Выберите файл с раширением JPG!");
                }

            }
        }
    }
    class postButton implements ActionListener {
        public void actionPerformed (ActionEvent e) {
            try {
                if (firstname.getText().equals("") || secname.getText().equals("") || patron.getText().equals("") || imglabel == null || position.getText().equals("")) {
                    ShowError("Вы заполнили не все поля!");
                } else {
                    sql.postUser(firstname.getText().trim(),
                            secname.getText().trim(),
                            patron.getText().trim(),
                            filechooser.getSelectedFile().getAbsolutePath(),
                            position.getText().trim(),
                            combobox.getSelectedItem().toString());
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }
    public void ShowError (String errorMsg) {
        JOptionPane.showMessageDialog(null, errorMsg, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }
}