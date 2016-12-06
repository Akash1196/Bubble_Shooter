package com.game.main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Point;

/**
 * Determines what to do when there is mouse input
 */
public class MouseInput extends MouseAdapter{

    private Handler handler;
    private int diameter;
    private Point mousePoint;

    public MouseInput(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePoint = e.getPoint();
        double rotation;
        for(int i = 0; i < handler.object.size(); i++){
            GameObject temp = handler.object.get(i);
            if(temp.getId() == ID.OrbBullet){
                diameter = temp.getDiameter();
            }
            if(temp.getId() == ID.BioCannon){
                int deltaX = mousePoint.x - Game.WIDTH/2;
                int deltaY = mousePoint.y - (Game.HEIGHT - diameter/2);
                rotation = Math.atan2(deltaX, deltaY);
                rotation = -Math.toDegrees(rotation) + 180;
                //rotate around center of cannon based on mouse location
                temp.setVelTheta(Math.toRadians(rotation));
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for(int i = 0; i < handler.object.size(); i++){
            GameObject temp = handler.object.get(i);
            if(temp.getId() == ID.OrbBullet) {
                int speed = 7;
                float dirX = e.getPoint().x - Game.WIDTH/2 + 11;
                float dirY = e.getPoint().y - (Game.HEIGHT - diameter/2);
                // normalize direction vector
                double dirLength = Math.sqrt(dirX*dirX + dirY*dirY);
                dirX /= dirLength;
                dirY /= dirLength;

                // now updated component velocities
                temp.setVelX(speed * dirX);
                temp.setVelY(speed * dirY);
            }
        }
    }
}
