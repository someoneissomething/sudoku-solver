import components.Cell;
import components.Grid;

import java.util.ArrayList;
import java.util.List;

public class Main {
    // Algorithm:
    // For each number:
    // 1. Find lines x,y that the number can't be in, and the positions it can't be in
    // 2. For each cell (9 * 9):
    //      a. Check if it's empty, if it is then continue, if not, move to the next cell
    //      b. Check if it's in an unavailable line or position, if it is then skip to the next cell, if not then continue
    //      c. Check the amount of empty cells in its lines that aren't in an unavailable line or position - if it returns 1 then fill the cell and move to the next one
    //      d. Check the amount of empty cells in its block that aren't in an unavailable line or position - if it returns 1 then fill the cell and move to the next one, if not then skip to the next cell
    //      e. TODO: Another thing to check: if the lines or block the cell is in contain all numbers except this number, it is that number

    private static final int[][] initialGrid = new int[][] {
            {0, 0, 0, 3, 9, 0, 0, 0, 0},
            {8, 0, 0, 1, 5, 0, 6, 9, 0},
            {0, 0, 6, 2, 8, 0, 0, 0, 7},
            {9, 8, 0, 0, 7, 0, 1, 0, 5},
            {2, 0, 0, 0, 0, 0, 0, 4, 0},
            {0, 0, 3, 0, 0, 9, 0, 2, 0},
            {0, 0, 9, 7, 6, 3, 0, 0, 0},
            {0, 7, 0, 0, 1, 0, 0, 6, 0},
            {5, 6, 0, 0, 0, 0, 0, 7, 0}
    };

    public static void main(String[] args) {
        Grid grid = new Grid();
        grid.initGridFromArray(initialGrid);

        System.out.println(grid);

        boolean wasACellFilled = true;

        while (wasACellFilled) {
            wasACellFilled = false;

            for (int currentNum = 1; currentNum <= 9; currentNum++) {
                for (int y = 0; y < 9; y++) {
                    for (int x = 0; x < 9; x++) {
                        List<ArrayList<Integer>> unavailableLines = getLinesContainingNumber(grid, currentNum);
                        Cell currentCell = grid.getCell(x, y);

                        if (checkCanCellBeFilled(currentCell, unavailableLines, grid)) {
                            grid.setCellNumber(x, y, currentNum);
                            wasACellFilled = true;
                        }
                    }
                }
            }
        }

        System.out.println("\n");
        System.out.println(grid);
    }

    public static List<ArrayList<Integer>> getLinesContainingNumber(Grid grid, int num) {
        ArrayList<Integer> cellXList = new ArrayList<>();
        ArrayList<Integer> cellYList = new ArrayList<>();
        ArrayList<Integer> blockList = new ArrayList<>();

        for (Cell[] cellsLine : grid.getCells()) {
            for (Cell cell : cellsLine) {
                if (cell.getNumber() == num) {
                    cellXList.add(cell.getX());
                    cellYList.add(cell.getY());
                    blockList.add(getBlockNumBasedOnPosition(cell.getX(), cell.getY()));
                }
            }
        }

        List<ArrayList<Integer>> result = new ArrayList<>();
        result.add(cellXList);
        result.add(cellYList);
        result.add(blockList);

        return result;
    }

    public static boolean checkCanCellBeFilled(Cell cell, List<ArrayList<Integer>> unavailableLines, Grid grid) {
        ArrayList<Integer> xList = unavailableLines.getFirst();
        ArrayList<Integer> yList = unavailableLines.get(1);
        ArrayList<Integer> blockList = unavailableLines.getLast();

        if (cell.getNumber() != 0) // Cell isn't empty, can't fill it
            return false;

        if (xList.contains(cell.getX()) || yList.contains(cell.getY()) || blockList.contains(getBlockNumBasedOnPosition(cell.getX(), cell.getY())))
            return false;
        // Cell is in a forbidden position for the current number, can't fill it

        return checkIsCellTheOnlyEmptyAvailable(cell, unavailableLines, grid);
    }

    public static boolean checkIsCellTheOnlyEmptyAvailable(Cell cell, List<ArrayList<Integer>> unavailableLines, Grid grid) {
        ArrayList<Integer> xList = unavailableLines.getFirst();
        ArrayList<Integer> yList = unavailableLines.get(1);
        ArrayList<Integer> blockList = unavailableLines.getLast();

        int[] blockStartPos = getBlockXYBasedOnNumber(getBlockNumBasedOnPosition(cell.getX(), cell.getY()));

        int amountInRow = 0;
        int amountInColumn = 0;
        int amountInBlock = 0;

        // Checking the row
        for (int columnInRow = 0; columnInRow < 9; columnInRow++) {
            amountInRow += isAvailable(grid.getCell(columnInRow, cell.getY()), xList, yList, blockList);
        }

        // Checking the column
        for (int rowInColumn = 0; rowInColumn < 9; rowInColumn++) {
            amountInColumn += isAvailable(grid.getCell(cell.getX(), rowInColumn), xList, yList, blockList);
        }

        // Checking the block
        for (int row = blockStartPos[1]; row < blockStartPos[1] + 3; row++) {
            for (int col = blockStartPos[0]; col < blockStartPos[0] + 3; col++) {
                amountInBlock += isAvailable(grid.getCell(col, row), xList, yList, blockList);
            }
        }

        return amountInRow == 1 || amountInColumn == 1 || amountInBlock == 1;
    }

    private static int isAvailable(Cell cell, List<Integer> xList, List<Integer> yList, List<Integer> blockList) {
        int blockNum = getBlockNumBasedOnPosition(cell.getX(), cell.getY());
        return (!xList.contains(cell.getX()) && !yList.contains(cell.getY()) && !blockList.contains(blockNum) && cell.getNumber() == 0) ? 1 : 0;
    }

    public static int getBlockNumBasedOnPosition(int x, int y) {
        if (x < 0 || x > 8 || y < 0 || y > 8) {
            return -1; // Invalid coordinates
        }

        int blockX = x / 3;
        int blockY = y / 3;
        return blockY * 3 + blockX;
    }

    public static int[] getBlockXYBasedOnNumber(int blockPos) {
        // [x, y]

        if (blockPos < 0 || blockPos > 8) {
            return new int[]{-1, -1}; // Invalid block number
        }

        int blockX = (blockPos % 3) * 3;
        int blockY = (blockPos / 3) * 3;
        return new int[]{blockX, blockY};
    }

    public static Cell[][] getCellGridFrom2DArray() {
        int[][] arrayOfInts = new int[][] {
                {9, 4, 0, 0, 3, 2, 0, 0, 5},
                {7, 0, 3, 9, 0, 0, 8, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 9},
                {0, 0, 0, 0, 0, 8, 9, 5, 0},
                {5, 8, 9, 7, 6, 3, 0, 0, 0},
                {0, 0, 0, 0, 0, 9, 0, 0, 0},
                {0, 3, 6, 0, 0, 0, 7, 0, 2},
                {1, 0, 2, 3, 0, 6, 0, 0, 0},
                {0, 0, 0, 8, 2, 0, 0, 0, 3},
        };

        Cell[][] cellGrid = new Cell[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cellGrid[i][j] = new Cell(arrayOfInts[i][j], j, i);
            }
        }

        return cellGrid;
    }

    private static void printGridOfCells(Cell[][] gridOfCells) {
        for (Cell[] cellsLine : gridOfCells) {
            for (Cell cell : cellsLine) {
                System.out.print(cell.getNumber() + " ");
            }

            System.out.println();
        }
    }
}