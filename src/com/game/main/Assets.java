package com.game.main;

import java.awt.image.BufferedImage;

public class Assets {

    public static BufferedImage red, pink, purple, blue, orange, empty, bioCannon;
    public static BufferedImage background;
    public static BufferedImage spriteSheet;

    public static void init(){
        bioCannon = ImageLoader.loadImage("cannon.png");

        background = ImageLoader.loadImage("bg.jpg");

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


        spriteSheet = ImageLoader.loadImage(("pokeballs.png"));

        red = spriteSheet.getSubimage(2 * spriteSheet.getWidth()/5, 0, spriteSheet.getWidth()/5,
                spriteSheet.getHeight()/6);
        pink = spriteSheet.getSubimage(3 * spriteSheet.getWidth()/5, 0, spriteSheet.getWidth()/5,
                spriteSheet.getHeight()/6);
        purple = spriteSheet.getSubimage(4 * spriteSheet.getWidth()/5, 0, spriteSheet.getWidth()/5,
                spriteSheet.getHeight()/6);
        blue = spriteSheet.getSubimage(spriteSheet.getWidth()/5, 2 * spriteSheet.getHeight()/6, spriteSheet.getWidth()/5,
                spriteSheet.getHeight()/6);
        orange = spriteSheet.getSubimage(2 * spriteSheet.getWidth()/5, 3 * spriteSheet.getHeight()/6, spriteSheet.getWidth()/5,
                spriteSheet.getHeight()/6);
        empty = spriteSheet.getSubimage(spriteSheet.getWidth()/4, spriteSheet.getHeight()/2, spriteSheet.getWidth()/4,
                spriteSheet.getHeight()/2);

        red = ImageLoader.loadImage("ball1.png");
        pink = ImageLoader.loadImage("ball2.png");
        purple = ImageLoader.loadImage("ball3.png");
        blue = ImageLoader.loadImage("ball5.png");
        orange = ImageLoader.loadImage("ball6.png");
    }
}
