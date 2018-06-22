package de.weltraumschaf.nano.container;

/**
 * @since 1.0.0
 */
final class Container {
    private volatile boolean running = true;

    void start() {
        System.out.println("Container starts...");
        while (running) {
            try {
                System.out.println("Container is running.");
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    void stop() {
        System.out.println("Container is stopping...");
        running = false;
        System.out.println("Container stopped.");
    }
}
