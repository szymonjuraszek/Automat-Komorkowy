package sample.boundary;

public class PeriodicalCondition extends BoundaryCondition {

    public PeriodicalCondition(int x,int y){
        sizeX=x;
        sizeY=y;
    }

    @Override
    public int funX(int x)
    {
        int row =sizeY;
        if(x == row) x =  0;
        if(x <  0) x = row-1;
        return x;
    }

    // wylicza współrzędną y w obrębie pola gry
    @Override
    public int funY(int y)
    {
        int column =sizeX;
        if(y == column) y =  0;
        if(y <  0) y = column-1;
        return y;
    }
}
