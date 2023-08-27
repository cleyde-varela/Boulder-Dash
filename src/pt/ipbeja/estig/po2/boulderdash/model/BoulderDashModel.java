package pt.ipbeja.estig.po2.boulderdash.model;

import javafx.scene.control.Alert;
import pt.ipbeja.estig.po2.boulderdash.gui.BoulderDashBoard;
import pt.ipbeja.estig.po2.boulderdash.gui.BoulderDashImage;
import pt.ipbeja.estig.po2.boulderdash.gui.View;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class BoulderDashModel {
    private Timer timer;
    private static View view;
    private static int score1;
    private static int score2;
    private static int score3;
    private static int nLines;
    private static int txtSize;
    int[] ranking = new int[5];
    private static int nColumns;
    private static int nDiamonds;
    private static int timerValue;
    private static int totalScore;
    private static int nMovements;
    private static int levelCounter;
    private static String[][] level; //array 2D from txt
    private static List<String> txt; //holds txt file content
    private AbstractPosition winGate;
    private static int diamondsCounter;
    private static Position rockStartPos;
    private static final int ASCII = 65;
    private static List<MoveableObject> movablesList; //holds scheme for movables
    private static AbstractPosition[][] abstractPositions; //holds scheme of board
    private static Map<MoveableObject, Boolean> diamondsOnMap;

    /* constructor */
    public BoulderDashModel(View view) {
        BoulderDashModel.view = view;
        levelCounter = 0;
        this.timer = new Timer();
    }

    /* getters & setters */
    public static int getLines() {
        return nLines;
    }

    public static int getColumns() {
        return nColumns;
    }

    public String getLevel_1() {
        return "Level_1.txt";
    }

    public static int getLevelCounter() {
        return levelCounter;
    }

    public static void setLevelCounter(int levelCounter) {
        BoulderDashModel.levelCounter = levelCounter;
    }

    public static int getDiamondsCounter() {
        return diamondsCounter;
    }

    public static void setDiamondsCounter(int diamondsCounter) {
        BoulderDashModel.diamondsCounter = diamondsCounter;
    }

    public static List<MoveableObject> getMovablesList() {
        return movablesList;
    }

    public static AbstractPosition[][] getAbstractPositions() {
        return abstractPositions;
    }

    public static Map<MoveableObject, Boolean> getDiamondsOnMap() {
        return diamondsOnMap;
    }

    /* score calculator */
    public static int calculateScore() {
        return (diamondsCounter * 100) - (5 * nMovements);
    }

    /**
     * Reads .txt file
     * @param filePath txt file name
     * @param separator string separator
     * Req.E4
     * Based on Base code IP 2020-2021
     */
    public static String[][] readFileToArray2D(String filePath, String separator) {
        Path path = Paths.get(filePath);
        try {
            txt = Files.readAllLines(path);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "There's a problem with the selected file");
            alert.showAndWait();
            e.printStackTrace();
        }
        createLevel(txt, separator);
        return level;
    }

    /**
     * Creates level from txt's content
     * @param txt list with .txt file content
     * @param separator string separator
     */
    public static void createLevel(List<String> txt, String separator) {
        //copy content from txt to level[][]
        level = new String[txt.size()][];
        for (int i = 0; i < txt.size(); i++) {
            level[i] = txt.get(i).split(separator);
        }
        nLines = Integer.parseInt(level[0][0]);
        nColumns = Integer.parseInt(level[0][1]);
        txtSize = txt.size();

        fillInAbsPos(level);

        fillInMoveablesList(level);

        fillDiamondsOnMap();

        if (BoulderDashBoard.isGui) {
            view.levelCreated();
        }
    }

    /**
     * Fill in AbstractPosition[][]
     * @param array 2D array
     * Req.E1
     */
    public static void fillInAbsPos(String[][] array) {
        abstractPositions = new AbstractPosition[nLines][nColumns];
        //lines
        for (int i = 1; i <= nLines; i++) {
            charToAbsPos(array[i][0], i, nColumns);
        }
    }

    /**
     * Auxiliar to fillInAbsPos()
     * string -> absPos
     * @param s line of array in fillInAbsPos
     * @param ln line number
     * @param col column number
     * Req.E1
     */
    //columns
    private static void charToAbsPos(String s, int ln, int col) {
        for (int x = 0; x < col; x++) {
            Position pos = new Position(ln - 1, x); // ln-1 otherwise there's a white line on top
            AbstractPosition abstractPosition = setAbstractPosition(s.charAt(x), pos);
            abstractPositions[ln - 1][x] = abstractPosition;
        }
    }

    /**
     * Allocates AbstractPositions
     * @param charAt char that indicates name
     * @param pos position to place
     * Req.E1
     * @return AbstractPosition
     */
    private static AbstractPosition setAbstractPosition(char charAt, Position pos) {
        AbstractPosition square = null;

        if (charAt == 'W') {
            square = new Wall("Wall", pos);
            return square;
        } else if (charAt == 'O') {
            square = new OccupiedTunnel("OccupiedTunnel", pos);
            return square;
        } else if (charAt == 'L') {
            square = new FreeTunnel("FreeTunnel", pos);
            return square;
        } else if (charAt == 'G') {
            square = new Gate("Gate", pos);
            return square;
        }
        return square;
    }

    /**
     * Allocates images in grid
     * @param s char that indicates name
     * @param position position to place
     * Req.E4
     * @return BoulderDasImage in position
     */
    public static BoulderDashImage setAbsPosImage(char s, Position position) {
        BoulderDashImage image = null;

        if (s == 'W') {
            image = new BoulderDashImage("Wall", position);
            return image;
        } else if (s == 'O') {
            image = new BoulderDashImage("OccupiedTunnel", position);
            return image;
        } else if (s == 'L') {
            image = new BoulderDashImage("FreeTunnel", position);
            return image;
        } else if (s == 'G') {
            image = new BoulderDashImage("Gate", position);
            return image;
        }
        return image;
    }

    /**
     * Fills in moveables list
     * @param level 2D array with .txt file content
     * Req.E3
     */
    private static void fillInMoveablesList(String[][] level) {
        movablesList = new ArrayList<>();
        String stop;

        for (int lines = (nLines + 1); lines <= txtSize - 1; lines++) {
            if (level[lines][0].equals("J")) {
                Position pos = new Position(Integer.parseInt(level[lines][1]), Integer.parseInt(level[lines][2]));
                MoveableObject moveable = setMoveableObject(level[lines][0], pos);
                movablesList.add(moveable);
            } else if (level[lines][0].equals("D") || level[lines][0].equals("E") || level[lines][0].equals("I")
                    || level[lines][0].equals("B") || level[lines][0].equals("P")) {
                for (int x = 1; x < level[lines].length; x += 2) {
                    stop = level[lines][x];
                    if (stop.equals("%")) {
                        break;
                    } else {
                        Position pos = new Position(Integer.parseInt(level[lines][x]), Integer.parseInt(level[lines][x + 1]));
                        MoveableObject moveable = setMoveableObject(level[lines][0], pos);
                        movablesList.add(moveable);
                    }
                }
            }
        }
    }

    /**
     * Allocates Moveable objects
     * @param name object's name
     * @param pos position to place
     * Req.E1
     * @return MoveableObject
     */
    public static MoveableObject setMoveableObject(String name, Position pos) {
        switch (name) {
            case "J":
                rockStartPos = pos;
                return Rockford.getInstance("Rockford", pos); //creates rockford
            case "D":
                return new Diamond("Diamond", pos);
            case "E":
                return new EnemySpider("Enemy", pos);
            case "I":
                return new EnemySpider("Enemy", pos);
            case "B":
                return new Bonus("Bonus", pos);
            case "P":
                return new Rock("Rock", pos);
        }
        return null;
    }

    /* Fills map of diamonds */
    private static void fillDiamondsOnMap() {
        diamondsOnMap = new HashMap<>();

        for (MoveableObject moveableObject : movablesList) {
            if (moveableObject.getName().equals("Diamond")) {
                diamondsOnMap.put(moveableObject, true);
                nDiamonds += 1;
            }
        }
    }

    /**
     * Allocates MoveableObjetcts' images in grid
     * @param name object's image name
     * @param pos position to place
     * Req.E4
     * @return BoulderDashImage in position
     */
    public static BoulderDashImage setMoveableObjectImage(String name, Position pos) {
        BoulderDashImage moveable = null;

        switch (name) {
            case "Rockford":
                moveable = new BoulderDashImage("Rockford", pos);
                return moveable;
            case "Diamond":
                moveable = new BoulderDashImage("Diamond", pos);
                return moveable;
            case "Enemy":
                moveable = new BoulderDashImage("Enemy", pos);
                return moveable;
            case "Bonus":
                moveable = new BoulderDashImage("Bonus", pos);
                return moveable;
            case "Rock":
                moveable = new BoulderDashImage("Rock", pos);
                return moveable;
        }
        return moveable;
    }

    /**
     * Rockford movement in GUI (keys)
     * @param direction rockford"s movement direction
     */
    public static void keyPressed(Direction direction) {
        Rockford rockford = Rockford.getInstance();
        Position startPos = rockford.getPosition();
        Position endPos = rockford.move(direction);

        Position def = rockford.moveTo(endPos, startPos);
        rockford.setPosition(def);

        if (startPos != def) {
            nMovements+=1;
            view.updateBoard(startPos, def);
            view.updateMovements("rockford " + numberToChar(startPos.getLine()) + " " + startPos.getCol() + " -> "
            + numberToChar(def.getLine()) + " " + def.getCol());
        }
    }

    /**
     * Converts int to char using the ascii table
     * https://asciichart.com/
     */
    private static char numberToChar(int line) {
        return (char) (line + ASCII);
    }

    /**
     * Sets rockford position before rockford's move as FreeTunnel
     * @param startPos rockford's starting position
     */
    public static void positionBehind(Position startPos) {
        for (int i = 0; i < abstractPositions.length; i++) {
            for (int j = 0; j < abstractPositions[i].length; j++) {
                if (abstractPositions[i][j].getAbsName().equals("OccupiedTunnel") &&
                        abstractPositions[i][j].getAbsPos().getLine() == startPos.getLine()
                        && abstractPositions[i][j].getAbsPos().getCol() == startPos.getCol()) {
                    abstractPositions[i][j] = new FreeTunnel("FreeTunnel", abstractPositions[i][j].getAbsPos());
                    BoulderDashImage imageBehind = new BoulderDashImage("FreeTunnel", abstractPositions[i][j].getAbsPos());
                    view.updatePositionBehind(imageBehind);
                }
            }
        }
    }

    /**
     * Checks on which AbstractPosition is rockford
     * @param endLine final position's line
     * @param endCol final position's column
     * @param startPos starting position
     */
    public static void whichAbsPosIsRockIn(int endLine, int endCol, Position startPos) {
        for (AbstractPosition[] abstractPositions : abstractPositions) {
            for (AbstractPosition abstractPosition : abstractPositions) {
                if (abstractPosition.getAbsName().equals("OccupiedTunnel") &&
                        abstractPosition.getAbsPos().getLine() == endLine
                        && abstractPosition.getAbsPos().getCol() == endCol) {
                    abstractPosition.checkRockford(endLine, endCol, startPos);
                } else if (abstractPosition.getAbsName().equals("FreeTunnel") &&
                        abstractPosition.getAbsPos().getLine() == endLine
                        && abstractPosition.getAbsPos().getCol() == endCol) {
                    abstractPosition.checkRockford(endLine, endCol, startPos);
                } else if (abstractPosition.getAbsName().equals("Gate") &&
                        abstractPosition.getAbsPos().getLine() == endLine
                        && abstractPosition.getAbsPos().getCol() == endCol) {
                    abstractPosition.checkRockford(endLine, endCol, startPos);
                }
            }
        }
    }

    /* Checks for game win */
    public boolean checkWin() {
        return diamondsCounter == nDiamonds;
    }

    /* Creates gate after boolean win checked */
    public void createGate() {
        winGate = new Gate("Gate", rockStartPos);
        BoulderDashImage image = setAbsPosImage('G', winGate.getAbsPos());
        System.out.println("Gate created");

        view.gateCreated(image);
    }

    /**
     * Checks if rockford reached gate
     * @param endLine final position's line
     * @param endCol final position's column
     * @param startPos starting position
     */
    public boolean checkGate(int endLine, int endCol, Position startPos) {
        if (winGate.getAbsPos().getLine() == endLine &&
                winGate.getAbsPos().getCol() == endCol) {
            winGate.checkRockford(endLine, endCol, startPos);
            stopTimer();
            return true;
        }
        return false;
    }

    /* Checks if next level can be started */
    public void checkNextLevel() {
        if (levelCounter == 1) {
            score1 = BoulderDashBoard.scoreCounter;
            System.out.println("L1 " + score1);
            view.updateLevel("Level_2.txt");
        } else if (levelCounter == 2) {
            score2 = BoulderDashBoard.scoreCounter;
            System.out.println("L2 " + score2);
            view.updateLevel("Level_3.txt");
        } else if (levelCounter == 3) {
            score3 = BoulderDashBoard.scoreCounter;
            System.out.println("L3 " + score3);
            totalScore = score1 + score2 + score3;
            System.out.println("total " + totalScore);
            System.out.println("Game finished");
            view.lastLevel(checkRanking(totalScore), createRankingString());
        }
    }

    /* Resets game information */
    public void restart() {
        txt = null;
        nLines = 0;
        txtSize = 0;
        level = null;
        nColumns = 0;
        nDiamonds = 0;
        nMovements = 0;
        diamondsCounter = 0;
        rockStartPos = null;
        movablesList.clear();
        diamondsOnMap.clear();

        Rockford.getInstance().deleteRockford();

        for (int i = 0; i < nLines; i++) {
            for (int j = 0; j < nColumns; j++) {
                abstractPositions[i][j] = null;
            }
        }

        switch (levelCounter) {
            case 0: score1 = 0;
            case 1: score2 = 0;
            case 2: score3 = 0;
        }

        view.restartGame();
    }

    /* Creates a new timer */
    public void resetTimer() {
        timerValue = -1;
        this.timer = new Timer();
    }

    /* Starts timer */
    public void startTimer() {
        this.resetTimer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                timerValue++;
                view.updateTimer(timerValue);
            }
        };
        this.timer.schedule(timerTask, 0, 1000);
    }

    /* Stops the current timer */
    public void stopTimer() {
        timer.cancel();
    }

    /* Get current timer value */
    public int getTimerValue() {
        return timerValue;
    }

    /* ranks scores */
    private String checkRanking(int finalScore) {
        String s = Integer.toString(finalScore);

        if (finalScore >= ranking[0]) {
            ranking[4] = ranking[3];
            ranking[3] = ranking[2];
            ranking[2] = ranking[1];
            ranking[1] = ranking[0];
            ranking[0] = finalScore;
            return s.concat("***");
        } else if (finalScore >= ranking[1]) {
            ranking[4] = ranking[3];
            ranking[3] = ranking[2];
            ranking[2] = ranking[1];
            ranking[1] = finalScore;
            return s.concat("***");
        } else if (finalScore >= ranking[2]) {
            ranking[4] = ranking[3];
            ranking[3] = ranking[2];
            ranking[2] = finalScore;
            return s.concat("***");
        } else if (finalScore >= ranking[3]) {
            ranking[4] = ranking[3];
            ranking[3] = finalScore;
            return s.concat("***");
        } else if (finalScore >= ranking[4]) {
            ranking[4] = finalScore;
            return s.concat("***");
        }
        return s;
    }

    /* array to String */
    private String createRankingString() {
        String rankingBox = "";
        for (int i = 0; i < ranking.length - 1; i++) {
            rankingBox += ranking[i] + "\n";
        }
        return rankingBox;
    }







    // toString and Hash
    @Override
    public String toString() {
        return "BoulderDashModel{" +
                "timer=" + timer +
                ", winGate=" + winGate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoulderDashModel that = (BoulderDashModel) o;
        return timer.equals(that.timer) && winGate.equals(that.winGate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timer, winGate);
    }
}

