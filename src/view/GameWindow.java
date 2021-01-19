package view;

/* Szükséges importok a program működéséhez */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import model.Model;
import model.Player;

public class GameWindow extends JFrame {
    
    /* Adattagok */
    private JFrame      frame;
    private JLabel      topLabel;
    private JLabel      downLabel;
    private JButton[][] buttons;
    private Model       model;
    private int         size;
    private int         clickedState;
    private int         clickedX, clickedY;
    private boolean     changed = true;
    private Image       whiteStonePic;
    private Image       blackStonePic;
    
    /* Konstruktor */
    public GameWindow(int size) throws IOException {
        this.size = size;
        setTitle("Eltoltás");
        
        URL url = MainWindow.class.getResource("icon.png");
        setIconImage(Toolkit.getDefaultToolkit().getImage(url));
        this.whiteStonePic = ImageIO.read(getClass().getResource("whiteStone.png"));
        this.blackStonePic = ImageIO.read(getClass().getResource("blackStone.png"));
        
        switch (size) {
            case 3:
                setSize(400,400);
                model = new Model(3);
                break;
            case 4:
                setSize(500,500);
                model = new Model(4);
                break;
            default:
                setSize(600,600);
                model = new Model(6);
                break;
        }
        setResizable(false);
        
        JPanel topPanel = new JPanel();
        JPanel gamePanel = new JPanel();
        JPanel turnsPanel = new JPanel();
        
        topLabel = new JLabel();
        downLabel = new JLabel();
        updateTopLabelText();
        updateDownLabelText();
        
        JButton newGameButton = new JButton();
        newGameButton.setText("Új játék");
        newGameButton.addActionListener((ActionEvent e) -> {
            try {
                newGame();
            } catch (IOException ex) {
                Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        topPanel.add(topLabel);
        topPanel.add(newGameButton);
        turnsPanel.add(downLabel);
        
        gamePanel.setLayout(new GridLayout(size,size));
        buttons = new JButton[size][size];
        for(int i=0; i<size; ++i) {
            for(int j=0; j<size; ++j) {
                addButton(gamePanel,i,j);
            }
        }

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(gamePanel, BorderLayout.CENTER);
        getContentPane().add(turnsPanel, BorderLayout.SOUTH);
        
        
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                showGameExitConfirmation();
            }

        });
    }
    
