package solution;

public class NBodySimulator {
    private Universe universe;
    private double timeStep;
    private int pauseTime;
    private boolean trace;

    public NBodySimulator(Universe universe, double dt, int pt, boolean doTrace) {
        this.universe = universe;
        this.timeStep = dt;
        this.pauseTime = pt;
        this.trace = doTrace;
    }

    private void createCanvas() {
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenRadius(0.025);
        double radius = universe.getRadius();
        StdDraw.setXscale(-radius, +radius);
        StdDraw.setYscale(-radius, +radius);
    }

    private void drawUniverse() {
        int n = universe.getNumBodies();
        for (int i = 0; i < n; i++) {
            Vector pos = universe.getBodyPosition(i);
            StdDraw.point(pos.cartesian(0), pos.cartesian(1));
        }
    }

    public void simulate() {
        createCanvas();
        while (true) {
            if (!trace) {
                StdDraw.clear();
            }
            universe.update(timeStep);
            drawUniverse();
            StdDraw.show();
            StdDraw.pause(pauseTime);
        }
    }
}