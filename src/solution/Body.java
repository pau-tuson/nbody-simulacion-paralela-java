package solution;

public class Body {
    private Vector position;
    private Vector velocity;
    private double mass;

    public Body(Vector position, Vector velocity, double mass) {
        this.position = position;
        this.velocity = velocity;
        this.mass = mass;
    }

    public Vector forceFrom(Body b) {
        double G = 6.67e-11;
        Vector delta = b.position.minus(this.position);
        double dist = delta.magnitude();
        double magnitude = (G * this.mass * b.mass) / (dist * dist);
        return delta.direction().scale(magnitude);
    }

    public void move(Vector f, double dt) {
        Vector a = f.scale(1.0 / mass);
        velocity = velocity.plus(a.scale(dt));
        position = position.plus(velocity.scale(dt));
    }

    public Vector getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "position " + position + ", velocity " + velocity + ", mass " + mass;
    }
}