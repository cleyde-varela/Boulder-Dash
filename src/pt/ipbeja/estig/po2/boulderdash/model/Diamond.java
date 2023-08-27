package pt.ipbeja.estig.po2.boulderdash.model;

import pt.ipbeja.estig.po2.boulderdash.gui.BoulderDashBoard;

public class Diamond extends MoveableObject{
    private String name;
    private Position position;

    /* constructor matching super */
    public Diamond(String name, Position position) {
        super(name, position);
        this.name = name;
        this.position = position;
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
     * Conditions rockford's movement
     * @param endLine line from target position
     * @param endCol column from target position
     * @param startPos starting position
     */
    @Override
    public void checkRockford(int endLine, int endCol, Position startPos) {
        BoulderDashModel.setDiamondsCounter(BoulderDashModel.getDiamondsCounter() + 1);
        BoulderDashBoard.scoreCounter += BoulderDashModel.calculateScore();
    }
}
