package org.dstu.lab1fx.task6;

import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Pair;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.lang.Double.parseDouble;

public class Molecule extends Group {
    public static final double ATOM_RADIUS = 10;

    public ArrayList<Atom> atoms = new ArrayList<>();
    public String name;

    public Molecule(String name, Atom... atoms) {
        this.atoms.addAll(Arrays.stream(atoms).toList());
        this.name = name;
    }

    public static Molecule load(File file) throws IOException {
        var ms = Files.readAllLines(file.toPath());

        var atoms_n = parseInt(ms.get(0));
        var molecule_name = ms.get(1);
        var atoms = new Atom[atoms_n];

        for (var i = 0; i < atoms_n; i++) {
            var atom_s = ms.get(i + 2);
            var splitted = atom_s.split(" ");
            atoms[i] = new Atom(splitted[0], parseDouble(splitted[1]), parseDouble(splitted[2]), parseDouble(splitted[3]));
        }

        return new Molecule(molecule_name, atoms);
    }
    public static Molecule load(File file, Point3D modifier) throws IOException {
        var ms = Files.readAllLines(file.toPath());

        var atoms_n = parseInt(ms.get(0));
        var molecule_name = ms.get(1);
        var atoms = new Atom[atoms_n];

        for (var i = 0; i < atoms_n; i++) {
            var atom_s = ms.get(i + 2);
            var splitted = atom_s.split(" ");
            atoms[i] = new Atom(splitted[0], parseDouble(splitted[1]) * modifier.getX(), parseDouble(splitted[2]) * modifier.getY(), parseDouble(splitted[3]) * modifier.getZ());
        }

        return new Molecule(molecule_name, atoms);
    }

    public Molecule build() {
        var connections = new ArrayList<Cylinder>();
        var connectionsCombinations = createPairCombinations(atoms);

        for (var comb : connectionsCombinations) {
            var p1 = new Point3D(comb.getKey().x, comb.getKey().y, comb.getKey().z);
            var p2 = new Point3D(comb.getValue().x, comb.getValue().y, comb.getValue().z);
            var c = createConnection(p1, p2);
            connections.add(c);
        }

        this.getChildren().addAll(connections);

        for (var atom : atoms) {
            var s = new Sphere();

            s.setRadius(ATOM_RADIUS);
            s.setTranslateX(atom.x);
            s.setTranslateY(atom.y);
            s.setTranslateZ(atom.z);

            this.getChildren().add(s);
        }

        System.out.println(this.getChildren().size());

        return this;
    }

    public Bounds getBounds() {
        return this.localToScene(this.getBoundsInLocal());
    }

    private Cylinder createConnection(Point3D origin, Point3D target) {
        Point3D yAxis = new Point3D(0, 1, 0); /* цилиндр изначально расположен вертикально (высота вдоль оси OY), направляющий вектор для оси OY - (0, 1, 0) */
        Point3D diff = target.subtract(origin); /* разность векторов target и origin - вектор, направленный от origin к target */
        double height = diff.magnitude(); // расстояние между origin и target - высота цилиндра
        Point3D mid = target.midpoint(origin); /* точка, лежащая посередине между target и origin - сюда нужно переместить цилиндр (поместить его центр) */
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());
        Point3D axisOfRotation = diff.crossProduct(yAxis); /* ось, вокруг которой нужно повернуть цилиндр - направлена перпендикулярно плоскости, в которой лежат пересекающиеся вектора diff (направление от origin к target) и yAxis (текущее направление высоты цилиндра), получается как векторное произведение diff и yAxis */
        double angle = Math.acos(diff.normalize().dotProduct(yAxis)); /* угол поворота цилиндра - угол между нормализованным (длина равна 1) вектором diff и вектором yAxis */
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);
        Cylinder line = new Cylinder(1, height); /* радиус цилиндра 1, нужно заменить на свое значение */
        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);
        return line;
    }
    private Cylinder createConnection(Point3D origin, Point3D target, double radius) {
        Point3D yAxis = new Point3D(0, 1, 0); /* цилиндр изначально расположен вертикально (высота вдоль оси OY), направляющий вектор для оси OY - (0, 1, 0) */
        Point3D diff = target.subtract(origin); /* разность векторов target и origin - вектор, направленный от origin к target */
        double height = diff.magnitude(); // расстояние между origin и target - высота цилиндра
        Point3D mid = target.midpoint(origin); /* точка, лежащая посередине между target и origin - сюда нужно переместить цилиндр (поместить его центр) */
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());
        Point3D axisOfRotation = diff.crossProduct(yAxis); /* ось, вокруг которой нужно повернуть цилиндр - направлена перпендикулярно плоскости, в которой лежат пересекающиеся вектора diff (направление от origin к target) и yAxis (текущее направление высоты цилиндра), получается как векторное произведение diff и yAxis */
        double angle = Math.acos(diff.normalize().dotProduct(yAxis)); /* угол поворота цилиндра - угол между нормализованным (длина равна 1) вектором diff и вектором yAxis */
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);
        Cylinder line = new Cylinder(radius, height); /* радиус цилиндра 1, нужно заменить на свое значение */
        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);
        return line;
    }

    private static <T> ArrayList<Pair<T, T>> createPairCombinations(List<T> list) {
        ArrayList<Pair<T, T>> pairList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                pairList.add(new Pair<>(list.get(i), list.get(j)));
            }
        }

        return pairList;
    }

    @Override
    public String toString() {
        return atoms.size() + "\n" + name + "\n" + String.join("\n", atoms.stream().map(x -> x.toString()).toList());
    }
}
