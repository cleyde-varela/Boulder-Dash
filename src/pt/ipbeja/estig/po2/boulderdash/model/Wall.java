package pt.ipbeja.estig.po2.boulderdash.model;

import pt.ipbeja.estig.po2.boulderdash.gui.BoulderDashBoard;

public class Wall extends AbstractPosition {
    private Position position;

    /* constructor matching super */
    public Wall(String absName, Position absPos) {
        super(absName, absPos);
        this.position = absPos;
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
