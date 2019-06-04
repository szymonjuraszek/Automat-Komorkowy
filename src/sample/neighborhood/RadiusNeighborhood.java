package sample.neighborhood;

import sample.SquareShape;
import sample.boundary.BoundaryCondition;
import sample.structure.Cell;


public class RadiusNeighborhood implements Neighborhood {

    private double radious;
    private double howManyCellsToCheck;

    @Override
    public void check(BoundaryCondition condition, Cell[][] points, int i, int j, Cell[][] tmpPoints) {

        double distance;
        if(points[i][j].getColorNumber()!=0){
            for(int x=0;x<howManyCellsToCheck;x++){
                for(int y=0;y<howManyCellsToCheck;y++){
                    if (( (points[condition.funY(i + x)][condition.funX(j + y)].getColorNumber()==0))) {
                        tmpPoints[condition.funY(i + x)][condition.funX(j + y)].setColorNumber( points[i][j].getColorNumber());
                    }
                }
            }
            tmpPoints[i][j].setColorNumber(points[i][j].getColorNumber());
        }



    }


    public void setRadious(double radious) {
        this.radious = radious;
        countHowManyCellsToCheck();
    }

    private void countHowManyCellsToCheck(){
        howManyCellsToCheck = (int) (radious/SquareShape.WIDTH);
        if(radious/SquareShape.WIDTH%radious >0){
            howManyCellsToCheck++;
        }
    }
}
