// Cleyde Varela, 21684
package pt.ipbeja.estig.po2.boulderdash.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipbeja.estig.po2.boulderdash.gui.BoulderDashBoard;
import pt.ipbeja.estig.po2.boulderdash.gui.BoulderDashImage;
import pt.ipbeja.estig.po2.boulderdash.gui.View;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BoulderDashModelTest {

    @BeforeEach
    void setUp() {
        View testView = new View() {
            @Override
            public void updateBoard(Position startPos, Position endPos) {
            }

            @Override
            public void restartGame() {
            }

            @Override
            public void levelCreated() {
            }

            @Override
            public void updateTimer(int timerValue) {
            }

            @Override
            public void updatePositionBehind(BoulderDashImage imageBehind) {
            }

            @Override
            public void gateCreated(BoulderDashImage image) {
            }

            @Override
            public void updateLevel(String level) {
            }

            @Override
            public void lastLevel(String totalScore, String s) {
            }

            @Override
            public void updateMovements(String movement) {
            }
        };
        BoulderDashModel model = new BoulderDashModel(testView);
    }

    /**
     * Shows content of txt file
     */
    @Test
    void txtTo2D() {
        System.out.println("txt to array 2D");
        String[][] txtFileIn2D = BoulderDashModel.readFileToArray2D("Level_1.txt", " ");
        //String[][] txtFileIn2D = BoulderDashModel.readFileToArray2D("Level_2.txt", " ");
        //String[][] txtFileIn2D = BoulderDashModel.readFileToArray2D("Level_3.txt", " ");
        String txtFileInString = Arrays.deepToString(txtFileIn2D);
        System.out.println(txtFileInString);

        System.out.println();

        System.out.println("one line in array 2D");
        String a = txtFileIn2D[1][0];
        System.out.println(a);
    }

    /**
     * Tests rockford movement UP
     */
    @Test
    void rockfordMoveUP() {
        System.out.println("-------------------------MoveUP------------------------------------");

        BoulderDashModel.readFileToArray2D("Test_1.txt", " ");

        Rockford.getInstance().setPosition(new Position(2, 6));
        Position initialPos = Rockford.getInstance().getPosition();
        System.out.println("initialPos of Rockford set for test: " + initialPos);

        BoulderDashModel.keyPressed(Direction.UP);
        Position finalPos = Rockford.getInstance().getPosition();
        System.out.println("Rockford's position after move UP: " + finalPos);

        Position expectedPos = new Position((initialPos.getLine() - 1), initialPos.getCol());
        System.out.println("Expected position: " + expectedPos);

        assertEquals(expectedPos, finalPos);
    }

    /**
     * Tests rockford movement DOWN
     */
    @Test
    void rockfordMoveDOWN() {
        System.out.println("---------------------------MoveDOWN-------------------------------------");

        BoulderDashModel.readFileToArray2D("Test_1.txt", " ");

        Rockford.getInstance().setPosition(new Position(2, 6));
        Position initialPos = Rockford.getInstance().getPosition();
        System.out.println("initialPos of Rockford set for test: " + initialPos);

        BoulderDashModel.keyPressed(Direction.DOWN);
        Position finalPos = Rockford.getInstance().getPosition();
        System.out.println("Rockford's position after move DOWN: " + finalPos);

        Position expectedPos = new Position((initialPos.getLine() + 1), initialPos.getCol());
        System.out.println("Expected position: " + expectedPos);

        assertEquals(expectedPos, finalPos);
    }

    /**
     * Tests rockford movement LEFT
     */
    @Test
    void rockfordMoveLEFT() {
        System.out.println("---------------------MoveLEFT----------------------------------");

        BoulderDashModel.readFileToArray2D("Test_1.txt", " ");

        Rockford.getInstance().setPosition(new Position(2, 6));
        Position initialPos = Rockford.getInstance().getPosition();
        System.out.println("initialPos of Rockford set for test: " + initialPos);

        BoulderDashModel.keyPressed(Direction.LEFT);
        Position finalPos = Rockford.getInstance().getPosition();
        System.out.println("Rockford's position after move LEFT: " + finalPos);

        Position expectedPos = new Position((initialPos.getLine()), initialPos.getCol() - 1);
        System.out.println("Expected position: " + expectedPos);

        assertEquals(expectedPos, finalPos);
    }

    /**
     * Tests rockford movement RIGHT
     */
    @Test
    void rockfordMoveRIGHT() {
        System.out.println("---------------------------MoveRIGHT---------------------------------");

        BoulderDashModel.readFileToArray2D("Test_1.txt", " ");

        Rockford.getInstance().setPosition(new Position(2, 6));
        Position initialPos = Rockford.getInstance().getPosition();
        System.out.println("initialPos of Rockford set for test: " + initialPos);

        BoulderDashModel.keyPressed(Direction.RIGHT);
        Position finalPos = Rockford.getInstance().getPosition();
        System.out.println("Rockford's position after move RIGHT: " + finalPos);

        Position expectedPos = new Position((initialPos.getLine()), initialPos.getCol() + 1);
        System.out.println("Expected position: " + expectedPos);

        assertEquals(expectedPos, finalPos);

    }

    /* NOTE: In the following tests Rockford's initial position is set to a place close to the destination
    in order to avoid a longer code simulating all moves;
    The Gate position is stated in the Test_1.txt file, which means it will be created at the
    beginning and not after catching all diamonds (testeF). */

    /**
     * Tests rockford's movement to a FreeTunnel
    * Req.E2
    */
    @Test
    void testeA() {
        System.out.println("-------------------Movimento para posicao livre----------------------------");

        BoulderDashModel.readFileToArray2D("Test_1.txt", " ");

        Rockford.getInstance().setPosition(new Position(2, 5));
        Position initialPos = Rockford.getInstance().getPosition();
        System.out.println("initialPos of Rockford set for test: " + initialPos);

        BoulderDashModel.keyPressed(Direction.LEFT);
        Position finalPos = Rockford.getInstance().getPosition();
        System.out.println("Rockford's position after move: " + finalPos);

        Position expectedPos = new Position(2 , 4); // free tunnel's position
        System.out.println("AbstractPosition in: " + expectedPos + " -> " + BoulderDashModel.getAbstractPositions()[2][4].getAbsName());

        assertEquals(expectedPos, finalPos);
    }

    /**
     * Tests rockford's movement to a wall
     * Req.E2
     */
    @Test
    void testeB() {
        System.out.println("----------------Movimento para posicao ocupada por uma parede--------------------");

       BoulderDashModel.readFileToArray2D("Test_1.txt", " ");

        Rockford.getInstance().setPosition(new Position(2, 9));
        Position initialPos = Rockford.getInstance().getPosition();
        System.out.println("initialPos of Rockford set for test: " + initialPos);

        BoulderDashModel.keyPressed(Direction.RIGHT);
        Position finalPos = Rockford.getInstance().getPosition();
        System.out.println("Rockford's position after move: " + finalPos);

        Position unexpectedPos = new Position(2 , 10); // wall's position
        System.out.println("AbstractPosition in: " + unexpectedPos + "-> " + BoulderDashModel.getAbstractPositions()[2][10].getAbsName());

        assertNotEquals(unexpectedPos, finalPos);
    }

    /**
     * Tests rockford's movement to a rock
     * Req.E2
     */
    @Test
    void testeC() {
        System.out.println("---------------Movimento para posicao ocupada por um pedregulho------------------");

        BoulderDashModel.readFileToArray2D("Test_1.txt", " ");

        Rockford.getInstance().setPosition(new Position(3, 6));
        Position initialPos = Rockford.getInstance().getPosition();
        System.out.println("initialPos of Rockford set for test: " + initialPos);

        BoulderDashModel.keyPressed(Direction.RIGHT);
        Position finalPos = Rockford.getInstance().getPosition();
        System.out.println("Rockford's position after move: " + finalPos);

        Position unexpectedPos = new Position(3 , 7); //rock's position
        System.out.println("Unexpected position: " + unexpectedPos);

        assertNotEquals(unexpectedPos, finalPos);

    }

    /**
     * Tests rockford's movement to a diamond
     * Req.E2
     */
    @Test
    void testeD() {
        System.out.println("----------------Movimento para posicao ocupada por um diamante----------------");

        BoulderDashModel.readFileToArray2D("Test_1.txt", " ");

        System.out.println("initial score: " + BoulderDashBoard.scoreCounter);

        String diamondsBefore = BoulderDashModel.getDiamondsOnMap().toString();
        System.out.println("Diamonds on grid before : \n" + diamondsBefore);

        Rockford.getInstance().setPosition(new Position(1, 5));
        Position initialPos = Rockford.getInstance().getPosition();
        System.out.println("initialPos of Rockford set for test: " + initialPos);

        Position diamondPosition = new Position(1, 6); // diamond's position

        BoulderDashModel.keyPressed(Direction.RIGHT);
        Position finalPos = Rockford.getInstance().getPosition();
        System.out.println("Rockford's position after move: " + finalPos);
        BoulderDashBoard.whichMoveableObjIsRockIn(finalPos.getLine(), finalPos.getCol(), initialPos);

        String diamondsAfter = BoulderDashModel.getDiamondsOnMap().toString(); //the removed diamond will have its boolean value false
        System.out.println("Diamonds on grid after : \n" + diamondsAfter);

        assertEquals(diamondPosition, finalPos);
        assertEquals(95, BoulderDashBoard.scoreCounter);
        assertNotEquals(diamondsAfter, diamondsBefore);
        System.out.println("final score: " + BoulderDashBoard.scoreCounter);
    }

    /* The game has no moveable enemies nor diamonds reason why the following test is in comments */
    @Test
    void testeE() {
        //System.out.println("--------------Movimento para posicao que faz um diamante cair----------------");

        //BoulderDashModel.readFileToArray2D("Test_1.txt", " ");

        //Rockford.getInstance().setPosition(new Position(1, 5)); // position near diamond
        //Position initialPos = Rockford.getInstance().getPosition();
        //System.out.println("initialPos of Rockford set for test: " + initialPos);

        // check diamond's initial position
        // move rockford
        //check diamond's last position that has to be the last FreeTunnel position in column
    }

    /**
     * Tests rockford's movement to the gate
     * Req.E2
     */
    @Test
    void testeF() {
        System.out.println("-------------------------Movimento para o portao-------------------");

        BoulderDashModel.readFileToArray2D("Test_1.txt", " ");

        System.out.println("initial win state: " + BoulderDashBoard.win);

        Rockford.getInstance().setPosition(new Position(2, 9));
        Position initialPos = Rockford.getInstance().getPosition();
        System.out.println("initialPos of Rockford set for test: " + initialPos);

        Position expectedPos = new Position(3 , 9); // gate's position
        System.out.println("AbstractPosition in: " + expectedPos + " -> " + BoulderDashModel.getAbstractPositions()[3][9].getAbsName());

        BoulderDashModel.keyPressed(Direction.DOWN);
        Position finalPos = Rockford.getInstance().getPosition();
        System.out.println("Rockford's position after move: " + finalPos);
        BoulderDashModel.whichAbsPosIsRockIn(finalPos.getLine(), finalPos.getCol(), initialPos);

        assertEquals(expectedPos, finalPos);
        assertTrue(BoulderDashBoard.win); // win is the boolean set to true after when rockford reaches the gate

        System.out.println("final win state: " + BoulderDashBoard.win);
    }
}