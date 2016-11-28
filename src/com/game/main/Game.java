package com.game.main;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable{

    public static final int WIDTH = 500, HEIGHT = 700;

    private Thread thread;
    private boolean running = false;

    private Handler handler;

    /**
     * Default constructor
     */
    public Game(){
        handler = new Handler();
        this.addKeyListener(new KeyInput(handler));

        new Window(WIDTH, HEIGHT, "Bubble Shooter", this);

        handler.buildHexGrid(10, 20, 3);
        handler.buildCannon();
        handler.loadCannonBall();
    }

    /**
     * Start thread
     */
    public synchronized void start(){
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    /**
     * Stop thread
     */
    public synchronized void stop(){
        try{
            thread.join();
            running = false;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Default game loop code
     */
    public void run(){
        this.requestFocus();// don't need to click on screen to get keyboard control :D
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(running){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1){
                tick();
                delta--;
            }
            if(running)
                render();
            frames++;

            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                //System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
        stop();

    }

    private void tick(){
        handler.tick();
    }

    /**
     * Renders all objects in game
     */
    private void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        handler.render(g);

        g.dispose();
        bs.show();
    }

    public static int clamp(int val, int min, int max) {
        if(val >= max){
            return val = max;
        }
        else if(val <= min){
            return val = min;
        }
        else{
            return val;
        }
    }

    public static void main(String args[]){
        new Game();
    }
}
