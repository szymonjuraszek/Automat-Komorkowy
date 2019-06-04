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

    public void calculateMeasuresOfGravity(int x,int y){
        this.measuresOfGravityX = new int[x][y];
        this.measuresOfGravityY = new int[x][y];
        Random random = new Random();

        for(int w=0;w<x;w++){
            for(int z=0;z<y;z++){
                measuresOfGravityX[w][z] = w * SquareShape.HEIGHT + random.nextInt(10);
                measuresOfGravityY[w][z] = z * SquareShape.WIDTH + random.nextInt(10);
            }
        }
    }


    public void setRadious(double radious) {
        this.radious = radious;
    }
}
