package com.game.main;

import java.awt.*;

/**
 * Creates the Bubble object that inherits default object characteristics from GameObject class
 * and adds Bubble exclusive features
 */
public class Bubble extends GameObject {

    private Color color; // color of bubble object

    public Bubble(int x, int y, int diameter, ID id, Color color) {
        super(x, y, id);
        this.color = color;
        super.diameter = diameter;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics bubble) {
        bubble.setColor(Color.black);
        bubble.fillOval(x - 1, y - 1, diameter + 2, diameter + 2);

        bubble.setColor(color);
        bubble.fillOval(x, y, diameter, diameter);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, diameter, diameter);
    }

    public Color getColor() {
        return color;
    }
}
