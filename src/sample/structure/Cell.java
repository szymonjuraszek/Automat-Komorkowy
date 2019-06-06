package sample.structure;

import sample.SquareShape;

import java.math.BigInteger;
import java.util.Random;

public class Cell {
    public static final BigInteger CRITICAL_DISSLOCATION = new BigInteger("13927458546192");
    private double specifyGravityX;
    private double specifyGravityY;
    private int colorNumber;
    private int energy;
    private BigInteger dislocationDensity;
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
        dislocationDensity=new BigInteger("0");

//        System.out.println(this.specifyGravityX);
//        System.out.println(this.specifyGravityY);
    }

    public int getColorNumber() {
        return colorNumber;
    }

    public void setColorNumber(int colorNumber) {
        this.colorNumber = colorNumber;
    }

    public void addDislocationDensity(BigInteger dislocationDensity){
        this.dislocationDensity =this.dislocationDensity.add(dislocationDensity);

    }

    public BigInteger getDislocationDensity() {
        return dislocationDensity;
    }

    public void setDislocationDensity(BigInteger dislocationDensity) {
        this.dislocationDensity = dislocationDensity;
    }


    public boolean isIfGlacial() {
        return ifGlacial;
    }

    public void setIfGlacial(boolean ifGlacial) {
        this.ifGlacial = ifGlacial;
    }
}

