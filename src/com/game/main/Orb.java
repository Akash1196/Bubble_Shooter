package com.game.main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Orb extends GameObject{

    private BufferedImage orb;

    private double diameter;

    public Orb(double x, double y, double diameter, BufferedImage orb, ID id) {
        super(x, y, id);
        super.image = orb;
        this.diameter = diameter;
        this.orb = orb;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        g.drawImage(orb, (int)x, (int)y, (int)this.diameter, (int)this.diameter, null);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)this.x, (int)this.y, (int)this.diameter, (int)this.diameter);
    }
}
