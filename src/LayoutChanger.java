import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;

public class LayoutChanger {

    public void changeLayout(int layout){
        MainGUI gui = new MainGUI();
        CardLayout scan = new CardLayout();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (layout == 1) {
                    gui.r.start();
                } else if (layout == 2) {
                    SwingUtilities.invokeLater(scan.r);
                }
            }
        });

    }
}
