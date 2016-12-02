package com.game.main;

import java.awt.*;
import java.util.LinkedList;

/**
 * Creates the cannon ball which acts as the current bubble that
 * is shot from the cannon
 */
public class CannonBall extends GameObject{

    private Color color; // color of bubble object
    private Handler handler;

    public CannonBall(int x, int y, int diameter, ID id, Color color, Handler handler) {
        super(x, y, id);
        this.color = color;
        this.handler = handler;
        super.diameter = diameter;
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

        collision();
    }

    @Override
    public void render(Graphics cannonBall) {
        cannonBall.setColor(Color.black);
        cannonBall.fillOval(x - 1, y - 1, diameter + 2, diameter + 2);

        cannonBall.setColor(color);
        cannonBall.fillOval(x, y, diameter, diameter);
    }

    private void collision(){
        for(int i = 0; i < handler.grid.size(); i++){
            for(int j = 0; j < handler.grid.get(i).size(); j++){
                Bubble bubble = handler.grid.get(i).get(j);
                // collision detection
                if(this.getBounds().intersects(bubble.getBounds())){
                    // check top
                    if(this.getY() >= bubble.getY() + diameter/2) {
                        handler.grid.addLast(new LinkedList<>());
                        bubble = new Bubble(bubble.getX() - diameter/2, bubble.getY() + diameter,
                                diameter, ID.Bubble, this.getColor());
                        handler.grid.get(i + 1).add(bubble);
                        handler.object.add(bubble);
                        handler.object.remove(this);
                        //velY *= -1;
                    }
                    // check bottom
                    if(this.getY() + diameter/2 <= bubble.getY()) {
                        velY *= -1;
                    }
                    // check left
                    if(this.getX() + diameter/2 <= bubble.getX()) {
                        velX *= -1;
                    }
                    // check right
                    if(this.getX() >= bubble.getX() + diameter/2) {
                        velX *= -1;
                    }
                    // check top left
                    if(this.getY() >= bubble.getY() + diameter/2 && this.getX() + diameter/2 <= bubble.getX()) {
                        velY *= -1;
                        velX *= -1;
                    }
                    // check top right
                    if(this.getY() >= bubble.getY() + diameter/2 && this.getX() >= bubble.getX() + diameter/2) {
                        velY *= -1;
                        velX *= -1;
                    }
                    // check bottom left
                    if(this.getY() + diameter/2 <= bubble.getY() && this.getX() + diameter/2 <= bubble.getX()) {
                        velY *= -1;
                        velX *= -1;
                    }
                    // check bottom right
                    if(this.getY() + diameter/2 <= bubble.getY() && this.getX() >= bubble.getX() + diameter/2) {
                        velY *= -1;
                        velX *= -1;
                    }
                }
            }
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, diameter, diameter);
    }

    public Color getColor() {
        return color;
    }
}
