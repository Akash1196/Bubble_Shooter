package com.game.main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Determines what to do when there is mouse input
 */
public class MouseInput extends MouseAdapter{

    private Handler handler;
    public double mouseX, mouseY;

    public MouseInput(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for(int i = 0; i < handler.object.size(); i++){
            GameObject temp = handler.object.get(i);
            if(temp.getId() == ID.Cannon){
                int centerX = 20;
                int centerY = 40;
                //temp.setTheta(Math.atan2(centerY - e.getY(), centerX - e.getX()) - Math.PI / 2);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for(int i = 0; i < handler.object.size(); i++){
            GameObject temp = handler.object.get(i);
            if(temp.getId() == ID.CannonBall){
                temp.setVelX(5);
                temp.setVelY(-5);
            }
        }
    }
}
