package cleancode.minesweeper.tobe.cell;

public class EmptyCell extends Cell {
    protected static final String EMPTY_SIGN = "■";

    @Override
    public boolean isLandMine() {
        return false;
    }

    @Override
    public boolean hasLandMineCount() {
        return false;
    }

    @Override
    public String getSign() {
        if (super.isOpened) {
            return EMPTY_SIGN;
        }
        if (super.isFlagged) {
            return FLAG_SIGN;
        }
        return UNCHECKED_SIGN;
    }
}
