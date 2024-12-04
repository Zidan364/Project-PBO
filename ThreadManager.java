public class ThreadManager {

    private Game game;
    private Thread gameThread;
    private boolean running;

    public ThreadManager(Game game) {
        this.game = game;
    }

    public synchronized void startGameLoop() {
        running = true;

        gameThread = new Thread(() -> {
            while (running) {
                game.update();
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    running = false;
                }
            }
        });
        gameThread.start();
    }

    public synchronized void stopGameLoop() {
        running = false;
        if (gameThread != null) {
            gameThread.interrupt(); 
            try {
                gameThread.join(); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            gameThread = null;
        }
    }
    public synchronized void restartGameLoop() {
        stopGameLoop();
        game.resetGame();
        startGameLoop();
    }
}
