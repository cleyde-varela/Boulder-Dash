package pt.ipbeja.estig.po2.boulderdash.model;

import pt.ipbeja.estig.po2.boulderdash.gui.BoulderDashBoard;

public class FreeTunnel extends AbstractPosition {

    /* constructor matching super */
    public FreeTunnel(String absName, Position absPos) {
        super(absName, absPos);
    }

    /**
     * Conditions rockford's movement
     * @param endLine line from target position
     * @param endCol column from target position
     * @param startPos starting position
     */
    @Override
    public void checkRockford(int endLine, int endCol, Position startPos) {
        BoulderDashBoard.getRockImage().updatePosition(endLine, endCol);
        BoulderDashBoard.getRockImage().toFront();
    }
}
