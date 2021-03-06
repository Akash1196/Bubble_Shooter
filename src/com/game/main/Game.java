package com.game.main;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable{

    public static final double WIDTH = 500, HEIGHT = 700;

    private Thread thread;
    private boolean running = false;

    public Handler handler;
    public MouseInput mouseInput;
    public Window window;
    public Menu menu;
    public GameOver gameover;
    private HUD hud;

    private BufferStrategy bs;
    private Graphics g;

    private String title;
    private int width, height;

    public enum STATE{
        Menu,
        Game,
        GameOver
    }

    public STATE gameState = STATE.Menu;

    /**
     * Default constructor
     */
    public Game(String title, int width, int height){
        this.title = title;
        this.width = width;
        this.height = height;

        handler = new Handler(8, 16, 5);
        mouseInput = new MouseInput(handler);

        menu = new Menu(this, handler);
        gameover = new GameOver(this, handler);
        hud = new HUD();
    }

    private void init(){
        window = new Window(title, width, height);
        window.getCanvas().addMouseListener(menu);
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
        gameOver(); // changes state depending on if game is over or not

        if(gameState == STATE.Game) {
            handler.tick();
            hud.tick();
        }
        else if(gameState == STATE.Menu){
            menu.tick();
        }
        else if(gameState == STATE.GameOver){
            gameover.tick();
        }
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
        g.drawImage(Assets.background, 0, 0, 500, 700, null);

        if(gameState == STATE.Game) {
            handler.render(g);
            hud.render(g);
        }else if(gameState == STATE.Menu){
            menu.render(g);
        }
        else if(gameState == STATE.GameOver){
            gameover.render(g);
        }

        // end drawing
        bs.show();
        g.dispose(); // get rid of the copy
    }

    private void gameOver(){
        if(gameState == STATE.Game) {
            if (HUD.SCORE >= 10 || handler.belowWindow || handler.boardEmpty()) {
                if(handler.belowWindow){
                    gameover.win = false;
                }
                gameState = Game.STATE.GameOver;
                handler.belowWindow = false;
                HUD.SCORE = 0;
                handler.numRows = 8;
                handler.numCols = 16;

                window.getCanvas().removeMouseListener(mouseInput);
                window.getCanvas().removeMouseMotionListener(mouseInput);
                window.getCanvas().addMouseListener(gameover);
            }
        }
    }
}
