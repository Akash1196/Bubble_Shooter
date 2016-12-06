package com.game.main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * Maintains, updates and renders all game objects
 *
 * This class is in essence the substitute for the BubbleManager.java
 * class that was created in Part 1 of the Bubble Shooter project
 */
public class Handler {

    public List<List<String>> grid; // List containing rows of bubbles
    public List<String> row; // List containing bubbles
    private List<Integer> x;
    private List<Integer> y;
    public LinkedList<GameObject> object; // List containing any and all objects that need to be rendered
    private Boolean[][] markVisited;
    private Random generator; // Random number generator variable
    private int matchingClusterCount;
    private int floatingClusterCount;
    private int rand; // Number that holds random number attained from generator
    private int numRows; // Number of rows of bubbles
    private int numCols; // Number of columns of bubbles
    private int numBubbleTypes; // Number of different colors of bubbles
    private int diameter; // diameter of bubble object

    public Handler(int numRows, int numCols, int numBubbleTypes) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.numBubbleTypes = numBubbleTypes;
        Assets.init();
    }

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

    public void initializeGraphics(){
        grid = new LinkedList<>();
        object = new LinkedList<>();
        this.diameter = Game.WIDTH/numCols;
        int x, y = 0;
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
                rand = random(numBubbleTypes);

                Orb orb = new Orb(x, y, diameter, getRandomOrb(), ID.Orb);
                row.add(getBubbleType() + " ");
                object.add(orb);

                x += diameter;
            }
            grid.add(row);
            y += diameter;
        }

        buildBioCannon();
        loadOrbBullet();
    }

    /**
     * Used to update the board after collision all logic is called from here
     * then update is called in the tick() method of the OrbBullet class
     */
    public void update(){
        rand = random(numBubbleTypes);

        // determine where orb should be added based on collision
        if(OrbBullet.currentY <= OrbBullet.hitY + diameter && OrbBullet.currentX + diameter >= OrbBullet.hitX){
            Orb newOrb = new Orb(OrbBullet.hitX - diameter/2, OrbBullet.hitY + diameter,
                    diameter, OrbBullet.currentImage, ID.Orb);
            object.add(newOrb);
            /**
            if(getR(newOrb.getY()) > numRows){
                numRows = getR(newOrb.getY()) + 1;
                grid.add(new ArrayList<>());
            }
            if(grid.get(getR(newOrb.getY())).isEmpty()) {
                for(int k = 0; k < numCols; k++){
                    grid.get(getR(newOrb.getY())).add(k, "  ");
                }
            }
            grid.get(getR(newOrb.getY())).set(getC(newOrb.getX(), newOrb.getY()), getBubbleType() + " ");
             */
        }
        else if(OrbBullet.currentY <= OrbBullet.hitY + diameter && OrbBullet.currentX <= OrbBullet.hitX + diameter) {
            Orb newOrb = new Orb(OrbBullet.hitX + diameter/2, OrbBullet.hitY + diameter,
                    diameter, OrbBullet.currentImage, ID.Orb);
            object.add(newOrb);
        }


        /**
        if(r >= numRows){
            numRows = r + 1;
            grid.add(new ArrayList<>());
        }

        if(grid.get(r).isEmpty()) {
            for(int k = 0; k < numCols; k++){
                grid.get(r).add(k, "  ");
            }
        }
        grid.get(r).set(c, getBubbleType() + " ");
         */
    }

    public void buildBioCannon(){
        BioCannon bioCannon = new BioCannon(Game.WIDTH/2, Game.HEIGHT, Assets.bioCannon,
                0, ID.BioCannon);
        object.add(bioCannon);
    }

    public void loadOrbBullet(){
        OrbBullet orbBullet = new OrbBullet(Game.WIDTH/2 - diameter/2,
                Game.HEIGHT - diameter, diameter, getRandomOrb(), ID.OrbBullet, this);
        object.add(orbBullet);
    }

    public void findCluster(int r, int c, Boolean matchType){
        // check left
        if(c - 1 >= 0) {
            if (grid.get(r).get(c - 1) != null && !markVisited[r][c - 1]) { // test if target cell is not visited
                markVisited[r][c - 1] = true; // mark the current node as visited
                if(matchType){
                    if (grid.get(r).get(c - 1).equals(grid.get(r).get(c))) {
                        x.add(r);
                        y.add(c - 1);
                        matchingClusterCount++;
                        findCluster(r, c - 1, true);
                    }
                }
                else {
                    if (!grid.get(r).get(c - 1).equals("  ") && !grid.get(r).get(c).equals("  ")) {
                        x.add(r);
                        y.add(c - 1);
                        findCluster(r, c - 1, false);
                    }
                }
            }
        }
        // check right
        if(c + 1 < numCols) {
            if (grid.get(r).get(c + 1) != null && !markVisited[r][c + 1]) { // test if target cell is not visited
                markVisited[r][c + 1] = true; // mark the current node as visited
                if(matchType){
                    if (grid.get(r).get(c + 1).equals(grid.get(r).get(c))) {
                        x.add(r);
                        y.add(c + 1);
                        matchingClusterCount++;
                        findCluster(r, c + 1, true);
                    }
                }
                else {
                    if (!grid.get(r).get(c + 1).equals("  ") && !grid.get(r).get(c).equals("  ")) {
                        x.add(r);
                        y.add(c + 1);
                        findCluster(r, c + 1, false);
                    }
                }
            }
        }
        // check if row is indented or not
        if(r % 2 == 1) {
            // check lone bubble
            if(!matchType) {
                if (r - 1 >= 0 && c + 1 < numCols && c - 1 >= 0) {
                    if (grid.get(r - 1).get(c).equals("  ") && !grid.get(r).get(c).equals("  ") && grid.get(r - 1).get(c + 1).equals("  ")
                            && grid.get(r).get(c - 1).equals("  ") && grid.get(r).get(c + 1).equals("  ")) {
                        x.add(r);
                        y.add(c);
                    }
                }
            }
            // check top left
            if(r - 1 >= 0) {
                if (grid.get(r - 1).get(c) != null && !markVisited[r - 1][c]) { // test if target cell is not visited
                    markVisited[r - 1][c] = true; // mark the current node as visited
                    if(matchType){
                        if (grid.get(r - 1).get(c).equals(grid.get(r).get(c))) {
                            x.add(r - 1);
                            y.add(c);
                            matchingClusterCount++;
                            findCluster(r - 1, c, true);
                        }
                    }
                    else {
                        if (!grid.get(r - 1).get(c).equals("  ") && !grid.get(r).get(c).equals("  ")) {
                            x.add(r - 1);
                            y.add(c);
                            findCluster(r - 1, c, false);
                        }
                    }
                }
            }
            // check top right
            if(r - 1 >= 0 && c + 1 < numCols) {
                if (grid.get(r - 1).get(c + 1) != null && !markVisited[r - 1][c + 1]) { // test if target cell is not visited
                    markVisited[r - 1][c + 1] = true; // mark the current node as visited
                    if(matchType){
                        if (grid.get(r - 1).get(c + 1).equals(grid.get(r).get(c))) {
                            x.add(r - 1);
                            y.add(c + 1);
                            matchingClusterCount++;
                            findCluster(r - 1, c + 1, true);
                        }
                    }
                    else {
                        if (!grid.get(r - 1).get(c + 1).equals("  ") && !grid.get(r).get(c).equals("  ")) {
                            x.add(r - 1);
                            y.add(c + 1);
                            findCluster(r - 1, c + 1, false);
                        }
                    }
                }
            }
            // check bottom left
            if(r + 1 < numRows) {
                if (grid.get(r + 1).get(c) != null && !markVisited[r + 1][c]) { // test if target cell is not visited
                    markVisited[r + 1][c] = true; // mark the current node as visited
                    if(matchType){
                        if (grid.get(r + 1).get(c).equals(grid.get(r).get(c))) {
                            x.add(r + 1);
                            y.add(c);
                            matchingClusterCount++;
                            findCluster(r + 1, c, true);
                        }
                    }
                    else {
                        if (!grid.get(r + 1).get(c).equals("  ") && !grid.get(r).get(c).equals("  ")) {
                            x.add(r + 1);
                            y.add(c);
                            findCluster(r + 1, c, false);
                        }
                    }
                }
            }
            // check bottom right
            if(r + 1 < numRows && c + 1 < numCols) {
                if (grid.get(r + 1).get(c + 1) != null && !markVisited[r + 1][c + 1]) { // test if target cell is not visited
                    markVisited[r + 1][c + 1] = true; // mark the current node as visited
                    if(matchType){
                        if (grid.get(r + 1).get(c + 1).equals(grid.get(r).get(c))) {
                            x.add(r + 1);
                            y.add(c + 1);
                            matchingClusterCount++;
                            findCluster(r + 1, c + 1, true);
                        }
                    }
                    else {
                        if (!grid.get(r + 1).get(c + 1).equals("  ") && !grid.get(r).get(c).equals("  ")) {
                            x.add(r + 1);
                            y.add(c + 1);
                            findCluster(r + 1, c + 1, false);
                        }
                    }
                }
            }
        }
        else{
            // check lone bubble
            if(!matchType) {
                if (r - 1 >= 0 && c + 1 < numCols && c - 1 >= 0) {
                    if (grid.get(r - 1).get(c - 1).equals("  ") && !grid.get(r).get(c).equals("  ") && grid.get(r - 1).get(c).equals("  ")
                            && grid.get(r).get(c - 1).equals("  ") && grid.get(r).get(c + 1).equals("  ")) {
                        x.add(r);
                        y.add(c);
                    }
                }
            }
            // check top left
            if(r - 1 >= 0 && c - 1 >= 0) {
                if (grid.get(r - 1).get(c - 1) != null && !markVisited[r - 1][c - 1]) { // test if target cell is not visited
                    markVisited[r - 1][c - 1] = true; // mark the current node as visited
                    if(matchType){
                        if (grid.get(r - 1).get(c - 1).equals(grid.get(r).get(c))) {
                            x.add(r - 1);
                            y.add(c - 1);
                            matchingClusterCount++;
                            findCluster(r - 1, c - 1, true);
                        }
                    }
                    else {
                        if (!grid.get(r - 1).get(c - 1).equals("  ") && !grid.get(r).get(c).equals("  ")) {
                            x.add(r - 1);
                            y.add(c - 1);
                            findCluster(r - 1, c - 1, false);
                        }
                    }
                }
            }
            // check top right
            if(r - 1 >= 0) {
                if (grid.get(r - 1).get(c) != null && !markVisited[r - 1][c]) { // test if target cell is not visited
                    markVisited[r - 1][c] = true; // mark the current node as visited
                    if(matchType){
                        if (grid.get(r - 1).get(c).equals(grid.get(r).get(c))) {
                            x.add(r - 1);
                            y.add(c);
                            matchingClusterCount++;
                            findCluster(r - 1, c, true);
                        }
                    }
                    else {
                        if (!grid.get(r - 1).get(c).equals("  ") && !grid.get(r).get(c).equals("  ")) {
                            x.add(r - 1);
                            y.add(c);
                            findCluster(r - 1, c, false);
                        }
                    }
                }
            }
            // check bottom left
            if(r + 1 < numRows && c - 1 >= 0) {
                if (grid.get(r + 1).get(c - 1) != null && !markVisited[r + 1][c - 1]) { // test if target cell is not visited
                    markVisited[r + 1][c - 1] = true; // mark the current node as visited
                    if(matchType){
                        if (grid.get(r + 1).get(c - 1).equals(grid.get(r).get(c))) {
                            x.add(r + 1);
                            y.add(c - 1);
                            matchingClusterCount++;
                            findCluster(r + 1, c - 1, true);
                        }
                    }
                    else {
                        if (!grid.get(r + 1).get(c - 1).equals("  ") && !grid.get(r).get(c).equals("  ")) {
                            x.add(r + 1);
                            y.add(c - 1);
                            findCluster(r + 1, c - 1, false);
                        }
                    }
                }
            }
            // check bottom right
            if(r + 1 < numRows) {
                if (grid.get(r + 1).get(c) != null && !markVisited[r + 1][c]) { // test if target cell is not visited
                    markVisited[r + 1][c] = true; // mark the current node as visited
                    if(matchType){
                        if (grid.get(r + 1).get(c).equals(grid.get(r).get(c))) {
                            x.add(r + 1);
                            y.add(c);
                            matchingClusterCount++;
                            findCluster(r + 1, c, true);
                        }
                    }
                    else {
                        if (!grid.get(r + 1).get(c).equals("  ") && !grid.get(r).get(c).equals("  ")) {
                            x.add(r + 1);
                            y.add(c);
                            findCluster(r + 1, c, false);
                        }
                    }
                }
            }
        }
    }

    public void removeMatchingCluster(){
        if(matchingClusterCount >= 3) {
            for (int i = 0; i < x.size(); i++) {
                grid.get(x.get(i)).set(y.get(i), "  ");
            }
            removeEmptyRows();
        }
    }

    public void removeFloatingCluster(){
        Boolean floating = true;
        floatingClusterCount = 0;

        for (int i = 0; i < x.size(); i++) {
            if (x.get(i) == 0) {
                floating = false;
            }
        }

        if (floating) {
            for (int j = 0; j < x.size(); j++) {
                floatingClusterCount++;
                grid.get(x.get(j)).set(y.get(j), "  ");
            }
            removeEmptyRows();
        }
    }

    public void findAndRemoveFloatingCluster(){
        int clusterCount = 0;

        for(int i = 0; i < numCols; i++) {
            findCluster(numRows - 1, i, false);
            removeFloatingCluster();
            clusterCount += floatingClusterCount;
        }
        for(int j = 0; j < numRows; j++) {
            findCluster(j, 0, false);
            removeFloatingCluster();
            clusterCount += floatingClusterCount;
        }

        floatingClusterCount = clusterCount;
    }

    private void removeEmptyRows(){
        int count;

        // if a row is empty then remove it from the grid and update instance variable
        for(int j = 0; j < numRows; j ++){
            count = 0;
            for(int k = 0; k < numCols; k++){
                if(grid.get(j).get(k).equals("  ")){
                    count++;
                    if(count == numCols){
                        grid.remove(j);
                        numRows--;
                        break;
                    }
                }
            }
        }
    }

    private void resetVisitedToFalse(){
        markVisited = new Boolean[numRows][numCols];

        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numCols; j++){
                markVisited[i][j] = false;
            }
        }
    }

    private BufferedImage getRandomOrb(){
        if(rand == 0){
            return Assets.airless;
        }
        else if(rand == 1){
            return Assets.curses;
        }
        else if(rand == 2){
            return Assets.flame;
        }
        else if(rand == 3){
            return Assets.venom;
        }
        else if(rand == 4){
            return Assets.blood;
        }
        else{
            return null;
        }
    }

    private String getBubbleType(){
        if(rand == 0){
            return "+";
        }
        else if(rand == 1){
            return ".";
        }
        else if(rand == 2){
            return "*";
        }
        else if(rand == 3){
            return "@";
        }
        else if(rand == 4){
            return "$";
        }
        else{
            return " "; // This statement should never be reached
        }
    }

    public int getX(int r, int c){
        int x = c * diameter;
        if(r % 2  == 1){
            x += diameter/2;
        }
        return x;
    }

    public int getY(int r){
        return r * diameter;
    }

    public int getC(int x, int y){
        int col = (int)Math.floor(x/diameter);
        int row = getR(y);
        if(row % 2 == 1){
            col++;
        }
        return col;
    }

    public int getR(int y){
        return (int)Math.floor(y/diameter);
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
}
