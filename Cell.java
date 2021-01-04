package battleship;

enum Status{
    E(0),
    F(1),
    H(2),
    M(3);
    private int val;
    Status(int val) { this.val = val; }

    public int getVal() { return val; }

    public void setVal(int val) { this.val = val; }
}

public class Cell {
    private int row;
    private int col;
    private Status status;
    private Ship ship;
    Cell(int x, int y) {
        this.row = x;
        this.col = y;
        this.status = Status.E;
        this.ship = null;
    }

    public Status getStatus() { return status; }

    public void setStatus(Status status) { this.status = status; }

    public Ship getShip() {
        return ship;
    }

    public void occupy(Ship ship) {
        this.setStatus(Status.F);
        this.ship = ship;
    }
}
