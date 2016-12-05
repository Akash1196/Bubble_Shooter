package com.game.main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Determines what to do when there is mouse input
 */
public class MouseInput extends MouseAdapter{

    private Handler handler;
    private double tempTheta;

    public MouseInput(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for(int i = 0; i < handler.object.size(); i++){
            GameObject temp = handler.object.get(i);
            if(temp.getId() == ID.Cannon){
                //rotate around center of cannon based on mouse location
                temp.setVelTheta(Math.atan2((Game.HEIGHT - 55) - e.getY(), (Game.WIDTH/2) - e.getX()) - Math.PI/2);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for(int i = 0; i < handler.object.size(); i++){
            GameObject temp = handler.object.get(i);
            if(temp.getId() == ID.Cannon){
                tempTheta = temp.getTheta();
            }
            if(temp.getId() == ID.CannonBall) {
                temp.setVelY(7 * Math.cos(tempTheta)); // y-component = vel * cos(angle)
                temp.setVelX(7 * Math.sin(tempTheta)); // x-component = vel * sin(angle)
            }
        }
    }
}
