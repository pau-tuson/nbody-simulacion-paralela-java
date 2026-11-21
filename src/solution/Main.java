package solution;

public class Main {
    public static void main(String[] args) {
        // Usa el fichero pasado por argumento o 'data/3body.txt' por defecto
        String nombreFichero = (args.length > 0) ? args[0] : "data/3body.txt";
        double dt = 20000.0;
        int pauseTime = 10;
        boolean trace = false;

        // Instanciamos la arquitectura OO
        Universe universe = new Universe(nombreFichero);
        NBodySimulator simulator = new NBodySimulator(universe, dt, pauseTime, trace);

        // Iniciamos la animación
        simulator.simulate();
    }
}