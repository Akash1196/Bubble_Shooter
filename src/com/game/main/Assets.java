package com.game.main;

import java.awt.image.BufferedImage;

public class Assets {

    public static BufferedImage venom, curses, flame, blood, airless, empty, bioCannon;
    public static BufferedImage red, pink, purple, blue, orange;
    public static BufferedImage background;
    public static BufferedImage spriteSheet;

    public static void init(){
        bioCannon = ImageLoader.loadImage("Biomech Dragon Cannon.png");

        background = ImageLoader.loadImage("1.jpg");

        spriteSheet = ImageLoader.loadImage(("balls.png"));

        red = spriteSheet.getSubimage(0, 0, spriteSheet.getWidth()/4,
                spriteSheet.getHeight()/2);
        pink = spriteSheet.getSubimage(spriteSheet.getWidth()/4, 0, spriteSheet.getWidth()/4,
                spriteSheet.getHeight()/2);
        purple = spriteSheet.getSubimage(2 * (spriteSheet.getWidth()/4), 0, spriteSheet.getWidth()/4,
                spriteSheet.getHeight()/2);
        blue = spriteSheet.getSubimage(3 * (spriteSheet.getWidth()/4), 0, spriteSheet.getWidth()/4,
                spriteSheet.getHeight()/2);
        orange = spriteSheet.getSubimage(0, spriteSheet.getHeight()/2, spriteSheet.getWidth()/4,
                spriteSheet.getHeight()/2);
        empty = spriteSheet.getSubimage(spriteSheet.getWidth()/4, spriteSheet.getHeight()/2, spriteSheet.getWidth()/4,
                spriteSheet.getHeight()/2);
    }
}
