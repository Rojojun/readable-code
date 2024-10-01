package cleancode.minesweeper.tobe;

import java.util.Arrays;
import java.util.Random;

public class GameBoard {
    private static final int LANDMINE_COUNT = 10;

    private final Cell[][] board;

    public GameBoard(int rowSize, int colSize) {
        board = new Cell[rowSize][colSize];
    }

    public void initializeGame() {
        int rowSize = board.length;
        int colSize = board[0].length;

        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                board[row][col] = Cell.create();
            }
        }

        for (int i = 0; i < LANDMINE_COUNT; i++) {
            int landMineCol = new Random().nextInt(10);
            int landMineRow = new Random().nextInt(8);
            findCell(landMineRow, landMineCol).turnOnLandmine();
        }

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 10; col++) {
                if (isLandMineCell(row, col)) {
                    continue;
                }
                int count = countNearbyLandMines(row, col);
                findCell(row, col).updateNearbyLandMineCount(count);
            }
        }
    }

    public int getColSize() {
        return board[0].length;
    }

    public int getRowSize() {
        return board.length;
    }

    public String getSign(int rowIndex, int colIndex) {
        Cell cell = findCell(rowIndex, colIndex);
        return cell.getSign();
    }

    public void flag(int selectedRowIndex, int selectedColIndex) {
        findCell(selectedRowIndex, selectedColIndex).flag();
    }

    public void open(int rowIndex, int colIndex) {
        findCell(rowIndex, colIndex).open();
    }

    private Cell findCell(int row, int col) {
        return board[row][col];
    }

    public boolean isLandMineCell(int selectedRowIndex, int selectedColIndex) {
        return findCell(selectedRowIndex, selectedColIndex).isLandMine();
    }

    public void openSurroundedCells(int row, int col) {
        if (row < 0 || row >= getRowSize() || col < 0 || col >= getColSize()) {
            return;
        }
        if (findCell(row, col).isOpened()) {
            return;
        }
        if (isLandMineCell(row, col)) {
            return;
        }

        open(row, col);

        if (findCell(row, col).hasLandMineCount()) {
            return;
        }

        openSurroundedCells(row - 1, col);
        openSurroundedCells(row - 1, col + 1);
        openSurroundedCells(row - 1, col - 1);
        openSurroundedCells(row, col - 1);
        openSurroundedCells(row, col + 1);
        openSurroundedCells(row + 1, col - 1);
        openSurroundedCells(row + 1, col);
        openSurroundedCells(row + 1, col + 1);
    }

    public boolean isAllCellChecked() {
        return Arrays.stream(board)
                .flatMap(Arrays::stream)
                .allMatch(Cell::isChecked);
    }

    private int countNearbyLandMines(int row, int col) {
        int count = 0;
        if (row - 1 >= 0 && col - 1 >= 0 && isLandMineCell(row - 1, col - 1)) {
            count++;
        }
        if (row - 1 >= 0 && isLandMineCell(row - 1, col)) {
            count++;
        }
        if (row - 1 >= 0 && col + 1 < 10 && isLandMineCell(row - 1, col + 1)) {
            count++;
        }
        if (col - 1 >= 0 && isLandMineCell(row, col - 1)) {
            count++;
        }
        if (col + 1 < 10 && isLandMineCell(row, col + 1)) {
            count++;
        }
        if (row + 1 < 8 && col - 1 >= 0 && isLandMineCell(row + 1, col - 1)) {
            count++;
        }
        if (row + 1 < 8 && isLandMineCell(row + 1, col)) {
            count++;
        }
        if (row + 1 < 8 && col + 1 < 10 && isLandMineCell(row + 1, col + 1)) {
            count++;
        }
        return count;
    }
}
