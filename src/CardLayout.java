import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.sun.tools.javac.Main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventListener;

import static java.lang.Thread.currentThread;

public class CardLayout {
    public String cuid;
    String readline;
    SerialPort comPort;
    String commPort = "COM7";
    int baudrate = 9600;
    protected JLabel scanmsg = new JLabel("WWW.");
    public JButton manual = new JButton("Ввести UID вручную");
    Thread r = new Thread(new Runnable() {
        @Override
        public void run() {
            // used for serial communication:
            MainGUI gui = new MainGUI();
            final JFrame frame = new JFrame("Card Scan Panel");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLayout(new BorderLayout(5,5));
            final JPanel pane = new JPanel(new BorderLayout(5,5));
            final JPanel scan = new JPanel(new GridBagLayout());
            scanmsg.setPreferredSize(new Dimension(150,25));
            manual.setPreferredSize(new Dimension(150,25));
            frame.add(pane);
            JPanel dynamicLabels = new JPanel(new BorderLayout(1,1));
            dynamicLabels.setBorder(new TitledBorder("Считывание данных с карты") );
            pane.add(dynamicLabels, BorderLayout.CENTER);
            dynamicLabels.add(scan, BorderLayout.CENTER);
            scan.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(110, 200, 110, 200), new EtchedBorder(5)));
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(4,4,0,0);
            scan.add(scanmsg,c);
            c.gridy++;
            scan.add(manual,c);
            frame.pack();
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
            manual.addActionListener(new ScanRFID());
            ReadComPort.start();


             }
     });

    Thread ReadComPort = new Thread(new Runnable() {
        @Override
        public void run() {
            initializeSerialPort();
        }
    });

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
        
    }
    class ScanRFID implements ActionListener {
        MainGUI gui = new MainGUI();
        @Override
        public void actionPerformed(ActionEvent e) {
            Thread Do = new Thread(new Runnable() {
                @Override
                public void run() {
                    MainGUI gui = new MainGUI();
                    System.out.println(currentThread());
                    cuid("A3 B8 F9 C6");
                    scanmsg.setText("A3 B8 F9 C6");
                    gui.PrintThread();
                    //frame.dispose();
                }
            });
            Do.run();
        }
    }
    public CardLayout (String id){
        ChangeUID(id);
        System.out.println(id);
    }
    public void ChangeUID(String id){
        this.cuid = id;
        System.out.println(cuid);
    }
    public String GetUID(){
        System.out.println(cuid);
        return cuid;
    }

}
