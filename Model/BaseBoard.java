package Model;

public abstract class BaseBoard {

    private final int width;
    private final int height;

    protected BaseBoard() {
        width = 0;
        height = 0;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    @Override
    public abstract String toString();
    public abstract boolean selectUnit(int row, int column);
}
