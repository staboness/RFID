/**
 * Created by Rodin Maxim on May, 2019
 */
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

class MainGUI {
    //Singleton pattern
    private static MainGUI instance = null;
    BufferedImage image;
    String filechooserAbsolutePath;
    JFrame frame;
    JPanel pane;
    JPanel leftpane;
    JPanel bottompane;
    JPanel centerpane;
    JPanel rightpane;
    JTextField rfid;
    JLabel imglabel;
    JTextField firstname;
    JLabel namelabel;
    JTextField secname;
    JLabel secnamelabel;
    JTextField patron;
    JLabel patronlabel;
    JLabel rfidlabel;
    JButton selectfile;
    JFileChooser filechooser;
    JTextField position;
    JLabel poslabel;
    JLabel accesslabel;
    JButton button;
    JButton rfidbtn;
    JTable table;
    JScrollPane scrollpane;
    JComboBox combobox;
    DefaultTableModel model;
    final String[] accesslevel = {"1", "2"};
    String[] colNames = {"Id", "Имя", "Фамилия", "Отчество", "Путь к фото", "RFID", "Уровень доступа", "Должность"};
    SqlConnect sql = new SqlConnect();

    public MainGUI() {
        //Content
        framecontent();
        //Initializing Nested Frames
        frameinit();
        //Content placehold && dimensions
        elementsSize();
        //Add buttons'n etc. to frames
        fillingPanels();
        //misc frame stuff
        frameFinish();
        //Table action listener
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                    try {
                        image = ImageIO.read(new File(table.getValueAt(table.getSelectedRow(), 4).toString()));
                        Image dimimg = image.getScaledInstance(imglabel.getWidth(), imglabel.getHeight(), Image.SCALE_SMOOTH);
                        UpdateInfoLayout update = new UpdateInfoLayout(table.getValueAt(table.getSelectedRow(), 0).toString(),
                                table.getValueAt(table.getSelectedRow(), 1).toString(),
                                table.getValueAt(table.getSelectedRow(), 2).toString(),
                                table.getValueAt(table.getSelectedRow(), 3).toString(),
                                table.getValueAt(table.getSelectedRow(), 4).toString(),
                                table.getValueAt(table.getSelectedRow(), 5).toString(),
                                table.getValueAt(table.getSelectedRow(), 6).toString(),
                                table.getValueAt(table.getSelectedRow(), 7).toString());
                        update.imglabel.setIcon(new ImageIcon(dimimg));
                        System.out.println(table.getValueAt(table.getSelectedRow(), 0).toString());
                    } catch (Exception e) {
                        System.out.println(e);
                    }

                }
            }
        });
        //Select image of user action listener
        selectfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
        //Post user listener
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SqlConnect sql = new SqlConnect();
                try {
                    if (firstname.getText().equals("") || secname.getText().equals("") || patron.getText().equals("") || imglabel == null || position.getText().equals("") || rfid.getText().equals("")) {
                        sql.ShowError("Вы заполнили не все поля!");
                    } else {
                        sql.postUser(firstname.getText().trim(),
                                secname.getText().trim(),
                                patron.getText().trim(),
                                filechooserAbsolutePath,
                                position.getText().trim(),
                                combobox.getSelectedItem().toString(),
                                rfid.getText().trim());
                                startTableFill();
                                sql.getConnection().close();
                    }
                } catch (Exception ex) {
                    sql.ShowError("Вы не выбрали изображение/произошла другая ошибка!");
                    System.out.println(ex);
                }
            }
        });
        //Open new CardLayout
        rfidbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout card = new CardLayout();
            }
        });
    }

    private void frameFinish(){
        frame.pack();
        try {
            image = ImageIO.read(new File(getClass().getResource("res/choosephoto.jpg").toURI()));
            filechooserAbsolutePath = "res/choosephoto.jpg";
            Image dimimg = image.getScaledInstance(imglabel.getWidth(), imglabel.getHeight(), Image.SCALE_SMOOTH);
            imglabel.setIcon(new ImageIcon(dimimg));
        } catch (Exception ex) {
            System.out.println(ex);
        }
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        rfid.setEnabled(false);
        frame.setResizable(false);
    }

    private void framecontent(){
        imglabel = new JLabel("");
        firstname = new JTextField("", 10);
        namelabel = new JLabel("Имя:");
        secname = new JTextField("", 10);
        secnamelabel = new JLabel("Фамилия:");
        patron = new JTextField("", 10);
        patronlabel = new JLabel("Отчество:");
        rfidlabel = new JLabel("RFID:");
        rfid = new JTextField("", 10);
        selectfile = new JButton("Фото");
        filechooser = new JFileChooser();
        position = new JTextField("", 10);
        poslabel = new JLabel("Должность:");
        accesslabel = new JLabel("Уровень доступа:");
        button = new JButton("Добавить");
        rfidbtn = new JButton("Сканировать карту пользователя");
        table = new JTable();
        scrollpane = new JScrollPane(table);
        combobox = new JComboBox(accesslevel);
    }

    private void frameinit(){
        frame = new JFrame("Main GUI Panel");
        pane = new JPanel(new BorderLayout(5, 5));
        leftpane = new JPanel(new GridBagLayout());
        bottompane = new JPanel(new FlowLayout());
        centerpane = new JPanel(new BorderLayout());
        rightpane = new JPanel(new GridLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(5, 5));
        frame.add(pane, BorderLayout.CENTER);
        pane.add(rightpane, BorderLayout.EAST);
        pane.add(leftpane, BorderLayout.WEST);
        pane.add(bottompane, BorderLayout.SOUTH);
        pane.add(centerpane, BorderLayout.CENTER);
    }

    private void elementsSize(){
        selectfile.setPreferredSize(new Dimension(220, 20));
        rfidbtn.setPreferredSize(new Dimension(220, 20));
        button.setPreferredSize(new Dimension(220, 20));
        combobox.setPreferredSize(new Dimension(113, 20));
        imglabel.setPreferredSize(new Dimension(300, 20));
    }

    protected void startTableFill() throws Exception {
        SqlConnect sql = new SqlConnect();
        model.setRowCount(0);
        Object[] rowData = new Object[8];
        for (int i = 0; i < sql.getUsers().size(); i++) {
            rowData[0] = sql.getUsers().get(i).getId();
            rowData[1] = sql.getUsers().get(i).getFname();
            rowData[2] = sql.getUsers().get(i).getSname();
            rowData[3] = sql.getUsers().get(i).getLname();
            rowData[4] = sql.getUsers().get(i).getPhoto();
            rowData[5] = sql.getUsers().get(i).getRfid();
            rowData[6] = sql.getUsers().get(i).getAccess();
            rowData[7] = sql.getUsers().get(i).getPosition();
            model.addRow(rowData);
            table.revalidate();
        }
        try {
            sql.getConnection().close();
            System.out.println("Connection closed at startTableFill");
        } catch (Exception e) {
            System.out.println(e);
        }
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
        c.gridy = 5;
        c.gridwidth = 2;
        leftpane.add(rfidbtn, c);
        c.gridwidth = 1;
        c.gridy = 6;
        leftpane.add(poslabel, c);
        c.gridy = 7;
        leftpane.add(accesslabel, c);
        //Labels done
        c.gridx = 1;
        c.gridy = 0;
        leftpane.add(firstname, c);
        c.gridy = 1;
        leftpane.add(secname, c);
        c.gridy = 2;
        leftpane.add(patron, c);
        c.gridy = 4;
        leftpane.add(rfid, c);
        c.gridy = 6;
        leftpane.add(position, c);
        c.gridy = 7;
        leftpane.add(combobox, c);
        c.gridy++;
        c.gridwidth = 2;
        c.gridx = 0;
        leftpane.add(button, c);
        //center pane
        centerpane.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10), new EtchedBorder()));
        centerpane.add(imglabel);
        //right pane (TABLE for MSQL)
        model = new DefaultTableModel();
        model.setColumnIdentifiers(colNames);
        table.setModel(model);
        rightpane.add(scrollpane);
        table.setFillsViewportHeight(true);
        table.setPreferredScrollableViewportSize(new Dimension(600, 100));
        try {
            startTableFill();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void setRfidUID (String UID) {
        rfid.setText(UID);
    }

    public void setRfidEnabled() {
        rfid.setEnabled(true);
    }

    public void setFilechooserAbsolutePath(String filechooserAbsolutePath) {
        this.filechooserAbsolutePath = filechooserAbsolutePath;
    }
    //Singleton pattern
    public static MainGUI getInstance(){
        if(instance == null)
            instance =  new MainGUI();
        return instance;
    }
}