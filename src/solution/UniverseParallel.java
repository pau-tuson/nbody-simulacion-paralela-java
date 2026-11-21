package solution;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UniverseParallel {

    private static final double G = 6.674e-11;
    private static final double SOFTENING = 1e-9;

    public static class Body {
        public double x, y, z;
        public double vx, vy, vz;
        public double fx, fy, fz;
        public double mass;

        public Body(double x, double y, double z, double vx, double vy, double vz, double mass) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.vx = vx;
            this.vy = vy;
            this.vz = vz;
            this.mass = mass;
        }
    }

    private final int numBodies;
    private final Body[] bodies;
    private final int numThreads;
    private final ExecutorService threadPool;

    public UniverseParallel(int n, int numThreads) {
        this.numBodies = n;
        this.numThreads = numThreads;
        this.threadPool = Executors.newFixedThreadPool(numThreads);
        this.bodies = initBodies(n);
    }

    private static Body[] initBodies(int n) {
        Body[] b = new Body[n];
        Random r = new Random(42);

        for (int i = 0; i < n; i++) {
            double x = r.nextDouble() * 2.0 - 1.0;
            double y = r.nextDouble() * 2.0 - 1.0;
            double z = r.nextDouble() * 2.0 - 1.0;
            double vx = r.nextDouble() * 2.0 - 1.0;
            double vy = r.nextDouble() * 2.0 - 1.0;
            double vz = r.nextDouble() * 2.0 - 1.0;
            double mass = r.nextDouble() * 100.0 + 1.0;

            b[i] = new Body(x, y, z, vx, vy, vz, mass);
        }
        return b;
    }

    public void update(double dt) {
        int chunkSize = (numBodies + numThreads - 1) / numThreads;

        // FASE 1: Cálculo concurrente de fuerzas O(N^2)
        CountDownLatch forceLatch = new CountDownLatch(numThreads);
        for (int t = 0; t < numThreads; t++) {
            final int start = t * chunkSize;
            final int end = Math.min(start + chunkSize, numBodies);

            threadPool.submit(() -> {
                try {
                    for (int i = start; i < end; i++) {
                        bodies[i].fx = 0.0;
                        bodies[i].fy = 0.0;
                        bodies[i].fz = 0.0;

                        for (int j = 0; j < numBodies; j++) {
                            if (i != j) {
                                double dx = bodies[j].x - bodies[i].x;
                                double dy = bodies[j].y - bodies[i].y;
                                double dz = bodies[j].z - bodies[i].z;

                                double distSqr = dx * dx + dy * dy + dz * dz + SOFTENING * SOFTENING;
                                double dist = Math.sqrt(distSqr);
                                double force = (G * bodies[i].mass * bodies[j].mass) / (dist * dist * dist);

                                bodies[i].fx += force * dx;
                                bodies[i].fy += force * dy;
                                bodies[i].fz += force * dz;
                            }
                        }
                    }
                } finally {
                    forceLatch.countDown();
                }
            });
        }

        try {
            forceLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // FASE 2: Integración (actualización física)
        CountDownLatch moveLatch = new CountDownLatch(numThreads);
        for (int t = 0; t < numThreads; t++) {
            final int start = t * chunkSize;
            final int end = Math.min(start + chunkSize, numBodies);

            threadPool.submit(() -> {
                try {
                    for (int i = start; i < end; i++) {
                        bodies[i].vx += (bodies[i].fx / bodies[i].mass) * dt;
                        bodies[i].vy += (bodies[i].fy / bodies[i].mass) * dt;
                        bodies[i].vz += (bodies[i].fz / bodies[i].mass) * dt;

                        bodies[i].x += bodies[i].vx * dt;
                        bodies[i].y += bodies[i].vy * dt;
                        bodies[i].z += bodies[i].vz * dt;
                    }
                } finally {
                    moveLatch.countDown();
                }
            });
        }

        try {
            moveLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void shutdown() {
        threadPool.shutdown();
    }

    public Body getBody(int i) {
        return bodies[i];
    }
}