package solution;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Universe {
    private int numBodies;
    private double radius;
    private Body[] bodies;

    public Universe(String fname) {
        try {
            Scanner in = new Scanner(new FileReader(fname));
            numBodies = Integer.parseInt(in.next());
            radius = Double.parseDouble(in.next());

            bodies = new Body[numBodies];
            for (int i = 0; i < numBodies; i++) {
                double rx = Double.parseDouble(in.next());
                double ry = Double.parseDouble(in.next());
                double vx = Double.parseDouble(in.next());
                double vy = Double.parseDouble(in.next());
                double mass = Double.parseDouble(in.next());

                Vector r = new Vector(new double[]{rx, ry});
                Vector v = new Vector(new double[]{vx, vy});
                bodies[i] = new Body(r, v, mass);
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void update(double dt) {
        Vector[] forces = new Vector[numBodies];
        for (int i = 0; i < numBodies; i++) {
            forces[i] = new Vector(new double[2]);
        }

        for (int i = 0; i < numBodies; i++) {
            for (int j = 0; j < numBodies; j++) {
                if (i != j) {
                    forces[i] = forces[i].plus(bodies[i].forceFrom(bodies[j]));
                }
            }
        }

        for (int i = 0; i < numBodies; i++) {
            bodies[i].move(forces[i], dt);
        }
    }

    public Vector getBodyPosition(int i) {
        return bodies[i].getPosition();
    }

    public int getNumBodies() {
        return numBodies;
    }

    public double getRadius() {
        return radius;
    }
}