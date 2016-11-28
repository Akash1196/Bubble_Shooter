package com.game.main;

import java.awt.*;

/**
 * Creates the Cannon object that inherits default object characteristics from GameObject class
 * and adds Bubble exclusive features
 */
public class Cannon extends GameObject {

    public Cannon(int x, int y,ID id) {
        super(x, y, id);
    }

    public void tick() {
        x += velX;
        y += velY;
    }

    public void render(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(x - 1, y - 1, 42, 82);
        g.setColor(Color.cyan);
        g.fillRect(x, y, 40, 80);
    }
}
