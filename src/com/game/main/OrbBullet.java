package com.game.main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class OrbBullet extends GameObject{

    private BufferedImage orbBullet;

    private int diameter;

    private Handler handler;

    public static int hitX, hitY, hitR, hitC, currentX, currentY; // the coordinates in pixels and row and col
    public static BufferedImage hitImage, currentImage;

    public OrbBullet(int x, int y, int diameter, BufferedImage orbBullet, ID id, Handler handler) {
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
        if(y > Game.HEIGHT - diameter || y < 0){
            velY *= - 1;
        }

        if(collision()){
            handler.update();
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(orbBullet, x, y,
                this.diameter, this.diameter, null);
    }

    private Boolean collision(){
        for(int i = 0; i < handler.object.size(); i++){
            GameObject temp = handler.object.get(i);
            // collision detection
            if(temp.getId() == ID.Orb) {
                if (this.getBounds().intersects(temp.getBounds())) {
                    this.hitX = temp.getX();
                    this.hitY = temp.getY();
                    this.hitR = handler.getR(hitY);
                    this.hitC = handler.getC(hitX, hitY);

                    this.currentX = this.getX();
                    this.currentY = this.getY();

                    this.currentImage = this.getImage();
                    this.hitImage = temp.getImage();

                    handler.object.remove(this);
                    handler.loadOrbBullet();

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(this.x, this.y, diameter, diameter);
    }
}