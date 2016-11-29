package com.game.main;

import java.awt.*;

/**
 * Creates the cannon ball which acts as the current bubble that
 * is shot from the cannon
 */
public class CannonBall extends GameObject{

    private int diameter; // diameter of bubble object
    private Color color; // color of bubble object

    public CannonBall(int x, int y, int diameter, ID id, Color color) {
        super(x, y, id);
        this.color = color;
        this.diameter = diameter;
    }

    @Override
    public void tick() {
        x += velX;
        y += velY;

        if(x >= Game.WIDTH - diameter || x <= 0){
            velX *= -1;
        }
        if(y >= Game.HEIGHT - (diameter * 2) || y <= 0){
            velY *= -1;
        }
    }

    @Override
    public void render(Graphics cannonBall) {
        cannonBall.setColor(Color.black);
        cannonBall.fillOval(x - 1, y - 1, diameter + 2, diameter + 2);

        cannonBall.setColor(color);
        cannonBall.fillOval(x, y, diameter, diameter);
    }
}
