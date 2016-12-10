# Bubble_Shooter
Copy with GUI

Akash Patel
CS 251
12/9/16
Project: BubbleShooter

How to play:
  Two ways to win: 
    (1) Reach a score of 8000 or greater 
    (2) Clear every bubble on the board
  One way to lose:
    (1) If bubbles stack all the way to the second to last row its game over for you
    
Scoring: 
  (1) 50 Points for every bubble popped in a matching cluster
  (2) 100 points for every bubble popped in a floating cluster
  
Classes:
  (1) Launcher class solely consists of the main method which executes the game.
  (2) Game class contains game loop and it renders and and updates board using a thread
  (3) Assets class loads in all used images
  (4) BioCannon class rotates and renders the cannon
  (5) GameObject class is abstract and extended by all game objects that are rendered and/or ticked
  (6) GameOver class is called when the game over condition is met and it creates the game over screen
  (7) Handler is the heavy duty class that processes collision and builds the gui (Bubble Manager equivalent)
  (8) HUD class is simply the score that is displayed in the bottom left corner of the screen
  (9) ID is an enum class that gives each renderable object its own ID i.e. "Bubble"
  (10) ImageLoader class is called by assets to load images once and only once each game
  (11) Menu class deals with the menu screen and user input involved
  (12) MouseInput class accounts for cannon rotation via mouse movement and "bullet" launch via mouse click
  (13) Orb class renders the bubble image
  (14) OrbBullet class is renders and detects collision pertaining to "bullet" movement
  (15) Window class creates a JFrame and canvas on top of the JFrame

Algorithm details:
  This game launches a bubble via mouse click then processes the collision. Depending on the balls x direction during 
  collision and the bubble that was hit a new bubble is added to the board at the nearest location. The row and column of that 
  added bubble is run through the findCluster method which recursively checks every bubble connected to the added bubble and 
  determines if there is a matching cluster greater than 3. If so, remove all matching bubbles. Next run the findCluster method 
  again but this time don't look for matching bubbles (disabled via boolean parameter) and run the method for every bubble in
  the top row. This will make sure to find any and all clusters connected to the top. Mark the bubbles connected to the top in a
  in a boolean matrix as "true". Loop through the boolean matrix and remove and false entries from the board becasue they aren't 
  connected to the top. The score is incremented by 50 every time a matching bubble is removed and by 100 every time a floating
  bubble is removed. If score >= 8000 || no more bubble left on board you win. If bubbles stack to the second to last row of the 
  window you lose. 
  
Bugs and the future:
  My collision detection is still subpar. On occasion due to the bounds of the bubbles being rectangle the collision detected and 
  addition of the ball looks a bit off. The difference is only giver or take the bubble's diameter/2, however, a but it is nonetheless.
  
  I plan to improve collision detection by implementing some sort of algorithm that can predict the path of the ball and add it to the
  proper position upon collision. I also plan to implement sound for cannon being fired, bubbles popping, game over situations and 
  just some background music. I also plan to add some animations to bubbles being popped and cannon being fired. Also a few animations
  on the Menu screen.
  
  
  
    
