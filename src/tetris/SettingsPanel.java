package tetris;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {
    private static String playerName = "Player";
    private static int startLevel = 1;
    private static boolean showGrid = true;
    private static String difficulty = "Bình thường";
    
    private JSlider levelSlider;
    private JCheckBox gridCheckBox;
    private ButtonGroup difficultyGroup;
    private JLabel levelLabel;
    
    public SettingsPanel(GameFrame gameFrame) {
        setLayout(new BorderLayout());
        setBackground(new Color(20, 30, 48));
        
        // Title
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(52, 152, 219));
        titlePanel.setPreferredSize(new Dimension(800, 80));
        
        JLabel titleLabel = new JLabel("CÀI ĐẶT");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(36, 59, 85));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));
        
        // === CẤP ĐỘ BẮT ĐẦU ===
        JLabel levelTitleLabel = new JLabel("Cấp độ bắt đầu:");
        levelTitleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        levelTitleLabel.setForeground(Color.WHITE);
        levelTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        levelLabel = new JLabel("Level " + startLevel + " (Tốc độ: " + getSpeedText(startLevel) + ")");
        levelLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        levelLabel.setForeground(new Color(46, 204, 113));
        levelLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        levelSlider = new JSlider(1, 10, startLevel);
        levelSlider.setBackground(new Color(36, 59, 85));
        levelSlider.setForeground(Color.WHITE);
        levelSlider.setMajorTickSpacing(1);
        levelSlider.setPaintTicks(true);
        levelSlider.setPaintLabels(true);
        levelSlider.setMaximumSize(new Dimension(600, 60));
        levelSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        levelSlider.addChangeListener(e -> {
            int level = levelSlider.getValue();
            levelLabel.setText("Level " + level + " (Tốc độ: " + getSpeedText(level) + ")");
        });
        
        contentPanel.add(levelTitleLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(levelLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(levelSlider);
        contentPanel.add(Box.createVerticalStrut(25));
        
        // === ĐỘ KHÓ ===
        JLabel diffLabel = new JLabel("Độ khó:");
        diffLabel.setFont(new Font("Arial", Font.BOLD, 22));
        diffLabel.setForeground(Color.WHITE);
        diffLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        difficultyGroup = new ButtonGroup();
        JRadioButton easyBtn = createRadioButton("Dễ (Rơi chậm hơn)");
        JRadioButton normalBtn = createRadioButton("Bình thường");
        JRadioButton hardBtn = createRadioButton("Khó (Rơi nhanh hơn)");
        
        difficultyGroup.add(easyBtn);
        difficultyGroup.add(normalBtn);
        difficultyGroup.add(hardBtn);
        
        if (difficulty.equals("Dễ")) easyBtn.setSelected(true);
        else if (difficulty.equals("Bình thường")) normalBtn.setSelected(true);
        else hardBtn.setSelected(true);
        
        contentPanel.add(diffLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(easyBtn);
        contentPanel.add(normalBtn);
        contentPanel.add(hardBtn);
        contentPanel.add(Box.createVerticalStrut(25));
        
        // === HIỂN THỊ LƯỚI ===
        JLabel gridLabel = new JLabel("Hiển thị:");
        gridLabel.setFont(new Font("Arial", Font.BOLD, 22));
        gridLabel.setForeground(Color.WHITE);
        gridLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        gridCheckBox = new JCheckBox("Hiện lưới trên bảng chơi", showGrid);
        gridCheckBox.setFont(new Font("Arial", Font.PLAIN, 18));
        gridCheckBox.setForeground(Color.WHITE);
        gridCheckBox.setBackground(new Color(36, 59, 85));
        gridCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        contentPanel.add(gridLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(gridCheckBox);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(20, 30, 48));
        
        JButton saveBtn = new JButton("LƯU");
        saveBtn.setFont(new Font("Arial", Font.BOLD, 20));
        saveBtn.setBackground(new Color(46, 204, 113));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setPreferredSize(new Dimension(180, 50));
        saveBtn.addActionListener(e -> {
            startLevel = levelSlider.getValue();
            showGrid = gridCheckBox.isSelected();
            
            if (easyBtn.isSelected()) difficulty = "Dễ";
            else if (normalBtn.isSelected()) difficulty = "Bình thường";
            else difficulty = "Khó";
            
            JOptionPane.showMessageDialog(this, 
                "Đã lưu cài đặt!\nLevel: " + startLevel + "\nĐộ khó: " + difficulty, 
                "Thành công", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton resetBtn = new JButton("MẶC ĐỊNH");
        resetBtn.setFont(new Font("Arial", Font.BOLD, 20));
        resetBtn.setBackground(new Color(230, 126, 34));
        resetBtn.setForeground(Color.WHITE);
        resetBtn.setFocusPainted(false);
        resetBtn.setPreferredSize(new Dimension(180, 50));
        resetBtn.addActionListener(e -> {
            levelSlider.setValue(1);
            normalBtn.setSelected(true);
            gridCheckBox.setSelected(true);
            JOptionPane.showMessageDialog(this, "Đã đặt lại cài đặt mặc định!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton backBtn = new JButton("TRỞ VỀ");
        backBtn.setFont(new Font("Arial", Font.BOLD, 20));
        backBtn.setBackground(new Color(231, 76, 60));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setPreferredSize(new Dimension(180, 50));
        backBtn.addActionListener(e -> gameFrame.showPanel("MENU"));
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(resetBtn);
        buttonPanel.add(backBtn);
        
        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JRadioButton createRadioButton(String text) {
        JRadioButton btn = new JRadioButton(text);
        btn.setFont(new Font("Arial", Font.PLAIN, 18));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(36, 59, 85));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }
    
    private String getSpeedText(int level) {
        if (level <= 3) return "Chậm";
        if (level <= 6) return "Trung bình";
        return "Nhanh";
    }
    
    public static String getPlayerName() {
        return playerName;
    }
    
    public static void setPlayerName(String name) {
        playerName = name;
    }
    
    public static int getStartLevel() {
        return startLevel;
    }
    
    public static boolean isShowGrid() {
        return showGrid;
    }
    
    public static String getDifficulty() {
        return difficulty;
    }
}
