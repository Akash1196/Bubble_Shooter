package com.game.main;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Menu extends MouseAdapter{

    private Game game;
    private Handler handler;

    public Menu(Game game, Handler handler ){
        this.game = game;
        this.handler = handler;
    }

    public void mousePressed(MouseEvent e){
        int mx = e.getX();
        int my = e.getY();

        if(mouseOver(mx, my, (int)Game.WIDTH/2 - 150, (int)Game.HEIGHT/2, 300, 80)){
            game.gameState = Game.STATE.Game;

            handler.buildHexGrid();
            handler.buildBioCannon();
            handler.loadOrbBullet();
        }
    }

    public void mouseReleased(MouseEvent e){
        int mx = e.getX();
        int my = e.getY();

        if(mouseOver(mx, my, (int)Game.WIDTH/2 - 150, (int)Game.HEIGHT/2, 300, 80)){
            game.window.getCanvas().addMouseListener(new MouseInput(handler));
            game.window.getCanvas().addMouseMotionListener(new MouseInput(handler));
            game.window.getCanvas().removeMouseListener(game.menu);
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
        Font fnt = new Font("arial", 1, 100);
        Font fnt1 = new Font("arial", 1, 50);

        g.setFont(fnt);
        g.setColor(Color.black);
        g.drawString("Menu", (int)Game.WIDTH/2 - 150, (int)Game.HEIGHT/3);

        g.setFont(fnt1);
        g.setColor(Color.black);
        g.drawString("Play", (int)Game.WIDTH/2 - 70, (int)Game.HEIGHT/2 + 55);

        g.setFont(fnt1);
        g.setColor(Color.black);
        g.drawString("Quit", (int)Game.WIDTH/2 - 70, (int)Game.HEIGHT/2 + 155);

        g.setFont(fnt1);
        g.setColor(Color.black);
        g.drawString("Help", (int)Game.WIDTH/2 - 70, (int)Game.HEIGHT/2 + 255);

        // buttons
        g.setColor(Color.black);
        g.drawRect((int)Game.WIDTH/2 - 150, (int)Game.HEIGHT/2, 300, 80);

        g.setColor(Color.black);
        g.drawRect((int)Game.WIDTH/2 - 150, (int)Game.HEIGHT/2 + 100, 300, 80);

        g.setColor(Color.black);
        g.drawRect((int)Game.WIDTH/2 - 150, (int)Game.HEIGHT/2 + 200, 300, 80);
    }
}
