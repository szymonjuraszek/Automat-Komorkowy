package sample.neighborhood;

import sample.boundary.BoundaryCondition;
import sample.structure.Cell;

import java.util.Random;

public interface Neighborhood {

    int check(BoundaryCondition condition, Cell point[][], int i, int j, Cell tmpPoints[][]);

    default int giveRandomIdNeighborhood(Cell points[][],BoundaryCondition condition,int i, int j) {
        Random random = new Random();
        int x;
        int y;
        do{
            x = random.nextInt(3)-1;
            y = random.nextInt(3)-1;
        }while(x==0 && y==0);

//        System.out.println(x);
//        System.out.println(y);

        return points[condition.funY(i + x)][condition.funX(j + y)].getColorNumber();
    }


}
