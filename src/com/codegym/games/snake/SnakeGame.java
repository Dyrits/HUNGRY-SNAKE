package com.codegym.games.snake;

import com.codegym.engine.cell.*;

import java.util.Random;

public class SnakeGame extends Game {
    public static final int WIDTH = 20, HEIGHT = 20;
    private static final int GOAL = 100;
    private Snake snake;
    private Apple apple;
    private SlowGreenApple slowGreenApple;
    private int turnDelay, score;
    private boolean isGameStopped;

    /**
     * Launch the game.
     */
    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    /**
     * Create a game, creating and initializing the different objects, and setting up the timer.
     */
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

    /**
     * Draw the different cells of the game, the snake and the apples.
     */
    private void drawScene() {
        for (int x = 0; x < WIDTH; x ++) {
            for (int y = 0; y < HEIGHT; y ++) {
                setCellValueEx(x, y, Color.LIGHTGREY, "");
            }
        }
        snake.draw(this);
        apple.draw(this);
        // The green apple is not drawn every turn.
        if (slowGreenApple.isAlive && slowGreenApple.displayed) { slowGreenApple.draw(this); }
    }

    /**
     *
     * @param slowApple boolean | Boolean value to create a Slow Green Apple if true and a normal one if false.
     *                  The default value is false.
     */
    private void createNewApple(boolean slowApple) {
        int x;
        int y;
        Apple randomApple = null;
        SlowGreenApple specialRandomApple = null;
        do {
            x = getRandomNumber(WIDTH);
            y = getRandomNumber(HEIGHT);
            if (slowApple) { specialRandomApple = new SlowGreenApple(x, y);}
            else { randomApple = new Apple(x, y); }
        } while (snake.checkCollision(slowApple ? specialRandomApple : randomApple));
        if (slowApple) { slowGreenApple = specialRandomApple; }
        else { apple = randomApple; }
    }

    private void createNewApple() {
        createNewApple(false);
    }


    /**
     * Turn off the timer and display a message dialog when the game is lost.
     */
    private void gameOver() {
        isGameStopped = true;
        stopTurnTimer();
        showMessageDialog(Color.FIREBRICK, "GAME OVER!\nSCORE: " + this.score, Color.WHITE, 25);
    }

    /**
     * Turn off the timer and display a message dialog when the game is won.
     */
    private void win() {
        isGameStopped = true;
        stopTurnTimer();
        showMessageDialog(Color.DARKGREEN, "VICTORY!\nTHE SNAKE IS NOT HUNGRY ANYMORE!\nSCORE: " + this.score, Color.WHITE, 25);
    }

    /**
     * Handle the different events happening in one turn.
     * The snake moves, the timer is adjusted, the apples are created and the display is refreshed.
     * @param integer int | The parameter is not used.
     */
    @Override
    public void onTurn(int integer) {
        if (!isGameStopped) {
            System.out.println(turnDelay);
            snake.move(apple, slowGreenApple);
            if (!apple.isAlive) {
                setScore(this.score += 5);
                setTurnTimer(this.turnDelay -= this.turnDelay < 100 ? 5 : 10);
                createNewApple();
            }
            checkSlowGreenApple();
            drawScene();
        }
        if (!snake.isAlive) { gameOver(); }
        if (snake.getLength() > GOAL) { win(); }
    }

    /**
     * Create a Slow Green Apple under various conditions.
     */
    public void checkSlowGreenApple() {
        if (this.score % 45 == 0 && this.score != 0) { slowGreenApple.willAppear = true; }
        // If the turn delay is too low (under 50 milliseconds), a Slow Green Apple is created.
        boolean highSpeed = this.turnDelay < 50 && !slowGreenApple.displayed;
        boolean isAvailable = this.score % 50 == 0 && slowGreenApple.willAppear && !slowGreenApple.displayed;
        if (isAvailable || highSpeed) {
            createNewApple(true);
            while (slowGreenApple.x == apple.x && slowGreenApple.y == apple.y) { createNewApple(true); }
        }
        if (!slowGreenApple.isAlive && slowGreenApple.displayed) {
            // The speed of the game is randomly reduced when a Slow Green Apple has been eaten.
            setTurnTimer(this.turnDelay += (new Random().nextInt(75)) + 1);
            slowGreenApple.x = -1;
            slowGreenApple.y = -1;
            slowGreenApple.displayed = false;
        }
    }

    /**
     * Handle keyboard input from the user.
     * @param key Key | Input from the user.
     */
    @Override
    public void onKeyPress(Key key) {
        switch (key) {
            case UP:
                if (!isGameStopped && snake.getSnakeParts(0).x != snake.getSnakeParts(1).x) { snake.setDirection(Direction.UP); }
                break;
            case DOWN:
                if (!isGameStopped && snake.getSnakeParts(0).x != snake.getSnakeParts(1).x) { snake.setDirection(Direction.DOWN);; }
                break;
            case LEFT:
                if (!isGameStopped && snake.getSnakeParts(0).y != snake.getSnakeParts(1).y) { snake.setDirection(Direction.LEFT); }
                break;
            case RIGHT:
                if (!isGameStopped && snake.getSnakeParts(0).y != snake.getSnakeParts(1).y) { snake.setDirection(Direction.RIGHT); }
                break;
            case SPACE:
                if (isGameStopped) { createGame(); }
                break;
        }
    }
}
