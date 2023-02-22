package com.example.snakegame;

import android.graphics.Path;
import android.graphics.Point;
import android.icu.text.RelativeDateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

public class Snake {
    private int length;
    private List<Point> body;
    private int direction;

    public Snake() {
        length = 1;
        body = new ArrayList<>();
        body.add(new Point(0, 0)); // Initial position of the snake
        direction = Path.Direction.RIGHT; // Initial direction of the snake
    }

    public void move() {
        Point head = body.get(0);
        Point newHead = new Point(head.x, head.y);

        switch (direction) {
            case Direction.UP:
                newHead.y--;
                break;
            case RelativeDateTimeFormatter.Direction.NEXT:
                newHead.y++;
                break;
            case Direction.LEFT:
                newHead.x--;
                break;
            case Direction.RIGHT:
                newHead.x++;
                break;
        }

        body.add(0, newHead);
        if (body.size() > length) {
            body.remove(length);
        }
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public List<Point> getBody() {
    }

    // getters and setters for length, body, and direction
    // ...
}
