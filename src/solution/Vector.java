package solution;

public class Vector {
    private final double[] data;

    public Vector(double[] data) {
        this.data = data.clone();
    }

    public double cartesian(int i) {
        return data[i];
    }

    public Vector plus(Vector b) {
        double[] c = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            c[i] = this.data[i] + b.data[i];
        }
        return new Vector(c);
    }

    public Vector minus(Vector b) {
        double[] c = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            c[i] = this.data[i] - b.data[i];
        }
        return new Vector(c);
    }

    public Vector scale(double alpha) {
        double[] c = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            c[i] = alpha * this.data[i];
        }
        return new Vector(c);
    }

    public double magnitude() {
        double sum = 0.0;
        for (double val : data) {
            sum += val * val;
        }
        return Math.sqrt(sum);
    }

    public Vector direction() {
        double mag = magnitude();
        if (mag == 0) return this;
        return this.scale(1.0 / mag);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < data.length; i++) {
            sb.append(data[i]);
            if (i < data.length - 1) sb.append(", ");
        }
        sb.append(")");
        return sb.toString();
    }
}