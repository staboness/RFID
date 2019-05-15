/**
 * Created by Rodin Maxim on May, 2019
 */
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CardLayout {
    public String cuid;
    String readline;
    SerialPort comPort;
    String commPort = "COM7";
    int baudrate = 9600;
    JFrame frame;
    JPanel pane;
    JPanel scan;
    JPanel dynamicLabels;
    JLabel scanmsg;
    JButton manual;
    MainGUI gui = new MainGUI();

    public CardLayout(String name, String secname, String patron, String position, Icon img, String imagepath) {
        frameinit();
        elementsSize();
        borders();
        //Dont pay attention
        gui.firstname.setText(name);
        gui.secname.setText(secname);
        gui.patron.setText(patron);
        gui.position.setText(position);
        gui.imglabel.setIcon(img);
        gui.setFilechooserAbsolutePath(imagepath);
        //Frame finish
        components();
        frameFinish();
        //Button to manually enter UID
        manual.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.setRfidEnabled();
                frame.dispose();
            }
        });
        initializeSerialPort();
    }
    //Initializing frame itself
    private void frameinit(){
        frame = new JFrame("Card Scan Panel");
        pane = new JPanel(new BorderLayout(5, 5));
        scan = new JPanel(new GridBagLayout());
        dynamicLabels = new JPanel(new BorderLayout(1, 1));
        scanmsg = new JLabel("Ожидание данных с карты...");
        manual = new JButton("Ввести UID вручную...");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout(5, 5));
    }
    //Setting elements size
    private void elementsSize(){
        scanmsg.setPreferredSize(new Dimension(170, 25));
        manual.setPreferredSize(new Dimension(170, 25));
    }
    //Doing borders
    private void borders(){
        frame.add(pane);
        dynamicLabels.setBorder(new TitledBorder("Считывание данных с карты"));
        pane.add(dynamicLabels, BorderLayout.CENTER);
        dynamicLabels.add(scan, BorderLayout.CENTER);
        scan.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(110, 200, 110, 200), new EtchedBorder(5)));
    }
    //Components placeholder
    private void components(){
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(4, 4, 0, 0);
        scan.add(scanmsg, c);
        c.gridy++;
        scan.add(manual, c);
    }

    private void frameFinish(){
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    void initializeSerialPort() {
        //System.out.println("Connecting to "+commPort+" with speed "+baudrate+" (check these from Arduino IDE!)");
        comPort = SerialPort.getCommPort(commPort);
        comPort.openPort();
        comPort.setBaudRate(baudrate);
        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                    System.err.println("No data on SerialPort");
                    return;
                }
                int bytesAvailable = comPort.bytesAvailable();
                if (bytesAvailable < 1) {
                    // System.err.println("Can not read from SerialPort");
                    return;
                }
                byte[] newData = new byte[bytesAvailable];
                int numRead = comPort.readBytes(newData, newData.length);
                // System.out.println("Read " + numRead + " bytes.");
                if (numRead > 0) {
                    for (int i = 0; i < newData.length; ++i) {
                        if ((char) newData[i] == '\n' || (char) newData[i] == '\r') {
                            readline = readline.trim();
                            if (readline.length() > 0) receive(readline);
                            readline = "";
                        } else
                            readline = readline + (char) newData[i];
                    }
                }
            }
        });
    }

    public void receive(String line) {
        if (line == null) return;
        System.out.println(line);
        if (line.startsWith("Card UID")) { // check for a value (string starting with 'Card UID')
            cuid(line.substring(10).trim());
        }
    }

    public void cuid(String id) {
        System.out.println("cuid=" + id + ".");
        gui.setRfidUID(id);
        frame.dispose();
    }
}
