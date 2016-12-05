package com.game.main;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Creates the Cannon object that inherits default object characteristics from GameObject class
 * and adds Bubble exclusive features
 */
public class Cannon extends GameObject {

    public Cannon(int x, int y, double theta, ID id) {
        super(x, y, id);
        super.theta = theta;
    }

    @Override
    public void tick(){
        theta = velTheta;
        //theta = Game.clamp(theta, -1.4, 1.4); // can't shoot bubble downward or completely horizontal!
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        AffineTransform old = g2d.getTransform();
        g2d.rotate(theta, Game.WIDTH/2, Game.HEIGHT - 55);

        //draw guiding dashed line
        g2d.setColor(Color.black);
        Stroke dashedLine = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0, new float[]{9}, 0);
        g2d.setStroke(dashedLine);
        g2d.drawLine(Game.WIDTH/2, Game.HEIGHT - 55, Game.WIDTH/2, 0);

        /**draw border
        g2d.fillOval(x - 15, y + 19, 72, 72);
        g2d.fillRect(x - 1, y - 1, 42, 82);

        //fill
        g2d.setColor(Color.cyan);
        g2d.fillOval(x - 14, y + 20, 70, 70);
        g2d.fillRect(x, y, 40, 80);*/
        g2d.setTransform(old);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(this.x, this.y, 40, 80);
    }
}
