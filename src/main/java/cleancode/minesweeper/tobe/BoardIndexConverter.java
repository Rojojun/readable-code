package cleancode.minesweeper.tobe;

public class BoardIndexConverter {
    private static final char BASE_CHAR_FOR_COL = 'a';

    public int getSelectedColIndex(String cellInput, int colSize) {
        char cellInputCol = cellInput.charAt(0);
        return convertColFrom(cellInputCol, colSize);
    }

    public int getSelectedRowIndex(String cellInput, int rowSize) {
        String cellInputRow = cellInput.substring(1);
        return convertRowFrom(cellInputRow, rowSize);
    }

    private int convertRowFrom(String cellInputRow, int rowSize) {
        int rowIndex = Integer.parseInt(cellInputRow) - 1;
        if (rowIndex >=  rowSize || rowIndex < 0) {
            throw new GameExcpetion("Invalid row index : " + rowIndex);
        }
        return rowIndex;
    }

    private int convertColFrom(char cellInputCol, int colSize) {
        int colIndex = cellInputCol - BASE_CHAR_FOR_COL;
        if (colIndex >= colSize || colIndex < 0) {
            throw new GameExcpetion("Invalid column index : " + colIndex);
        }

        return colIndex;
    }
}
