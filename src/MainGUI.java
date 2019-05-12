import com.sun.tools.javac.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

class MainGUI {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                //Content
                JLabel imglabel = new JLabel();
                JTextField firstname = new JTextField("", 10);
                JLabel namelabel = new JLabel("Имя:");
                JTextField secname = new JTextField("", 10);
                JLabel secnamelabel = new JLabel("Фамилия:");
                JTextField patron = new JTextField("", 10);
                JLabel patronlabel = new JLabel("Отчество:");
                JLabel rfidlabel = new JLabel("RFID:");
                JTextField rfid = new JTextField("", 10);
                JButton selectfile = new JButton("Фото");
                JFileChooser filechooser = new JFileChooser();
                JTextField position = new JTextField("", 10);
                JLabel poslabel = new JLabel("Должность:");
                JLabel accesslabel = new JLabel("Уровень доступа:");
                JButton button = new JButton("Добавить");
                JButton rfidbtn = new JButton("Сканировать карту пользователя");
                JButton unclickable = new JButton("Добавление пользователя");
                JButton journal = new JButton("Журнал пользователей");
                final String[] accesslevel = {"1", "2"};
                String[] colNames = {"Id", "Имя", "Фамилия", "Отчество", "Путь к фото", "Уровень доступа", "Должность"};
                JTable table = new JTable();
                JScrollPane scrollpane = new JScrollPane(table);
                JComboBox combobox = new JComboBox(accesslevel);
                sqlConnect sql = new sqlConnect();
                //Initializing Nested Frames
                final JFrame frame = new JFrame("Main GUI Panel");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout(5,5));
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                frame.setBounds(dim.width / 3 - frame.getSize().width / 3, dim.height / 4 - frame.getSize().height / 4, 640, 480);
                final JPanel pane = new JPanel(new BorderLayout(5,5));
                frame.add(pane, BorderLayout.CENTER);
                final JPanel leftpane = new JPanel(new GridBagLayout());
                final JPanel bottompane = new JPanel(new FlowLayout());
                final JPanel centerpane = new JPanel(new BorderLayout());
                final JPanel rightpane = new JPanel(new GridLayout());
                pane.add(rightpane, BorderLayout.EAST);
                pane.add(leftpane, BorderLayout.WEST);
                pane.add(bottompane, BorderLayout.SOUTH);
                pane.add(centerpane, BorderLayout.CENTER);
                GridBagConstraints c = new GridBagConstraints();
               //Content placehold && dimensions
                selectfile.setPreferredSize(new Dimension(220,20));
                rfidbtn.setPreferredSize(new Dimension(220,20));
                button.setPreferredSize(new Dimension(220, 20));
                combobox.setPreferredSize(new Dimension(113,20));
                unclickable.setPreferredSize(new Dimension(250,30));
                journal.setPreferredSize(new Dimension(250,30));
                imglabel.setPreferredSize(new Dimension(300,20));
                //Left pane
                c.weightx = 0.5;
                c.insets = new Insets(4,4,0,0);
                c.weighty = 0.5;
                c.gridx = 0;
                c.gridy = 0;
                c.anchor = GridBagConstraints.LINE_START;
                leftpane.add(namelabel,c);
                c.gridy = 1;
                leftpane.add(secnamelabel,c);
                c.gridy = 2;
                leftpane.add(patronlabel,c);
                c.gridy = 3;
                c.gridwidth = 2;
                leftpane.add(selectfile,c);
                c.gridwidth = 1;
                c.gridy = 4;
                c.gridx = 0;
                leftpane.add(rfidlabel,c);
                c.gridy = 5;
                c.gridwidth = 2;
                leftpane.add(rfidbtn,c);
                c.gridwidth = 1;
                c.gridy = 6;
                leftpane.add(poslabel,c);
                c.gridy = 7;
                leftpane.add(accesslabel,c);
                //Labels done
                c.gridx = 1;
                c.gridy = 0;
                leftpane.add(firstname,c);
                c.gridy = 1;
                leftpane.add(secname,c);
                c.gridy = 2;
                leftpane.add(patron,c);
                c.gridy = 4;
                leftpane.add(rfid,c);
                c.gridy = 6;
                leftpane.add(position,c);
                c.gridy = 7;
                leftpane.add(combobox,c);
                c.gridy++; c.gridwidth = 2; c.gridx = 0;
                leftpane.add(button,c);
                //center pane
                centerpane.add(imglabel);
                //right pane (TABLE for MSQL)
                DefaultTableModel model = new DefaultTableModel();
                model.setColumnIdentifiers(colNames);
                table.setModel(model);
                rightpane.add(scrollpane);
                table.setFillsViewportHeight(true);
                table.setPreferredScrollableViewportSize(new Dimension(600,100));
                Object[] rowData = new Object[7];
                for(int i = 0; i < sql.getUsers().size(); i++) {
                    rowData[0] = sql.getUsers().get(i).getId();
                    rowData[1] = sql.getUsers().get(i).getFname();
                    rowData[2] = sql.getUsers().get(i).getSname();
                    rowData[3] = sql.getUsers().get(i).getLname();
                    rowData[4] = sql.getUsers().get(i).getPhoto();
                    rowData[5] = sql.getUsers().get(i).getAccess();
                    rowData[6] = sql.getUsers().get(i).getPosition();
                    model.addRow(rowData);
                }
                //misc frame stuff
                unclickable.setEnabled(false);
              //  bottompane.add(unclickable);
             //   bottompane.add(journal);
                frame.pack();
                frame.setVisible(true);
            //    rfid.setEnabled(false);
                unclickable.setEnabled(false);
                frame.setResizable(false);
                //Select image of user button
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
                                Image dimimg = image.getScaledInstance(imglabel.getWidth(), imglabel.getHeight(),
                                        Image.SCALE_SMOOTH);
                                imglabel.setIcon(new ImageIcon(dimimg));
                            } catch (Exception ex) {
                                System.out.println(ex);
                                //ShowError("Выберите файл с раширением JPG!");
                            }

                        }
                    }
                });
                //Post user button
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sqlConnect sql = new sqlConnect();
                        try {
                            if (firstname.getText().equals("") || secname.getText().equals("") || patron.getText().equals("") || imglabel == null || position.getText().equals("")) {
                                ShowError("Вы заполнили не все поля!");
                            } else {
                                sql.postUser(firstname.getText().trim(),
                                        secname.getText().trim(),
                                        patron.getText().trim(),
                                        filechooser.getSelectedFile().getAbsolutePath(),
                                        position.getText().trim(),
                                        combobox.getSelectedItem().toString(),
                                        rfid.getText().trim());
                                        scrollpane.repaint();
                                        table.revalidate();
                                        scrollpane.revalidate();
                                        table.repaint();
                            }
                        } catch (Exception ex) {
                            ShowError("Вы не выбрали изображение/произошла другая ошибка!");
                            System.out.println(ex);
                        }
                    }
                });
                //Open new ScanCardLayout
                rfidbtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        layoutChanger layout = new layoutChanger();
                        layout.changeLayout(3);
                    }
                });

            }
        };

    public void ShowError(String errorMsg) {
        JOptionPane.showMessageDialog(null, errorMsg, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }
}
