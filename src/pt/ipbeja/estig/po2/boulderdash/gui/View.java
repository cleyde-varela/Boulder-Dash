package pt.ipbeja.estig.po2.boulderdash.gui;

import pt.ipbeja.estig.po2.boulderdash.model.Position;

public interface View {

    /**
     * Updates position of moveable objects
     * @param startPos starting position
     * @param endPos final position
     * Req.E5
     */
    void updateBoard(Position startPos, Position endPos);

    /* notifies restart */
    void restartGame();

    /* notifies level created */
    void levelCreated();

    /**
     * Updates timer value
     * @param timerValue value to update
     * Req.NE6
     */
    void updateTimer(int timerValue);

    /** Updates the image of rockford's last position
     * @param imageBehind image to update
     */
    void updatePositionBehind(BoulderDashImage imageBehind);

    /** Notifies gate created
     * @param image gate image
     */
    void gateCreated(BoulderDashImage image);

    /** Notifies gui to start next level
     * @param level next level name
     * Req.NE5
     */
    void updateLevel(String level);

    /* Notifies last level*/
    void lastLevel(String totalScore, String s);

    /* Updates movements in gui */
    void updateMovements(String movement);
}
