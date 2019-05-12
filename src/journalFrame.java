import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;


public class journalFrame extends JFrame {

    private JLabel imglabel = new JLabel();
    private JTextField firstname = new JTextField("", 8); private JLabel namelabel = new JLabel("Имя:");
    private JTextField secname = new JTextField("", 8); private JLabel secnamelabel = new JLabel("Фамилия:");
    private JTextField patron = new JTextField("", 10); private JLabel patronlabel = new JLabel("Отчество:");
    private JButton adduserbtn = new JButton("Добавление пользователя");
    private JButton unclickable = new JButton("Журнал пользователей");
    String[] colNames = {"Id", "Имя", "Фамилия", "Отчество", "Путь к фото", "Уровень доступа", "Должность"};
    private JTable table = new JTable();
    JScrollPane pane = new JScrollPane(table);

    public journalFrame(){
        sqlConnect sql = new sqlConnect();
        setTitle("RFID APP");
        setLayout(new FlowLayout());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(dim.width/3-this.getSize().width/3, dim.height/4-this.getSize().height/4, 640, 480);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = this.getContentPane();
        container.setLayout(null);
        this.setResizable(false);
        adduserbtn.setBounds(10, 380, 300, 50);
        unclickable.setBounds( 310, 380, 300, 50);
        firstname.setBounds(10,135,250,25);
        namelabel.setBounds(10,110,250,25);
        secname.setBounds(10,185,250,25);
        secnamelabel.setBounds(10,160,250,25);
        patron.setBounds(10,235,250,25);
        patronlabel.setBounds(10, 210, 250, 25);
        imglabel.setBounds(290, 120, 300, 230);
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(colNames);
        table.setModel(model);
        pane.setBounds(10,10,600,100);
        table.setFillsViewportHeight(true);
        table.setPreferredScrollableViewportSize(new Dimension(600,200));
        unclickable.setEnabled(false);
        container.add(firstname); container.add(namelabel);
        container.add(secname); container.add(secnamelabel);
        container.add(patron); container.add(patronlabel);
        container.add(pane);
        container.add(adduserbtn);
        container.add(unclickable);
        container.add(imglabel);
        adduserbtn.addActionListener(new addUserEvent());
        Object[] rowData = new Object[7];
        for(int i = 0; i < sql.getUsers().size(); i++){
            rowData[0] = sql.getUsers().get(i).getId();
            rowData[1] = sql.getUsers().get(i).getFname();
            rowData[2] = sql.getUsers().get(i).getSname();
            rowData[3] = sql.getUsers().get(i).getLname();
            rowData[4] = sql.getUsers().get(i).getPhoto();
            rowData[5] = sql.getUsers().get(i).getAccess();
            rowData[6] = sql.getUsers().get(i).getPosition();
            model.addRow(rowData);
        }
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });

    }
    class addUserEvent implements ActionListener{
        public void actionPerformed(ActionEvent e){
            layoutChanger layout = new layoutChanger();
            try {
                dispose();
                layout.changeLayout(2);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        int selectedRowIndex = table.getSelectedRow();
        firstname.setText(model.getValueAt(selectedRowIndex, 1).toString());
        secname.setText(model.getValueAt(selectedRowIndex, 2).toString());
        patron.setText(model.getValueAt(selectedRowIndex, 3).toString());
        try {
            File folderInput = new File(model.getValueAt(selectedRowIndex, 4).toString());
            BufferedImage folderImage = ImageIO.read(folderInput);
            Image dimimg = folderImage.getScaledInstance(imglabel.getWidth(), imglabel.getHeight(),
                    Image.SCALE_SMOOTH);
            imglabel.setIcon(new ImageIcon(dimimg));
        } catch (Exception ex){
            System.out.println(ex);
        }

    }


}
