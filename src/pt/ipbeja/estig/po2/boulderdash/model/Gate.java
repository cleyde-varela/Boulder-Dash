package pt.ipbeja.estig.po2.boulderdash.model;

import javafx.scene.control.Alert;
import pt.ipbeja.estig.po2.boulderdash.gui.BoulderDashBoard;

public class Gate extends AbstractPosition {

    /* constructor matching super */
    public Gate(String absName, Position absPos) {
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
       if (BoulderDashBoard.isPaneCreated && BoulderDashModel.getLevelCounter() < 2) { //to avoid errors in test class
           Alert winMessage = new Alert(Alert.AlertType.INFORMATION);
           winMessage.setTitle(null);
           winMessage.setHeaderText(null);
           winMessage.setContentText("Win! \n Click [OK] to play the next level");
           winMessage.show();
       }
        BoulderDashBoard.win = true;
       BoulderDashModel.setLevelCounter(BoulderDashModel.getLevelCounter() + 1);
    }
}
