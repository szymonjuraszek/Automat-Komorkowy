package sample.neighborhood;

import sample.SquareShape;
import sample.boundary.BoundaryCondition;
import sample.structure.Cell;

import java.util.Random;

public class RadiusNeighborhood implements Neighborhood {

    private double radious;
    private int [][] measuresOfGravityX;
    private int [][] measuresOfGravityY;

    @Override
    public void check(BoundaryCondition condition, Cell[][] point, int i, int j, Cell[][] tmpPoints) {





    }


    public void setRadious(double radious) {
        this.radious = radious;
    }
}
