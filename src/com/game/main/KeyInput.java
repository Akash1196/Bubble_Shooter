package com.game.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Deals with key input for cannon rotation
 */
public class KeyInput extends KeyAdapter{

    private Handler handler;

    public KeyInput(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        for(int i = 0; i < handler.object.size(); i++){
            GameObject temp = handler.object.get(i);

            if(temp.getId() == ID.Cannon){
                //key events for the cannon object
                if(key == KeyEvent.VK_RIGHT){
                    temp.setVelX(5);
                }
                else if(key == KeyEvent.VK_LEFT){
                    temp.setVelX(-5);
                }
            }

            if(key == KeyEvent.VK_SPACE){
                if(temp.getId() == ID.CannonBall) {
                    temp.setVelX(5);
                    temp.setVelY(-5);
                }
                if(key == KeyEvent.VK_RIGHT){
                    temp.setVelX(0);
                }
                else if(key == KeyEvent.VK_LEFT){
                    temp.setVelX(0);
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        for(int i = 0; i < handler.object.size(); i++){
            GameObject temp = handler.object.get(i);

            if(temp.getId() == ID.Cannon || temp.getId() == ID.CannonBall){
                //key events for the cannon object
                if(key == KeyEvent.VK_RIGHT){
                    temp.setVelX(0);
                }
                else if(key == KeyEvent.VK_LEFT){
                    temp.setVelX(0);
                }
            }
        }
    }
}
