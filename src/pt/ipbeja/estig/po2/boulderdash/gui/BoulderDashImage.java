package pt.ipbeja.estig.po2.boulderdash.gui;

import javafx.scene.image.Image;
import pt.ipbeja.estig.po2.boulderdash.model.Position;

import javafx.scene.image.ImageView;

import java.util.Objects;

public class BoulderDashImage extends ImageView {

    private String imageName;
    private Position imagePosition;
    private final static int IMAGE_SIZE = 60;

    //constructor
    public BoulderDashImage(String imageName, Position imagePosition){
        this.setImage(imageName);
        this.setPositions(imagePosition);
    }

    /* getters & setters */
    public String getImageName() {
        return imageName;
    }

    public Position getImagePosition() {
        return imagePosition;
    }

    public int getLine() {
        return this.imagePosition.getLine();
    }

    public int getCol() {
        return this.imagePosition.getCol();
    }

    public static int getImageSize() {
        return IMAGE_SIZE;
    }

    public void setImage(String imageName) {
        this.imageName = imageName;
        String locationImage = "resources/" + this.imageName + ".png";
        Image image = new Image(locationImage);
        this.setImage(image);
        this.setFitHeight((IMAGE_SIZE));
        this.setFitWidth(IMAGE_SIZE);
    }

    public void setPositions(Position imagePosition) {
        this.imagePosition = imagePosition;
        this.setX(imagePosition.getCol() * IMAGE_SIZE);
        setY(imagePosition.getLine() * IMAGE_SIZE);
    }

    /**
     * Updates position
     * @param dLine difference
     * @param dCol difference
     */
    public void updatePosition(int dLine, int dCol) {
            int line = this.imagePosition.getLine() + dLine;
            int col = this.imagePosition.getCol() + dCol;
            this.imagePosition = new Position(line, col);
    }

    @Override
    public String toString() {
        return "BoulderDashImage{" +
                "imageName='" + imageName + '\'' +
                ", imagePosition=" + imagePosition +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoulderDashImage that = (BoulderDashImage) o;
        return imageName.equals(that.imageName) && imagePosition.equals(that.imagePosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageName, imagePosition);
    }
}
