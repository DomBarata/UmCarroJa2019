import java.io.Serializable;

public class Ponto <T extends Number> implements Serializable {

    private T x;
    private T y;

    /*
     * Main constructor for objects of class Point
     */
    public Ponto(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public Ponto(Ponto<T> p) {
        this(p.getX(), p.getY());
    }

    public Ponto() {
        throw new java.lang.Error("Cannot instantiate new point without providing initial coordinates.");
    }

    public T getX() {
        return this.x;
    }

    public T getY() {
        return this.y;
    }

    public void setX(T x) {
        this.x = x;
    }

    public void setY(T y) {
        this.y = y;
    }

    public double distanceTo(Ponto<T> b) {

        double dX = Math.abs( b.getX().doubleValue() - this.x.doubleValue() );
        double dY = Math.abs( b.getY().doubleValue() - this.y.doubleValue() );

        return Math.sqrt( Math.pow(dX, 2) + Math.pow(dY, 2) );
    }

    @Override
    public String toString() {
        return this.x + "," + this.y;
    }

}