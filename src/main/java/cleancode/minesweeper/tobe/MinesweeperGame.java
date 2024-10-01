package cleancode.minesweeper.tobe;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;

public class MinesweeperGame {
    public static final int BOARD_ROW_SIZE = 8;
    public static final int BOARD_COL_SIZE = 10;
    public static final int LANDMINE_COUNT = 10;
    public static final Scanner SCANNER = new Scanner(System.in);
    private static final Cell[][] BOARD = new Cell[BOARD_ROW_SIZE][BOARD_COL_SIZE];

    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배
    private static Logger logger = Logger.getLogger("MinesweeperGame");

    public static void main(String[] args) {
        showGameStartComments();
        initializeGame();

        while (true) {
            try {
                showBoard();

                if (doesUserWinTheGame()) {
                    System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
                    break;
                }
                if (doesUserLoseTheGame()) {
                    System.out.println("지뢰를 밟았습니다. GAME OVER!");
                    break;
                }

                String cellInput = getCellInputFromUser(SCANNER);
                String userActionInput = getUserActionInputFromUser("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)", SCANNER);

                actOnCell(cellInput, userActionInput);
            } catch (AppException e) {
                logger.warning(e.getMessage());
            } catch (Exception e) {
                logger.warning("프로그램에서 문제가 발생하였습니다.");
            }
        }
    }

    private static void actOnCell(String cellInput, String userActionInput) {
        int selectedColIndex = getSelectedColIndex(cellInput);
        int selectedRowIndex = getSelectedRowIndex(cellInput);

        if (doesUserChooseToPlantFlag(userActionInput)) {
            BOARD[selectedRowIndex][selectedColIndex].flag();
            checkIfGameIsOver();
            return;
        }

        if (doesUserChooseToOpenCell(userActionInput)) {
            if (isLandMineCell(selectedRowIndex, selectedColIndex)) {
                BOARD[selectedRowIndex][selectedColIndex].open();
                changeGameStatusToLose();
                return;
            }
            open(selectedRowIndex, selectedColIndex);
            checkIfGameIsOver();
            return;
        }

        System.out.println("잘못된 번호를 선택하셨습니다.");
    }

    private static void changeGameStatusToLose() {
        gameStatus = -1;
    }

    private static boolean isLandMineCell(int selectedRowIndex, int selectedColIndex) {
        return BOARD[selectedRowIndex][selectedColIndex].isLandMine();
    }

    private static boolean doesUserChooseToOpenCell(String userActionInput) {
        return userActionInput.equals("1");
    }

    private static boolean doesUserChooseToPlantFlag(String userActionInput) {
        return userActionInput.equals("2");
    }

    private static int getSelectedRowIndex(String cellInput) {
        char cellInputRow = cellInput.charAt(1);
        int selectedRowIndex = convertRowFrom(cellInputRow);
        return selectedRowIndex;
    }

    private static int getSelectedColIndex(String cellInput) {
        char cellInputCol = cellInput.charAt(0);
        int selectedColIndex = convertColFrom(cellInputCol);
        return selectedColIndex;
    }

    private static String getUserActionInputFromUser(String message, Scanner scanner) {
        System.out.println(message);
        String userActionInput = scanner.nextLine();
        return userActionInput;
    }

    private static String getCellInputFromUser(Scanner scanner) {
        String cellInput = getUserActionInputFromUser("선택할 좌표를 입력하세요. (예: a1)", scanner);
        return cellInput;
    }

    private static boolean doesUserLoseTheGame() {
        return gameStatus == -1;
    }

    private static boolean doesUserWinTheGame() {
        return gameStatus == 1;
    }

    private static void checkIfGameIsOver() {
        boolean isAllChecked = isAllCellChecked();
        if (isAllChecked) {
            changeGameStatusWin();
        }
    }

    private static void changeGameStatusWin() {
        gameStatus = 1;
    }

    private static boolean isAllCellChecked() {
        return Arrays.stream(BOARD)
                .flatMap(Arrays::stream)
                .allMatch(Cell::isChecked);
    }

    private static int convertRowFrom(char cellInputRow) {
        int rowIndex = Character.getNumericValue(cellInputRow) - 1;
        if (rowIndex >= BOARD_ROW_SIZE) {
            throw new AppException("Invalid row index : " + rowIndex);
        }
        return rowIndex;
    }

    private static int convertColFrom(char cellInputCol) {
        return switch (cellInputCol) {
            case 'a' -> 0;
            case 'b' -> 1;
            case 'c' -> 2;
            case 'd' -> 3;
            case 'e' -> 4;
            case 'f' -> 5;
            case 'g' -> 6;
            case 'h' -> 7;
            case 'i' -> 8;
            case 'j' -> 9;
            default -> throw new AppException("Unexpected value: " + cellInputCol);
        };
    }

    private static void showBoard() {
        System.out.println("   a b c d e f g h i j");
        for (int row = 0; row < 8; row++) {
            System.out.printf("%d  ", row + 1);
            for (int col = 0; col < 10; col++) {
                System.out.print(BOARD[row][col].getSign() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void initializeGame() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 10; col++) {
                BOARD[row][col] = Cell.create();
            }
        }

        for (int i = 0; i < LANDMINE_COUNT; i++) {
            int col = new Random().nextInt(10);
            int row = new Random().nextInt(8);
            BOARD[row][col].turnOnLandmine();
        }

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 10; col++) {
                if (isLandMineCell(row, col)) {
                    continue;
                }
                    int count = countNearbyLandMines(row, col);
                    BOARD[row][col].updateNearbyLandMineCount(count);
            }
        }
    }

    private static int countNearbyLandMines(int row, int col) {
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

    private static void showGameStartComments() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private static void open(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 10) {
            return;
        }
        if (BOARD[row][col].isOpened()) {
            return;
        }
        if (isLandMineCell(row, col)) {
            return;
        }

        BOARD[row][col].open();

        if (BOARD[row][col].hasLandMineCount()) {
            BOARD[row][col].open();
            return;
        }

        open(row - 1, col - 1);
        open(row - 1, col);
        open(row - 1, col + 1);
        open(row, col - 1);
        open(row, col + 1);
        open(row + 1, col - 1);
        open(row + 1, col);
        open(row + 1, col + 1);
    }

}
