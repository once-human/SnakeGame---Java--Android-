package com.example.snakegame;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int UNIT_SIZE = 50;

    private Snake snake;
    private Point food;
    private int score;

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private Paint paint;

    private boolean gameIsRunning;
    private Thread gameThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        paint = new Paint();

        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(this);

        gameIsRunning = false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startButton:
                startGame();
                break;
            case R.id.restartButton:
                setContentView(R.layout.activity_main);
                surfaceView = findViewById(R.id.surfaceView);
                surfaceHolder = surfaceView.getHolder();
                paint = new Paint();
                startGame();
                break;
            case R.id.quitButton:
                finish();
                break;
            case R.id.mainMenuButton:
                setContentView(R.layout.activity_main);
                surfaceView = findViewById(R.id.surfaceView);
                surfaceHolder = surfaceView.getHolder();
                paint = new Paint();
                break;
        }
    }

    private void startGame() {
        snake = new Snake(new Point(5, 5));
        score = 0;
        spawnFood();
        gameThread = new GameThread();
        gameThread.start();
    }

    private void spawnFood() {
        int x = (int) (Math.random() * surfaceView.getWidth() / UNIT_SIZE);
        int y = (int) (Math.random() * surfaceView.getHeight() / UNIT_SIZE);
        food = new Point(x, y);
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);
            paint.setColor(Color.WHITE);
            paint.setTextSize(50);
            canvas.drawText("Score: " + score, 10, 50, paint);

            // Draw the snake
            paint.setColor(Color.GREEN);
            List<Point> body = snake.getBody();
            for (int i = 0; i < body.size(); i++) {
                Point part = body.get(i);
                canvas.drawRect(part.x * UNIT_SIZE, part.y * UNIT_SIZE,
                        (part.x + 1) * UNIT_SIZE, (part.y + 1) * UNIT_SIZE, paint);
            }

            // Draw the food
            paint.setColor(Color.RED);
            canvas.drawCircle(food.x * UNIT_SIZE + UNIT_SIZE / 2,
                    food.y * UNIT_SIZE + UNIT_SIZE / 2, UNIT_SIZE / 2, paint);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void update() {
        if (!snake.move()) {
            gameIsRunning = false;
        }

        if (snake.getHead().equals(food)) {
            snake.grow();
            score += 10;
            spawnFood();
        }
    }

    private void showGameOverDialog() {
        runOnUiThread(() -> {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.game_over)
                    .setMessage(getString(R.string.score_message, score))
                    .setPositiveButton(R.string.restart, (dialog, which) -> {
                        restartGame();
                    })
                    .setNegativeButton(R.string.quit, (dialog, which) -> {
                        finish();
                    })
                    .show();
        });
    }

    private void restartGame() {
        setContentView(R.layout.activity_main);
        surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        paint = new Paint();
        startGame();
    }
