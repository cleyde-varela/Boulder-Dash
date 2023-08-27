package pt.ipbeja.estig.po2.boulderdash.model;

import java.util.Objects;

public class Position {
    private int line;
    private int col;

    /* constructor */
    public Position(int line, int col) {
        this.line = line;
        this.col = col;
    }

    /* getters */
    public int getLine() {
        return this.line;
    }

    public int getCol() {
        return this.col;
    }

    /**
     * Used to verify if the position Rockford wants to move in is inside the board
     * @param direction direction of movement
     * @return position
     */
    public Position nearPosition(Direction direction) {
        switch (direction) {
            case UP:    return new Position(this.getLine() - 1, this.getCol());
            case DOWN:  return new Position(this.getLine() + 1, this.getCol());
            case LEFT:  return new Position(this.getLine(), this.getCol() - 1);
            case RIGHT: return new Position(this.getLine(), this.getCol() + 1);
            default: assert(false); return null;
        }
    }

    /**
     * Checks if position is inside the board
     * @return true if inside, false otherwise
     */
    public boolean isInside() {
        return Position.isInside(this.getLine(), this.getCol());
    }

    /**
     * Checks if line and column are inside the board
     * @param line position's line to check
     * @param col position's column to check
     * @return true if inside, false otherwise
     */
    public static boolean isInside(int line, int col)
    {
        return 0 <= line && line < BoulderDashModel.getLines() &&
                0 <= col && col < BoulderDashModel.getColumns();
    }

    @Override
    public String toString() {
        return "Position{" +
                "line=" + line +
                ", col=" + col +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return line == position.line && col == position.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, col);
    }
}
