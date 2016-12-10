package com.game.main;

import java.awt.image.BufferedImage;

public class Assets {

    public static BufferedImage red, pink, purple, blue, orange, empty, bioCannon;
    public static BufferedImage background;
    public static BufferedImage spriteSheet;

    public static void init(){
        bioCannon = ImageLoader.loadImage("cannon.png");

        background = ImageLoader.loadImage("bg.jpg");

        red = ImageLoader.loadImage("ball1.png");
        pink = ImageLoader.loadImage("ball2.png");
        purple = ImageLoader.loadImage("ball3.png");
        blue = ImageLoader.loadImage("ball5.png");
        orange = ImageLoader.loadImage("ball6.png");
        empty = ImageLoader.loadImage("bg.jpg"); // used as place holder
    }
}
