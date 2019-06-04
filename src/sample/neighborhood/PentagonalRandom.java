package sample.neighborhood;

import sample.boundary.BoundaryCondition;
import sample.structure.Cell;

import java.util.Random;

public class PentagonalRandom implements Neighborhood {
    @Override
    public void check(BoundaryCondition condition, Cell[][] points, int i, int j, Cell[][] tmpPoints) {
        Random random = new Random();
        int choose=random.nextInt(4);
        int x, y;

        if(points[i][j].getColorNumber()!=0) {
            switch (choose) {
//            top
//            0 1 1
//            0 1 1
//            0 1 1
                case 0: {
                        for (x = -1; x < 2; x++) {
                            for (y = 0; y < 2; y++) {
                                if (((points[condition.funY(i + x)][condition.funX(j + y)].getColorNumber() == 0))) {
                                    tmpPoints[condition.funY(i + x)][condition.funX(j + y)].setColorNumber(points[i][j].getColorNumber());
                                }
                            }
                        }
                    break;
                }
                case 1: {
                    //bot
                    // 1 1 0
//             1 1 0
//            1 1 0
                        for (x = -1; x < 2; x++) {
                            for (y = -1; y < 1; y++) {
                                if (((points[condition.funY(i + x)][condition.funX(j + y)].getColorNumber() == 0))) {
                                    tmpPoints[condition.funY(i + x)][condition.funX(j + y)].setColorNumber(points[i][j].getColorNumber());
                                }
                            }
                        }
                    break;
                }
                case 2: {
                    //left
                    // 1 1 1
//            1 1 1
//            0 0 0
                        for (x = -1; x < 1; x++) {
                            for (y = -1; y < 2; y++) {
                                if (((points[condition.funY(i + x)][condition.funX(j + y)].getColorNumber() == 0))) {
                                    tmpPoints[condition.funY(i + x)][condition.funX(j + y)].setColorNumber(points[i][j].getColorNumber());
                                }
                            }
                        }
                    break;
                }
                //right
                //0 0 0
//            1 1 1
//            1 1 1
                case 3: {
                        for (x = 0; x < 2; x++) {
                            for (y = -1; y < 2; y++) {
                                if (((points[condition.funY(i + x)][condition.funX(j + y)].getColorNumber() == 0))) {
                                    tmpPoints[condition.funY(i + x)][condition.funX(j + y)].setColorNumber(points[i][j].getColorNumber());
                                }
                            }
                        }
                    break;
                }

            }
            tmpPoints[i][j].setColorNumber( points[i][j].getColorNumber());
        }

    }





}
