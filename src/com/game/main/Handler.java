package com.game.main;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

/**
 * Maintains, updates and renders all game objects
 *
 * This class is in essence the substitute for the BubbleManager.java
 * class that was created in Part 1 of the Bubble Shooter project
 */
public class Handler {

    public LinkedList<LinkedList<Bubble>> grid; // List containing rows of bubbles
    public LinkedList<Bubble> row; // List containing bubbles
    public LinkedList<GameObject> object; // List containing any and all objects that need to be rendered
    private Random generator; // Random number generator variable
    private int rand; // Number that holds random number attained from generator
    private int numRows; // Number of rows of bubbles
    private int numCols; // Number of columns of bubbles
    private int numBubbleColors; // Number of different colors of bubbles
    private int diameter; // diameter of bubble object

    public void tick(){
        // Loop through all objects
        for(int i = 0; i < object.size(); i++){
            GameObject temp = object.get(i);
            temp.tick();
        }
    }

    public void render(Graphics g){
        // Loop through all objects and render
        for(int i = 0; i < object.size(); i++){
            GameObject temp = object.get(i);

            temp.render(g);
        }
    }

    /**
     * Add the cannon
     */
    public void buildCannon(){
        GameObject cannon = new Cannon(Game.WIDTH/2 - 20, Game.HEIGHT - 100, 0, ID.Cannon);
        object.add(cannon);
    }

    /**
     * Add the cannon ball
     */
    public void loadCannonBall(){
        CannonBall cannonBall = new CannonBall(Game.WIDTH/2 - 13, Game.HEIGHT - 55,
                diameter, ID.CannonBall, getRandomColor(), this);
        object.add(cannonBall);
    }

    /**
     * Add bubbles to the grid list
     */
    public void buildHexGrid(int numRows, int numCols, int numBubbleColors){
        grid = new LinkedList<>();
        object = new LinkedList<>();
        this.numRows = numRows;
        this.numCols = numCols;
        this.numBubbleColors = numBubbleColors;
        this.diameter = Game.WIDTH/numCols;
        int x, y = 2* diameter;
        Boolean oddRow;

        for(int i = 0; i < numRows; i++){
            row = new LinkedList<>();
            if(i % 2 == 1){
                x = diameter/2;
                oddRow = true;
            }
            else{
                x = 0;
                oddRow = false;
            }

            for(int j = 0; j < numCols; j++){
                if(oddRow){
                    j = 1;
                    oddRow = !oddRow;
                }
                generator = new Random();
                rand = random(numBubbleColors);

                Bubble bubble = new Bubble(x, y, diameter, ID.Bubble, getRandomColor());
                row.add(bubble);
                object.add(bubble);

                x += diameter;
            }
            grid.add(row);
            y += diameter;
        }

    }

    /**
     * Returns a random color based on the random number generated
     */
    private Color getRandomColor(){
        if(rand == 0){
            return Color.red;
        }
        else if(rand == 1){
            return Color.green;
        }
        else if(rand == 2){
            return Color.blue;
        }
        else if(rand == 3){
            return Color.yellow;
        }
        else if(rand == 4){
            return Color.magenta;
        }
        else{
            return Color.white; // This statement should never be reached
        }
    }

    /**
     * Get a random number from 0 to 5
     */
    private int random(int i)
    {
        return generator.nextInt(i);
    }

    /**
     * Setters and getters
     */
    public void setNumBubbleColors(int numBubbleColors) {
        this.numBubbleColors = numBubbleColors;
    }
}
