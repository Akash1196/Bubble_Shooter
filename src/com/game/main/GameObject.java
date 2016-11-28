package com.game.main;

import java.awt.*;

/**
 * Holds inheritable characteristics of all game objects i.e. bubbles, pointer, etc.
 */
public abstract class GameObject {

    protected int x, y; // Coordinate of objects
    protected ID id; // id of object
    protected int velX, velY; // x and y velocity of object

    public GameObject(int x, int y, ID id){
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public abstract void tick();
    public abstract void render(Graphics g);

    /**
     * Setters and Getters
     */
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public int getVelX() {
        return velX;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public int getVelY() {
        return velY;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }
}
