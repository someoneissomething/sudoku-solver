import components.Grid;

public class Main {
    /** Algorithm:<br>
     * For each number:<br>
     * 1. Find lines x,y that the number can't be in, and the positions it can't be in<br>
     * 2. For each cell (9 * 9):<br>
     *      &emsp; a. Check if it's empty, if it is then continue, if not, move to the next cell<br>
     *      &emsp; b. Check if it's in an unavailable line or position, if it is then skip to the next cell, if not then continue<br>
     *      &emsp; c. Check the amount of empty cells in its lines that aren't in an unavailable line or position - if it returns 1 then fill the cell and move to the next one<br>
     *      &emsp; d. Check the amount of empty cells in its block that aren't in an unavailable line or position - if it returns 1 then fill the cell and move to the next one, if not then skip to the next cell<br>
     *      &emsp; e. Another thing to check: if the lines or block the cell is in contain all numbers except this number, it is that number */

    private static final int[][] initialGrid = new int[][] {
            {2, 0, 0, 0, 0, 5, 0, 0, 3},
            {0, 1, 0, 0, 7, 0, 0, 9, 0},
            {0, 0, 4, 9, 0, 0, 8, 0, 0},
            {6, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 8, 0, 0, 3, 0, 0, 2, 0},
            {0, 0, 3, 0, 0, 0, 0, 0, 6},
            {0, 0, 5, 0, 0, 1, 7, 0, 0},
            {0, 9, 0, 0, 4, 0, 0, 1, 0},
            {0, 0, 0, 3, 0, 0, 0, 0, 8}
    };

    public static void main(String[] args) {
        Grid grid = new Grid();
        grid.initGridFromArray(initialGrid);

        System.out.println(grid);
        grid.fillGrid();
        System.out.println(grid);
    }
}