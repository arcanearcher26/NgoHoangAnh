package tetris;

import java.io.*;
import java.util.*;

public class ScoreManager {
    private static final String FILE = "tetris_scores.txt";
    
    public static void saveScore(String name, int score) {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(FILE, true));
            out.println(name + "," + score);
            out.close();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    public static List<String[]> getTopScores(int limit) {
        List<String[]> scores = new ArrayList<>();
        
        try {
            File file = new File(FILE);
            if (!file.exists()) return scores;
            
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    scores.add(parts);
                }
            }
            br.close();
            
            scores.sort((a, b) -> Integer.parseInt(b[1]) - Integer.parseInt(a[1]));
            
            if (scores.size() > limit) {
                scores = scores.subList(0, limit);
            }
            
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
        
        return scores;
    }
}
