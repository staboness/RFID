import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.Thread.currentThread;

public class scanCardLayout extends JFrame {

    // used for serial communication:
    String readline;
    SerialPort comPort;
    String commPort = "COM7";
    int baudrate = 9600;
    private JLabel scanmsg = new JLabel("Ожидание сканирования карты...");
    protected JButton manual = new JButton("Ввести UID вручную");

    public scanCardLayout() {
        SwingUtilities.isEventDispatchThread();
        setTitle("RFID APP");
        setLayout(new FlowLayout());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(dim.width / 3 - this.getSize().width / 3, dim.height / 4 - this.getSize().height / 4, 320, 240);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Container container = this.getContentPane();
        container.setLayout(null);
        this.setResizable(false);
        container.add(scanmsg);
        container.add(manual);
        scanmsg.setBounds(50,80,250,25);
        manual.setBounds(25,100,250,25);
        manual.addActionListener(new manualEnter());
        initializeSerialPort();
    }
    class manualEnter implements ActionListener{
        public void actionPerformed(ActionEvent e){
            addUser au = new addUser();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            System.out.println(currentThread());
                            au.rfid.setText("GGGGG");
                            au.rfid.paintImmediately(au.rfid.getBounds());  // repaint(), etc. according to changed states
                        }
                    });
                }
            }).run();
        }
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

    }

}
