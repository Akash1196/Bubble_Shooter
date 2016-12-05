package com.game.main;

import java.awt.*;

/**
 * Creates the cannon ball which acts as the current bubble that
 * is shot from the cannon
 */
public class CannonBall extends GameObject{

    private Color color; // color of bubble object
    private Handler handler;
    public static GameObject hitBubble;
    public static GameObject currentCannonBall;

    public CannonBall(int x, int y, int diameter, ID id, Color color, Handler handler) {
        super(x, y, id);
        this.color = color;
        super.color = color;
        this.handler = handler;
        super.diameter = diameter;
    }

    @Override
    public void tick(){
        x += velX;
        y += velY;

        if(x >= Game.WIDTH - diameter || x <= 0){
            velX *= -1;
        }
        if(y >= Game.HEIGHT - (diameter * 2) || y <= 0){
            velY *= -1;
        }

        if(collision()){
            handler.addBubble();
        }
    }

    @Override
    public void render(Graphics cannonBall) {
        cannonBall.setColor(Color.black);
        cannonBall.fillOval(x - 1, y - 1, diameter + 2, diameter + 2);

        cannonBall.setColor(color);
        cannonBall.fillOval(x, y, diameter, diameter);
    }

    private Boolean collision(){
        for(int i = 0; i < handler.object.size(); i++){
            GameObject temp = handler.object.get(i);
            // collision detection
            if(temp.getId() == ID.Bubble) {
                if (this.getBounds().intersects(temp.getBounds())) {
                    hitBubble = temp;
                    currentCannonBall = this;
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

    public Color getColor() {
        return color;
    }
}
