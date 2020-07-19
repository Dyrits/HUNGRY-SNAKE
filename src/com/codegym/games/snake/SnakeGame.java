package com.codegym.games.snake;

import com.codegym.engine.cell.*;

import java.util.Random;

public class SnakeGame extends Game {
    public static final int WIDTH = 20, HEIGHT = 20;
    private static final int GOAL = 100;
    private Snake snake;
    private Apple apple;
    private SlowApple slowApple;
    private int turnDelay, score;
    private boolean isGameStopped;

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    private void createGame() {
        this.score = 0;
        setScore(this.score);
        isGameStopped = false;
        snake = new Snake(WIDTH / 2, HEIGHT / 2);
        createNewApple();
        createNewApple(true);
        turnDelay = 250;
        setTurnTimer(turnDelay);
        drawScene();
    }

    private void drawScene() {
        for (int x = 0; x < WIDTH; x ++) {
            for (int y = 0; y < HEIGHT; y ++) {
                setCellValueEx(x, y, Color.LIGHTGREY, "");
            }
        }
        snake.draw(this);
        apple.draw(this);
        if (slowApple.isAlive && slowApple.displayed) { slowApple.draw(this); }
    }

    private void createNewApple(boolean special) {
        int x;
        int y;
        Apple randomApple = null;
        SlowApple specialRandomApple = null;
        do {
            x = getRandomNumber(WIDTH);
            y = getRandomNumber(HEIGHT);
            if (special) { specialRandomApple = new SlowApple(x, y);}
            else { randomApple = new Apple(x, y); }
        } while (snake.checkCollision(special ? specialRandomApple : randomApple));
        if (special) { slowApple = specialRandomApple; }
        else { apple = randomApple; }
    }

    private void createNewApple() {
        createNewApple(false);
    }


    private void gameOver() {
        isGameStopped = true;
        stopTurnTimer();
        showMessageDialog(Color.FIREBRICK, "GAME OVER!", Color.WHITE, 45);
    }

    private void win() {
        isGameStopped = true;
        stopTurnTimer();
        showMessageDialog(Color.DARKGREEN, "VICTORY, THE SNAKE IS NOT HUNGRY ANYMORE!", Color.WHITE, 45);
    }

    @Override
    public void onTurn(int integer) {
        System.out.println(turnDelay);
        snake.move(apple, slowApple);
        if (!apple.isAlive) {
            setScore(this.score += 5);
            setTurnTimer(this.turnDelay -= 10);
            createNewApple();
        }
        addSlowApple();
        if (!snake.isAlive) { gameOver(); }
        if (snake.getLength() > GOAL) { win(); }
        drawScene();
    }

    public void addSlowApple() {
        if (this.score % 45 == 0 && this.score != 0) { slowApple.willAppear = true; }
        boolean highSpeed = this.turnDelay < 50 && !slowApple.displayed;
        boolean isAvailable = this.score % 50 == 0 && slowApple.willAppear && !slowApple.displayed;
        if (isAvailable || highSpeed) {
            createNewApple(true);
            while (slowApple.x == apple.x && slowApple.y == apple.y) { createNewApple(true); }
        }
        if (!slowApple.isAlive && slowApple.displayed) {
            setTurnTimer(this.turnDelay += (new Random().nextInt(75)) + 1);
            slowApple.x = -1;
            slowApple.y = -1;
            slowApple.displayed = false;
        }
    }

    @Override
    public void onKeyPress(Key key) {
        switch (key) {
            case UP:
                if (snake.getSnakeParts(0).x != snake.getSnakeParts(1).x) { snake.setDirection(Direction.UP); }
                break;
            case DOWN:
                if (snake.getSnakeParts(0).x != snake.getSnakeParts(1).x) { snake.setDirection(Direction.DOWN);; }
                break;
            case LEFT:
                if (snake.getSnakeParts(0).y != snake.getSnakeParts(1).y) { snake.setDirection(Direction.LEFT); }
                break;
            case RIGHT:
                if (snake.getSnakeParts(0).y != snake.getSnakeParts(1).y) { snake.setDirection(Direction.RIGHT); }
                break;
            case SPACE:
                if (isGameStopped) { createGame(); }
                break;
        }
    }
}
