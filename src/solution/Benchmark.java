package solution;

public class Benchmark {
    private static final int N = 1000;
    private static final int STEPS = 100;
    private static final double DT = 0.01;
    private static final double T_SECUENCIAL = 0.186; // Tiempo de referencia base de la Parte 1

    public static void main(String[] args) {
        int[] threadCounts = {1, 2, 4, 8, 16};

        System.out.println("================================ BENCHMARK PARTE 2 ================================");
        System.out.printf("%-10s | %-12s | %-10s | %-12s | %-45s%n",
                "Hilos (T)", "Tiempo (s)", "Speedup", "Eficiencia", "Posicion P0");
        System.out.println("------------------------------------------------------------------------------------------------------------------");

        for (int threads : threadCounts) {
            UniverseParallel universe = new UniverseParallel(N, threads);

            long start = System.currentTimeMillis();
            for (int s = 0; s < STEPS; s++) {
                universe.update(DT);
            }
            long end = System.currentTimeMillis();
            double timeSeconds = (end - start) / 1000.0;

            universe.shutdown();

            double speedup = T_SECUENCIAL / timeSeconds;
            double efficiency = speedup / threads;
            UniverseParallel.Body p0 = universe.getBody(0);
            String posP0 = "(" + p0.x + ", " + p0.y + ", " + p0.z + ")";

            System.out.printf("%-10d | %-12.4f | %-9.2fx | %-12.2f | %-45s%n",
                    threads, timeSeconds, speedup, efficiency, posP0);
        }
        System.out.println("==================================================================================================================");
    }
}