package sample.boundary;

public abstract class BoundaryCondition {
    protected int sizeX=45;
    protected int sizeY=80;

    public abstract int funX(int x);
    public abstract int funY(int y);

    public int getSizeX() {
        return sizeX;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }
}
