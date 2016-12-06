package com.game.main;

import java.awt.image.BufferedImage;

public class Assets {

    public static BufferedImage venom, curses, flame, blood, airless, empty, bioCannon;

    public static void init(){
        venom = ImageLoader.loadImage("Orb of Venom.png");
        curses = ImageLoader.loadImage("Orb of Curses.png");
        flame = ImageLoader.loadImage("Orb of Flame.png");
        blood = ImageLoader.loadImage("Orb of Blood.png");
        airless = ImageLoader.loadImage("Airless.png");
        empty = ImageLoader.loadImage("Empty Orb.png");
        bioCannon = ImageLoader.loadImage("Biomech Dragon Cannon.png");
    }
}
