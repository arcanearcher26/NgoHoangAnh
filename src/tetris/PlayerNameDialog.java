package tetris;

import javax.swing.*;
import java.awt.*;

public class PlayerNameDialog extends JDialog {
    private String playerName = null;
    private JTextField nameField;
    
    public PlayerNameDialog(JFrame parent) {
        super(parent, "Tên người chơi", true);
        setSize(400, 200);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(36, 59, 85));
        
        // Title
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(52, 152, 219));
        JLabel titleLabel = new JLabel("NHẬP TÊN CỦA BẠN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Input panel
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setBackground(new Color(36, 59, 85));
        
        JLabel nameLabel = new JLabel("Tên:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setForeground(Color.WHITE);
        
        nameField = new JTextField(15);
        nameField.setFont(new Font("Arial", Font.PLAIN, 18));
        nameField.addActionListener(e -> onOK());
        
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(36, 59, 85));
        
        JButton okBtn = new JButton("OK");
        okBtn.setFont(new Font("Arial", Font.BOLD, 18));
        okBtn.setBackground(new Color(46, 204, 113));
        okBtn.setForeground(Color.WHITE);
        okBtn.setFocusPainted(false);
        okBtn.setPreferredSize(new Dimension(100, 40));
        okBtn.addActionListener(e -> onOK());
        
        JButton cancelBtn = new JButton("HỦY");
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 18));
        cancelBtn.setBackground(new Color(231, 76, 60));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setPreferredSize(new Dimension(100, 40));
        cancelBtn.addActionListener(e -> onCancel());
        
        buttonPanel.add(okBtn);
        buttonPanel.add(cancelBtn);
        
        add(titlePanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        nameField.requestFocusInWindow();
    }
    
    private void onOK() {
        playerName = nameField.getText().trim();
        if (playerName.isEmpty()) {
            playerName = "Player";
        }
        dispose();
    }
    
    private void onCancel() {
        playerName = null;
        dispose();
    }
    
    public String getPlayerName() {
        return playerName;
    }
}
