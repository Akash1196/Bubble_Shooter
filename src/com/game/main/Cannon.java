package com.game.main;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Creates the Cannon object that inherits default object characteristics from GameObject class
 * and adds Bubble exclusive features
 */
public class Cannon extends GameObject {

    private double theta;

    public Cannon(int x, int y, double theta, ID id) {
        super(x, y, id);
        this.theta = theta;
    }

    @Override
    public void tick() {
        theta += velTheta;
        theta = Game.clamp(theta, -1.4, 1.4); // can't shoot bubble downward or completely horizontal!
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        AffineTransform old = g2d.getTransform();
        g2d.rotate(theta, x + 20, Game.HEIGHT - 48);
        g2d.setColor(Color.black);
        g2d.fillOval(x - 15, y + 19, 72, 72);
        g2d.fillRect(x - 1, y - 1, 42, 82);
        g2d.setColor(Color.cyan);
        g2d.fillOval(x - 14, y + 20, 70, 70);
        g2d.fillRect(x, y, 40, 80);
        g2d.setTransform(old);
    }
}
