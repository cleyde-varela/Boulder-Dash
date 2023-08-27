package pt.ipbeja.estig.po2.boulderdash.model;

public class Rockford extends MoveableObject{
    private static Rockford rockford;
    private String name;
    private Position position;

    /* constructor matching super */
    private Rockford(String name, Position position) {
        super(name, position);
        this.name = name;
        this.position = position;
    }

    /**
     * Guarantes Rockford's unique instance (Singleton)
     * Req E1
     */
    public static synchronized Rockford getInstance(String name, Position position) {
        if (rockford == null) {
            rockford = new Rockford(name, position);
        }
        return getInstance();
    }

    /* Deletes rockford instance */
    public void deleteRockford() {
        rockford = null;
    }

    /* getters & setters */
    public static synchronized Rockford getInstance() {
        return rockford;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Rockford's movement
     * @param direction movement direction
     * @return position position after movement
     */
    public Position move(Direction direction) {
        switch (direction) {
            case UP:
                position = new Position(position.getLine() - 1, position.getCol());
                break;
            case DOWN:
                position = new Position(position.getLine() + 1, position.getCol());
                break;
            case LEFT:
                position = new Position(position.getLine(), position.getCol() - 1);
                break;
            case RIGHT:
                position = new Position(position.getLine(), position.getCol() + 1);
                break;
        }
        return position;
    }

    /**
     * Conditions rockford's movement
     * @param newPosition target position
     * @param startPos starting position
     * @return  position
     */
    public Position moveTo(Position newPosition, Position startPos) {
        if (!newPosition.isInside()) {
            System.out.println("Rockford cannot move to that position!");
            return startPos;
        }
        else if (checkWall(newPosition, startPos) || checkRock(newPosition, startPos)) {
            return startPos;
        }
        return newPosition;
    }

    /**
     * Checks wall
     * @param newPosition target position
     * @param startPos starting position
     * @return true if is a wall, false otherwise
     */
    public boolean checkWall(Position newPosition, Position startPos) {
        for (int i = 0; i < BoulderDashModel.getAbstractPositions().length; i++) {
            for (int j = 0; j < BoulderDashModel.getAbstractPositions()[i].length; j++) {
                if (BoulderDashModel.getAbstractPositions()[i][j].getAbsName().equals("Wall") &&
                        BoulderDashModel.getAbstractPositions()[i][j].getAbsPos().getLine() == newPosition.getLine()
                        && BoulderDashModel.getAbstractPositions()[i][j].getAbsPos().getCol() == newPosition.getCol()) {
                    BoulderDashModel.getAbstractPositions()[i][j].checkRockford(newPosition.getLine(), newPosition.getCol(), startPos);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks rock
     * @param newPosition target position
     * @param startPos starting position
     * @return true if is a rock and false otherwise
     */
    public boolean checkRock(Position newPosition, Position startPos) {
        for (MoveableObject moveable : BoulderDashModel.getMovablesList()) {
            if (moveable.getName().equals("Rock") && moveable.getPosition().getLine() == newPosition.getLine() &&
                    moveable.getPosition().getCol() == newPosition.getCol()) {
                moveable.checkRockford(newPosition.getLine(), newPosition.getCol(), startPos);
                return true;
            }
        }
        return false;
    }

    /* not used here */
    @Override
    public void checkRockford(int endLine, int endCol, Position startPos) {
    }

    @Override
    public String toString() {
        return "Rockford{}";
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
