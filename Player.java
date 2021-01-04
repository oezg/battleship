package battleship;

import java.util.Arrays;
import java.util.Scanner;

public class Player {
    private final String name;
    private final static int size = 10;
    private Cell[][] field;
    private Ship[] myShips;
    private Player opponent;
    public final static Scanner scanner = new Scanner(System.in);

    Player(String name) {
        this.name = name;
        field = new Cell[size][size];
        myShips = new Ship[Fleet.values().length];
        buildShips();
        setField();
    }
    private void buildShips() {
        for (Fleet vessel : Fleet.values()) myShips[vessel.ordinal()] = new Ship(vessel);
    }
    private void setField() {
        for (int i = 0; i < size; i++) for (int j = 0; j < size; j++) field[i][j] = new Cell(i, j);
    }
    public String getName() {
        return name;
    }

    public Cell[][] getField() { return field; }

    public Cell getCell(int x, int y) { return getField()[x][y]; }

    public Player getOpponent() { return opponent; }

    public void setOpponent(Player opponent) { this.opponent = opponent; }

    public boolean isLoser() {
        boolean loser = true;
        for (Ship s : myShips) if (!s.isSunk()) loser = false;
        return loser;
    }
    public void placeShips() {
        System.out.printf("%s, place your ships to the game field\n", getName());
        printField(true);
        for (Ship s: myShips) {
            System.out.printf("Enter the coordinates of the %s (%d cells):\n\n", s.getName(), s.getLen());
            int[] coors = new int[4];
            while (takeCoordinates(coors, s));
            markCells(coors, s);
            printField(true);
        }
        System.out.println("Press Enter and pass the move to another player\n...");
    }

    public void markCells(int[] coors, Ship ship) {
        int index = 0;
        for (int i = Math.min(coors[0], coors[2]); i <= Math.max(coors[0], coors[2]); i++) {
            for (int j = Math.min(coors[1], coors[3]); j <= Math.max(coors[1], coors[3]); j++) {
                field[i][j].occupy(ship);
                ship.addCell(index++, field[i][j]);
            }
        }
    }

    public void printBothFields() {
        for (int i = -1; i < size; i++) {
            for (int j = -1; j < size; j++) {
                String str;
                if (i < 0 && j < 0) str = " ";
                else if (i < 0 && j >= 0) str = String.valueOf(j + 1);
                else if (i >= 0 && j < 0) str = String.valueOf((char) ('A' + i));
                else {
                    if (getOpponent().getCell(i, j).getStatus() == Status.H) str = "X";
                    else if (getOpponent().getCell(i, j).getStatus() == Status.M) str = "M";
                    else str = "~";
                }
                System.out.print(str + " ");
            }
            System.out.println();
        }
        System.out.println("----------------------");
        printField(false);
    }

    public Messages shoot(int[] shot) {
        Cell cell = getOpponent().getCell(shot[0], shot[1]);
        Messages message = null;
        if (cell.getStatus() == Status.E) {
            cell.setStatus(Status.M);
            message = Messages.M;
        } else if (cell.getStatus() == Status.F) {
            cell.setStatus(Status.H);
            if (cell.getShip().isSunk()) {
                if (getOpponent().isLoser()) message = Messages.W;
                else message = Messages.S;
            } else message = Messages.H;
        }
        return message;
    }

    public void printField(boolean withSpace) {
        if (withSpace) System.out.println();
        for (int i = -1; i < size; i++) {
            for (int j = -1; j < size; j++) {
                String str;
                if (i < 0 && j < 0) str = " ";
                else if (i < 0 && j >= 0) str = String.valueOf(j + 1);
                else if (i >= 0 && j < 0) str = String.valueOf((char) ('A' + i));
                else {
                    if (getCell(i, j).getStatus() == Status.H) str = "X";
                    else if (getCell(i, j).getStatus() == Status.M) str = "M";
                    else if (getCell(i, j).getStatus() == Status.F) str = "O";
                    else str = "~";
                }
                System.out.print(str + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public boolean takeCoordinates(int[] coors, Ship s) {
        try {
            String input = scanner.next();
            coors[0] = input.charAt(0) - 'A';
            coors[1] = Integer.parseInt(input.substring(1)) - 1;
            input = scanner.next();
            coors[2] = input.charAt(0) - 'A';
            coors[3] = Integer.parseInt(input.substring(1)) - 1;
        } catch (IllegalArgumentException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return isWrongCoors(coors, s);
    }

    public boolean isWrongCoors(int[] coors, Ship s) {
        boolean wrongCoors = false;
        int width = Math.abs(coors[1] - coors[3]) + 1;
        int height = Math.abs(coors[0] - coors[2]) + 1;
        if (width == 1 || height == 1) {
            if (width * height != s.getLen()) {
                wrongCoors = true;
                System.out.printf("Error! Wrong length of the %s! Try again:\n", s.getName());
            } else if (isTooClose(coors)) {
                wrongCoors = true;
                System.out.println("Error! You placed it too close to another one. Try again:\n");
            }
        } else {
            wrongCoors = true;
            System.out.println("Error! Wrong ship location! Try again:\n");
        }
        return wrongCoors;
    }

    public boolean isTooClose(int[] coors) {
        boolean tooClose = false;
        for (int i = Math.min(coors[0], coors[2]) - 1; i >= 0 && i < size && i < Math.max(coors[0], coors[2]) + 2; i++) {
            for (int j = Math.min(coors[1], coors[3]) - 1; j >= 0 && j < size && j < Math.max(coors[1], coors[3]) + 2; j++) {
                tooClose = field[i][j].getStatus().getVal() == 1 || tooClose;
            }
        }
        return tooClose;
    }
}
