package tetris;

import java.util.Random;

public class Piece {
    public int[][] shape;
    public int x, y;
    private int rotation = 0;
    
    private static final int[][][] SHAPES = {
        // I
        {{1,1,1,1}},
        // J
        {{2,0,0}, {2,2,2}},
        // L
        {{0,0,3}, {3,3,3}},
        // O
        {{4,4}, {4,4}},
        // S
        {{0,5,5}, {5,5,0}},
        // T
        {{0,6,0}, {6,6,6}},
        // Z
        {{7,7,0}, {0,7,7}}
    };
    
    public Piece() {
        Random rand = new Random();
        int type = rand.nextInt(SHAPES.length);
        shape = copyArray(SHAPES[type]);
        x = 3;
        y = 0;
    }
    
    public void rotate() {
        int[][] rotated = new int[shape[0].length][shape.length];
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                rotated[j][shape.length - 1 - i] = shape[i][j];
            }
        }
        shape = rotated;
        rotation = (rotation + 1) % 4;
    }
    
    public void rotateBack() {
        for (int i = 0; i < 3; i++) {
            rotate();
        }
    }
    
    private int[][] copyArray(int[][] arr) {
        int[][] copy = new int[arr.length][];
        for (int i = 0; i < arr.length; i++) {
            copy[i] = arr[i].clone();
        }
        return copy;
    }
}
