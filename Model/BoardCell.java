package Model;
import Units.*;
public class BoardCell {
    public BaseUnit unit;
    public boolean visited = false;
    public BoardCell(BaseUnit unit) {
        this.unit = unit;
    }
}
