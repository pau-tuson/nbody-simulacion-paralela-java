# Simulación de N cuerpos en Java (secuencial y paralela)

Simulación gravitatoria de N cuerpos en Java. Cada paso calcula las fuerzas entre todos los pares de cuerpos (O(N²)) y después mueve cada cuerpo. Tiene una versión secuencial con animación y una versión **paralela con hilos**, con un estudio de rendimiento.

Proyecto en pareja de la UAB (Grado en Ingeniería Informática).

## Estructura

- `src/solution/Body`, `Vector`, `Universe`: modelo físico.
- `src/solution/NBodySimulator`: dibujo con `StdDraw` (librería de Princeton, Sedgewick & Wayne), separado de la física.
- `src/solution/UniverseParallel`: versión paralela.
- `src/solution/Benchmark`: mide tiempos con 1, 2, 4, 8 y 16 hilos.
- `data/`: sistemas de ejemplo (2, 3 y 4 cuerpos, planetas).
- `docs/`: diagrama de clases (PlantUML), justificación del diseño de la parte 1 y resultados del benchmark.

## Paralelización (mi parte)

Yo hice la **parte 2**, la versión paralela:

- Un `ExecutorService` con un *pool* fijo de hilos reparte los cuerpos en bloques.
- Cada paso tiene dos fases, primero calcular fuerzas y después mover. Entre las dos hay una barrera con **`CountDownLatch`**, para que ningún hilo mueva un cuerpo mientras otro todavía está leyendo su posición. Así se evitan las condiciones de carrera.
- Para validarlo, la posición final del cuerpo P0 tiene que ser exactamente la misma que en la versión secuencial, y lo es en todas las ejecuciones.

Resultados en mi máquina (N = 1000, 100 pasos):

| Hilos | Tiempo (s) | Aceleración respecto a 1 hilo |
|---|---|---|
| 1 | 0.556 | 1.0x |
| 2 | 0.270 | 2.1x |
| 4 | 0.153 | 3.6x |
| 8 | 0.142 | 3.9x |
| 16 | 0.130 | 4.3x |

A partir de 4 hilos la mejora se estanca, por el coste de sincronizar y de crear las tareas en cada paso. El análisis completo está en `docs/benchmark.txt`.

## Cómo ejecutarlo

```bash
javac -d out src/solution/*.java
java -cp out solution.Main data/planets.txt   # animación
java -cp out solution.Benchmark               # benchmark de la versión paralela
```

## Tecnologías

Java · Concurrencia (`ExecutorService`, `CountDownLatch`) · Diseño orientado a objetos · PlantUML
