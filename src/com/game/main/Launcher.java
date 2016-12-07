package com.game.main;

public class Launcher {

    public static void main(String args[]){
        Game game = new Game("Bubble Shooter", (int)Game.WIDTH, (int)Game.HEIGHT);
        game.start();
    }
}
