package pt.ipbeja.estig.po2.boulderdash.model;

import pt.ipbeja.estig.po2.boulderdash.gui.BoulderDashBoard;

public class Bonus extends MoveableObject{

    /*constructor matching super */
    public Bonus(String name, Position position) {
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
     * Conditions rockford's movement
     * @param endLine line from target position
     * @param endCol column from target position
     * @param startPos starting position
     */
    @Override
    public void checkRockford(int endLine, int endCol, Position startPos) {
        //BoulderDashBoard.scoreCounter += 500;
    }
}
