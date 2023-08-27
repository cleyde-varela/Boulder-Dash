package pt.ipbeja.estig.po2.boulderdash.model;

import javafx.scene.control.Alert;
import pt.ipbeja.estig.po2.boulderdash.gui.BoulderDashBoard;

public class EnemySpider extends MoveableObject {

    /* constructor matching super */
    public EnemySpider(String name, Position position) {
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
     *
     * @param endLine  line from target position
     * @param endCol   column from target position
     * @param startPos starting position
     */
    @Override
    public void checkRockford(int endLine, int endCol, Position startPos) {
        if (BoulderDashBoard.isPaneCreated) { //to avoid errors in test class
            Alert show = new Alert(Alert.AlertType.INFORMATION);
            show.setTitle(null);
            show.setHeaderText(null);
            show.setContentText("Game over!");
            show.show();
        }
        BoulderDashBoard.gameOver = true;
    }
}