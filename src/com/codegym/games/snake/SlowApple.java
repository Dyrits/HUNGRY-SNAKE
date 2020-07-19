package com.codegym.games.snake;

import com.codegym.engine.cell.Color;
import com.codegym.engine.cell.Game;

public class SlowApple extends Apple {
    public boolean displayed;
    public boolean willAppear;

    SlowApple(int x, int y) {
        super(x, y);
        this.willAppear = false;
        this.displayed = true;
    }


    @Override
    public void draw(Game game) {
        game.setCellValueEx(this.x, this.y, Color.NONE, APPLE_SIGN, Color.YELLOWGREEN, 75);
    }
}
