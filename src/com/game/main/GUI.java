package com.game.main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Akash Patel
 * CS 251L
 */
public class GUI extends JFrame implements ActionListener {

    private JPanel imagePanel;
    private JPanel scorePanel;
    private JButton button;
    private JLabel label;
    private int numClicks = 0;
    private Thread thread;
    private boolean running = false;
    private Handler handler;

    //Constructor that add all the components to the window and aligns them
    public GUI() {
        handler = new Handler();

        JFrame frame = new JFrame("Bubble Shooter");
        imagePanel = new ImagePanel();
        scorePanel = new ScorePanel();
        label = new JLabel("Score: ");
        button = new JButton("New Game");

        button.addActionListener(this);

        scorePanel.setLayout(new GridBagLayout());
        GridBagConstraints lc = new GridBagConstraints();
        lc.fill = GridBagConstraints.HORIZONTAL;
        lc.gridx = 0;
        lc.gridy = 0;
        scorePanel.add(label, lc);

        GridBagConstraints bc = new GridBagConstraints();
        bc.fill = GridBagConstraints.HORIZONTAL;
        bc.gridx = 0;
        bc.gridy = 1;
        scorePanel.add(button, bc);

        //imagePanel.add(this);
        //frame.add(imagePanel);
        frame.add(scorePanel, BorderLayout.NORTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Layout Practice");
        frame.setResizable(false);
        frame.setSize(new Dimension(500, 700));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        //this.start();
        handler.buildBoard(20, 10, 3);
    }

    /**
     * Start thread
     *
    public synchronized void start(){
        thread = new Thread(this);
        thread.start();
        running = true;
    }


     * Stop thread
     *
    public synchronized void stop(){
        try{
            thread.join();
            running = false;
        }catch(Exception e){
            e.printStackTrace();
        }
    }     *
    **
     * Default game loop code
     *
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

    }     */

    private void tick(){
        handler.tick();
    }

    /**
     * This method increments the numClicks count by 1, triggers a dialog to pop up,
     * and updates the label and the dialog box with the new count each time the
     * button is pressed
     *
     * @param e the mouse click on the button
     */
    public void actionPerformed(ActionEvent e) {
        numClicks++;
        label.setText("Score: " + numClicks);

        JOptionPane.showMessageDialog(getContentPane(),
                "You have clicked " + numClicks + " times!",
                "Button clicked dialog box",
                JOptionPane.INFORMATION_MESSAGE);
    }

    //JPanel class that creates the panel for the image and paints the image
    private class ImagePanel extends JPanel {
        //sets size of image panel and color
        private ImagePanel() {
            setPreferredSize(new Dimension(500, 700));
            setBackground(Color.black);
        }

        //overrides paintComponent and makes grid
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Timer timer = new Timer(1000 / 60, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    repaint();
                }
            });
            handler.render(g);
        }
    }

    //JPanel for score
    private class ScorePanel extends JPanel {
        //defines the size and background color of the label panel
        private ScorePanel() {
            setPreferredSize(new Dimension(500, 50));
            setBackground(Color.white);
        }

        //No shapes or colors are needed so the parent method is called (no changes)
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
        }
    }

    public static void main(String args[]){
        new GUI();
    }
}