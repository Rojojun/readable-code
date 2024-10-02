package cleancode.minesweeper.tobe.cell;

public class NumberCell extends Cell {
    private final int nearByLandMineCount;

    public NumberCell(int nearByLandMineCount) {
        super();
        this.nearByLandMineCount = nearByLandMineCount;
    }

    @Override
    public boolean isLandMine() {
        return false;
    }

    @Override
    public boolean hasLandMineCount() {
        return true;
    }

    @Override
    public String getSign() {
        if (super.isOpened) {
            return String.valueOf(nearByLandMineCount);
        }
        if (super.isFlagged) {
            return FLAG_SIGN;
        }
        return UNCHECKED_SIGN;
    }
}
