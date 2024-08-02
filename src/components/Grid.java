package components;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    private Cell[][] cells;

    public Grid() {
        cells = new Cell[9][9];
    }

    public void initGridFromArray(int[][] arr) {
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                cells[y][x] = new Cell(arr[y][x], x, y);
            }
        }
    }

    public void initEmptyGrid() {
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                cells[y][x] = new Cell(0, x, y);
            }
        }
    }

    public Cell[][] getCells() {
        return this.cells;
    }

    public Cell getCell(int x, int y) {
        return this.cells[y][x];
    }

    public void setCellNumber(int x, int y, int number) {
        this.cells[y][x].setNumber(number);
    }

    public void fillGrid() {
        boolean wasACellFilled = true;

        while (wasACellFilled) {
            wasACellFilled = false;

            for (int currentNum = 1; currentNum <= 9; currentNum++) {
                for (int y = 0; y < 9; y++) {
                    for (int x = 0; x < 9; x++) {
                        List<ArrayList<Integer>> unavailableLines = getLinesContainingNumber(currentNum);
                        Cell currentCell = this.getCell(x, y);

                        if (checkCanCellBeFilled(currentCell, unavailableLines, currentNum)) {
                            this.setCellNumber(x, y, currentNum);
                            wasACellFilled = true;
                        }
                    }
                }
            }
        }
    }

    public List<ArrayList<Integer>> getLinesContainingNumber(int num) {
        ArrayList<Integer> cellXList = new ArrayList<>();
        ArrayList<Integer> cellYList = new ArrayList<>();
        ArrayList<Integer> blockList = new ArrayList<>();

        for (Cell[] cellsLine : this.getCells()) {
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

    public boolean checkCanCellBeFilled(Cell cell, List<ArrayList<Integer>> unavailableLines, int currentNum) {
        ArrayList<Integer> xList = unavailableLines.getFirst();
        ArrayList<Integer> yList = unavailableLines.get(1);
        ArrayList<Integer> blockList = unavailableLines.getLast();

        if (cell.getNumber() != 0) // Cell isn't empty, can't fill it
            return false;

        if (xList.contains(cell.getX()) || yList.contains(cell.getY()) || blockList.contains(getBlockNumBasedOnPosition(cell.getX(), cell.getY())))
            return false;
        // Cell is in a forbidden position for the current number, can't fill it

        return checkIsCellTheOnlyEmptyAvailable(cell, unavailableLines, currentNum);
    }

    public boolean checkIsCellTheOnlyEmptyAvailable(Cell cell, List<ArrayList<Integer>> unavailableLines, int currentNum) {
        ArrayList<Integer> xList = unavailableLines.getFirst();
        ArrayList<Integer> yList = unavailableLines.get(1);
        ArrayList<Integer> blockList = unavailableLines.getLast();

        ArrayList<Integer> numberList = new ArrayList<>();
        numberList.add(0);

        int[] blockStartPos = getBlockXYBasedOnNumber(getBlockNumBasedOnPosition(cell.getX(), cell.getY()));

        int amountInRow = 0;
        int amountInColumn = 0;
        int amountInBlock = 0;

        // Checking the row
        for (int columnInRow = 0; columnInRow < 9; columnInRow++) {
            Cell currentCell = this.getCell(columnInRow, cell.getY());

            amountInRow += isAvailable(currentCell, xList, yList, blockList);
            numberList = updateNumberList(currentCell, numberList);
        }

        // Checking the column
        for (int rowInColumn = 0; rowInColumn < 9; rowInColumn++) {
            Cell currentCell = this.getCell(cell.getX(), rowInColumn);

            amountInColumn += isAvailable(currentCell, xList, yList, blockList);
            numberList = updateNumberList(currentCell, numberList);
        }

        // Checking the block
        for (int row = blockStartPos[1]; row < blockStartPos[1] + 3; row++) {
            for (int col = blockStartPos[0]; col < blockStartPos[0] + 3; col++) {
                Cell currentCell = this.getCell(col, row);

                amountInBlock += isAvailable(currentCell, xList, yList, blockList);
                numberList = updateNumberList(currentCell, numberList);
            }
        }

        // If the number lists contains all numbers except the one we're dealing with, then it is the only one available
        if (!numberList.contains(currentNum) && numberList.size() == 9)
            return true;

        return amountInRow == 1 || amountInColumn == 1 || amountInBlock == 1;
    }

    private int isAvailable(Cell cell, List<Integer> xList, List<Integer> yList, List<Integer> blockList) {
        int blockNum = getBlockNumBasedOnPosition(cell.getX(), cell.getY());
        return (!xList.contains(cell.getX()) && !yList.contains(cell.getY()) && !blockList.contains(blockNum) && cell.getNumber() == 0) ? 1 : 0;
    }

    private ArrayList<Integer> updateNumberList(Cell cell, ArrayList<Integer> numberList) {
        if (!numberList.contains(cell.getNumber()))
            numberList.add(cell.getNumber());

        return numberList;
    }

    public int getBlockNumBasedOnPosition(int x, int y) {
        if (x < 0 || x > 8 || y < 0 || y > 8) {
            return -1; // Invalid coordinates
        }

        int blockX = x / 3;
        int blockY = y / 3;
        return blockY * 3 + blockX;
    }

    public int[] getBlockXYBasedOnNumber(int blockPos) {
        // [x, y]

        if (blockPos < 0 || blockPos > 8) {
            return new int[]{-1, -1}; // Invalid block number
        }

        int blockX = (blockPos % 3) * 3;
        int blockY = (blockPos / 3) * 3;
        return new int[]{blockX, blockY};
    }

    @Override
    public String toString() {
        StringBuilder stringToPrint = new StringBuilder();

        int blockXCounter = 0;
        int blockYCounter = 0;

        for (Cell[] cellsLine : this.cells) {
            for (Cell cell : cellsLine) {
                if ((blockXCounter + 1) % 3 == 0)
                    stringToPrint.append(cell.getNumber()).append("   ");
                else
                    stringToPrint.append(cell.getNumber()).append(" ");

                blockXCounter++;
            }

            blockXCounter = 0;

            if ((blockYCounter + 1) % 3 == 0)
                stringToPrint.append("\n\n");
            else
                stringToPrint.append("\n");

            blockYCounter++;
        }

        return stringToPrint.toString();
    }
}