package sample.boundary;

public class AbsorbingCondition extends BoundaryCondition {

    public AbsorbingCondition(int x,int y){
        sizeX=x;
        sizeY=y;
    }

    @Override
    public int funX(int x)
    {
        int row =sizeY;
        if(x >= row) x =  row-1;
        if(x <  0) x = 0;
        return x;
    }

    // wylicza współrzędną y w obrębie pola gry
    @Override
    public int funY(int y)
    {
        int column =sizeX;
        if(y >= column) y = column-1;
        if(y <  0) y = 0;
        return y;
    }
}
