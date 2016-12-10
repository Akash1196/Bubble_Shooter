package com.game.main;

import java.awt.*;

public class HUD {

    public static int SCORE = 0;

    public void tick(){

    }

    public void render(Graphics g){
        Font fnt = new Font("Arial", 1, 30);

        g.setFont(fnt);
        g.setColor(Color.white);
        g.drawString("Score: " + SCORE, 0, (int)Game.HEIGHT);
    }
}
