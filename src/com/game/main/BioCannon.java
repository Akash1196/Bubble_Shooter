package com.game.main;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class BioCannon extends GameObject{

    private BufferedImage bioCannon;
    public static int height, width;

    public BioCannon(double x, double y, BufferedImage bioCannon, double theta, ID id) {
        super(x, y, id);
        this.bioCannon = bioCannon;
        this.height = bioCannon.getHeight();
        this.width = bioCannon.getWidth();
        super.cannonHeight =  bioCannon.getHeight();
        super.cannonHeight =  bioCannon.getWidth();
        super.theta = theta;
    }

    @Override
    public void tick() {
        theta = velTheta;
        //theta = Game.clamp(theta,-1.4, 1.4);
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D)g.create();
        AffineTransform old = g2d.getTransform();
        g2d.rotate(theta, Game.WIDTH/2 - diameter/2, Game.HEIGHT - diameter/2);

        //draw guiding dashed line
        g2d.setColor(Color.white);
        Stroke dashedLine = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0, new float[]{9}, 0);
        g2d.setStroke(dashedLine);
        g2d.drawLine((int)(Game.WIDTH/2 - diameter/2), (int)(Game.HEIGHT - diameter/2),
                (int)Game.WIDTH/2, 0);

        // draw cannon
        int width = bioCannon.getWidth()/2;
        int height = bioCannon.getHeight()/2;
        g2d.drawImage(bioCannon, (int)(x - width/2),
                (int)(y - height), width, height, null);

        g2d.setTransform(old);
    }

    @Override
    public Rectangle getBounds() {
        return null;
    }
}
