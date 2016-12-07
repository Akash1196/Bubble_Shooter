package com.game.main;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Holds inheritable characteristics of all game objects i.e. bubbles, pointer, etc.
 */
public abstract class GameObject {

    protected double x, y; // Coordinate of objects
    protected ID id; // id of object
    protected double velX, velY; // x and y velocity of object
    protected double velTheta; // angle velocity of cannon rotation
    protected double theta; // angle of rotation
    protected double diameter; // diameter of Bubble() and CannonBall()
    protected int cannonHeight, cannonWidth;
    protected BufferedImage image; // type of orb

    public GameObject(double x, double y, ID id){
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public abstract void tick();
    public abstract void render(Graphics g);
    public abstract Rectangle getBounds();

    /**
     * Setters and Getters
     */
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public double getVelX() {
        return velX;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public double getVelY() {
        return velY;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    public double getVelTheta() {
        return velTheta;
    }

    public void setVelTheta(double velTheta) {
        this.velTheta = velTheta;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public double getDiameter() {
        return diameter;
    }

    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    public int getCannonHeight() {
        return cannonHeight;
    }

    public void setCannonHeight(int cannonHeight) {
        this.cannonHeight = cannonHeight;
    }

    public int getCannonWidth() {
        return cannonWidth;
    }

    public void setCannonWidth(int cannonWidth) {
        this.cannonWidth = cannonWidth;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
