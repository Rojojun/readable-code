package cleancode.minesweeper.tobe.cell;

public class LandMineCell extends Cell {
    protected static final String MINE_SIGN = "☼";

    @Override
    public boolean isLandMine() {
        return true;
    }

    @Override
    public boolean hasLandMineCount() {
        return false;
    }

    @Override
    public String getSign() {
        if (super.isOpened) {
            return MINE_SIGN;
        }
        if (super.isFlagged) {
            return FLAG_SIGN;
        }
        return UNCHECKED_SIGN;
    }
}
