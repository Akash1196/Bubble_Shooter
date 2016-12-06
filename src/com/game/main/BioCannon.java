package com.game.main;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class BioCannon extends GameObject{

    private BufferedImage bioCannon;
    public static int height, width;

    public BioCannon(int x, int y, BufferedImage bioCannon, double theta, ID id) {
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
        g2d.drawLine(Game.WIDTH/2 - diameter/2, Game.HEIGHT - diameter/2,
                Game.WIDTH/2, Game.HEIGHT/2 + 50);

        // draw cannon
        g2d.drawImage(bioCannon, (x - bioCannon.getWidth()/2) + 11, y - bioCannon.getHeight(), null);

        g2d.setTransform(old);
    }

    @Override
    public Rectangle getBounds() {
        return null;
    }
}
