package sample.structure;


public class Cell {
    private double specifyGravity;
    private int colorNumber;

    public Cell() {
        System.out.println("Knstruktor Cell");
    }

    public int getColorNumber() {
        return colorNumber;
    }

    public void setColorNumber(int colorNumber) {
        this.colorNumber = colorNumber;
    }

    public double getSpecifyGravity() {
        return specifyGravity;
    }

    public void setSpecifyGravity(double specifyGravity) {
        this.specifyGravity = specifyGravity;
    }
}
