package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.File;

public class MenuPanel extends JPanel {
    private GameFrame gameFrame;
    private ImageIcon logoIcon;
    private float titleGlow = 0f;
    private boolean glowIncreasing = true;
    private Timer glowTimer;
    
    public MenuPanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(20, 30, 48));
        
        // Animation cho glow effect
        glowTimer = new Timer(50, e -> {
            if (glowIncreasing) {
                titleGlow += 0.03f;
                if (titleGlow >= 1f) glowIncreasing = false;
            } else {
                titleGlow -= 0.03f;
                if (titleGlow <= 0f) glowIncreasing = true;
            }
            repaint();
        });
        glowTimer.start();
        
        // Load logo
        String[] paths = {"Tetris-logo1.png", "images/Tetris-logo1.png", "src/images/Tetris-logo1.png"};
        for (String path : paths) {
            File f = new File(path);
            if (f.exists()) {
                logoIcon = new ImageIcon(path);
                break;
            }
        }
        
        // Title Panel
        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (logoIcon != null && logoIcon.getIconWidth() > 0) {
                    Image img = logoIcon.getImage();
                    int logoWidth = 400;
                    int logoHeight = 200;
                    int x = (getWidth() - logoWidth) / 2;
                    int y = 30;
                    
                    // Vẽ glow cho logo
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, titleGlow * 0.3f));
                    g2d.setColor(new Color(255, 184, 28));
                    g2d.fillRoundRect(x - 10, y - 10, logoWidth + 20, logoHeight + 20, 30, 30);
                    
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                    g2d.drawImage(img, x, y, logoWidth, logoHeight, this);
                }
            }
        };
        titlePanel.setOpaque(false);
        titlePanel.setPreferredSize(new Dimension(800, 260));
        
        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 0, 20));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 200, 80, 200));
        
        ModernButton playBtn = new ModernButton("CHƠI NGAY", new Color(76, 209, 55));
        ModernButton settingsBtn = new ModernButton("CÀI ĐẶT", new Color(52, 152, 219));
        ModernButton leaderboardBtn = new ModernButton("XẾP HẠNG", new Color(241, 196, 15));
        ModernButton exitBtn = new ModernButton("THOÁT", new Color(231, 76, 60));
        
        playBtn.addActionListener(e -> {
            PlayerNameDialog dialog = new PlayerNameDialog((JFrame) SwingUtilities.getWindowAncestor(this));
            dialog.setVisible(true);
            String name = dialog.getPlayerName();
            if (name != null) {
                SettingsPanel.setPlayerName(name);
                gameFrame.showPanel("GAME");
                GamePanel gp = (GamePanel) gameFrame.getMainPanel().getComponent(1);
                gp.startNewGame();
            }
        });
        
        settingsBtn.addActionListener(e -> gameFrame.showPanel("SETTINGS"));
        leaderboardBtn.addActionListener(e -> {
            gameFrame.showPanel("LEADERBOARD");
            LeaderboardPanel lp = (LeaderboardPanel) gameFrame.getMainPanel().getComponent(3);
            lp.updateTable();
        });
        exitBtn.addActionListener(e -> System.exit(0));
        
        buttonPanel.add(playBtn);
        buttonPanel.add(settingsBtn);
        buttonPanel.add(leaderboardBtn);
        buttonPanel.add(exitBtn);
        
        add(titlePanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        // Gradient background đẹp hơn
        GradientPaint gp = new GradientPaint(0, 0, new Color(20, 30, 48), 
                                             0, getHeight(), new Color(36, 59, 85));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Thêm các chấm trang trí
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
        g2d.setColor(new Color(100, 200, 255));
        for (int i = 0; i < 20; i++) {
            int x = (int)(Math.sin(i * 0.5) * 100) + i * 40;
            int y = (int)(Math.cos(i * 0.3) * 50) + i * 30;
            g2d.fillOval(x, y, 30, 30);
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}

// Class ModernButton - Nút bấm đẹp với shadow và bo góc
class ModernButton extends JButton {
    private Color bgColor;
    private boolean isHovered = false;
    
    public ModernButton(String text, Color color) {
        super(text);
        this.bgColor = color;
        setFont(new Font("Arial", Font.BOLD, 22));
        setForeground(Color.WHITE);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                isHovered = true;
                repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                isHovered = false;
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Shadow
        g2d.setColor(new Color(0, 0, 0, 80));
        g2d.fillRoundRect(6, 6, getWidth() - 6, getHeight() - 6, 25, 25);
        
        // Background với gradient
        Color color1 = isHovered ? bgColor.brighter() : bgColor;
        Color color2 = isHovered ? bgColor : bgColor.darker();
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
        g2d.setPaint(gp);
        g2d.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 25, 25);
        
        // Border sáng
        g2d.setColor(isHovered ? color1.brighter() : new Color(255, 255, 255, 100));
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 25, 25);
        
        // Glow effect khi hover
        if (isHovered) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            g2d.setColor(Color.WHITE);
            g2d.fillRoundRect(2, 2, getWidth() - 10, getHeight() - 10, 25, 25);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
        
        super.paintComponent(g);
    }
}
