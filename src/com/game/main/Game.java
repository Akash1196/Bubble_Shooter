package com.game.main;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Game extends Canvas implements Runnable{

    public static final double WIDTH = 500, HEIGHT = 700;

    private Thread thread;
    private boolean running = false;

    private Handler handler;
    private Window window;

    private BufferStrategy bs;
    private Graphics g;

    private String title;
    private int width, height;

    /**
     * Default constructor
     */
    public Game(String title, int width, int height){
        this.title = title;
        this.width = width;
        this.height = height;

        handler = new Handler(8, 16, 3);
    }

    private void init(){
        window = new Window(title, width, height);
        window.getCanvas().addMouseListener(new MouseInput(handler));
        window.getCanvas().addMouseMotionListener(new MouseInput(handler));
    }

    /**
     * Start thread
     */
    public synchronized void start(){
        if(running)
            return;
        running = true;
        thread = new Thread(this);
        thread.start();
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
        init();

        int fps = 60;
        double timePerTick = 1000000000 / fps;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();
        long timer = 0;
        int ticks = 0;

        while(running){
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            timer += now - lastTime;
            lastTime = now;

            if(delta >= 1) {
                tick();
                render();
                ticks++;
                delta--;
            }

            if(timer >= 1000000000){
                //System.out.println("Ticks and Frames: " + ticks);
                ticks = 0;
                timer = 0;
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
        bs = window.getCanvas().getBufferStrategy();
        if(bs == null){
            window.getCanvas().createBufferStrategy(3);
            return;
        }

        g = bs.getDrawGraphics();

        // start drawing
        g.drawImage(Assets.background, 0, 0, null);

        handler.render(g);
        //g.drawImage(testImage, 0, 0, null);

        // end drawing
        bs.show();
        g.dispose(); // get rid of the copy
    }
}
