package com.game.main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Orb extends GameObject{

    private BufferedImage orb;

    private int diameter;

    public Orb(int x, int y, int diameter, BufferedImage orb, ID id) {
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
        g.drawImage(orb, x, y, this.diameter, this.diameter, null);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(this.x, this.y, diameter, diameter);
    }
}
