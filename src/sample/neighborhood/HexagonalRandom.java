package sample.neighborhood;

import sample.boundary.BoundaryCondition;
import sample.structure.Cell;

import java.util.Random;

public class HexagonalRandom implements Neighborhood {
    @Override
    public void check(BoundaryCondition condition, Cell[][] points, int i, int j, Cell[][] tmpPoints) {
        Random random = new Random();

        int choose=random.nextInt(2);
        System.out.println(choose);

        switch (choose){
            case 0:{
                int x, y;

                if(points[i][j].getColorNumber()!=0) {
                    for (x = -1; x < 2; x++) {
                        if (x == -1) {
                            for (y = 0; y < 2; y++) {
                                if (((points[condition.funY(i + x)][condition.funX(j + y)].getColorNumber() == 0))) {
                                    tmpPoints[condition.funY(i + x)][condition.funX(j + y)].setColorNumber(points[i][j].getColorNumber());
                                }
                            }
                        } else if (x == 1) {
                            for (y = -1; y < 1; y++) {
                                if (((points[condition.funY(i + x)][condition.funX(j + y)].getColorNumber() == 0))) {
                                    tmpPoints[condition.funY(i + x)][condition.funX(j + y)].setColorNumber(points[i][j].getColorNumber());
                                }
                            }
                        } else {
                            for (y = -1; y < 2; y++) {
                                if ((points[condition.funY(i + x)][condition.funX(j + y)].getColorNumber() == 0)) {
                                    tmpPoints[condition.funY(i + x)][condition.funX(j + y)].setColorNumber(points[i][j].getColorNumber());
                                }
                            }
                        }
                    }

                    tmpPoints[i][j].setColorNumber(points[i][j].getColorNumber());
                }

                break;
            }
            case 1:{
                int x, y;

                if(points[i][j].getColorNumber()!=0) {
                    for (x = -1; x < 2; x++) {
                        if (x == -1) {
                            for (y = 0; y < 1; y++) {
                                if (((points[condition.funY(i + x)][condition.funX(j + y)].getColorNumber() == 0))) {
                                    tmpPoints[condition.funY(i + x)][condition.funX(j + y)].setColorNumber(points[i][j].getColorNumber());
                                }
                            }
                        } else if (x == 1) {
                            for (y = 0; y < 2; y++) {
                                if (((points[condition.funY(i + x)][condition.funX(j + y)].getColorNumber() == 0))) {
                                    tmpPoints[condition.funY(i + x)][condition.funX(j + y)].setColorNumber(points[i][j].getColorNumber());
                                }
                            }
                        } else {
                            for (y = -1; y < 2; y++) {
                                if ((points[condition.funY(i + x)][condition.funX(j + y)].getColorNumber() == 0)) {
                                    tmpPoints[condition.funY(i + x)][condition.funX(j + y)].setColorNumber(points[i][j].getColorNumber());
                                }
                            }
                        }
                    }

                    tmpPoints[i][j].setColorNumber(points[i][j].getColorNumber());
                }

                break;
            }
        }
    }
}