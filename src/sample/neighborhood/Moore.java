package sample.neighborhood;

import sample.boundary.BoundaryCondition;
import sample.structure.Cell;

import java.util.Random;

public class Moore implements Neighborhood {

    @Override
    public int check(BoundaryCondition condition, Cell points[][], int i, int j, Cell tmpPoints[][]) {

        int x, y;
        int energy=0;

        // oblicza liczbę sąsiadów komórki [x,y]
        if(points[i][j].getColorNumber()!=0){
            for (x = -1; x < 2; x++)
                for (y = -1; y < 2; y++) {
                    if (( (points[condition.funY(i + x)][condition.funX(j + y)].getColorNumber()==0))) {
                        tmpPoints[condition.funY(i + x)][condition.funX(j + y)].setColorNumber( points[i][j].getColorNumber());
                    }
                    if ( points[condition.funY(i + x)][condition.funX(j + y)].getColorNumber() != (points[i][j].getColorNumber()) ) {
                        energy++;
                    }
                }

            tmpPoints[i][j].setColorNumber(points[i][j].getColorNumber());
        }

        return energy;
    }

}


























//    @Override
//    public void check(BoundaryCondition condition, int points[][], int i, int j,int tmpPoints[][]) {
//
//        int howManyNeighbours = 0;
//        int x, y;
//
//        // oblicza liczbę sąsiadów komórki [x,y]
//        for (x = -1; x < 2; x++)
//            for (y = -1; y < 2; y++) {
//                if (((x != 0) || (y != 0)) && (points[condition.funY(i + x)][condition.funX(j + y)]==1)) {
//                    howManyNeighbours++;
//                }
//            }
//
//        // sprawdza reguły przeżycia komórki lub narodzin nowej
//        if (points[i][j]==1)    // gdy komórka żywa
//        {
//            if ((howManyNeighbours == 2) || (howManyNeighbours == 3)) {
//                tmpPoints[i][j]=1;
//            } else {
//                tmpPoints[i][j]=0;
//            }
//        } else  // gdy komórka martwa
//        {
//            if (howManyNeighbours == 3) {
//                tmpPoints[i][j]=1;
//            } else {
//                tmpPoints[i][j]=0;
//            }
//        }
//
//
//
//    }
