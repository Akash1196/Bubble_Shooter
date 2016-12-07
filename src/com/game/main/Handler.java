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

    public List<List<Orb>> grid; // List containing rows of bubbles
    public List<Orb> row; // List containing bubbles
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
    private double diameter; // diameter of bubble object

    public Handler(int numRows, int numCols, int numBubbleTypes) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.numBubbleTypes = numBubbleTypes;

        Assets.init();// load images in once and only once when game is initialized

        buildHexGrid();
        buildBioCannon();
        loadOrbBullet();
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
        for(int i = 0; i < grid.size(); i++){
            for(int j = 0; j < row.size(); j++) {
                if(grid.get(i).get(j).getImage() != Assets.empty) {
                    Orb temp = grid.get(i).get(j);

                    temp.render(g);
                }
            }
        }
    }

    public void buildHexGrid(){
        grid = new ArrayList<>();
        object = new LinkedList<>();
        this.diameter = (Game.WIDTH - 10)/numCols;

        for(int i = 0; i < numRows; i++){
            row = new ArrayList<>();

            for(int j = 0; j < numCols; j++){
                Orb orb = new Orb(getX(i,j), getY(i), diameter, getRandomOrb(), ID.Orb);
                row.add(orb);
            }
            grid.add(row);
        }

        updateGridGUI();
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

    /**
     * Used to update the board after collision all logic is called from here
     * then update is called in the tick() method of the OrbBullet class
     */
    public void processCollision(){
        Orb newOrb; // added orb based on collision
        int r = 0, c = 0; // row and column location of added orb
        // reset coordinate lists and count
        x = new ArrayList<>();
        y = new ArrayList<>();
        matchingClusterCount = 0;

        // add bottom
        if(OrbBullet.currentY <= (OrbBullet.hitY + diameter)
                && OrbBullet.currentY >= (OrbBullet.hitY + diameter/2)){
            newOrb = new Orb(OrbBullet.hitX - diameter/2, OrbBullet.hitY + diameter,
                    diameter, OrbBullet.currentImage, ID.Orb);
            r = getR(OrbBullet.hitY + diameter);
            c = getC(OrbBullet.hitX - diameter/2);
        } // add top
        else if((OrbBullet.currentY + diameter) >= (OrbBullet.hitY)
                && (OrbBullet.currentY + diameter) <= (OrbBullet.hitY + diameter/2)){
            newOrb = new Orb(OrbBullet.hitX + diameter/2, OrbBullet.hitY - diameter,
                    diameter, OrbBullet.currentImage, ID.Orb);
            r = getR(OrbBullet.hitY - diameter);
            c = getC(OrbBullet.hitX + diameter/2);
        } // add left
        else if((OrbBullet.currentX + diameter) >= OrbBullet.hitX
                && (OrbBullet.currentX + diameter) <= (OrbBullet.hitX + diameter/2)){
            newOrb = new Orb(OrbBullet.hitX - diameter, OrbBullet.hitY,
                    diameter, OrbBullet.currentImage, ID.Orb);
            r = getR(OrbBullet.hitY);
            c = getC(OrbBullet.hitX - diameter);
        } // add right
        else if(OrbBullet.currentX <= (OrbBullet.hitX + diameter)
                && OrbBullet.currentX >= (OrbBullet.hitX + diameter/2)){
            newOrb = new Orb(OrbBullet.hitX + diameter, OrbBullet.hitY,
                    diameter, OrbBullet.currentImage, ID.Orb);
            r = getR(OrbBullet.hitY);
            c = getC(OrbBullet.hitX + diameter);
        } // else statement shouldn't be reached
        else{
            newOrb = new Orb(OrbBullet.hitX + diameter, OrbBullet.hitY,
                    diameter, Assets.empty, ID.Orb);
        }

        if(r >= numRows){
            numRows = r + 1;
            grid.add(new ArrayList<>());
        }

        if(grid.get(r).isEmpty()) {
            for(int k = 0; k < numCols; k++){
                grid.get(r).add(k, new Orb(getX(r, k), getY(r), diameter, Assets.empty, ID.Orb));
            }
        }
        grid.get(r).set(c, newOrb);

        resetVisitedToFalse();
        findCluster(r, c, true);
        removeMatchingCluster();
        //findAndRemoveFloatingCluster();
        updateGridGUI();
        System.out.print("\n\nx: " + x + "\ny: " + y + "\nmatchingClusterCount: " + matchingClusterCount +
                                "\nfloatingClusterCount: " + floatingClusterCount);
    }

    public void findCluster(int r, int c, Boolean matchType){
        // check left
        if(c - 1 >= 0) {
            if (grid.get(r).get(c - 1) != null && !markVisited[r][c - 1]) { // test if target cell is not visited
                markVisited[r][c - 1] = true; // mark the current node as visited
                if(matchType){
                    if (grid.get(r).get(c - 1).getImage().equals(grid.get(r).get(c).getImage())) {
                        x.add(r);
                        y.add(c - 1);
                        matchingClusterCount++;
                        findCluster(r, c - 1, true);
                    }
                }
                else {
                    if (!grid.get(r).get(c - 1).getImage().equals(Assets.empty)
                            && !grid.get(r).get(c).getImage().equals(Assets.empty)) {
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
                    if (grid.get(r).get(c + 1).getImage().equals(grid.get(r).get(c).getImage())) {
                        x.add(r);
                        y.add(c + 1);
                        matchingClusterCount++;
                        findCluster(r, c + 1, true);
                    }
                }
                else {
                    if (!grid.get(r).get(c + 1).getImage().equals(Assets.empty)
                            && !grid.get(r).get(c).getImage().equals(Assets.empty)) {
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
                    if (grid.get(r - 1).get(c).getImage().equals(Assets.empty)
                            && !grid.get(r).get(c).getImage().equals(Assets.empty)
                            && grid.get(r - 1).get(c + 1).getImage().equals(Assets.empty)
                            && grid.get(r).get(c - 1).getImage().equals(Assets.empty)
                            && grid.get(r).get(c + 1).getImage().equals(Assets.empty)) {
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
                        if (grid.get(r - 1).get(c).getImage().equals(grid.get(r).get(c).getImage())) {
                            x.add(r - 1);
                            y.add(c);
                            matchingClusterCount++;
                            findCluster(r - 1, c, true);
                        }
                    }
                    else {
                        if (!grid.get(r - 1).get(c).getImage().equals(Assets.empty)
                                && !grid.get(r).get(c).getImage().equals(Assets.empty)) {
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
                        if (grid.get(r - 1).get(c + 1).getImage().equals(grid.get(r).get(c).getImage())) {
                            x.add(r - 1);
                            y.add(c + 1);
                            matchingClusterCount++;
                            findCluster(r - 1, c + 1, true);
                        }
                    }
                    else {
                        if (!grid.get(r - 1).get(c + 1).getImage().equals(Assets.empty)
                                && !grid.get(r).get(c).getImage().equals(Assets.empty)) {
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
                        if (grid.get(r + 1).get(c).getImage().equals(grid.get(r).get(c).getImage())) {
                            x.add(r + 1);
                            y.add(c);
                            matchingClusterCount++;
                            findCluster(r + 1, c, true);
                        }
                    }
                    else {
                        if (!grid.get(r + 1).get(c).getImage().equals(Assets.empty)
                                && !grid.get(r).get(c).getImage().equals(Assets.empty)) {
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
                        if (grid.get(r + 1).get(c + 1).getImage().equals(grid.get(r).get(c).getImage())) {
                            x.add(r + 1);
                            y.add(c + 1);
                            matchingClusterCount++;
                            findCluster(r + 1, c + 1, true);
                        }
                    }
                    else {
                        if (!grid.get(r + 1).get(c + 1).getImage().equals(Assets.empty)
                                && !grid.get(r).get(c).getImage().equals(Assets.empty)) {
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
                    if (grid.get(r - 1).get(c - 1).getImage().equals(Assets.empty)
                            && !grid.get(r).get(c).getImage().equals(Assets.empty)
                            && grid.get(r - 1).get(c).getImage().equals(Assets.empty)
                            && grid.get(r).get(c - 1).getImage().equals(Assets.empty)
                            && grid.get(r).get(c + 1).getImage().equals(Assets.empty)) {
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
                        if (grid.get(r - 1).get(c - 1).getImage().equals(grid.get(r).get(c).getImage())) {
                            x.add(r - 1);
                            y.add(c - 1);
                            matchingClusterCount++;
                            findCluster(r - 1, c - 1, true);
                        }
                    }
                    else {
                        if (!grid.get(r - 1).get(c - 1).getImage().equals(Assets.empty)
                                && !grid.get(r).get(c).getImage().equals(Assets.empty)) {
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
                        if (grid.get(r - 1).get(c).getImage().equals(grid.get(r).get(c).getImage())) {
                            x.add(r - 1);
                            y.add(c);
                            matchingClusterCount++;
                            findCluster(r - 1, c, true);
                        }
                    }
                    else {
                        if (!grid.get(r - 1).get(c).getImage().equals(Assets.empty)
                                && !grid.get(r).get(c).getImage().equals(Assets.empty)) {
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
                        if (grid.get(r + 1).get(c - 1).getImage().equals(grid.get(r).get(c).getImage())) {
                            x.add(r + 1);
                            y.add(c - 1);
                            matchingClusterCount++;
                            findCluster(r + 1, c - 1, true);
                        }
                    }
                    else {
                        if (!grid.get(r + 1).get(c - 1).getImage().equals(Assets.empty)
                                && !grid.get(r).get(c).getImage().equals(Assets.empty)) {
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
                        if (grid.get(r + 1).get(c).getImage().equals(grid.get(r).get(c).getImage())) {
                            x.add(r + 1);
                            y.add(c);
                            matchingClusterCount++;
                            findCluster(r + 1, c, true);
                        }
                    }
                    else {
                        if (!grid.get(r + 1).get(c).getImage().equals(Assets.empty)
                                && !grid.get(r).get(c).getImage().equals(Assets.empty)) {
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
                grid.get(x.get(i)).set(y.get(i), new Orb(getX(x.get(i), y.get(i)), getY(x.get(i)),
                        diameter, Assets.empty, ID.Orb));
            }
            removeEmptyRows();
        }

        updateGridGUI();
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
                grid.get(x.get(j)).set(y.get(j), new Orb(getX(x.get(j), y.get(j)), getY(x.get(j)),
                        diameter, Assets.empty, ID.Orb));
            }
            removeEmptyRows();
        }

        updateGridGUI();
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

    public void updateGridGUI(){
        System.out.print("\n\n");
        for(int i = 0; i < this.grid.size(); i++){
            for(int j = 0; j < this.row.size(); j++){
                // don't add the "empty" orb that serves as a place holder
                if(grid.get(i).get(j).getImage() != Assets.empty) {
                    //object.add(grid.get(i).get(j));
                    System.out.print("(" + getR(grid.get(i).get(j).getY()) + ", "
                    + getC(grid.get(i).get(j).getX()) + ") ");
                }else {
                    System.out.print("(EMPTY) ");
                }
            }
            System.out.print("\n");
        }
    }

    private void removeEmptyRows(){
        int count;
        // if a row is empty then remove it from the grid and update instance variable
        for(int j = 0; j < numRows; j ++){
            count = 0;
            for(int k = 0; k < numCols; k++){
                if(grid.get(j).get(k).getImage().equals(Assets.empty)){
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
        generator = new Random();
        rand = random(numBubbleTypes);

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

    public double getX(int r, int c){
        double x = c * diameter;
        if(r % 2  == 1){
            x += diameter/2;
        }
        return x;
    }

    public double getY(int r){
        return r * diameter;
    }

    public int getC(double x){
        return (int)Math.floor(x/diameter);
    }

    public int getR(double y){
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
