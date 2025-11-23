package tetris;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    public GameFrame() {
        setTitle("Tetris Game");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        MenuPanel menuPanel = new MenuPanel(this);
        GamePanel gamePanel = new GamePanel(this);
        SettingsPanel settingsPanel = new SettingsPanel(this);
        LeaderboardPanel leaderboardPanel = new LeaderboardPanel(this);
        
        mainPanel.add(menuPanel, "MENU");
        mainPanel.add(gamePanel, "GAME");
        mainPanel.add(settingsPanel, "SETTINGS");
        mainPanel.add(leaderboardPanel, "LEADERBOARD");
        
        add(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }
    
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
