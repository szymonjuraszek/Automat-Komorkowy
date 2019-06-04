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

        if(x >= row) x =  row-x;
        if(x <  0) x = row+x;
        return x;
    }

    // wylicza współrzędną y w obrębie pola gry
    @Override
    public int funY(int y)
    {
        int column =sizeX;
        if(y >= column) y =  column-y;
        if(y <  0) y = column+y;
        return y;
    }
}
