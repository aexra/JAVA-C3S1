package org.dstu.lab1fx.task6;
public class Atom {
    public String name;
    public double x, y, z;
    public int r, g, b;

    public Atom(String name, double x, double y, double z, int r, int g, int b) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    @Override
    public String toString() {
        return name + " " + x + " " + y + " " + z;
    }
}
