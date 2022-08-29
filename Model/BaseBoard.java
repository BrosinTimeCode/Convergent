package Model;

public abstract class BaseBoard {

    public int width;
    public int height;

    @Override
    public abstract String toString();

    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
