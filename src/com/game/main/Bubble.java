package com.game.main;

import java.awt.*;

/**
 * Creates the Bubble object that inherits default object characteristics from GameObject class
 * and adds Bubble exclusive features
 */
public class Bubble extends GameObject {

    private int diameter; // diameter of bubble object
    private Color color; // color of bubble object

    public Bubble(int x, int y, int diameter, ID id, Color color) {
        super(x, y, id);
        this.color = color;
        this.diameter = diameter;
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.setColor(Color.black);
        g.fillOval(x - 1, y - 1, diameter + 2, diameter + 2);

        g.setColor(color);
        g.fillOval(x, y, diameter, diameter);
    }
}
