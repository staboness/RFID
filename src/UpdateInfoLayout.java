import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by Rodin Maxim on Май, 2019
 */
public class UpdateInfoLayout {
    int id;
    String filechooserAbsolutePath;
    JFrame frame;
    JPanel pane;
    JPanel centerpane;
    JPanel leftpane;
    JTextField userrfid;
    JLabel imglabel;
    JTextField fname;
    JLabel namelabel;
    JTextField sname;
    JLabel secnamelabel;
    JTextField patronymic;
    JLabel patronlabel;
    JLabel rfidlabel;
    JButton selectfile;
    JFileChooser filechooser;
    JTextField pos;
    JLabel poslabel;
    JLabel accesslabel;
    JButton update;
    JComboBox combobox;
    SqlConnect sql = new SqlConnect();
    MainGUI gui = MainGUI.getInstance();
    final String[] accesslevel = {"1", "2"};

    public UpdateInfoLayout(String idString, String firstname, String secname, String patron, String image, String rfid, String level, String position){
        frameinit();
        framecontent();
        fillingPanels();
        elementsSize();
        frameFinish();
        id = Integer.parseInt(idString);
        this.fname.setText(firstname);
        this.sname.setText(secname);
        this.patronymic.setText(patron);
        filechooserAbsolutePath = image;
        this.userrfid.setText(rfid);
        combobox.setSelectedItem(level);
        this.pos.setText(position);

        selectfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BufferedImage image;
                filechooser.setCurrentDirectory(new java.io.File("V:/HD wallpapers"));
                filechooser.setDialogTitle("Выберите фото сотрудника");
                filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                if (filechooser.showOpenDialog(selectfile) == JFileChooser.APPROVE_OPTION) {
                    try {
                        image = ImageIO.read(new File(filechooser.getSelectedFile().getAbsolutePath()));
                        filechooserAbsolutePath = filechooser.getSelectedFile().getAbsolutePath();
                        Image dimimg = image.getScaledInstance(imglabel.getWidth(), imglabel.getHeight(),
                                Image.SCALE_SMOOTH);
                        imglabel.setIcon(new ImageIcon(dimimg));
                        sql.getConnection().close();
                        System.out.println("Connection closed at imagebutton");
                    } catch (Exception ex) {
                        System.out.println(ex);
                        sql.ShowError("Выберите файл с раширением JPG!");
                    }

                }
            }
        });

        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sql.updateUsers(id,
                            fname.getText().trim(),
                            sname.getText().trim(),
                            patronymic.getText().trim(),
                            filechooserAbsolutePath,
                            userrfid.getText().trim(),
                            combobox.getSelectedItem().toString().trim(),
                            pos.getText().trim());
                    frame.dispose();
                    gui.startTableFill();
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        });
    }

    private void frameinit(){
        frame = new JFrame("Update Info Panel");
        pane = new JPanel(new BorderLayout(5, 5));
        centerpane = new JPanel(new BorderLayout());
        leftpane = new JPanel(new GridBagLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout(5, 5));
        frame.add(pane, BorderLayout.CENTER);
        pane.add(centerpane, BorderLayout.CENTER);
        pane.add(leftpane, BorderLayout.WEST);
    }
    private void frameFinish(){
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
    }
    private void framecontent(){
        imglabel = new JLabel();
        fname = new JTextField("", 10);
        namelabel = new JLabel("Имя:");
        sname = new JTextField("", 10);
        secnamelabel = new JLabel("Фамилия:");
        patronymic = new JTextField("", 10);
        patronlabel = new JLabel("Отчество:");
        rfidlabel = new JLabel("RFID:");
        userrfid = new JTextField("", 10);
        selectfile = new JButton("Фото");
        filechooser = new JFileChooser();
        pos = new JTextField("", 10);
        poslabel = new JLabel("Должность:");
        accesslabel = new JLabel("Уровень доступа:");
        update = new JButton("Обновить");
        combobox = new JComboBox(accesslevel);
    }
    private void elementsSize(){
        selectfile.setPreferredSize(new Dimension(220, 20));
        update.setPreferredSize(new Dimension(220, 20));
        combobox.setPreferredSize(new Dimension(113, 20));
        imglabel.setPreferredSize(new Dimension(300, 20));
    }
    private void fillingPanels(){
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        leftpane.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10), new EtchedBorder()));
        c.weightx = 0.5;
        c.insets = new Insets(4, 4, 0, 0);
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        leftpane.add(namelabel, c);
        c.gridy = 1;
        leftpane.add(secnamelabel, c);
        c.gridy = 2;
        leftpane.add(patronlabel, c);
        c.gridy = 3;
        c.gridwidth = 2;
        leftpane.add(selectfile, c);
        c.gridwidth = 1;
        c.gridy = 4;
        c.gridx = 0;
        leftpane.add(rfidlabel, c);
        c.gridwidth = 1;
        c.gridy = 6;
        leftpane.add(poslabel, c);
        c.gridy = 7;
        leftpane.add(accesslabel, c);
        //Labels done
        c.gridx = 1;
        c.gridy = 0;
        leftpane.add(fname, c);
        c.gridy = 1;
        leftpane.add(sname, c);
        c.gridy = 2;
        leftpane.add(patronymic, c);
        c.gridy = 4;
        leftpane.add(userrfid, c);
        c.gridy = 6;
        leftpane.add(pos, c);
        c.gridy = 7;
        leftpane.add(combobox, c);
        c.gridy++;
        c.gridwidth = 2;
        c.gridx = 0;
        leftpane.add(update, c);
        //center pane
        centerpane.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10), new EtchedBorder()));
        centerpane.add(imglabel);
    }
}
