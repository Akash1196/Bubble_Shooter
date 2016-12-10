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

    List<List<Orb>> grid; // list containing rows of bubbles
    List<Orb> row; // List containing bubbles
    LinkedList<GameObject> object; // List containing any and all objects that need to be rendered
    private List<Integer> x; // x coordinate of matching bubbles
    private List<Integer> y; // y coordinate of matching bubbles
    private Boolean[][] markVisited;
    private Boolean[][] markConnectedToTop;
    private Random generator; // random number generator variable
    private int matchingClusterCount;
    public int numRows; // number of rows of bubbles
    public int numCols; // number of columns of bubbles
    private int numBubbleTypes; // Number of different colors of bubbles
    private double diameter; // diameter of bubble object
    private int currentBulletRow;
    private int getCurrentBulletCol;
    public Boolean belowWindow = false;

    public Handler(int numRows, int numCols, int numBubbleTypes) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.numBubbleTypes = numBubbleTypes;

        HUD.SCORE = 0; // new handler means new score

        Assets.init();// load images in once and only once when game is initialized
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

    /**
     * Initial GUI components
     */

    public void buildHexGrid(){
        grid = new ArrayList<>();
        object = new LinkedList<>();
        this.diameter = (Game.WIDTH - 14)/numCols;

        for(int i = 0; i < numRows; i++){
            row = new ArrayList<>();

            for(int j = 0; j < numCols; j++){
                Orb orb = new Orb(getX(i,j), getY(i), diameter, getRandomOrb(), ID.Orb);
                row.add(orb);
            }
            grid.add(row);
        }
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
        // reset coordinate lists and count
        x = new ArrayList<>();
        y = new ArrayList<>();
        matchingClusterCount = 0;

        addOrb();
        if(!belowWindow) {
            resetVisitedToFalse();
            resetConnectedToTopToFalse();
            findCluster(currentBulletRow, getCurrentBulletCol, true);
            removeMatchingCluster();
            removeFloatingCluster();
        }
    }

    /**
     * Determine where to add the ball and make necessary adjustments
     */
    private void addOrb(){
        Orb newOrb; // added orb based on collision
        int r = 0, c = 0; // row and column location of added orb

        // add bottom left
        if(OrbBullet.currentY <= (OrbBullet.hitY + diameter)
                && OrbBullet.currentY >= (OrbBullet.hitY + diameter/2)){
            if(OrbBullet.currentOrbBullet.getVelX() >= 0) {
                newOrb = new Orb(OrbBullet.hitX - diameter / 2, OrbBullet.hitY + diameter,
                        diameter, OrbBullet.currentImage, ID.Orb);
                r = getR(OrbBullet.hitY + diameter);
                c = getC(OrbBullet.hitX - diameter / 2);
            }
            else{
                newOrb = new Orb(OrbBullet.hitX + diameter / 2, OrbBullet.hitY + diameter,
                        diameter, OrbBullet.currentImage, ID.Orb);
                r = getR(OrbBullet.hitY + diameter);
                c = getC(OrbBullet.hitX + diameter / 2);
            }
        }// add top right
        else if((OrbBullet.currentY + diameter) >= (OrbBullet.hitY)
                && (OrbBullet.currentY + diameter) <= (OrbBullet.hitY + diameter/2)){
            if(OrbBullet.currentOrbBullet.getVelX() < 0) {
                newOrb = new Orb(OrbBullet.hitX + diameter / 2, OrbBullet.hitY - diameter,
                        diameter, OrbBullet.currentImage, ID.Orb);
                r = getR(OrbBullet.hitY - diameter);
                c = getC(OrbBullet.hitX + diameter / 2);
            }
            else{
                newOrb = new Orb(OrbBullet.hitX - diameter / 2, OrbBullet.hitY - diameter,
                        diameter, OrbBullet.currentImage, ID.Orb);
                r = getR(OrbBullet.hitY - diameter);
                c = getC(OrbBullet.hitX - diameter / 2);
            }
        } // else statement shouldn't be reached
        else{
            newOrb = new Orb(Game.WIDTH/2, Game.HEIGHT/2,
                    diameter, Assets.empty, ID.Orb);
        }

        if(r >= numRows){
            numRows = r + 1;
            grid.add(new ArrayList<>());
        }

        if(r + 1 == 21){
            belowWindow = true;
            numRows = 8;
            numCols = 16;
        }

        if(grid.get(r).isEmpty()) {
            for(int k = 0; k < numCols; k++){
                grid.get(r).add(k, new Orb(getX(r, k), getY(r), diameter, Assets.empty, ID.Orb));
            }
        }

        if(c < 0){
            c++;
            newOrb.setX(newOrb.getX() + diameter);
        }
        if(c > numCols - 1){
            c--;
            newOrb.setX(newOrb.getX() - diameter);
        }

        if(!grid.get(r).get(c).getImage().equals(Assets.empty)) {
            c++;
            if (!grid.get(r).get(c).getImage().equals(Assets.empty)) {
                c -= 2;
                newOrb.setX(newOrb.getX() - diameter);
                grid.get(r).set(c, newOrb);
            }
            else {
                newOrb.setX(newOrb.getX() + diameter);
                grid.get(r).set(c, newOrb);
            }
        }
        else{
            grid.get(r).set(c, newOrb);
        }

        this.currentBulletRow = r;
        this.getCurrentBulletCol = c;
    }

    /**
     * Cluster analysis methods
     */

    private void findCluster(int r, int c, Boolean matchType){
        // check left
        if(c - 1 >= 0) {
            if (!markVisited[r][c - 1]) { // test if target cell is not visited
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
                        markConnectedToTop[r][c - 1] = true;
                        findCluster(r, c - 1, false);
                    }
                }
            }
        }
        // check right
        if(c + 1 < numCols) {
            if (!markVisited[r][c + 1]) { // test if target cell is not visited
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
                        markConnectedToTop[r][c + 1] = true;
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
                        markConnectedToTop[r][c] = true;
                    }
                }
            }
            // check top left
            if(r - 1 >= 0) {
                if (!markVisited[r - 1][c]) { // test if target cell is not visited
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
                            markConnectedToTop[r - 1][c] = true;
                            findCluster(r - 1, c, false);
                        }
                    }
                }
            }
            // check top right
            if(r - 1 >= 0 && c + 1 < numCols) {
                if (!markVisited[r - 1][c + 1]) { // test if target cell is not visited
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
                            markConnectedToTop[r - 1][c + 1] = true;
                            findCluster(r - 1, c + 1, false);
                        }
                    }
                }
            }
            // check bottom left
            if(r + 1 < numRows) {
                if (!markVisited[r + 1][c]) { // test if target cell is not visited
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
                            markConnectedToTop[r + 1][c] = true;
                            findCluster(r + 1, c, false);
                        }
                    }
                }
            }
            // check bottom right
            if(r + 1 < numRows && c + 1 < numCols) {
                if (!markVisited[r + 1][c + 1]) { // test if target cell is not visited
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
                            markConnectedToTop[r + 1][c + 1] = true;
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
                        markConnectedToTop[r][c] = true;
                    }
                }
            }
            // check top left
            if(r - 1 >= 0 && c - 1 >= 0) {
                if (!markVisited[r - 1][c - 1]) { // test if target cell is not visited
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
                            markConnectedToTop[r - 1][c - 1] = true;
                            findCluster(r - 1, c - 1, false);
                        }
                    }
                }
            }
            // check top right
            if(r - 1 >= 0) {
                if (!markVisited[r - 1][c]) { // test if target cell is not visited
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
                            markConnectedToTop[r - 1][c] = true;
                            findCluster(r - 1, c, false);
                        }
                    }
                }
            }
            // check bottom left
            if(r + 1 < numRows && c - 1 >= 0) {
                if (!markVisited[r + 1][c - 1]) { // test if target cell is not visited
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
                            markConnectedToTop[r + 1][c - 1] = true;
                            findCluster(r + 1, c - 1, false);
                        }
                    }
                }
            }
            // check bottom right
            if(r + 1 < numRows) {
                if (!markVisited[r + 1][c]) { // test if target cell is not visited
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
                            markConnectedToTop[r + 1][c] = true;
                            findCluster(r + 1, c, false);
                        }
                    }
                }
            }
        }
    }

    private void removeMatchingCluster(){
        if(matchingClusterCount >= 3) {
            for (int i = 0; i < x.size(); i++) {
                grid.get(x.get(i)).set(y.get(i), new Orb(getX(x.get(i), y.get(i)), getY(x.get(i)),
                        diameter, Assets.empty, ID.Orb));

                HUD.SCORE += 50; // 50 points for ever bubble in a matching cluster
            }
        }

        resetVisitedToFalse();
    }

    private void removeFloatingCluster(){
        for(int i = 0; i < numCols; i++) {
            findCluster(0, i, false);
        }
        checkTop();
    }

    /**
     * Helper methods
     */

    private void checkTop(){
        for(int i = 0; i < numRows; i ++){
            for(int j = 0; j < numCols; j++){
                if(markConnectedToTop[i][j] == false && !grid.get(i).get(j).getImage().equals(Assets.empty)){
                    grid.get(i).set(j, new Orb(getX(i, j), getY(i),
                            diameter, Assets.empty, ID.Orb));

                    HUD.SCORE += 100; // 100 points for every bubble in a floating cluster
                }
            }
        }
    }

    public Boolean boardEmpty(){
        int count = 0;

        for(int i = 0; i < grid.size(); i++){
            for(int j = 0; j < row.size(); j++){
                if(grid.get(i).get(j).getImage().equals(Assets.empty)){
                    count++;
                }
                if(count == (numRows * numCols)){
                    return true;
                }
            }
        }

        return false;
    }

    private void resetVisitedToFalse(){
        markVisited = new Boolean[numRows][numCols];

        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numCols; j++){
                markVisited[i][j] = false;
            }
        }
    }

    private void resetConnectedToTopToFalse(){
        markConnectedToTop = new Boolean[numRows][numCols];

        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numCols; j++){
                markConnectedToTop[i][j] = false;
            }
        }
    }

    /**
     * Setters and getters
     */

    private BufferedImage getRandomOrb(){
        generator = new Random();
        int rand = random(numBubbleTypes);

        if(rand == 0){
            return Assets.red;
        }
        else if(rand == 1){
            return Assets.pink;
        }
        else if(rand == 2){
            return Assets.purple;
        }
        else if(rand == 3){
            return Assets.blue;
        }
        else if(rand == 4){
            return Assets.orange;
        }
        else{
            return null;
        }
    }

    private int random(int i)
    {
        return generator.nextInt(i);
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

    public int getNumRows() {
        return numRows;
    }
}