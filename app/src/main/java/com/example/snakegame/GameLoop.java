package com.example.snakegame;

public class GameLoop extends Thread {
    private GameView gameView;
    private boolean isRunning = false;
    private final static long FPS = 10;

    public GameLoop(GameView gameView) {
        this.gameView = gameView;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    @Override
    public void run() {
        long startTime;
        long sleepTime;

        while (isRunning) {
            startTime = System.currentTimeMillis();

            gameView.update();
            gameView.draw();

            sleepTime = FPS - (System.currentTimeMillis() - startTime);

            try {
                if (sleepTime > 0) {
                    sleep(sleepTime);
                } else {
                    sleep(10);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
