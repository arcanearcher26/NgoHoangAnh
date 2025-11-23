package tetris;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class LeaderboardPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    
    public LeaderboardPanel(GameFrame gameFrame) {
        setLayout(new BorderLayout());
        setBackground(new Color(20, 30, 48));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(241, 196, 15));
        titlePanel.setPreferredSize(new Dimension(800, 80));
        
        JLabel titleLabel = new JLabel("BẢNG XẾP HẠNG");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        String[] columns = {"Hạng", "Tên", "Điểm"};
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 18));
        table.setRowHeight(40);
        table.setBackground(new Color(36, 59, 85));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(70, 90, 110));
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setBackground(new Color(52, 152, 219));
        header.setForeground(Color.WHITE);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 3; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        scrollPane.getViewport().setBackground(new Color(36, 59, 85));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(20, 30, 48));
        
        JButton refreshBtn = new JButton("LÀM MỚI");
        refreshBtn.setFont(new Font("Arial", Font.BOLD, 20));
        refreshBtn.setBackground(new Color(46, 204, 113));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setPreferredSize(new Dimension(180, 50));
        refreshBtn.addActionListener(e -> updateTable());
        
        JButton backBtn = new JButton("TRỞ VỀ");
        backBtn.setFont(new Font("Arial", Font.BOLD, 20));
        backBtn.setBackground(new Color(231, 76, 60));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setPreferredSize(new Dimension(180, 50));
        backBtn.addActionListener(e -> gameFrame.showPanel("MENU"));
        
        buttonPanel.add(refreshBtn);
        buttonPanel.add(backBtn);
        
        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public void updateTable() {
        model.setRowCount(0);
        List<String[]> scores = ScoreManager.getTopScores(10);
        
        for (int i = 0; i < scores.size(); i++) {
            String[] score = scores.get(i);
            String rank;
            if (i == 0) rank = "1";
            else if (i == 1) rank = "2";
            else if (i == 2) rank = "3";
            else rank = String.valueOf(i + 1);
            
            model.addRow(new Object[]{rank, score[0], score[1]});
        }
    }
}
