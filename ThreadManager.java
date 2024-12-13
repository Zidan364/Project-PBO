public class ThreadManager {

    private Game game;
    private Thread gameThread;

    public ThreadManager(Game game) {
        this.game = game;
    }

    public void startGameLoop() {
        stopGameLoop(); // Ensure no old thread is running
        gameThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                game.update();
                try {
                    Thread.sleep(16); // ~60 FPS
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        gameThread.start();
    }

    public void stopGameLoop() {
        if (gameThread != null && gameThread.isAlive()) {
            gameThread.interrupt();
            try {
                gameThread.join(); // Wait for thread to finish
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            gameThread = null;
        }
    }
}