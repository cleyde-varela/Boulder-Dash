// Cleyde Varela, 21684
package pt.ipbeja.estig.po2.boulderdash.gui;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.util.Duration;
import pt.ipbeja.estig.po2.boulderdash.model.*;

import java.util.*;
import java.util.List;

public class BoulderDashBoard extends Application implements View {

    private static Pane pane;
    private  static Pane grid;
    public static boolean win;
    private String playerName;
    private static VBox momBox;
    private Button closeButton;
    public static boolean isGui;
    private Label movementLabel;
    private Button restartButton;
    private static Label timeLabel;
    public static boolean gameOver;
    public static int scoreCounter;
    private static Button scoreButton;
    private static BorderPane movesBox;
    private static String currentLevel;
    public static boolean isPaneCreated;
    private static boolean isGateCreated;
    private static BoulderDashModel model;
    private static BoulderDashImage rockImage;
    private static final double TRANSLATION_TIME_IN_MILLIS = 100;
    private static Map<MoveableObject, BoulderDashImage> movingImages;
    private static final Map<KeyCode, Direction> directionMap = new HashMap<>();

    /* maps JavaFX key code to model direction */
    static {
        directionMap.put(KeyCode.UP, Direction.UP);
        directionMap.put(KeyCode.DOWN, Direction.DOWN);
        directionMap.put(KeyCode.LEFT, Direction.LEFT);
        directionMap.put(KeyCode.RIGHT, Direction.RIGHT);
    }

    /* getters */
    public static BoulderDashImage getRockImage() {
        return rockImage;
    }

    /* Starts game */
    @Override
    public void start(Stage primaryStage) {
        isGui = true;
        model = new BoulderDashModel(this); // creates model

        askName();

        showCommands();

        Scene scene = this.createScene();

        primaryStage.setTitle("BoulderDash Game");
        primaryStage.setScene(scene);

        primaryStage.setOnCloseRequest((e) -> System.exit(0));
        primaryStage.show();

        model.startTimer();
    }

    //TODO fix buttons and infinityloop
    private void askName() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(null);
        dialog.setHeaderText("Please enter your name");
        dialog.getDialogPane().setContentText("Name:");
        dialog.showAndWait();

        TextField name = dialog.getEditor();
        playerName = name.getText();

