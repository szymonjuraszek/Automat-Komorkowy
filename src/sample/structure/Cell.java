package sample.structure;

import sample.SquareShape;
import java.util.Random;

public class Cell {
    public static double CRITICAL_DISSLOCATION = 1.1710667062009494E9;
    private double specifyGravityX;
    private double specifyGravityY;
    private int colorNumber;
    private int energy;
    private double dislocationDensity;
    private boolean ifGlacial;

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
        ifGlacial=false;
        dislocationDensity=0.0;
    }

    public int getColorNumber() {
        return colorNumber;
    }

    public void setColorNumber(int colorNumber) {
        this.colorNumber = colorNumber;
    }

    public void addDislocationDensity(double dislocationDensity){
        this.dislocationDensity +=dislocationDensity;

    }

    public double getDislocationDensity() {
        return dislocationDensity;
    }

    public void setDislocationDensity(double dislocationDensity) {
        this.dislocationDensity = dislocationDensity;
    }


    public boolean isIfGlacial() {
        return ifGlacial;
    }

    public void setIfGlacial(boolean ifGlacial) {
        this.ifGlacial = ifGlacial;
    }
}

