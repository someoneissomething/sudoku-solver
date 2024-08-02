package components;

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