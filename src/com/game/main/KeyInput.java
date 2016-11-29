package com.game.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Deals with key input for cannon rotation
 */
public class KeyInput extends KeyAdapter{

    private Handler handler;
    private double tempTheta = Math.PI/2;

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
                    temp.setVelTheta(Math.toRadians(2));
                    tempTheta += Math.toRadians(2);
                    tempTheta = Game.clamp(tempTheta, -1.5, 1.5);
                }
                else if(key == KeyEvent.VK_LEFT){
                    temp.setVelTheta(Math.toRadians(-2));
                    tempTheta += Math.toRadians(-2);
                    tempTheta = Game.clamp(tempTheta, -1.5, 1.5);
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        for(int i = 0; i < handler.object.size(); i++){
            GameObject temp = handler.object.get(i);

            if(temp.getId() == ID.Cannon){
                //key events for the cannon object
                if(key == KeyEvent.VK_RIGHT){
                    temp.setVelTheta(0);
                }
                else if(key == KeyEvent.VK_LEFT){
                    temp.setVelTheta(0);
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char keyChar = e.getKeyChar();

        for(int i = 0; i < handler.object.size(); i++){
            GameObject temp = handler.object.get(i);

            if(temp.getId() == ID.CannonBall) {
                if(keyChar == ' ') {
                    System.out.println(Math.toDegrees(tempTheta));
                    temp.setVelX((int)(5 * Math.cos(tempTheta))); // x-component = vel * cos(angle)
                    temp.setVelY((int)(5 * Math.sin(tempTheta))); // y-component = vel * sin(angle)
                }
            }
        }
    }
}
