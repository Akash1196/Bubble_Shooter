package com.game.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

/**
 * Deals with key input for cannon rotation
 */
public class KeyInput extends KeyAdapter{

    private Handler handler;
    private LinkedList<GameObject> tempObjectList; // temp list...remove cannon ball when spacebar released
    private double tempTheta;
    private boolean cannonBallFired = false; // ensures pressing space only fires cannonball once and not repeatedly

    public KeyInput(Handler handler) {
        this.handler = handler;
        createTempObjectList();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        for(int i = 0; i < tempObjectList.size(); i++){
            GameObject temp = tempObjectList.get(i);

            if(temp.getId() == ID.Cannon){
                //key events for the cannon object
                if(key == KeyEvent.VK_RIGHT){
                    temp.setVelTheta(Math.toRadians(2));
                    //tempTheta += Math.toRadians(2);
                    //tempTheta = Game.clamp(tempTheta, -1.5, 1.5);
                }
                else if(key == KeyEvent.VK_LEFT){
                    temp.setVelTheta(Math.toRadians(-2));
                    //tempTheta += Math.toRadians(-2);
                    //tempTheta = Game.clamp(tempTheta, -1.5, 1.5);
                }
            }

            if(temp.getId() == ID.CannonBall) {
                for(int j = 0; j < tempObjectList.size(); j++){
                    GameObject temp2 = tempObjectList.get(j);
                    if(temp2.getId() == ID.Cannon){
                        tempTheta = temp2.getTheta();
                    }
                }
                if(key == KeyEvent.VK_SPACE) {
                    if(!cannonBallFired) {
                        temp.setVelY((int) (6 * Math.cos(tempTheta))); // y-component = vel * cos(angle)
                        temp.setVelX((int) (6 * Math.sin(tempTheta))); // x-component = vel * sin(angle)
                        cannonBallFired = !cannonBallFired;
                    }
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

            if(temp.getId() == ID.CannonBall) {
                if(key == KeyEvent.VK_SPACE) {
                    tempObjectList.remove(temp);
                    cannonBallFired = !cannonBallFired;
                }
            }
        }
    }

    private void createTempObjectList(){
        tempObjectList = new LinkedList<>();
        for(int i = 0; i < handler.object.size(); i++){
            tempObjectList.add(handler.object.get(i));
        }
    }

    /**
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
    }*/
}
