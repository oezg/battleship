package battleship;

enum Fleet {
    A("Aircraft Carrier", 5),
    B("Battleship", 4),
    S("Submarine", 3),
    C("Cruiser", 3),
    D("Destroyer", 2);
    String name;
    int len;
    Fleet(String name, int len) {
        this.name = name;
        this.len = len;
    }
    String getName() {
        return this.name;
    }
    int getLen() {
        return this.len;
    }
}

public class Ship {
    private final String name;
    private final int len;
    private Cell[] cells;

    public Ship(Fleet vessel) {
        this.name = vessel.getName();
        this.len = vessel.getLen();
        this.cells = new Cell[len];
    }

    public String getName() {
        return this.name;
    }

    public int getLen() {
        return this.len;
    }

    public void addCell(int index, Cell cell) {
        cells[index] = cell;
    }

    public boolean isSunk() {
        boolean sunk = true;
        for (Cell cell : cells) if (cell.getStatus() != Status.H) sunk = false;
        return sunk;
    }
}
