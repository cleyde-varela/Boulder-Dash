package pt.ipbeja.estig.po2.boulderdash.model;

import java.util.Objects;

public abstract class MoveableObject {
    private String name;
    private Position position;

    /* constructor */
    public MoveableObject(String name, Position position) {
        this.name = name;
        this.position = position;
    }

    /* getters & setters */
    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }

    public void setName(String name) {
        this.name = name;
    }

    /* Moveable objects' movement */
    public abstract Position move(Direction direction);

    /* Moveable objects' movement */
    public abstract Position moveTo(Position nearPosition, Position startPos);

    /* Allows every subclass to decide what happens if rockford's there */
    public abstract void checkRockford(int endLine, int endCol, Position startPos);

    @Override
    public String toString() {
        return "MoveableObject{" +
                "name='" + name + '\'' +
                ", position=" + position +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveableObject that = (MoveableObject) o;
        return name.equals(that.name) && position.equals(that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, position);
    }
}
