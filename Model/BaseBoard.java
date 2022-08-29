package Model;

public abstract class BaseBoard {

    public int width;
    public int height;

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    @Override
    public abstract String toString();
    public abstract boolean selectUnit(int row, int column);
}
