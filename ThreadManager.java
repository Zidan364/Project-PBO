public class ThreadManager {

    private Game game;
    private Thread gameThread;

    public ThreadManager(Game game) {
        this.game = game;
    }

    public void startGameLoop() {
        gameThread = new Thread(() -> {
            while (true) {
                if (Thread.interrupted()) break;
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
        if (gameThread != null) {
            gameThread.interrupt();
        }
    }
}
