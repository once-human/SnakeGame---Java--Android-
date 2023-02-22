package com.example.snakegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

public class GameView extends SurfaceView implements Runnable {
    private volatile boolean playing;
    private Thread gameThread = null;

    // Game objects
    private final Snake snake;
    private final Food food;

    // For drawing
    private final Paint paint;
    private SurfaceHolder surfaceHolder;

    // Screen size
    private int screenX, screenY;

    // Collision detection
    private Rect wallRect;

    // Score
    private int score = 0;

    public GameView(Context context, int screenX, int screenY) {
        super(context);
        this.screenX = screenX;
        this.screenY = screenY;

        snake = new Snake();
        food = new Food(100, 100, 25);

        surfaceHolder = getHolder();
        paint = new Paint();

        // Set up wall rectangle for collision detection
        wallRect = new Rect(0, 0, screenX, screenY);
    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }

    private void update() {
        snake.move();
        checkCollision();
        checkFoodCollision();
    }

    private void checkCollision() {
        // Check if snake collides with the walls
        if (!wallRect.contains(snake.getHead().x, snake.getHead().y)) {
            gameOver();
        }

        // Check if snake collides with its own body
        List<Point> body = snake.getBody();
        for (int i = 1; i < body.size(); i++) {
            if (snake.getHead().equals(body.get(i))) {
                gameOver();
            }
        }
    }

    private void checkFoodCollision() {
        Rect snakeRect = snake.getBounds();
        Rect foodRect = food.getBounds();

        if (snakeRect.intersect(foodRect)) {
            snake.setLength(snake.getLength() + 1);

            // Increment the score
            score++;

            // Move the food to a new random location
            int x = (int) (Math.random() * getWidth());
            int y = (int) (Math.random() * getHeight());
            food.setX(x);
            food.setY(y);
        }
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            Canvas canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);
            paint.setColor(Color.WHITE);
            paint.setTextSize(50);
            canvas.drawText("Score: " + score, 10, 50, paint);

            // Draw the snake
            paint.setColor(Color.GREEN);
            List<Point> body = snake.getBody();
            for (int i = 0; i < body.size(); i++) {
                Point p = body.get(i);
                canvas.drawRect(p.x * snake.getBlockSize(), p.y * snake.getBlockSize(),
                        (p.x + 1) * snake.getBlockSize(), (p.y + 1) * snake.getBlockSize(), paint);
            }
            // Draw the food
            paint.setColor(Color.RED);
            Rect foodRect = food.getBounds();
            canvas.drawRect(foodRect.left, foodRect.top, foodRect.right, foodRect.bottom, paint);

            surface
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                snake.turnUp();
                break;
            case MotionEvent.ACTION_DOWN:
                snake.turnDown();
                break;
            case MotionEvent.ACTION_MOVE:
                // Handle touch move event
                break;
        }
        return true;
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void control() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void gameOver() {
        // Display a message to the user
        new AlertDialog.Builder(getContext())
                .setTitle("Game Over")
                .setMessage("Your score is " + score)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Return to the MainActivity
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        getContext().startActivity(intent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

        // Stop the game
        playing = false;
        gameThread.interrupt();
    }
}


@Override
public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_UP:
        snake.turnUp();
        break;
        case MotionEvent.ACTION_DOWN:
        snake.turnDown();
        break;
        case MotionEvent.ACTION_MOVE:
        // Determine swipe direction
        float xDiff = event.getX() - previousX;
        float yDiff = event.getY() - previousY;
        if (Math.abs(xDiff) > Math.abs(yDiff)) {
        // Horizontal swipe
        if (xDiff > 0) {
        snake.turnRight();
        } else {
        snake.turnLeft();
        }
        } else {
        // Vertical swipe
        if (yDiff > 0) {
        snake.turnDown();
        } else {
        snake.turnUp();
        }
        }
        break;
        }
        previousX = event.getX();
        previousY = event.getY();
        return true;
        }

public void pause() {
        playing = false;
        try {
        gameThread.join();
        } catch (InterruptedException e) {
        e.printStackTrace();
        }
        }

public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
        }

@Override
public void run() {
        while (playing) {
        update();
        draw();
        control();
        }
        }

private void update() {
        // Update game state
        }

private void draw() {
        // Draw game objects
        }

private void control() {
        try {
        Thread.sleep(10);
        } catch (InterruptedException e) {
        e.printStackTrace();
        }
        }

private void gameOver() {
        // Display a message to the user
        new AlertDialog.Builder(getContext())
        .setTitle("Game Over")
        .setMessage("Your score is " + score)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int which) {
        // Return to the MainActivity
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getContext().startActivity(intent);
        }
        })
        .setIcon(android.R.drawable.ic_dialog_alert)
        .show();

        // Stop the game
        playing = false;
        Thread.currentThread().interrupt();
        }
        }
