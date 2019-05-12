import java.awt.*;

public class layoutChanger {

    public void changeLayout(int layout){
        journalFrame journal = new journalFrame();
        addUser mainpanel = new addUser();
        scanCardLayout scan = new scanCardLayout();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (layout == 1) {
                    journal.setVisible(true);
                } else if (layout == 2) {
                    mainpanel.setVisible(true);
                } else if (layout == 3){
                    scan.setVisible(true);
                }
            }
        });

    }
}
