package pt.ipbeja.estig.po2.boulderdash.model;

public class Rock extends MoveableObject {

    /* constructor matching super */
    public Rock(String name, Position position) {
        super(name, position);
    }

    /* not used here */
    @Override
    public Position move(Direction direction) {
        return null;
    }

    /* not used here */
    @Override
    public Position moveTo(Position nearPosition, Position startPos) {
        return null;
    }

    /**
     * Limits rockford's movement
     * @param endLine line from target position
     * @param endCol column from target position
     * @param startPos starting position
     */
    @Override
    public void checkRockford(int endLine, int endCol, Position startPos) {
        Rockford.getInstance().setPosition(new Position(startPos.getLine(), startPos.getCol()));
    }
}
