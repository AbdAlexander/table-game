package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

public class MainWindow extends JFrame {
   
    private boolean changed = true;
    
    public MainWindow() {
        setTitle("Eltoltás játék");
        setSize(850,280);
        setResizable(false);
        
        URL url = MainWindow.class.getResource("icon.png");
        setIconImage(Toolkit.getDefaultToolkit().getImage(url));
        
        mainWindowDesc(); // A játék leírása
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (changed) {
                    showExitConfirmation();
                } else {
                    System.exit(0);
                }
            }
        });
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }
    
    private void showExitConfirmation() {

        int n = JOptionPane.showConfirmDialog(this,
                "Valóban ki akar lépni?",
                "Megerősítés", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    private void mainWindowDesc() {
        JPanel greetingsPanel = new JPanel();
        JPanel chooserPanel = new JPanel();
        JPanel descPanel = new JPanel();
        
        JLabel greetingsText = new JLabel();   
        JLabel sizeChoser = new JLabel();
        
        greetingsText.setText("Üdvözöllek az Eltolás nevű játékban!");
        sizeChoser.setText("Kérlek válaszd ki a játéktábla nagyságát: ");
        
        JButton size3x3 = new JButton("3x3-as tábla");
        JButton size4x4 = new JButton("4x4-es tábla");
        JButton size6x6 = new JButton("6x6-os tábla");
        size3x3.addActionListener(new gameTableActionListener("3x3"));
        size4x4.addActionListener(new gameTableActionListener("4x4"));
        size6x6.addActionListener(new gameTableActionListener("6x6"));
        
        JTextArea desc = new JTextArea("Játék leírása:\n Adott egy " +
            "n × n mezőből álló tábla, amelyen kezdetben a játékosoknak n fehér,\n illetve n fekete kavics áll " +
            "rendelkezésre, amelyek elhelyezkedése véletlenszerű.\n A játékos kiválaszthat egy saját " +
            "kavicsot, amelyet függőlegesen, vagy vízszintesen eltolhat.\n Eltoláskor azonban nem csak az " +
            "adott kavics, hanem a vele az eltolás irányában szomszédos kavicsok\n is eltolódnak, a szélső " +
            "mezőn lévők pedig lekerülnek a játéktábláról. A játék célja,\n hogy adott körszámon belül (5n) " +
            "az ellenfél minél több kavicsát letoljuk a pályáról (azaz nekünk maradjon több kavicsunk).\n Ha" +
            " mindkét játékosnak ugyanannyi marad, akkor a játék döntetlen.",6,20);
        desc.setFont(new Font("Serif", Font.ITALIC, 16));
        desc.setWrapStyleWord(true);
        desc.setOpaque(false);
        desc.setEditable(false);
        
        greetingsPanel.add(greetingsText);
        chooserPanel.add(sizeChoser);
        chooserPanel.add(size3x3);
        chooserPanel.add(size4x4);
        chooserPanel.add(size6x6);
        descPanel.add(desc);
        
        setLayout(new BorderLayout());
        add(greetingsPanel, BorderLayout.NORTH);
        add(chooserPanel, BorderLayout.CENTER);
        add(descPanel, BorderLayout.SOUTH);
    }

    private static class gameTableActionListener implements ActionListener {
        private final String tableSize;
        
        public gameTableActionListener(String tableSize) {
            this.tableSize = tableSize;
        }
        
        @Override
        public void actionPerformed(ActionEvent a) {
            switch (tableSize) {
                case "3x3":
                    GameWindow gw3;
                try {
                    gw3 = new GameWindow(3);
                    gw3.setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
                    break;

                case "4x4":
                    GameWindow gw4;
                try {
                    gw4 = new GameWindow(4);
                    gw4.setVisible(true); 
                } catch (IOException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }                
                    break;

                case "6x6":
                    GameWindow gw6;
                try {
                    gw6 = new GameWindow(6);
                    gw6.setVisible(true); 
                } catch (IOException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }                      
                    break;
            }     
        }
    }
}
