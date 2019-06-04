package sample.structure;


import sample.SquareShape;

import java.util.Random;

public class Cell {
    private double specifyGravityX;
    private double specifyGravityY;
    private int colorNumber;
    private int energy;

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public double getSpecifyGravityX() {
        return specifyGravityX;
    }

    public double getSpecifyGravityY() {
        return specifyGravityY;
    }

    private static final Random random = new Random();

    public Cell() {

    }

    public Cell(int x,int y) {
        this.specifyGravityX = x * SquareShape.HEIGHT + random.nextDouble()*10;
        this.specifyGravityY = y * SquareShape.WIDTH + random.nextDouble()*10;

        System.out.println(this.specifyGravityX);
        System.out.println(this.specifyGravityY);
    }

    public int getColorNumber() {
        return colorNumber;
    }

    public void setColorNumber(int colorNumber) {
        this.colorNumber = colorNumber;
    }

}

