package com.game.main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class OrbBullet extends GameObject{

    private BufferedImage orbBullet;

    private double diameter;

    private Handler handler;

    public static double hitX, hitY, hitR, hitC, currentX, currentY; // the coordinates in pixels and row and col
    public static BufferedImage hitImage, currentImage;
    public static GameObject hitOrb, currentOrbBullet;

    public OrbBullet(double x, double y, double diameter, BufferedImage orbBullet, ID id, Handler handler) {
        super(x, y, id);
        super.image = orbBullet;
        this.diameter = diameter;
        this.orbBullet = orbBullet;
        this.handler = handler;
    }

    @Override
    public void tick() {
        x += velX;
        y += velY;

        if(x > Game.WIDTH - diameter || x < 0){
            velX *= -1;
        }
        if(y > Game.HEIGHT){
            handler.object.remove(this);
            handler.loadOrbBullet();
        }
        if(y < 0){
            velY *= - 1;
        }

        if(collision()){
            handler.processCollision();
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(orbBullet, (int)x, (int)y, (int)this.diameter, (int)this.diameter, null);
    }

    private Boolean collision(){
        for(int i = 0; i < handler.grid.size(); i++){
            for(int j = 0; j < handler.row.size(); j++) {
                Orb temp = handler.grid.get(i).get(j);
                // don't check for collisions with the "empty" place holder
                if(temp.getImage() != Assets.empty) {
                    // collision detection and information retrieval
                    if (getBounds().intersects(temp.getBounds())) {
                        hitX = temp.getX();
                        hitY = temp.getY();
                        hitR = handler.getR(hitY);
                        hitC = handler.getC(hitX);

                        currentX = this.getX();
                        currentY = this.getY();

                        currentImage = this.getImage();
                        hitImage = temp.getImage();

                        currentOrbBullet = this;
                        hitOrb = temp;

                        handler.object.remove(this);
                        handler.loadOrbBullet();

                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)this.x, (int)this.y, (int)this.diameter, (int)this.diameter);
    }
}