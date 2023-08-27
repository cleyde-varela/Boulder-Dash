package pt.ipbeja.estig.po2.boulderdash.model;

import java.util.Objects;

public abstract class AbstractPosition {
    private String absName;
    private Position absPos;

    /* constructor */
    public AbstractPosition(String absName, Position absPos) {
        this.absName = absName;
        this.absPos = absPos;
    }

    /* Allows every subclass to decide what happens to rockford */
    public abstract void checkRockford(int endLine, int endCol, Position startPos);

    /* getters */
    public String getAbsName() {
        return absName;
    }

    public Position getAbsPos() {
        return absPos;
    }

    @Override
    public String toString() {
        return "AbstractPosition{" +
                "absName='" + absName + '\'' +
                ", absPos=" + absPos +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractPosition that = (AbstractPosition) o;
        return absName.equals(that.absName) && absPos.equals(that.absPos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(absName, absPos);
    }
}
