package sample.neighborhood;

import sample.boundary.BoundaryCondition;
import sample.structure.Cell;

public interface Neighborhood {

    void check(BoundaryCondition condition, Cell point[][], int i, int j, Cell tmpPoints[][]);
}
