package com.game.main;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameOver extends MouseAdapter{

    private Game game;
    private Handler handler;
    public Boolean win = true;

    public GameOver(Game game, Handler handler ){
        this.game = game;
        this.handler = handler;
    }

    public void mouseClicked(MouseEvent e){
        int mx = e.getX();
        int my = e.getY();

        if(mouseOver(mx, my, (int)Game.WIDTH/2 - 150, (int)Game.HEIGHT/2, 300, 80)){
            game.gameState = Game.STATE.Menu;

            game.window.getCanvas().removeMouseListener(game.gameover);
            game.window.getCanvas().addMouseListener(game.menu);
        }
    }

    private Boolean mouseOver(int mx, int my, int x, int y, int width, int height){
        if(mx > x && mx < x + width){
            if(my > y && my < y + height){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    public void tick(){

    }

    public void render(Graphics g){
        Font fnt = new Font("Arial", 1, 80);
        Font fnt1 = new Font("Arial", 1, 35);

        if(win) {
            g.setFont(fnt);
            g.setColor(Color.black);
            g.drawString("You Win!", (int) Game.WIDTH / 2 - 180, (int) Game.HEIGHT / 3);
        }
        else {
            g.setFont(fnt);
            g.setColor(Color.black);
            g.drawString("You Lose!", (int) Game.WIDTH / 2 - 190, (int) Game.HEIGHT / 3);
        }

        g.setFont(fnt1);
        g.setColor(Color.black);
        g.drawString("Return to Menu", (int)Game.WIDTH/2 - 130, (int)Game.HEIGHT/2 + 50);

        // buttons
        g.setColor(Color.black);
        g.drawRect((int)Game.WIDTH/2 - 150, (int)Game.HEIGHT/2, 300, 80);
    }
}
