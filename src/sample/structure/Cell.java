package sample.structure;


import sample.SquareShape;

import java.util.Random;

public class Cell {
    private int specifyGravityX;
    private int specifyGravityY;
    private int colorNumber;
    private static final Random random = new Random();

    public Cell() {

    }

    public Cell(int x,int y) {
        this.specifyGravityX = x * SquareShape.HEIGHT + random.nextInt(10);
        this.specifyGravityY = y * SquareShape.WIDTH + random.nextInt(10);
    }

    public int getColorNumber() {
        return colorNumber;
    }

    public void setColorNumber(int colorNumber) {
        this.colorNumber = colorNumber;
    }

}