        if (playerName.length() != 0 && playerName.length() <= 8) {
            System.out.println(playerName + " is about to play");
        } else {
            askName();
        }
    }

    //TODO fix buttons
    /**
     * Shows a dialog box with commands on how to play
     * Req.E6
     * code based on https://stackoverflow.com/questions/29393459/set-image-on-left-side-of-dialog
     */
    private void showCommands() {
        Alert showCommands = new Alert(Alert.AlertType.INFORMATION);
        showCommands.setTitle("BoulderDash");
        showCommands.setHeaderText(null);

        ImageView imageView = new ImageView(new Image("resources/Commands.png"));

        showCommands.setGraphic(imageView);
        showCommands.initModality(Modality.APPLICATION_MODAL);
        showCommands.getDialogPane().setContentText("\n\n\n\n\n   Click [OK] down below to play the game");

        showCommands.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> System.out.println("Starting game.. ."));
    }

    /**
     * Creates GUI Scene
     * Req.E3
     * @return the created scene
     */
    private Scene createScene() {
        momBox = new VBox();
        momBox.setStyle("-fx-background-color: #FFD8AE; ");

        this.createCloseButton();
        this.createRestartButton();
        this.createTimelabel();
        this.createScoreButton();

        grid = this.createGrid(model.getLevel_1());

        movesBox = this.createMovesBox();

        momBox.getChildren().addAll(closeButton, restartButton, timeLabel, scoreButton, grid, movesBox);

        Scene scene = new Scene(momBox, 900, 600);

        this.setKeyHandle(scene);

        return scene;
    }

    /* Creates restart button */
    private void createRestartButton() {
        this.restartButton = new Button("Restart");
        this.restartButton.setStyle("-fx-background-color: #FFBC73; ");
        this.restartButton.setMaxWidth(70);

        this.restartButton.setOnAction(e -> {
            System.out.println("Button clicked -> Restarting game.. .\n");
            model.restart();
        });
    }

    /* Creates close button */
    private void createCloseButton () {
        this.closeButton = new Button("Close");
        this.closeButton.setStyle("-fx-background-color: #FFC8C7; ");
        this.closeButton.setOnAction(event -> System.exit(0));
        this.closeButton.setMaxWidth(70);
    }

    /* Creates score button */
    private void createScoreButton() {
        scoreButton = new javafx.scene.control.Button("Score: " + scoreCounter);
        scoreButton.setStyle("-fx-background-color: #CCFFCC; ");
        scoreButton.setMaxWidth(100);
    }

    /* Creates timer label */
    private void createTimelabel() {
        timeLabel = new Label("" + model.getTimerValue());
        timeLabel.setStyle("-fx-background-color: #FFDF73; ");
        timeLabel.setMaxWidth(100);
    }

    /* Creates a box to show rockford's movements */
    private BorderPane createMovesBox() {
        BorderPane moveBox = new BorderPane();
        moveBox.setMaxWidth(150);
        this.movementLabel = new Label();
        this.movementLabel.setMaxWidth(150);
        this.movementLabel.setStyle("-fx-background-color: #FFBC73; ");
        moveBox.setBottom(this.movementLabel);
        return moveBox;
    }

    /**
     * Creates grid
     * Req.E3
     * @return pane
     */
    private Pane createGrid(String level) {
        currentLevel = level;

        BoulderDashModel.readFileToArray2D(level, " ");

        createBoard(BoulderDashModel.getAbstractPositions());

        addMoveableObjects(BoulderDashModel.getMovablesList());

        return pane;
    }

    /**
     * Enables use of keyboard to play
     * Req E6
     */
    private void setKeyHandle(Scene scene) {
        scene.setOnKeyPressed((KeyEvent event) -> {
            Direction direction = directionMap.get(event.getCode());
            BoulderDashModel.keyPressed(direction);
        });
    }

    /**
     * Creates board from gridPositions[][]
     * @param gridPositions model abstractPositions array
     * Req.E3
     */
    private static void createBoard(AbstractPosition[][] gridPositions) {
        for (int i = 1; i <= BoulderDashModel.getLines(); i++) {

            for (int j = 0; j < BoulderDashModel.getColumns(); j++) {

                switch (gridPositions[i - 1][j].getAbsName()) {
                    case "Wall": {
                        BoulderDashImage image = BoulderDashModel.setAbsPosImage('W', gridPositions[i - 1][j].getAbsPos());
                        pane.getChildren().add(image);
                        break;
                    }
                    case "OccupiedTunnel": {
                        BoulderDashImage image = BoulderDashModel.setAbsPosImage('O', gridPositions[i - 1][j].getAbsPos());
                        pane.getChildren().add(image);
                        break;
                    }
                    case "FreeTunnel": {
                        BoulderDashImage image = BoulderDashModel.setAbsPosImage('L', gridPositions[i - 1][j].getAbsPos());
                        pane.getChildren().add(image);
                        break;
                    }
                    case "Gate": {
                        BoulderDashImage image = BoulderDashModel.setAbsPosImage('G', gridPositions[i - 1][j].getAbsPos());
                        pane.getChildren().add(image);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Adds moveable objects to grid and images to movingImages
     * @param moveables model moveablesList list
     * Req.E3
     */
    private static void addMoveableObjects(List<MoveableObject> moveables) {
        for (MoveableObject moveableObject : moveables) {

            switch (moveableObject.getName()) {
                case "Rockford":
                    setRockfordOnGUI(moveableObject);
                    break;
                case "Diamond":
                    setDiamondOnGUI(moveableObject);
                    break;
                case "Enemy":
                    setEnemyOnGUI(moveableObject);
                    break;
                case "Bonus":
                    setBonusOnGUI(moveableObject);
                    break;
                case "Rock":
                    setRockOnGUI(moveableObject);
                    break;
            }
        }
    }

    /* Allocates rockford image on pane */
    private static void setRockfordOnGUI(MoveableObject moveableObject){
        rockImage = BoulderDashModel.setMoveableObjectImage(moveableObject.getName(), moveableObject.getPosition());
        pane.getChildren().add(rockImage);
        movingImages.put(moveableObject, rockImage);
    }

    /* Allocates diamond image on pane */
    private static void setDiamondOnGUI(MoveableObject moveableObject){
        if (BoulderDashModel.getAbstractPositions()[moveableObject.getPosition().getLine()]
                [moveableObject.getPosition().getCol()].getAbsName().equals("Wall")) {
            BoulderDashModel.getAbstractPositions()[moveableObject.getPosition().getLine()]
                    [moveableObject.getPosition().getCol()] =
                    new OccupiedTunnel("OccupiedTunnel", BoulderDashModel.getAbstractPositions()[moveableObject.getPosition().getLine()]
                            [moveableObject.getPosition().getCol()].getAbsPos());
        }
        BoulderDashImage moveable = BoulderDashModel.setMoveableObjectImage(moveableObject.getName(), moveableObject.getPosition());
        pane.getChildren().add(moveable);
        movingImages.put(moveableObject, moveable);
    }

    /* Allocates enemy image on pane */
    private static void setEnemyOnGUI(MoveableObject moveableObject){
        BoulderDashImage moveable = BoulderDashModel.setMoveableObjectImage(moveableObject.getName(), moveableObject.getPosition());
        pane.getChildren().add(moveable);
        movingImages.put(moveableObject, moveable);
    }

    /* Allocates bonus image on pane */
    private static void setBonusOnGUI(MoveableObject moveableObject){
        if (BoulderDashModel.getAbstractPositions()[moveableObject.getPosition().getLine()]
                [moveableObject.getPosition().getCol()].getAbsName().equals("Wall")) {
            BoulderDashModel.getAbstractPositions()[moveableObject.getPosition().getLine()]
                    [moveableObject.getPosition().getCol()] =
                    new OccupiedTunnel("OccupiedTunnel", BoulderDashModel.getAbstractPositions()[moveableObject.getPosition().getLine()]
                            [moveableObject.getPosition().getCol()].getAbsPos());
        }
        BoulderDashImage moveable = BoulderDashModel.setMoveableObjectImage(moveableObject.getName(), moveableObject.getPosition());
        pane.getChildren().add(moveable);
        movingImages.put(moveableObject, moveable);
    }

    /* Allocates rock image on pane */
    private static void setRockOnGUI(MoveableObject moveableObject){
        BoulderDashImage moveable = BoulderDashModel.setMoveableObjectImage(moveableObject.getName(), moveableObject.getPosition());
        pane.getChildren().add(moveable);
        movingImages.put(moveableObject, moveable);
    }

    /**
     * Checks on which MoveableObject is rockford
     * @param endLine final position's line
     * @param endCol final position's column
     * @param startPos starting position
     */
    public static void whichMoveableObjIsRockIn(int endLine, int endCol, Position startPos) {
        for (MoveableObject moveable : BoulderDashModel.getMovablesList()) {
              if (moveable.getName().equals("Diamond") && moveable.getPosition().getLine() == endLine &&
                      moveable.getPosition().getCol() == endCol) {
                  moveable.checkRockford(endLine, endCol, startPos);
                  if (isPaneCreated) {
                      removeDiamond(moveable);
                      scoreButton.setText("Score: " + scoreCounter);
                  } else {
                      BoulderDashModel.getDiamondsOnMap().put(moveable, false);
                  }
              } else if (moveable.getName().equals("Bonus") && moveable.getPosition().getLine() == endLine &&
                      moveable.getPosition().getCol() == endCol) {
                  moveable.checkRockford(endLine, endCol, startPos);
                  if (isPaneCreated) {
                      removeBonus(moveable);
                      scoreButton.setText("Score: " + scoreCounter);
                  }
              } else if (moveable.getName().equals("Enemy") && moveable.getPosition().getLine() == endLine &&
                      moveable.getPosition().getCol() == endCol) {
                  moveable.checkRockford(endLine,endCol, startPos);
                  model.stopTimer();
              }
        }
    }

    /* removes diamond catched */
    public static void removeDiamond(MoveableObject moveable) {
        movingImages.get(moveable).setImage("FreeTunnel");
        BoulderDashModel.getDiamondsOnMap().put(moveable, false);
        moveable.setName("DisabledDiamond");
    }

    /* removes bonus catched */
    public static void removeBonus(MoveableObject moveable) {
        movingImages.get(moveable).setImage("FreeTunnel");
        moveable.setName("DisabledBonus");
    }

    /* resets GUI information */
    public static void resetGUI() {
        win = false;
        gameOver = false;
        scoreCounter = 0;
        isGateCreated = false;

        scoreButton.setText("Score: " + scoreCounter);
        timeLabel.setText("0");

        movingImages.clear();

        model.resetTimer();
    }

    /* shows points and asks to close program */
    private void endGame(String totalScore, String s) {
        momBox.getChildren().removeAll(grid, restartButton, scoreButton, timeLabel, movesBox);

        Label scoresBox = new Label("            Player: " + playerName + "\n            " +
                currentLevel.substring(0,currentLevel.length() -4) + "\n" +
                "            Final score: " + totalScore + "\n\n" + "<<RANKING>>\n" + s);
        scoresBox.setStyle("-fx-background-color: #CCFFCC; ");
        scoresBox.setMaxWidth(Integer.MAX_VALUE);

        Label endGameMessage = new Label("The game is over!\nPlease click [Close]");
        endGameMessage.setStyle("-fx-background-color: #FFDF73; ");
        endGameMessage.setMaxWidth(Integer.MAX_VALUE);

        momBox.getChildren().addAll(scoresBox, endGameMessage);
    }

    /* level created in GUI */
    @Override
    public void levelCreated() {
        pane = new Pane();
        isPaneCreated = true;
        gameOver = false;
        movingImages = new HashMap<>();
    }

    /**
     * Update GUI after movement of Moveable object
     * @param startPos rockford's position before move
     * @param endPos rockford's position after move
     */
    @Override
    public void updateBoard(Position startPos, Position endPos) {
        Platform.runLater(() -> {
            if (rockImage != null) {
                TranslateTransition tt =
                        new TranslateTransition(Duration.millis(TRANSLATION_TIME_IN_MILLIS), rockImage);
                int beginLine = startPos.getLine();
                int beginCol = startPos.getCol();
                int endLine = endPos.getLine();
                int endCol = endPos.getCol();
                int dCol = endCol - beginCol;
                int dLine = endLine - beginLine;

                tt.setByX(dCol * BoulderDashImage.getImageSize());
                tt.setByY(dLine * BoulderDashImage.getImageSize());
                tt.setOnFinished(e -> {
                    BoulderDashModel.whichAbsPosIsRockIn(endLine, endCol, startPos);
                    whichMoveableObjIsRockIn(endLine, endCol, startPos);

                    if (model.checkWin() && !isGateCreated && !gameOver) {
                        model.createGate();
                    } else if (model.checkWin() && isGateCreated && !gameOver) {
                        if (model.checkGate(endLine, endCol, startPos)) {
                            model.checkNextLevel();
                        }
                    } else if (gameOver) {
                        System.out.println("Game over -> Restarting game.. .");
                        model.restart();
                    }
                });
                tt.play();
            } else System.out.println("Null image");
        });
    }

    /* creates gate in GUI */
    @Override
    public void gateCreated(BoulderDashImage image) {
        pane.getChildren().add(image);
        isGateCreated = true;
    }

    /* let player know that last level was completed */
    @Override
    public void lastLevel(String totalScore, String s) {
        Alert finalMessage = new Alert(Alert.AlertType.INFORMATION);
        finalMessage.setTitle(null);
        finalMessage.setHeaderText(null);
        finalMessage.setContentText("!! << Game finished >> !! \n");
        finalMessage.show();

        endGame(totalScore, s);
    }

    /**
     * Starts next level after previous completed
     * @param level new level
     */
    @Override
    public void updateLevel(String level) {
        resetGUI();

        currentLevel = level;

        momBox.getChildren().removeAll(closeButton, restartButton, timeLabel, scoreButton, grid, movesBox);
        grid = createGrid(level);
        momBox.getChildren().addAll(closeButton, restartButton, timeLabel, scoreButton, grid, movesBox);

        model.startTimer();
    }

    /* resets game in GUI */
    @Override
    public void restartGame() {
        resetGUI();

        momBox.getChildren().removeAll(closeButton, restartButton, timeLabel, scoreButton, grid, movesBox);
        grid = createGrid(currentLevel);
        momBox.getChildren().addAll(closeButton, restartButton, timeLabel, scoreButton, grid, movesBox);

        model.startTimer();
    }

    /* updates time label */
    @Override
    public void updateTimer(int timerValue) {
        Platform.runLater(() -> {
                int minutes = timerValue / 59;
                int seconds = timerValue % 59;
                timeLabel.setText(" " + minutes + "min" + "  " + seconds + "sec" );

        });
    }

    /**
     * Updates image in pane
     * @param imageBehind image to add in pane
     */
    @Override
    public void updatePositionBehind(BoulderDashImage imageBehind) {
        pane.getChildren().add(imageBehind);
    }

    /**
     * Updates movement pane
     * @param movement string with new information
     */
    @Override
    public void updateMovements(String movement) {
        Platform.runLater(() -> movementLabel.setText(movement));
    }

    // toString and Hash
    @Override
    public String toString() {
        return "BoulderDashBoard{" +
                "closeButton=" + closeButton +
                ", restartButton=" + restartButton +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoulderDashBoard that = (BoulderDashBoard) o;
        return closeButton.equals(that.closeButton) && restartButton.equals(that.restartButton);
    }

    @Override
    public int hashCode() {
        return Objects.hash(closeButton, restartButton);
    }
}