    /* Gombokat hozzáadjuk a panelhez, illetve eseménykezelővel látjuk el */
    private void addButton(JPanel panel, final int i, final int j) {
        
        buttons[i][j] = new JButton();
        checkAndChangeButtonContent(i,j);
        panel.add(buttons[i][j]);

        buttons[i][j].addActionListener((ActionEvent e) -> {
            if(clickedState == 0) {
                if(!model.getActualPlayer().name().equals(buttons[i][j].getText())) {
                    JOptionPane.showMessageDialog(frame, "HIBA: Csak a saját köveidet"
                            + " tudod mozgatni!","Téves mezőre való kattintás",0);
                    return;
                }
                clickedX = i; clickedY = j;
                clickedState = 1;
                updateDownLabelText();
            } else if(clickedState == 1) {
                clickedState = 0;
                model.step(clickedX, clickedY, i, j);
                for(int r=0; r<size; r++) {
                    for(int c=0; c<size; c++) {
                        checkAndChangeButtonContent(r,c);
                    }
                }
                updateTopLabelText();
                updateDownLabelText();
                try {
                    isOver();
                } catch (IOException ex) {
                    Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    private void newGame() throws IOException {
        GameWindow newGW = new GameWindow(size);
        newGW.setVisible(true);
        this.dispose();
    }
    private void updateTopLabelText() {
        topLabel.setText(
                "<html>Aktuális játékos: "
                + (model.getActualPlayer()==Player.WHITE ? "FEHÉR" : "FEKETE") 
                + "<br>Fehér kövek száma: " + model.getWhiteStonesNum()
                + "<br>Fekete kövek száma: " + model.getBlackStonesNum() + "</html>");
    }
    private void updateDownLabelText() {
        if(clickedState == 0)
            downLabel.setText(
                    "<html><div style='text-align: center;'>"
                    + "Fennmaradt lépések száma: " + model.getRemainedTurns() 
                    + "<br>Válassz ki egy kavicsot, amit mozgatni szeretnél!</div></html>");
        else if(clickedState == 1)
            downLabel.setText(
                    "<html><div style='text-align: center;'>"
                    + "Fennmaradt lépések száma: " + model.getRemainedTurns() 
                    + "<br>Válaszd ki hova szeretnéd mozgatni a kiválasztott kavicsot!</div></html>");
    }
    
    private void checkAndChangeButtonContent(int i, int j) {
        Player buttonValue = model.getButtonContent(i, j);
        if(null != buttonValue.name())switch (buttonValue.name()) {
            case "WHITE":
                buttons[i][j].setIcon(new ImageIcon(whiteStonePic));
                buttons[i][j].setBackground(Color.white);
                buttons[i][j].setText("WHITE");
                buttons[i][j].setForeground(Color.white);
                break;
            case "BLACK":
                buttons[i][j].setIcon(new ImageIcon(blackStonePic));
                buttons[i][j].setBackground(Color.white);
                buttons[i][j].setText("BLACK");
                buttons[i][j].setForeground(Color.white);
                break;
            case "NOBODY":
                buttons[i][j].setIcon(null);
                buttons[i][j].setBackground(Color.white);
                buttons[i][j].setText("NOBODY");
                buttons[i][j].setForeground(Color.white);            
                break;
            default:
                break;
        } 
    }
   
    private void isOver() throws IOException {
        int whiteStonesNum = model.getWhiteStonesNum();
        int blackStonesNum = model.getBlackStonesNum();
        if(model.getRemainedTurns()==0 
                || whiteStonesNum == 0 
                || blackStonesNum == 0) {
            Player winner = model.getWinner();
            if(winner != Player.NOBODY) {
                if(whiteStonesNum == 0) {
                    int result = JOptionPane.showConfirmDialog(frame, 
                       "Nem maradt több köve a fehérnek!\n"
                       + "A FEKETE győzött!\n"
                       + "Szeretnél új játékot kezdeni?","Végeredmény",
                       JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE); 

                    if(result == JOptionPane.YES_OPTION) newGame();
                    else dispose();                                 
                } else if(blackStonesNum == 0) {
                    int result = JOptionPane.showConfirmDialog(frame, 
                       "Nem maradt több köve a feketének!\n"
                       + "A FEHÉR győzött!\n"
                       + "Szeretnél új játékot kezdeni?","Végeredmény",
                       JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE); 

                    if(result == JOptionPane.YES_OPTION) newGame();
                    else dispose();                                    
                } else {
                    int result = JOptionPane.showConfirmDialog(frame, 
                       "Nem lehet többet lépni!\n"
                       + "A " + (winner==Player.WHITE ? "FEHÉR" : "FEKETE") + " győzött!\n"
                       + "Szeretnél új játékot kezdeni?","Végeredmény",
                       JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE); 

                    if(result == JOptionPane.YES_OPTION) newGame();
                    else dispose();    
                }

            } else { 
                int result = JOptionPane.showConfirmDialog(frame, "Nem lehet többet lépni!\n"
                       + "A végeredmény döntetlen lett!\n"
                       + "Szeretnél új játékot kezdeni?","Végeredmény",
                       JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(result == JOptionPane.YES_OPTION) newGame();
                else dispose();
            }
        }
    }
    
    private void showGameExitConfirmation() {
        int n = JOptionPane.showConfirmDialog(this, "Valóban ki akar lépni?",
                "Megerősítés", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            this.dispose();
        }
    }
}

