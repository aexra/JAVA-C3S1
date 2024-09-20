package org.dstu.lab1fx.task6;
public class Atom {
    public String name;
    public double x, y, z;

    public Atom(String name, double x, double y, double z) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return name + " " + x + " " + y + " " + z;
    }
}
