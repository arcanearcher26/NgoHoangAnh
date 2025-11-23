package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener {
    private static final int TILE_SIZE = 30;
    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGHT = 20;
    
    private GameFrame gameFrame;
    private Timer timer;
    private boolean running = false;
    private int[][] board;
    private Piece currentPiece;
    private Piece nextPiece;
    private int score = 0;
    private int level = 1;
    private int linesCleared = 0;
    
    public GamePanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        setBackground(new Color(20, 30, 48));
        setFocusable(true);
        
        board = new int[BOARD_HEIGHT][BOARD_WIDTH];
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!running) return;
                
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_A:
                        movePiece(-1, 0);
                        break;
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_D:
                        movePiece(1, 0);
                        break;
                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_S:
                        movePiece(0, 1);
                        break;
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_SPACE:
                        rotatePiece();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        pauseGame();
                        break;
                }
                repaint();
            }
        });
    }
    
    public void startNewGame() {
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                board[i][j] = 0;
            }
        }
        
        score = 0;
        level = SettingsPanel.getStartLevel();
        linesCleared = 0;
        running = true;
        
        nextPiece = new Piece();
        spawnNewPiece();
        
        if (timer != null) timer.stop();
        
        int baseSpeed = 500;
        String difficulty = SettingsPanel.getDifficulty();
        if (difficulty.equals("Dễ")) {
            baseSpeed = 600;
        } else if (difficulty.equals("Khó")) {
            baseSpeed = 400;
        }
        
        int speed = Math.max(100, baseSpeed - (level - 1) * 50);
        timer = new Timer(speed, this);
        timer.start();
        
        requestFocusInWindow();
    }
    
    private void spawnNewPiece() {
        currentPiece = nextPiece;
        nextPiece = new Piece();
        
        if (checkCollision(currentPiece, 0, 0)) {
            gameOver();
        }
    }
    
    private void movePiece(int dx, int dy) {
        if (!checkCollision(currentPiece, dx, dy)) {
            currentPiece.x += dx;
            currentPiece.y += dy;
        } else if (dy > 0) {
            mergePiece();
            clearLines();
            spawnNewPiece();
        }
    }
    
    private void rotatePiece() {
        currentPiece.rotate();
        if (checkCollision(currentPiece, 0, 0)) {
            currentPiece.rotateBack();
        }
    }
    
    private boolean checkCollision(Piece piece, int dx, int dy) {
        for (int i = 0; i < piece.shape.length; i++) {
            for (int j = 0; j < piece.shape[i].length; j++) {
                if (piece.shape[i][j] != 0) {
                    int newX = piece.x + j + dx;
                    int newY = piece.y + i + dy;
                    
                    if (newX < 0 || newX >= BOARD_WIDTH || newY >= BOARD_HEIGHT) {
                        return true;
                    }
                    if (newY >= 0 && board[newY][newX] != 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private void mergePiece() {
        for (int i = 0; i < currentPiece.shape.length; i++) {
            for (int j = 0; j < currentPiece.shape[i].length; j++) {
                if (currentPiece.shape[i][j] != 0) {
                    int boardY = currentPiece.y + i;
                    int boardX = currentPiece.x + j;
                    if (boardY >= 0 && boardY < BOARD_HEIGHT && boardX >= 0 && boardX < BOARD_WIDTH) {
                        board[boardY][boardX] = currentPiece.shape[i][j];
                    }
                }
            }
        }
    }
    
    private void clearLines() {
        int lines = 0;
        for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {
            boolean full = true;
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (board[i][j] == 0) {
                    full = false;
                    break;
                }
            }
            
            if (full) {
                lines++;
                for (int k = i; k > 0; k--) {
                    for (int j = 0; j < BOARD_WIDTH; j++) {
                        board[k][j] = board[k-1][j];
                    }
                }
                for (int j = 0; j < BOARD_WIDTH; j++) {
                    board[0][j] = 0;
                }
                i++;
            }
        }
        
        if (lines > 0) {
            linesCleared += lines;
            score += lines * 100 * level;
            level = linesCleared / 10 + 1;
            
            int newDelay = Math.max(100, 500 - (level - 1) * 50);
            timer.setDelay(newDelay);
        }
    }
    
    private void pauseGame() {
        if (timer != null) {
            timer.stop();
            int choice = JOptionPane.showConfirmDialog(this,
                "Tạm dừng. Tiếp tục?", "Pause", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                timer.start();
            } else {
                running = false;
                gameFrame.showPanel("MENU");
            }
        }
    }
    
    private void gameOver() {
        running = false;
        if (timer != null) timer.stop();
        
        ScoreManager.saveScore(SettingsPanel.getPlayerName(), score);
        
        int choice = JOptionPane.showConfirmDialog(this,
            "Game Over!\nĐiểm: " + score + "\nLines: " + linesCleared + "\nChơi lại?",
            "Game Over", JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            startNewGame();
        } else {
            gameFrame.showPanel("MENU");
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            movePiece(0, 1);
            repaint();
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        int offsetX = 200;
        int offsetY = 50;
        
        // Vẽ shadow cho board
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillRoundRect(offsetX + 5, offsetY + 5, BOARD_WIDTH * TILE_SIZE, BOARD_HEIGHT * TILE_SIZE, 15, 15);
        
        // Draw board background với bo góc
        g2d.setColor(new Color(30, 40, 60));
        g2d.fillRoundRect(offsetX, offsetY, BOARD_WIDTH * TILE_SIZE, BOARD_HEIGHT * TILE_SIZE, 15, 15);
        
        // Draw grid
        if (SettingsPanel.isShowGrid()) {
            g2d.setColor(new Color(50, 60, 80));
            for (int i = 0; i <= BOARD_HEIGHT; i++) {
                g2d.drawLine(offsetX, offsetY + i * TILE_SIZE, 
                            offsetX + BOARD_WIDTH * TILE_SIZE, offsetY + i * TILE_SIZE);
            }
            for (int i = 0; i <= BOARD_WIDTH; i++) {
                g2d.drawLine(offsetX + i * TILE_SIZE, offsetY, 
                            offsetX + i * TILE_SIZE, offsetY + BOARD_HEIGHT * TILE_SIZE);
            }
        }
        
        // Draw board pieces
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (board[i][j] != 0) {
                    drawTile(g2d, offsetX + j * TILE_SIZE, offsetY + i * TILE_SIZE, board[i][j]);
                }
            }
        }
        
        // Draw current piece
        if (currentPiece != null) {
            for (int i = 0; i < currentPiece.shape.length; i++) {
                for (int j = 0; j < currentPiece.shape[i].length; j++) {
                    if (currentPiece.shape[i][j] != 0) {
                        int x = offsetX + (currentPiece.x + j) * TILE_SIZE;
                        int y = offsetY + (currentPiece.y + i) * TILE_SIZE;
                        drawTile(g2d, x, y, currentPiece.shape[i][j]);
                    }
                }
            }
        }
        
        // === PANEL ĐIỂM BÊN TRÁI (ĐẸP HƠN) ===
        int scoreBoxX = 20;
        int scoreBoxY = offsetY;
        int scoreBoxW = 160;
        
        // Shadow
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillRoundRect(scoreBoxX + 3, scoreBoxY + 3, scoreBoxW, 200, 20, 20);
        
        // Background
        GradientPaint scoreGradient = new GradientPaint(
            scoreBoxX, scoreBoxY, new Color(41, 128, 185),
            scoreBoxX, scoreBoxY + 200, new Color(52, 152, 219)
        );
        g2d.setPaint(scoreGradient);
        g2d.fillRoundRect(scoreBoxX, scoreBoxY, scoreBoxW, 200, 20, 20);
        
        // Border
        g2d.setColor(new Color(255, 255, 255, 150));
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawRoundRect(scoreBoxX, scoreBoxY, scoreBoxW, 200, 20, 20);
        
        // Text
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("ĐIỂM", scoreBoxX + 50, scoreBoxY + 35);
        g2d.setFont(new Font("Arial", Font.BOLD, 28));
        g2d.setColor(new Color(255, 230, 109));
        g2d.drawString(String.valueOf(score), scoreBoxX + 50, scoreBoxY + 70);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("LEVEL", scoreBoxX + 50, scoreBoxY + 110);
        g2d.setFont(new Font("Arial", Font.BOLD, 28));
        g2d.setColor(new Color(142, 255, 159));
        g2d.drawString(String.valueOf(level), scoreBoxX + 50, scoreBoxY + 145);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("Lines: " + linesCleared, scoreBoxX + 35, scoreBoxY + 180);
        
        // === PANEL NEXT BÊN PHẢI (ĐẸP HƠN) ===
        int nextBoxX = offsetX + BOARD_WIDTH * TILE_SIZE + 30;
        int nextBoxY = offsetY;
        int nextBoxSize = 170;
        
        // Shadow cho NEXT box
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillRoundRect(nextBoxX + 3, nextBoxY + 3, nextBoxSize, nextBoxSize + 40, 20, 20);
        
        // Header "TIẾP THEO"
        GradientPaint nextHeaderGradient = new GradientPaint(
            nextBoxX, nextBoxY, new Color(241, 196, 15),
            nextBoxX + nextBoxSize, nextBoxY + 40, new Color(243, 156, 18)
        );
        g2d.setPaint(nextHeaderGradient);
        g2d.fillRoundRect(nextBoxX, nextBoxY, nextBoxSize, 40, 20, 20);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("TIẾP THEO", nextBoxX + 30, nextBoxY + 27);
        
        // Background preview box
        GradientPaint nextBgGradient = new GradientPaint(
            nextBoxX, nextBoxY + 40, new Color(44, 62, 80),
            nextBoxX, nextBoxY + 40 + nextBoxSize, new Color(52, 73, 94)
        );
        g2d.setPaint(nextBgGradient);
        g2d.fillRoundRect(nextBoxX, nextBoxY + 40, nextBoxSize, nextBoxSize, 0, 0);
        
        // Border
        g2d.setColor(new Color(255, 255, 255, 150));
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawRoundRect(nextBoxX, nextBoxY, nextBoxSize, nextBoxSize + 40, 20, 20);
        
        // Vẽ khối tiếp theo
        if (nextPiece != null) {
            int pieceWidth = nextPiece.shape[0].length;
            int pieceHeight = nextPiece.shape.length;
            int startX = nextBoxX + (nextBoxSize - pieceWidth * TILE_SIZE) / 2;
            int startY = nextBoxY + 40 + (nextBoxSize - pieceHeight * TILE_SIZE) / 2;
            
            for (int i = 0; i < nextPiece.shape.length; i++) {
                for (int j = 0; j < nextPiece.shape[i].length; j++) {
                    if (nextPiece.shape[i][j] != 0) {
                        int x = startX + j * TILE_SIZE;
                        int y = startY + i * TILE_SIZE;
                        drawTile(g2d, x, y, nextPiece.shape[i][j]);
                    }
                }
            }
        }
        
        // Draw controls
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.setColor(new Color(150, 150, 180));
        g2d.drawString("Điều khiển: Phím mũi tên hoặc WASD", offsetX, offsetY + BOARD_HEIGHT * TILE_SIZE + 30);
        g2d.drawString("Space/Up: Xoay | ESC: Tạm dừng", offsetX, offsetY + BOARD_HEIGHT * TILE_SIZE + 50);
    }
    
    private void drawTile(Graphics2D g2d, int x, int y, int colorType) {
        Color color;
        switch(colorType) {
            case 1: color = new Color(0, 240, 240); break;
            case 2: color = new Color(0, 0, 240); break;
            case 3: color = new Color(240, 160, 0); break;
            case 4: color = new Color(240, 240, 0); break;
            case 5: color = new Color(0, 240, 0); break;
            case 6: color = new Color(160, 0, 240); break;
            case 7: color = new Color(240, 0, 0); break;
            default: color = Color.GRAY;
        }
        
        // Vẽ gradient cho khối
        GradientPaint gradient = new GradientPaint(
            x, y, color.brighter(),
            x + TILE_SIZE, y + TILE_SIZE, color
        );
        g2d.setPaint(gradient);
        g2d.fillRoundRect(x + 2, y + 2, TILE_SIZE - 4, TILE_SIZE - 4, 8, 8);
        
        // Viền sáng trên + trái
        g2d.setColor(color.brighter().brighter());
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawLine(x + 3, y + 3, x + TILE_SIZE - 3, y + 3);
        g2d.drawLine(x + 3, y + 3, x + 3, y + TILE_SIZE - 3);
        
        // Viền tối dưới + phải
        g2d.setColor(color.darker().darker());
        g2d.drawLine(x + TILE_SIZE - 3, y + 3, x + TILE_SIZE - 3, y + TILE_SIZE - 3);
        g2d.drawLine(x + 3, y + TILE_SIZE - 3, x + TILE_SIZE - 3, y + TILE_SIZE - 3);
    }
}
