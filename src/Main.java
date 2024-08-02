import components.Grid;

public class Main {
    // Algorithm:
    // For each number:
    // 1. Find lines x,y that the number can't be in, and the positions it can't be in
    // 2. For each cell (9 * 9):
    //      a. Check if it's empty, if it is then continue, if not, move to the next cell
    //      b. Check if it's in an unavailable line or position, if it is then skip to the next cell, if not then continue
    //      c. Check the amount of empty cells in its lines that aren't in an unavailable line or position - if it returns 1 then fill the cell and move to the next one
    //      d. Check the amount of empty cells in its block that aren't in an unavailable line or position - if it returns 1 then fill the cell and move to the next one, if not then skip to the next cell
    //      e. Another thing to check: if the lines or block the cell is in contain all numbers except this number, it is that number

    private static final int[][] initialGrid = new int[][] {
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 9, 0, 7, 4, 0},
            {7, 0, 3, 0, 0, 0, 0, 9, 6},
            {0, 0, 0, 6, 7, 0, 0, 0, 4},
            {0, 0, 9, 0, 0, 0, 2, 5, 0},
            {0, 0, 0, 5, 4, 0, 0, 1, 0},
            {0, 6, 2, 0, 1, 0, 3, 0, 0},
            {1, 8, 0, 7, 6, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 5, 0, 0}
    };

    public static void main(String[] args) {
        Grid grid = new Grid();
        grid.initGridFromArray(initialGrid);

        System.out.println(grid);
        grid.fillGrid();
        System.out.println(grid);
    }
}