package src;// the idea here is i am going to have PacMan inherit in JPanel. basically i am going to have this pacman class inherit the JPanel so that this class is basically a version of JPanel


import java.awt.*;  // AWT (Abstract Window Toolkit) is a part of Java's GUI toolkit that allows creating graphical user interfaces.The * means all classes from java.awt are imported. common classes are Frame(creates a window) , Button( creates a clickable window) , Label(displays text) , TextField ( accepts user input)
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import src.SoundManager;
import javax.swing.*;
import java.util.Iterator;





public class PacMan extends JPanel implements ActionListener , KeyListener {   // i have written extends JPanel bcz this PacMan class inherits JPanel class
    private SoundManager soundManager = new SoundManager();


    // variable to debug the map
    int debugMapLevel = 1;   // if i found a problem in map and i want to go to that specific map then i only need to change here. nothing to be done in the else code



    // variables for the reverse control
    boolean controlsReversed = false;
    boolean pacmanIsOnReverse = false;


    // variables for lava tiles toggle
    boolean lavaEnabled = true;
    boolean pacmanOnLavaButton = false;
    boolean ghostOnLavaButton = false;

    //  For controlling per-map ghost behavior
    boolean ghostLavaKillTemporary = false;
    boolean ghostLavaKillPermanent = false;




    // variables for the toggle button gate
    boolean gate1Open = true;
    boolean gate2Open = true;
    boolean pacmanOnButton1 = false;
    boolean pacmanOnButton2 = false;
    boolean ghostOnButton1 = false;
    boolean ghostOnButton2 = false;


    // variables for spider nets
    boolean isInSpiderNet = false;

    // variable for bushes
    boolean isInBushes = false;

    // variables for sacred zone
    boolean isInSacredZone = false;


    // variable for slime puddle
    boolean isInSlimePuddle= false;


    // variables for electric shocks
// long pacmanStunEndTime = 0;
    Set<Block> stunnedGhosts = new HashSet<>();
    Map<Block, Long> ghostStunEndTimes = new HashMap<>();
    Set<Block> stunnedPacman = new HashSet<>();
    Map<Block, Long> pacmanStunEndTimes = new HashMap<>();



    // variables for teleporting
    private boolean isTeleporting = false;
    private long lastTeleportTime = 0;
    private static final long TELEPORT_COOLDOWN = 500; // Cooldown period in milliseconds (adjust as needed)


    // WALL COLLISON
    boolean hitWall = false;



    // to specify the position of ghost , food , wall and pacmn
    class Block{
        int x; // x position
        int y; // y positon
        int width;
        int height;
        Image image ;
        boolean isSpeedBoosted = false;  // Flag to track speed boost status
        long boostStartTime = 0;  // Time when the speed boost started
        int phaseTilesRemaining = 0; // Used for phantom wall-passing


        // declaring some properties to make our pacman movable
        char direction = 'U';  // U D L R  ( it can be anything)
        int velocityX = 0;  // 0 means we are not moving therefore here we are not moving along rows
        int velocityY = 0;  // here we are not moving along columns
        // note: we will use the same member variable for our ghost also bcz since we are in the same class Block and initially we used the same class for walls
        // we just want to pack all in the same class just to make things easier
        // now when i press any key i want to update te direction so i create a function in this class updateDirection



        // now as we start the game our ghost , pacman will be moving around so we need to store the original x and y starting position bcz as the game begins the x and y positions will be changing
        int startX;
        int startY;

        // creating a constructor ( since it is a constructor so name should be same as class name)

        Block( Image image , int x , int y , int width , int height)
        {

            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX =x;
            this.startY = y;

            // we had used this to differentiate between the paramters and the variables
            // this.image means the variable image declared in class BLOCK and = image measn the paramter which is being passed in this constructor

        }


        // when i presss a key i will call this function which first updates the direction and then calls the another function to update the velocity
        void updateDirection(char direction){  // INPUTIING A PARAMTER DIRECTION

            // storing the previous direction;
            char prevDirection = this.direction;
            this.direction = direction;  // now we had created a variable direction in the class Block so this.direction refers to that variable i.e we are talking here about the variable declared in the class Block not the one passed by you. on the right side of equal to we have direction this is the one which is being passed in the form of paramter
            updateVelocity();  // now we are calling this function

            // now i will iterate through all the walls. and everytime i update the direction i need to make sure that pacman is able to change direction without crashing into the wall
            this.x += this.velocityX;
            this.y += this.velocityY;
            // ieterating through all the walls
            for ( Block wall: walls)
            {
                if ( collision(this , wall))  // if this object collides with the wall. here this is being refered as pacman. we will use the same function for ghost as well
                {
                    // then we gonna take a step back ( since we were moving previously so stepping back will make our pacman still at that place)
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    // now we need to update the direction
                    this.direction = prevDirection;
                    updateVelocity(); // we need to call update velocity again

                }
            }

            // now our problem is solved
            // now we have to change the image of pacman like when it moves upward then its face should point upward like that
            // so we will do that in key release function
        }

        void updateVelocity(){

            int speed;



            speed = tileSize/4;




            if ( this.direction == 'U')
            {
                this.velocityX = 0;  // bcz it is now moving only in the y axis so x axis zero
                this.velocityY = -speed;  // it is negative bcz we are going towards zero. and i had made it 32/4 therefore it is 8px therefore every frame is going to go up in pixels or we can say every frame is going to go a quarter of the tile size per sec
            }

            else if ( this.direction == 'D'){
                this.velocityX = 0;
                this.velocityY = speed;
            }

            else if ( this.direction == 'L')
            {
                this.velocityX = -speed;
                this.velocityY = 0;
            }

            else if( this.direction == 'R')
            {
                this.velocityX = speed;
                this.velocityY = 0;
            }

            // now go to keyreleased function at last
        }






        // making a reset function to reset the positions of every object after the ghost collides with pacman
        void reset()
        {
            this.x = this.startX;
            this.y = this.startY;
        }

        // now go back to move function to the ghost collision loop

    }


    // defining few properties
    private int rowCount = 23;
    private int colCount = 21;
    private int tileSize = 32;

    private int widthBoard = colCount*tileSize;
    private int heightBoard = rowCount*tileSize;

    // done this so that our JPanel has same size as the window
    // VERY IMPORTANT POINT
    // why made them private?
    // bcz we dont want their values to get changed by someone if they were public . these are the base of our game and we dont want bugs in them


    // creating variables to store images

    // theme 1: level 1-5
    private Image woodWallImage;  // woods tiles acitng as walls for theme level 1-5
    private Image redGhost1Image;
    private Image orangeGhost1Image;
    private Image bushes;




    // theme 2: levek 6-10
    private Image desertWallImage;
    private Image redGhost2Image;
    private Image orangeGhost2Image;
    private Image pinkGhostImage;
    private Image speedImage;
    private Image warpEntranceImage;
    private Image warpExitImage;





    // theme 3 : level 11-14
    private Image seaWallImage;
    private Image redGhost3Image;
    private Image orangeGhost3Image;
    private Image pinkGhost2Image;
    private Image blueGhostImage;
    private Image teleportImage;
    private Image electric;




    // theme 4: level 15
    private Image bonus1Wall;





    // theme 5: level 16-20
    private Image frostWallImage;
    private Image redGhost4Image;
    private Image orangeGhost4Image;
    private Image pinkGhost3Image;
    private Image blueGhost2Image;
    private Image iceImage;
    private Image button1Image;
    private Image gate1Image;




    // theme 6: level 21-24
    private Image lavaWallImage;
    private Image redGhost5Image;
    private Image orangeGhost5Image;
    private Image pinkGhost4Image;
    private Image blueGhost3Image;
    private Image lavaImage;
    private Image button2Image;
    private Image gate2Image;
    private Image teleport2Image;





    // theme 7: level 25
    private Image bonus2Wall;





    // theme 8: level 26-30
    private Image machineWallImage;
    private Image redGhost6Image;
    private Image orangeGhost6Image;
    private Image pinkGhost5Image;
    private Image blueGhost4Image;
    private Image phantomImage;
    private Image reverseImage;





    // theme 9: level 31-35
    private Image moltenSphireWallImage;
    private Image redGhost7Image;
    private Image orangeGhost7Image;
    private Image pinkGhost6Image;
    private Image blueGhost5Image;
    private Image spiderWeb;
    private Image sacredZone;
    private Image slime;





    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;











    // creating hashset for the constructor block used above

    HashSet<Block> foods;  // hashset of block and is named as foods
    HashSet<Block> spaces1;  // symbol : . // for moving pacman from one end to other
    HashSet<Block> exitSpaces1;  // SYMBOL : E // for letting pacman get back to map after spaces
    HashSet<Block> ghosts;  // hashset of block and is named as ghost
    Block pacman;   // we had declared a block for pacman
    HashSet<Block> blackSpace;



    HashSet<Block> walls;
    HashSet<Block> bush;  // :



    HashSet<Block> spaces2;  // SYMBOL : - // for moving pacman from one end to other

    HashSet<Block> exitSpaces2;  // symbol : e // for letting pacman get back to map after spaces


    HashSet<Block> iceBlocks;   // this is for level 15 where i will be using it .
    HashSet<Block> teleportPads;  // for level 17
    HashSet<Block> speedZones; // for level 19
    HashSet<Block> phantomZones; //for level 21
    HashSet<Block> reverseControls; // for level 23
    HashSet<Block> warpEntrance;  // for level 25
    HashSet<Block> warpExit;   // for level 25
    HashSet<Block> Button1;  // for level 27
    HashSet<Block> Gate1;
    HashSet<Block> Button2;  // for level 28
    HashSet<Block> Gate2;
    HashSet<Block> spiderNets;  // tiles that slow down pacman
    HashSet<Block> lavaTiles;
    HashSet<Block> sacredZones;
    HashSet<Block> stickyMist;
    HashSet<Block> slimePuddle;
    HashSet<Block> electricShocks;


    /*
Why are we creating a HashSet<Block> for all these game objects?

we had build a tile-based game, where each tile is represented by a Block object. These Block objects might be:

Walls
Food dots
Teleport pads
Ice tiles
Lava tiles
Buttons, gates, electric shocks... etc.

Instead of checking each tile one by one on the map for interactions (which is slow and messy), you're grouping certain types of tiles into HashSet<Block> collections



A HashSet is ideal because:

-->Fast Lookup: HashSet.contains(block) is O(1) — very fast.
-->No Duplicates: Prevents duplicate entries for the same tile type.
-->Easy to manage: You can easily add, remove, or check for membership



Why not use an array or list?
Using a List<Block> would require scanning through the whole list every time you want to check if a Block belongs to that group — slow



Why is Block used as the object in HashSet<Block>?

--> Because each game tile is a Block object, and you're using those objects to:

Store position (x, y)
Handle movement
Check tile types
Apply special behaviors (like stun, speed, etc.)

*/









    // now we had done the representation of the objects in the game
    // moving further with the tile map




    // creating a timer for game loop
    Timer gameLoop;
    // now inside the below constructor i initialize this variable




    // making the ghost move
    // creating an array of character
    char[] directions = {'U' , 'D' , 'R' , 'L'}; // up down right left

    // Random is a built in class in java
    // creating a random object
    Random random = new Random();
    // basically for each ghost we are going to randomly select the direction
    // moving to the constructor PacMan



    // adding remaining things like score , lives and etc
    int score =0;
    int lives  = 3; // by default we start with 3 lives
    boolean gameOver = false;
    // pacman has 3 lives and if he collides with the ghost it will lose one life and when lives = 0 then game over becomes true . and if it is gameover then we stop the pacman from moving

    // lets starts with the score
    // everytime the pacman collides  food it gonna eat it and gain 10 points . and everytime the packman eats the food we want to remove that food from hashSet

    // go to move function




    Runnable[] levelLoaders;
//Runnable is an interface in Java that represents a block of code you want to run

    // creating a constructor ( constructor is a special method that rus automaticaly when an object of the class is created)
    PacMan(){   // here the PacMan constructor sets up the game board ( which extends JPanel)

        setPreferredSize(new Dimension(widthBoard , heightBoard));   // this sets the preffered size of the JPanel which means it tells the layout manager that widht = widthboard and height = heightboard
        setBackground(Color.BLACK);

        addKeyListener(this);  // when the class PacMan implements keylistener it takes on the properties of keylistener . so i dont need to create a seperate key listener object i can just reference pacman using this
        // this keylistener is going to listener the three functions which are written keytyped, keypressed , keyreleased to process the key processes

        // now i need to ensure that our JPanel listens to key presses. in a window we can have multiple components which all can be listening key presses. but in this key we had one component ( declared just above this) but we need to make sure that this is the component that listens to the key presses
        // so i write
        setFocusable(true);
        // now go back to App.java file and write pacmanGame.requestFocus(); but make sure to write it before setvisible
        // after running the file when we press any key we got keyevent printed as the code number so the arrow keys as specific number such as
        // 37 -> left arrow
        // 39 -> right arrow
        // 38 - > up arrow
        // 40 -> down arrow

        // now we want to make our pacman movable
        // if i want to move the pacman left or right it is basically the x axis line. for left i will go -ve x axis and for right i will go towards +ve x axis as i am going away from zero
        // similary top is moving -ve y axis bcz i am moving close to zero and +ve y axis when moving down bcz i am moving away from zero
        // therefore in order to move the pacman we need to set the velocity in x direction and y direction
        // therefore i will add three more variable in the class Block




        // now we had our JPanel . now go back to App.java file and create an instance/object of this JPanel now




        // coming back from there
        // lets add images

        // for the images we are creating member variables to store the images

        // so i created them outside the constructor but inside the class


        // loading images inside the constructor
        // theme 1 done
        woodWallImage = new ImageIcon(getClass().getResource("./woodTile.png")).getImage();
        bushes = new ImageIcon(getClass().getResource("./bush.png")).getImage();
        redGhost1Image = new ImageIcon(getClass().getResource("./redGhost1.png")).getImage();
        orangeGhost1Image = new ImageIcon(getClass().getResource("./orangeGhost1.png")).getImage();



        // theme 2  done
        desertWallImage = new ImageIcon(getClass().getResource("./desertWallImage.png")).getImage();
        redGhost2Image  = new ImageIcon(getClass().getResource("./redGhost2.png")).getImage();
        orangeGhost2Image  = new ImageIcon(getClass().getResource("./orangeGhost2.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
        speedImage = new ImageIcon(getClass().getResource("./speed.png")).getImage();
        warpEntranceImage = new ImageIcon(getClass().getResource("./wrapEntrance.png")).getImage();
        warpExitImage = new ImageIcon(getClass().getResource("./wrapExit.png")).getImage();




        // theme 3 done


        seaWallImage = new ImageIcon(getClass().getResource("./seaWall.png")).getImage();
        redGhost3Image = new ImageIcon(getClass().getResource("./redGhost3.png")).getImage();
        orangeGhost3Image= new ImageIcon(getClass().getResource("./orangeGhost3.png")).getImage();
        pinkGhost2Image =  new ImageIcon(getClass().getResource("./pinkGhost2.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        teleportImage = new ImageIcon(getClass().getResource("./teleport.png")).getImage();
        electric = new ImageIcon(getClass().getResource("./electric.png")).getImage();




        // theme 4: done

        bonus1Wall = new ImageIcon(getClass().getResource("./bonus1Wall.png")).getImage();




        // theme 5 done
        frostWallImage = new ImageIcon(getClass().getResource("./frostWallImage.png")).getImage();
        redGhost4Image = new ImageIcon(getClass().getResource("./redGhost4.png")).getImage();
        orangeGhost4Image= new ImageIcon(getClass().getResource("./orangeGhost4.png")).getImage();
        pinkGhost3Image =  new ImageIcon(getClass().getResource("./pinkGhost3.png")).getImage();
        blueGhost2Image = new ImageIcon(getClass().getResource("./blueGhost2.png")).getImage();
        iceImage = new ImageIcon(getClass().getResource("./ice.png")).getImage();
        button1Image = new ImageIcon(getClass().getResource("./button1.png")).getImage();
        gate1Image = new ImageIcon(getClass().getResource("./gate1.png")).getImage();;




        // theme 6 done
        lavaWallImage= new ImageIcon(getClass().getResource("./lavaWallImage.png")).getImage();
        redGhost5Image = new ImageIcon(getClass().getResource("./redGhost5.png")).getImage();
        orangeGhost5Image= new ImageIcon(getClass().getResource("./orangeGhost5.png")).getImage();
        pinkGhost4Image =  new ImageIcon(getClass().getResource("./pinkGhost4.png")).getImage();
        blueGhost3Image = new ImageIcon(getClass().getResource("./blueGhost3.png")).getImage();
        lavaImage = new ImageIcon(getClass().getResource("./lava.png")).getImage();
        button2Image = new ImageIcon(getClass().getResource("./button2.png")).getImage();
        // gate2Image = new ImageIcon(getClass().getResource("./button.png")).getImage();   // i think it is of no use
        teleport2Image = new ImageIcon(getClass().getResource("./teleport2.png")).getImage();




        // theme 7  done

        bonus2Wall = new ImageIcon(getClass().getResource("./bonus2Wall.png")).getImage();



        // theme 8 done
        machineWallImage = new ImageIcon(getClass().getResource("./machineWall.png")).getImage();
        redGhost6Image = new ImageIcon(getClass().getResource("./redGhost6.png")).getImage();
        orangeGhost6Image= new ImageIcon(getClass().getResource("./orangeGhost6.png")).getImage();
        pinkGhost5Image =  new ImageIcon(getClass().getResource("./pinkGhost5.png")).getImage();
        blueGhost4Image = new ImageIcon(getClass().getResource("./blueGhost4.png")).getImage();
        reverseImage = new ImageIcon(getClass().getResource("./reverse.png")).getImage();
        // phantomImage = new ImageIcon(getClass().getResource("./cherry.png")).getImage();




        // theme 9 done
        moltenSphireWallImage = new ImageIcon(getClass().getResource("./moltenSphire.png")).getImage();
        redGhost7Image = new ImageIcon(getClass().getResource("./redGhost7.png")).getImage();
        orangeGhost7Image= new ImageIcon(getClass().getResource("./orangeGhost7.png")).getImage();
        pinkGhost6Image =  new ImageIcon(getClass().getResource("./pinkGhost6.png")).getImage();
        blueGhost5Image = new ImageIcon(getClass().getResource("./blueGhost5.png")).getImage();
        spiderWeb = new ImageIcon(getClass().getResource("./spiderWeb.png")).getImage();
        sacredZone = new ImageIcon(getClass().getResource("./sacredZone.png")).getImage();
        slime = new ImageIcon(getClass().getResource("./slime.png")).getImage();





        pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();















        // in order to draw the image i need to specify where it is i.e the x and y position and widht and height. and i need to store all these info for fodd , wall , ghost , and pacman
        // for this i will use a class BLOCK



        // now talking about the tile map
        /*
         it will be a 2d array and i will use characters ( we can use numbers also ). therefore i am going to use an array of strings which basically means an array of array of characters
         therefore we can think of each row as a string within our array. each tile is going to be represented by a character. if a tile has X then it means there is a wall at there. and where we have empty tiles it is whitespace character.
         in these tiles i am going to place a food here and it is in this tiles that the pacman is moving. and O will simply mean that the tile is empty. in this we had like there cannot be placed any food. and this is the one where pacman can bypass the area and like if it
         goes to left it comes out from right. this is represented as O in this code

         i had done this bcz it is easier to place some O in the maze rather than placing them everywhere inside the maze instead of empty space.
         also we had p,o,r,b which stands for pink , orange , red , blue. and also we have P which stands for pacman. like they are the starting positions where these all will be placed


         we start from the 0,0 and if we say we had to go to 4,2 tile then it will be 4*32 , 2*32 bcz tile size will also be have to considered.
         since it had moved 4 tiles across right so 4*32 and 2 tiles down so 2*32
         therefore we create variables specifying the tile widht and height

         therefore we traverse the array such that if we occured across X then we place a wall , if we occur across p,r,o,b then we place their a ghost and if we came across whitespace then we lace their a food

         therefore we had three main repeating things - wall , ghost , and food so i will create a 3 different hash sets to ietrate through it and i need not have a hashset for pacman bcz it is used a single time and hence i will create a variable for it

         why are we using a hashset?

         -> hashset does not allow duplicates ensuring each tile is only stored once
         -> Useful when tracking walls, ghosts, and food because their positions do not repeat in the grid
         -> If you stored wall positions in a list (ArrayList), checking if a tile is a wall would take O(n).
         -> With a HashSet, checking if a tile is a wall (or contains a ghost/food) takes O(1).
         -> Instead of looping through an entire 2D array, we can just iterate over a HashSet containing the required tiles
         -> HashSet directly checks if (x,y) exists in O(1) time


         why not use a 2d array?

         -> Searching for walls, ghosts, and food would still take O(n²) in worst cases.
         -> Memory usage is higher than a HashSet, since it stores all tiles, even empty one


         */

        // runnable and loading map logic
        // it is an array of runnables where levelLoaders is an object or reference variable to access the members

        // This is an array of such tasks. Each item in levelLoaders is a small, runnable code block
        // It’s a clean way to organize level setup logic

        // This creates an array of Runnables — one per map — based on however many maps MapLoader provides.... runnables is the object...i.e if maploader has returned 30 then we have [30] . thus we had 30 runnable objects which can be runned using the index..
        // the for loop will run through each index as theri are 30 levels
        levelLoaders = new Runnable[MapLoader.getMapCount()];

        // here we are dynamically generating an array of level-loading tasks. this is good practice for large games ( like we had 35+maps). it is modular , flexible


         /*
          what is encapsulation?
          Encapsulation means bundling data (variables) and methods (functions) that operate on that data into a single unit — usually a class — and restricting direct access to some of the object's components

--> Why Is Encapsulation Important?

Benefit                      	Description
-------                   ----------------------
Security	              Prevents outside code from putting your object into an invalid state.
Control	                  You control how data is accessed or modified (via methods).
Flexibility	              You can change internal implementation without affecting external code.
Ease of Maintenance	      Keeps the code modular and clean.

thus Encapsulation is the practice of keeping fields private and providing public methods to get and set their values, allowing you to control and protect the data


--> Why Encapsulation Helps in Your Game:
Benefit	                            How it applies to your game
--------                           -------------------------------
Game logic protection 	            Prevents other parts of the game from messing up Pac-Man or ghost data.
Easier debugging	                You know exactly which methods affect game state.
Clean code	                        You organize logic in the class it belongs to (e.g., ghost movement inside Ghost, stun logic inside PacMan).
Future-proofing	                    You can change how blocks or ghosts work internally without changing the rest of the game.
*/
        // here encapsulation is used ( also in the block class we had used encapsulation)

        for (int i = 0; i < levelLoaders.length; i++) {
            final int index = i; // this is important because it captures the correct value of i for each Runnable. Without it, all Runnables might reference the same final value of i

            // this is an inner class which is implementing runnable interface...actually we are saying At this index i, I want to store a small piece of code (a task) that I can run later.
            levelLoaders[i] = new Runnable() {
                @Override
                public void run() {
                    String[] mapData = MapLoader.getMap(index);
                    loadMap(mapData); // Custom map-loading logic
                }
            };
        }

/*
 why final?
 Java’s anonymous classes (like Runnable) capture variables by reference, not by value. Without making a final copy, all Runnables would share the same i, which changes every loop iteration
 thus writing final create a new, final copy of i for that specific Runnable. Now each Runnable captures its correct value of i at the time of creation


 note: runnable interface is a built in interface which has only one method run.....This is used when you want to encapsulate a task (some code) that you want to run later, like loading a level

 // actaully what we did above is that ....we are saying that for level at index this build the structure of the map and MapLoader.getMap(index) fetches the String[] data for that level’s map.
 also loadMap(mapData) is your own method for converting that String[] (tile layout) into game objects (walls, food, ice, etc.)

 after this we can then just use .run() to run any map

 Instead of calling the loadMap() method immediately inside the loop, you're storing each map-loading task as a Runnable, and running it only when needed

 */

// we need to just call .run() on any index to trigger that level’s setup


        // checking number of walls , foods , ghosts





        levelLoaders[debugMapLevel-1].run();  // Load the starting debug level
// resetPositions();

        // calling the function loadmap bcz we want to check the number of walls , foods , ghosts and its algo is written inside the function loadMap();
        //  System.out.println(walls.size());  // walls was the hashset where all the walls when encountered were stored
        //  System.out.println(foods.size());  // same here
        //  System.out.println(ghosts.size()); // same here
        // now i run the code and i see that it prints the number

        // but when i run this file then it gives error that the main method not found bcz i have declared main method in app.java so i need to run that file and not PacMan.java
        // therefore our loadmap is working and we have all resources in our asset. now i comment our these print statements




        // ghost movement  ( after loadMap())
        // iterating through each ghost
        for ( Block ghost : ghosts)
        {
            char newDirection = directions[random.nextInt(4)];  // since directions has four characters and random.nexint(4) gives a number betwen zero and 4 but not 4 so total of 4 numbers(0,1,2,3) it can give therefore we have directions[i] where i can be {0,1,2,3} . and the i is the index therefore if directions[0] then U character if 1 then D characters as according we had made the array of charcaters
            ghost.updateDirection(newDirection);  // therefore basicallt we assign each ghost a new direction

        }
        // now go to the move function to consider abt the collisions of the ghost




        gameLoop = new Timer(50 , this);  // the delay is in milliseconds . and this refers to the ActionListener which is implemented in the class PacMan. basically it means that this class where you are using me must have implemented action listener
        // there are 1000 ms in 1 sec so there are 1000/50 = 2ofps i.e 20 frame per second

        // It creates a repeating timer that calls an actionPerformed() method every 50ms. This is typically used for game loops to update animations, movement, or physics.
        // how does it work?
        // -> first we will start the timer by gameLoop.start();
        // -> this will start the timer triggering actionPerformed() every 50ms
        // -> since this refers to the class implementing ActionListener, we must have
        //  @Override
        //  public void actionPerformed(ActionEvent e) {
        //  // Update game logic (e.g., move Pac-Man, detect collisions, repaint screen)
        //  repaint();
        //  }
        // -> This repaints the game screen every 50ms, creating the illusion of motion.

        gameLoop.start();  // starting the timer

        // now the loop has started and when i run the loop is running but i dont see any changes so lets add some features of moving the pacman up down when pressed key
        // for that we need key listner like wise action listener . so we write it above where we written ActionListner


    }  //  this is the end of PacMan constructor






    public void loadMap(String[] mapData) {
        // Play theme music when loading new level
        soundManager.playThemeMusic(debugMapLevel);

        // i need to initilise all the hashset first

        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();  // " "


        teleportPads = new HashSet<Block>(); // T AND %  // these hashsets like teleport and iceblocks even if you are not using them in the current level but if you have somehwere in the program the usage of them then you had to implement this here also.
        iceBlocks = new HashSet<Block>();   // I   // bcz we have used somehwere in our program the for loop which iterates over this hashset so it doesn’t matter if there are actually any ice blocks in the map — if the HashSet was never initialized, then Java throws a NullPointerException.
        // therefore Always initialize every hashset, even if it's empty .This way, your code won’t crash if it tries to iterate over or check for eg iceBlocks, because it will just be an empty set instead of null.
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // ~
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1  = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>();  // <


        for (int row = 0; row < mapData.length; row++) {
            String line = mapData[row];
            for (int col = 0; col < line.length(); col++) {
                char tile = line.charAt(col);
                // getting x position ( it will be the 'how many columns we are from the left')
                int x = col*tileSize;
                // getting y position (  it will be the 'how many rows from the top')
                int y = row*tileSize;



                if ( tile == 'X'){  // theme 1: wood wall

                    // since we made hashset for the block constructor so we will had to make object of the block....and since i had a paramterized constructor block which has arguments like image , x , y positions , widht and height....thus we are passing which image i want for this object of hashset, what is the positon related to x and y is of the image , what is its dimensions
                    Block wood = new Block(woodWallImage , x , y , tileSize , tileSize);
                    walls.add(wood);   // then since we had used hashset so i am now adding the object wood to it
                }

                else if ( tile == '<')
                {
                    Block bS = new Block(null , x , y , tileSize , tileSize);
                    blackSpace.add(bS);
                }

                else if ( tile == '['){  // theme 2: desert wall

                    Block wood = new Block(desertWallImage , x , y , tileSize , tileSize);
                    walls.add(wood);
                }

                else if ( tile == ']'){  // theme 3: sea wall

                    Block wood = new Block(seaWallImage , x , y , tileSize , tileSize);
                    walls.add(wood);
                }

                else if ( tile == '{'){  // theme 4: bonus 1 wall

                    Block wood = new Block(bonus1Wall , x , y , tileSize , tileSize);
                    walls.add(wood);
                }

                else if ( tile == '}'){  // theme 5: frost wall

                    Block wood = new Block(frostWallImage , x , y , tileSize , tileSize);
                    walls.add(wood);
                }

                else if ( tile == '`'){  // theme 6: lava wall

                    Block wood = new Block(lavaWallImage , x , y , tileSize , tileSize);
                    walls.add(wood);
                }

                else if ( tile == '/'){  // theme 7: bonus 2 wall

                    Block wood = new Block(bonus2Wall , x , y , tileSize , tileSize);
                    walls.add(wood);
                }

                else if ( tile == '_'){  // theme 8:  machine wall

                    Block wood = new Block(machineWallImage , x , y , tileSize , tileSize);
                    walls.add(wood);
                }

                else if ( tile == '='){  // theme 9: moltenspire wall

                    Block wood = new Block(moltenSphireWallImage , x , y , tileSize , tileSize);
                    walls.add(wood);
                }




                else if ( tile == ' ')  // food
                {
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down

                }


                else if ( tile == '.')   // allowing pacman to move map from end to other
                {
                    Block space1  = new Block(null , x, y, tileSize, tileSize);
                    spaces1.add(space1);
                }

                else if ( tile == 'E')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tile == '-')   // allowing pacman to move map from end to other
                {
                    Block space2  = new Block(null , x, y, tileSize, tileSize);
                    spaces2.add(space2);
                }

                else if ( tile == 'e')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }

                else if( tile == 'a')   // orange ghost 1 [theme 1]
                {

                    Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost1);
                }

                else if( tile == 's')   // orange ghost 2 [ theme 2]
                {

                    Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost2);
                }

                else if( tile == 'd')   // orange ghost 3 [ thme 3]
                {

                    Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost3);
                }

                else if( tile == 'f')   // orange ghost 4 [ theme 5]
                {

                    Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost4);
                }

                else if( tile == 'z')   // orange ghost 5 [theme 6]
                {

                    Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost5);
                }

                else if( tile == 'x')   // orange ghost 6 [ theme 8]
                {

                    Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost6);
                }

                else if( tile == 'c')   // orange ghost 7 [ theme 9]
                {

                    Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost7);
                }


                else if( tile == 'r')   // red ghost 1
                {

                    Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost1);
                }

                else if( tile == 'g')   // red ghost 2
                {

                    Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost2);
                }

                else if( tile == 'h')   // red ghost 3
                {

                    Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost3);
                }

                else if( tile == 'j')   // red ghost4
                {

                    Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost4);
                }


                else if( tile == 'k')   // red ghost5
                {

                    Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost5);
                }


                else if( tile == 'y')   // red ghost6
                {

                    Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost6);
                }

                else if( tile == 'u')   // red ghost7
                {

                    Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost7);
                }


                else if( tile == 't')   // pink ghost 1
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }

                else if( tile == 'b')   // pink ghost 2
                {

                    Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost2);
                }

                else if( tile == 'n')   // pink ghost 3
                {

                    Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost3);
                }

                else if( tile == 'm')   // pink ghost 4
                {

                    Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost4);
                }

                else if( tile == 'q')   // pink ghost 5
                {

                    Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost5);
                }

                else if( tile == 'w')   // pink ghost 6
                {

                    Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost6);
                }



                else if( tile == 'Y')   // blue ghost 1
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }

                else if( tile == 'U')   // blue ghost 2
                {

                    Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost2);
                }

                else if( tile == 'O')   // blue ghost 3
                {

                    Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost3);
                }

                else if( tile == 'K')   // blue ghost 4
                {

                    Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost4);
                }

                else if( tile == 'J')   // blue ghost 5
                {

                    Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost5);
                }


                else if ( tile == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }


                else if ( tile == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }

                else if ( tile == '%')
                {
                    Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                    teleportPads.add(teleport2);
                }

                else if ( tile == ':')
                {
                    Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                    bush.add(bushe);
                }

                else if ( tile == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }

                else if ( tile == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }

                else if ( tile == '~')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tile == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }



                else if ( tile == 'N')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tile == '&')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }


                else if ( tile == '1')
                {
                    Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                    Button1.add(button1);
                }

                else if ( tile == 'A')
                {
                    Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                    Gate1.add(gate1);
                    walls.add(gate1);
                }


                else if ( tile == '2')
                {
                    Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                    Button2.add(button2);
                }

                else if ( tile == 'B')
                {
                    Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                    Gate2.add(gate2);
                    walls.add(gate2);
                }

                else if ( tile == 'W')
                {
                    Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                    spiderNets.add(web);
                }

                else if ( tile == 'V')
                {
                    Block lava = new Block(lavaImage, x, y, tileSize, tileSize);
                    lavaTiles.add(lava);
                }

                else if ( tile == 'Z')
                {
                    Block sacred = new Block ( sacredZone , x, y , tileSize , tileSize);
                    sacredZones.add(sacred);
                }



                else if ( tile == 'L')
                {
                    Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                    slimePuddle.add(slimemist);
                }

                else if (tile == 'Q')
                {
                    Block electricS = new Block(electric, x, y, tileSize, tileSize);
                    electricShocks.add(electricS);
                }


                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts


            }
        }
    }






    // creating a function paintComponent in this we will draw all of our objects onto the game


    @Override   // bcz it overrides the method of jPanl
    public void paintComponent(Graphics g)   // it will take in graphics g
    {
        super.paintComponent(g);  // basically super is going to import the function "paintComponent" from JPanel
        // This calls the original method from JPanel (the superclass).
        // It clears the screen and prepares it for fresh drawing.
        // Without this, old drawings might not get erased and you'd see weird overlapping graphics or flickering


        // calling draw function ( custom method )
        draw(g);  // this will draw graphics g
    }

    // Java automatically calls this whenever your game panel needs to be redrawn (e.g. when the game updates, window resizes, etc.).
    // Graphics g is the paintbrush — you use it to draw shapes, images, text, etc. on the screen


    public void draw(Graphics g)  // taking graphics g
    {


        // lava
        for (Block lava : lavaTiles) {
            if (lavaEnabled) {
                g.drawImage(lava.image, lava.x, lava.y, tileSize, tileSize, null);
            } else {
                g.setColor(Color.darkGray);
                g.fillRect(lava.x, lava.y, tileSize, tileSize);
            }
        }





        // black spaces
        g.setColor(Color.BLACK);
        for ( Block bS : blackSpace)
        {
            g.fillRect(bS.x, bS.y, tileSize, tileSize);
        }


        // spider nets
        for ( Block web : spiderNets)
        {
            g.drawImage(web.image, web.x, web.y, web.width , web.height , null);
        }



        // since i want the pacman to move above the ice tiles and not below and since it is a 2d game so the layering depends on the order of draw so i draw the ice tiles first and then pacman
        // DISPLAYING ICE
        for( Block ice : iceBlocks)
        {
            g.drawImage(ice.image, ice.x, ice.y , ice.width , ice.height , null);
        }



        // DISPLAYING ELELECTIC SHOCKS
        for( Block elec : electricShocks)
        {
            g.drawImage(elec.image, elec.x, elec.y , elec.width , elec.height , null);
        }


        // displaying sacred zones
        for ( Block sacred : sacredZones)
        {
            g.drawImage(sacred.image, sacred.x, sacred.y, sacred.width , sacred.height , null);
        }


        //  // displying the sticky mist
        //  for ( Block sticky : stickyMist)
        //  {
        //     g.drawImage(sticky.image, sticky.x, sticky.y, sticky.width , sticky.height , null);
        //  }



        // displaying slime puddle

        for ( Block slime : slimePuddle)
        {
            g.drawImage(slime.image, slime.x, slime.y, slime.width , slime.height , null);
        }


        // displaying button1 and gate 1
        // Draw Buttons (B)

        for (Block button : Button1) {
            g.drawImage(button.image, button.x, button.y, button.width , button.height , null);
        }

        for (Block button : Button2) {
            g.drawImage(button.image, button.x, button.y, button.width , button.height , null);
        }

        // Draw Gates (G)
        if ( gate1Open)
        {

            g.setColor(Color.BLACK); // change color if open/closed
            for (Block gate : Gate1) {
                g.fillRect(gate.x, gate.y, tileSize, tileSize);
            }
        }

        if ( gate2Open)
        {

            g.setColor(Color.BLACK); // change color if open/closed
            for (Block gate : Gate2) {
                g.fillRect(gate.x, gate.y, tileSize, tileSize);
            }
        }





        // displaying teleportation pads
        // similarly teleport pads needs to be drawn before pacman
        for ( Block tele: teleportPads)
        {
            g.drawImage(tele.image, tele.x, tele.y , tele.width , tele.height , null);
        }


        // displaying the free zones
        g.setColor(Color.BLACK);
        for ( Block space1 : spaces1)
        {
            g.fillRect(space1.x, space1.y, tileSize, tileSize);
        }


        g.setColor(Color.BLACK);
        for ( Block exit1 : exitSpaces1)
        {
            g.fillRect(exit1.x, exit1.y, tileSize, tileSize);
        }

        g.setColor(Color.BLACK);
        for ( Block space2 : spaces2)
        {
            g.fillRect(space2.x, space2.y, tileSize, tileSize);
        }


        g.setColor(Color.BLACK);
        for ( Block exit2 : exitSpaces2)
        {
            g.fillRect(exit2.x, exit2.y, tileSize, tileSize);
        }


        // displaying speed zones
        for ( Block speed : speedZones)
        {
            g.drawImage(speed.image, speed.x, speed.y, speed.width , speed.height , null);
        }


        // displaying the reverse controls
        for ( Block rev : reverseControls)
        {
            g.drawImage(rev.image, rev.x, rev.y, rev.width , rev.height , null);
        }


        // displaying the wrap exit tiles
        for ( Block exit : warpExit)
        {
            g.drawImage(exit.image, exit.x, exit.y, exit.width, exit.height , null);
        }

        // displaying the warp entrance tiles
        for ( Block entrance : warpEntrance)
        {
            g.drawImage(entrance.image, entrance.x, entrance.y, entrance.width, entrance.height , null);
        }



        // lets draw our pacman first
        // since we have pacman as a block object and it has the information x,y position and width and height of pacman
        // 1) firsly lets draw a rectangle at that position just for testing
        // g.fillRect(pacman.x, pacman.y, pacman.width, pacman.height);   // this prints a rectangle at the position we had assigned for pacman

        // 2) but i want pacman to be there so
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width , pacman.height , null);   // here we had done two extra things . 1) we had written the name of variable pacman where we had stpred the image pacman so we write pacman.image and at last we had written null which is used so that the java automatically loads the image

        // doing the same for other images

        // displaying ghost
        for ( Block ghost : ghosts)  // here i had created a for each loop where ghost is the element declared by us it will be used to traverse the whole hashset ghosts written after : . therefore this for loop displays all the items in the ghosts hashset
        {
            g.drawImage(ghost.image , ghost.x, ghost.y, ghost.width , ghost.height , null);

        }

        // bushes
        for ( Block bu : bush)
        {
            g.drawImage(bu.image, bu.x, bu.y, bu.width , bu.height , null);
        }


        // displaying walls
        for ( Block wall : walls){
            g.drawImage(wall.image, wall.x, wall.y, wall.width , wall.height , null);
        }



        // displaying phantom zones. it is done after walls are drawn bcz they will display above the wall . if drawn before walls then it will make then hide behind walls
        g.setColor(new Color(100, 130, 160, 120)); // semi-transparent purple
        for (Block phantom : phantomZones) {
            g.fillRect(phantom.x, phantom.y, phantom.width, phantom.height);
        }

        // displaying foods

        // changing colour to white
        g.setColor(Color.WHITE);
        for ( Block food : foods){
            // g.drawImage(food.image, food.x, food.y, food.width , food.height , null);
            // this is wrong bcz we dont have image for the food
            //  we will use rectangle

            g.fillRect(food.x, food.y, food.width, food.height);

        }

        // now move back to paintComponet function
        // since the paintcomponet function only gets called once so we only draw one time
        // but in game we are constantly moving all the characters so the ghost move up down left right so we need to constantly redraw on the JPanel to reflect those new positions
        // to do that we need a game loop
        // for that i implement a ActionListener in the main class i.e PacMan extends JPanel implements ActionListener





        // score

        g.setFont(new Font("Arial" , Font.PLAIN , 18));
        if ( gameOver)
        {
            g.drawString("GAME OVER" + String.valueOf(score), tileSize/2 , tileSize/2);
        }
        else
        {
            g.drawString("x" + String.valueOf(lives)+ " Score: " + String.valueOf(score), tileSize/2 , tileSize/2);
        }

    }






    public void move(){
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;
        // based on our code one of these ( on the right side of equal to ) are going to be zero [ since if pacman is moving up then x velocity is zero bcz it is moving in y axis ...]
        // so i added both to the x and y position of pacman
        // therefore our move function updates the x and y positons of pacman
        // now we need to call this function before paint
        // i.e inside the below function

        // since velocity is tilesize/4 so we expect the pacman to move one quarter of the tile size



        // electric shocks

        // here we are storing the current time in milli secodns which we will then use to compare with the stun end time...this wll let us know that pacman should move or not
        long currentTime = System.currentTimeMillis();

        // boolean to check if we should skip pacman movement
        boolean pacmanStunnedThisFrame = false;

        // Check if Pac-Man is stunned

        // since we had declared a hashset for pacmanstunendtiems so we check if pacman is in that set...if yes then it means pacman is stunned
        if (pacmanStunEndTimes.containsKey(pacman))
        {
            // since we had also defined the long in the hashset which tell the milli seconds used
            // so we get that value and compare with our current time
            long end1 = pacmanStunEndTimes.get(pacman);
            if (System.currentTimeMillis() < end1)   // if stun time is still left then
            {

                // we set the velocity to 0 which will freeze him
                pacman.velocityX =0;
                pacman.velocityY = 0;
                pacman.x += pacman.velocityX;  // these two are not required here but are not harmless also...so keep them to maintain consistency
                pacman.y += pacman.velocityY;
                pacmanStunnedThisFrame = true;  // set to true i.e pacman is stunned
            }

            else // if the stunned time has excedded the current time then we remove the pacman from hashset
            {
                pacmanStunEndTimes.remove(pacman);  // Stun ended
            }
        }

        // Check for collision with shock tile
        for (Block shock : electricShocks)
        {
            if (collision(pacman, shock))  // if there is a collision
            {
                // Play shock sound if in theme 3
                if (debugMapLevel == 3) {
                    soundManager.playElectricShockSound();
                }

                // then we put the pacman to the hashset and set a 2 sec stun duration
                pacmanStunEndTimes.put(pacman, System.currentTimeMillis() + 2000);

                // this stops the pacman where it was...it basically undoes the movement
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;

                // now set velocity zero
                pacman.velocityX =0;
                pacman.velocityY = 0;

                // since we do checking for one frame so this frame is checked now we make the boolean again false
                pacmanStunnedThisFrame = false;
                System.out.println("Pac-Man stunned!");
                // since we encountered a collison and reset the pacman velocity so we dont need to check for any other tiles now at this pt so we exit the loop
                break;  // exit shock tile loop
            }
        }

        //  Skip movement ONLY if stunned this frame
        if (pacmanStunnedThisFrame)
        {
            return;  // safe now, no velocity updates or motion
        }







        // reverse controls

        boolean currentlyOnReverse = false;

        for (Block reverse : reverseControls)
        {
            if (collision(pacman, reverse))
            {
                // make bool true
                currentlyOnReverse = true;

                // at the st of program we had declared their variables false initally
                if (!pacmanIsOnReverse) // if pacman is not rversed
                {

                    // then it means he just stepped onto the R tile
                    // therefore we now toggle the bool that controls are reversed....earlier it was false which i declared initally
                    controlsReversed = !controlsReversed;
                    // System.out.println("Controls reversed: " + controlsReversed);
                }

                break; // therefore found one, no need to check more
            }
        }

// Update tracking flag
        pacmanIsOnReverse = currentlyOnReverse;






        // phantom zones
        // Check if Pac-Man is entering a Phantom Zone
        for (Block phantom : phantomZones)
        {
            if(collision(pacman, phantom))
            {
                // if yes then stops him...that is we block him like it is a wall
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }




        boolean alreadyInWeb = isInsideSpiderNet(pacman); // checks if pacman is currently on a spider web
        boolean alreadyInBush = isInsideBushes(pacman); // checks if pacman is currently on a BUSHES

        if(alreadyInWeb)
        {
            // slow down pacmans movement inside the web
            if(pacman.direction == 'L')
            {
                pacman.velocityX = -tileSize / 8;  // Slow down horizontally....earlier the speed was tile size /4 now it is half of that
                pacman.velocityY = 0;  // No vertical movement
            }

            else if(pacman.direction == 'R')
            {
                pacman.velocityX = +tileSize / 8;
                pacman.velocityY = 0;
            }

            else if(pacman.direction == 'U')
            {
                pacman.velocityY = -tileSize / 8;  // Slow down vertically
                pacman.velocityX = 0;
            }

            else if(pacman.direction == 'D')
            {
                pacman.velocityX = 0;
                pacman.velocityY = +tileSize / 8;
            }

        }

        else if( alreadyInBush)
        {
            // Slow down Pac-Man's movement inside the web
            if (pacman.direction == 'L')
            {
                pacman.velocityX = -tileSize / 8;  // Slow down horizontally
                pacman.velocityY = 0;  // No vertical movement
            }


            else if(pacman.direction == 'R')
            {
                pacman.velocityX = +tileSize / 8;
                pacman.velocityY = 0;
            }

            else if(pacman.direction == 'U')
            {
                pacman.velocityY = -tileSize / 8;  // Slow down vertically
                pacman.velocityX = 0;
            }

            else if(pacman.direction == 'D')
            {
                pacman.velocityX = 0;
                pacman.velocityY = +tileSize / 8;
            }

        }



        else if(isOnIce(pacman))
        {
            // ICE SLIDING LOGIC
            boolean onIceNow = isOnIce(pacman); // check current tile. isOnIce is a function which returns boolean value. this functions checks if the tile is ice or not. if yes then returns true therefore here boolean onIceNow = true

            // Update velocity based on current direction
            pacman.updateVelocity();

            // Try moving
            pacman.x += pacman.velocityX;
            pacman.y += pacman.velocityY;
            // these two are increasing the speed of pacman when on ice tiles



            // Check for wall hit
            for (Block wall : walls) {
                if (collision(pacman, wall)) {
                    pacman.x -= pacman.velocityX;
                    pacman.y -= pacman.velocityY;

                    // Stop motion
                    pacman.velocityX = 0;
                    pacman.velocityY = 0;
                    break; // if we used here return then move function is ended therefore now when pacman hits a wall then everyone does not move ghosts also until a key is pressed by user to make it move in open area. therefore this is wrong to put here return
                }
            }


            // checking for black space collision


            // 4. If still on ice, keep sliding automatically in same direction next frame
            // But if off ice, zero out velocity so it doesn’t keep going
            if (!isOnIce(pacman)) {
                pacman.velocityX = 0;
                pacman.velocityY = 0;
            }


        }
        else {
            // Normal speed when not on ice or spider net or slime
            pacman.updateVelocity();
        }





        // lava tiles toggle
        // Lava Toggle Button Logic
        boolean currentlyOnLavaButton = false;

        for (Block button : Button2) {
            if (collision(pacman, button)) {
                currentlyOnLavaButton = true;

                if (!pacmanOnLavaButton) { // Just stepped on
                    lavaEnabled = !lavaEnabled;

                    if (lavaEnabled) {
                        System.out.println("Lava turned ON");
                    } else {
                        System.out.println("Lava turned OFF");
                    }
                }

                break;
            }
        }
        pacmanOnLavaButton = currentlyOnLavaButton;






        // warp tunnels
        // Handle warp entrance ('W') for Pac-Man
        for (Block warpIn : warpEntrance)
        {
            if (collision(pacman, warpIn))
            {
                if (!warpExit.isEmpty())  // if wrap exit hashset is not empty
                {
                    // Convert HashSet to array and pick a random exit
                    Block[] exitsArray = warpExit.toArray(new Block[0]);
                    int index = random.nextInt(exitsArray.length);
                    Block chosenExit = exitsArray[index];

                    // we had made this bcz we had made the w tiles as walls so when warped to w the pacman got stuck so we made this. earliear we had only
                    // pacman.x = chosenExit.x;
                    // pacman.y = chosenExit.y;
                    // but this got our pacman stuck so we need to warped to the next tile thats why we did this. and switch to handle the direction and place the pacman correctly after wrapping
                    switch (pacman.direction) {
                        case 'U':
                            pacman.x = chosenExit.x;
                            pacman.y = chosenExit.y - tileSize;
                            break;
                        case 'D':
                            pacman.x = chosenExit.x;
                            pacman.y = chosenExit.y + tileSize;
                            break;
                        case 'L':
                            pacman.x = chosenExit.x - tileSize;
                            pacman.y = chosenExit.y;
                            break;
                        case 'R':   // if pacman enters from left then we want him toh appear from the right... and that too one next tile...
                            pacman.x = chosenExit.x + tileSize;  // this spawan the pacman one tile next to the warp exit...since warp exit is also a wall so it prevent the paxcman from wall collison and lands him on a walkable tile
                            pacman.y = chosenExit.y;
                            break;
                    }


                    // System.out.println("PacMan teleported to random warp exit at (" + chosenExit.x + ", " + chosenExit.y + ")");
                }
                break; // Exit after teleporting
            }
        }


        for ( Block warpOut : warpExit)
        {
            if ( collision(pacman, warpOut))
            {
                // this will be a wall for pacman

                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;

                // Completely stop movement
                pacman.velocityX = 0;
                pacman.velocityY = 0;

                hitWall = true;
                break;
            }
        }




        // TELEPORTING THE PACMAN FROM ONE MAP END TO OTHER
        // Handle E ↔ e warp for pacman
        for(Block space1 : spaces1)
        {
            if(collision(pacman, space1))
            {
                for(Block exit1 : exitSpaces1)
                {
                    teleport(space1, exit1, pacman);  // function
                    break;
                }
                break;
            }
        }

        for(Block exit1 : exitSpaces1)
        {
            if(collision(pacman, exit1))
            {
                for(Block space1 : spaces1)
                {
                    teleport(exit1, space1, pacman);
                    break;
                }
                break;
            }
        }


// Handle M ↔ m warp for pacman
        for(Block space2 : spaces2)
        {
            if(collision(pacman, space2))
            {
                for(Block exit2 : exitSpaces2)
                {
                    teleport(space2, exit2, pacman);
                    break;
                }

                break;
            }
        }

        for(Block exit2 : exitSpaces2)
        {
            if(collision(pacman, exit2))
            {
                for(Block space2 : spaces2)
                {
                    teleport(exit2, space2, pacman);
                    break;
                }

                break;
            }
        }


        // TELEPORTING THE ghost FROM ONE MAP END TO OTHER
        // Handle E ↔ e warp for ghost
        for (Block space1 : spaces1) {
            for ( Block ghost : ghosts){

                if (collision(ghost, space1)) {
                    for (Block exit1 : exitSpaces1) {
                        teleport(space1, exit1, ghost);
                        break;
                    }
                    break;
                }
            }

        }

        for (Block exit1 : exitSpaces1) {
            for ( Block ghost :ghosts)
            {
                if (collision(ghost, exit1)) {
                    for (Block space1 : spaces1) {
                        teleport(exit1, space1, ghost);
                        break;
                    }
                    break;
                }
            }

        }

// Handle M ↔ m warp for ghost
        for (Block space2 : spaces2) {
            for ( Block ghost : ghosts)
            {

                if (collision(ghost, space2)) {
                    for (Block exit2 : exitSpaces2) {
                        teleport(space2, exit2, ghost);
                        break;
                    }
                    break;
                }
            }

        }

        for (Block exit2 : exitSpaces2) {
            for ( Block ghost : ghosts)
            {
                if (collision(ghost, exit2)) {
                    for (Block space2 : spaces2) {
                        teleport(exit2, space2, ghost);
                        break;
                    }
                    break;
                }
            }

        }




        // checking pacman and teleport collison

        // I added a flag called isTeleporting to ensure that teleportation happens only once per frame. When Pac-Man touches a teleport pad, we set isTeleporting = true to block further teleportation until the next frame.
        if ( isTeleporting)
        {
            // dont do anything
        }


        for ( Block tele : teleportPads)
        {
            if ( collision(tele, pacman))  // if pacmn collides with teleport pads
            {
                // The usage of Timer might not be ideal for real-time game loops. If you're working in a game loop, a better approach could be to use the System.currentTimeMillis() method or System.nanoTime() to track the time elapsed and trigger the cooldown behavior.
                // therefore we make use of tracking timer not timer
                // Let’s switch to an approach where we track the time elapsed manually to avoid using Timer. This approach will use a timestamp to track when the teleportation occurred and ensure it only happens once until a certain amount of time has passed.
                long currenttTime = System.currentTimeMillis();
                // Check if enough time has passed since last teleportation
                if (currentTime - lastTeleportTime > TELEPORT_COOLDOWN)
                {
                    isTeleporting = true; // Mark as teleporting
                    teleportPacMan(); // Teleport Pac-Man
                    lastTeleportTime = currentTime; // Update last teleport time

                }
                break; // Stop checking further once teleportation happens
            }
        }



        // speed zones
        for( Block ghost : ghosts)
        {
            boolean onSpeedZone = isOnSpeedZone(ghost); // Check if the ghost is on a speed zone (Speed tile "S")

            //  Update velocity based on the current direction
            ghost.updateVelocity();

            //  If ghost is on speed zone, increase speed (like ice sliding)
            if(onSpeedZone)
            {
                ghost.velocityX *= 2;  // Double the velocity when on speed zone
                ghost.velocityY *= 2;

            }

            // If ghost is no longer on the speed zone, reset velocity to normal
            if(!onSpeedZone)
            {
                // WHEN THIS WAS PLACED AT LAST THEN THE GHOST WERE MOVING AT 2X SPEED ALWAYS BUT WHEN I PLACED IT HERE THEN THEY STARTED MOVING NORMAL
                ghost.velocityX /= 2; // Reset speed to normal
                ghost.velocityY /= 2;
            }


            int newX = ghost.x + ghost.velocityX;
            int newY = ghost.y + ghost.velocityY;


            //  Check for collision with walls at the new position
            boolean collisionDetected = false;
            for(Block wall : walls)
            {
                if (collision(newX, newY, wall))
                {
                    // Check if the ghost's new position collides with any wall
                    collisionDetected = true;
                    break;
                }
            }

            //  If no collision, update ghost's position
            if (!collisionDetected)
            {
                ghost.x = newX;
                ghost.y = newY;
            }


        }



        // --- Toggle for Button 1 & Gate 1 ---
        boolean currentlyOnButton1 = false;

        for (Block button : Button1)
        {
            if(collision(pacman, button))
            {
                currentlyOnButton1 = true;

                if(!pacmanOnButton1)
                {
                    // Just stepped onto it
                    gate1Open = !gate1Open;

                    if(gate1Open)
                    {
                        for(Block gate : Gate1)
                        {
                            walls.remove(gate); // gate becomes walkable
                        }
                        // System.out.println("Gate 1 opened!");
                    }

                    else
                    {
                        for(Block gate : Gate1)
                        {
                            walls.add(gate); // gate becomes solid again
                        }
                        // System.out.println("Gate 1 closed!");
                    }



                }

                break;
            }
        }
        pacmanOnButton1 = currentlyOnButton1;




        // checking wall collision
        // now i want to iterate through all the walls in the hashset
        // so
        // for ( Block wall : walls){
        //     if (collision(pacman , wall))   // if collison happens between pacman and this current wall
        //     {
        //         pacman.x -= pacman.velocityX;
        //         pacman.y -= pacman.velocityY;
        //         break; // once we found a collision we dont need to continue with hashset bcz we already foubd the wall that we are colliding to
        //         // i will take a step back
        //         // what is happening here is
        //         // -> If a collision is detected, we undo the last movement, so Pac-Man stays in place.
        //         // -> Since we break out of the loop after detecting a collision, we avoid unnecessary checks for other walls.
        //         // -> Instead of stopping abruptly, we just reverse Pac-Man's last movement, making the transition feel natural.
        //         // -> Pac-Man stays in place instead of going through the wall
        //         // -> it does not means that the pacman will start moving backwards

        //     }
        // }


        for(Block wall : walls)
        {
            if(collision(pacman, wall))
            {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;

                // Completely stop movement
                pacman.velocityX = 0;
                pacman.velocityY = 0;

                hitWall = true;
                break;
            }
        }


        // now our pacman stops after colliding with the wall but it got stuck sometimes when we there is no way
        // so to make that fix the pacman should only change the direction if it is able to which basically means that it should not change the direction if there is a wall
        // therefore go towards the update direction function



        // Always check ghost button toggle, even if lava is off
        boolean currentlyGhostOnLavaButton = false;

        for (Block ghost : ghosts)
        {
            for(Block button : Button2)
            {
                if(collision(ghost, button))
                {
                    currentlyGhostOnLavaButton = true;

                    if(!ghostOnLavaButton)
                    {  // Just stepped ON it
                        lavaEnabled = !lavaEnabled;

                        if(lavaEnabled)
                        {
                            System.out.println("Lava turned ON by ghost!");
                        }

                        else
                        {
                            System.out.println("Lava turned OFF by ghost!");
                        }
                    }

                    break;  // dont check more buttons
                }
            }
            if (currentlyGhostOnLavaButton)
            {
                break;  // dont check more ghosts

            }
        }

        // 💡 Update toggle tracker each frame
        ghostOnLavaButton = currentlyGhostOnLavaButton;



        // lava collsiion
        if (lavaEnabled)
        {
            // Pac-Man lava logic
            for(Block lava : lavaTiles)
            {
                if(collision(pacman, lava))
                {
                    lives--;
                    System.out.println("Pac-Man stepped on lava! Lives: " + lives);

                    if (lives <= 0)
                    {
                        gameOver = true;
                        return;
                    }

                    resetPositions();
                    break;
                }
            }
        }

        if( lavaEnabled && (ghostLavaKillTemporary ) )
        {
            Iterator<Block> ghostIterator = ghosts.iterator();
            while (ghostIterator.hasNext())
            {
                Block ghost = ghostIterator.next();

                for (Block lava : lavaTiles)
                {
                    if (lavaEnabled && collision(ghost, lava))
                    {
                        ghostIterator.remove();  // remove from current ghosts
                        System.out.println("Ghost removed on lava!");
                        break;
                    }
                }
            }

        }



        boolean currentlyGhostOnIceButton1 = false;

        for (Block ghost : ghosts)
        {
            for (Block button : Button1)
            {
                if (collision(ghost, button))
                {
                    currentlyGhostOnIceButton1 = true;

                    if (!ghostOnButton1)
                    {
                        // Just stepped ON it
                        gate1Open = !gate1Open;

                        if (gate1Open)
                        {
                            for (Block gate : Gate1)
                            {
                                walls.remove(gate); // gate becomes walkable
                                System.out.println("gate 1 ON by ghost!");
                            }
                            // System.out.println("Gate 1 opened!");
                        }

                        else
                        {
                            for (Block gate : Gate1)
                            {
                                walls.add(gate); // gate becomes solid again
                                System.out.println("🧯 gate 2 OFF by ghost!");
                            }
                            // System.out.println("Gate 1 closed!");
                        }

                    }

                    break;  // dont check more buttons
                }
            }
            if (currentlyGhostOnIceButton1) break;  // dont check more ghosts
        }

        // 💡 Update toggle tracker each frame
        ghostOnButton1 = currentlyGhostOnIceButton1;











        // ghost collison

        for ( Block ghost : ghosts)
        {
            // for each ghost

            for (Block phantom : phantomZones)
            {
                if (collision(ghost, phantom))
                {
                    // ghost.phaseTilesRemaining = 8;  // IF WE WANT OUR GHOSTS TO CROSS ONE BLOCK OF WALL (NOT INCLUDING SPACE I.E ONLY ONE BLOCK IS TO BE CROSSED OVERALL)
                    // THEREFORE IF WE WANT TO CROSS TWO BLOCKS WHETHER IT IS TWO WALLS OR ONE WALL ONE EMPTY THEN IT WILL TAKE DOUBLE THEREFORE 16
                    // BUT IT IS NOT EFFICENT WAY TO DO WHAT WE HAVE GOT IS THE CONCEPT BEHIND IT IS
                    /*
                     *“One tile takes 8 phase units, two tiles takes 16.”

That’s because your ghost speed is tileSize / 4 = 32 / 4 = 8 pixels per frame.

And one tile is 32 pixels, so:

To move across 1 tile, it needs 4 frames.

But each frame, your ghost moves 8 pixels.

So, to fully move:

1 tile = 32 pixels / 8 pixels/frame = 4 frames

But since your game loop calls move() every frame and you decrement phaseTilesRemaining every time...

To allow 4 frames → you need phaseTilesRemaining = 4

BUT you're decrementing it per frame, not per pixel!

If you decrement once per move(), and each frame moves 8 pixels, then:

8 means 8 frames, so it crosses 2 tiles (8px * 8 = 64px = 2 tiles)

If you want 1 tile only, then 4 is enough

BUT! It may also depend on how often you're checking collisions (and where in the frame update loop)


                     */

                    // THEREFORE CORRECT WAY IS
                    //  ghost.phaseTilesRemaining = (tileSize / ghostSpeed) * numTilesToPass;

                    ghost.x += ghost.velocityX;
                    ghost.y += ghost.velocityY;

                    ghost.phaseTilesRemaining = (tileSize / 8) * 2;  // GHOST SPEED IN OUR PROGRAM IS TILESIZE/4 I.E 32/4
                    // NOTE: HERE NUMBER OF TILES TWO BCZ I THINK IT COUNTS THE PHANTOM ZONE TILE ALSO SO ONE WALL AND ONE PHANTOM ZONE THEREFORE 2


                    ghost.x += ghost.velocityX;
                    ghost.y += ghost.velocityY;


                    break;

                }
                // }

                // ghost.x += ghost.velocityX;
                //         ghost.y += ghost.velocityY;


                // Handle walls ONLY if not in phase mode
                // Only check for wall collision if ghost is NOT phasing

                if (ghost.phaseTilesRemaining > 0)
                {
                    // Ghost is phasing, so skip wall collision

                    ghost.phaseTilesRemaining--;
                }

                else
                {
                    // Ghost is not phasing, so check walls
                    for (Block wall : walls)
                    {
                        if (collision(ghost, wall))
                        {
                            ghost.x += ghost.velocityX;
                            ghost.y += ghost.velocityY;
                            ghost.updateDirection(directions[random.nextInt(4)]);
                            break;
                        }
                    }
                }

            }

            // Check if this ghost is stunned
            if (ghostStunEndTimes.containsKey(ghost))
            {
                long end = ghostStunEndTimes.get(ghost);
                if (System.currentTimeMillis() < end)
                {
                    ghost.velocityX = 0;
                    ghost.velocityY = 0;
                    continue;  // Skip movement
                }

                else
                {
                    ghostStunEndTimes.remove(ghost);  // Stun ended
                }
            }


// Check for collision with shock tile
            for (Block shock : electricShocks)
            {
                if (collision(ghost, shock))
                {
                    ghostStunEndTimes.put(ghost, System.currentTimeMillis() + 2000);  // 2 sec stun
                    ghost.velocityX = 0;
                    ghost.velocityY = 0;
                    System.out.println("Ghost stunned!");
                    continue;  // Skip movement this frame
                }
            }




            boolean inSacred = isInsideSacredZone(ghost);  // your sacred zone check

            if (inSacred)
            {
                // Slow down ghost like spider webs
                if (ghost.direction == 'L')
                {
                    ghost.velocityX = -tileSize /32;
                    ghost.velocityY = 0;
                }

                else if (ghost.direction == 'R')
                {
                    ghost.velocityX = +tileSize /32;
                    ghost.velocityY = 0;
                }

                else if (ghost.direction == 'U')
                {
                    ghost.velocityY = -tileSize /32;
                    ghost.velocityX = 0;
                }

                else if (ghost.direction == 'D')
                {
                    ghost.velocityX = 0;
                    ghost.velocityY = +tileSize /32;
                }
            }


            else
            {
                // Normal movement outside sacred zones
                // ghost.updateVelocity();   // commented out this and now the ghost are moving corectly . previously it was like they were moving very fastly on normal tiles as well
            }




            // ghost gets slow doown in slime
            boolean inSlime = isInsideSlimePuddle(ghost);


            if (inSlime)
            {
                // Slow down ghost like spider webs
                if (ghost.direction == 'L')
                {
                    ghost.velocityX = -tileSize /32;
                    ghost.velocityY = 0;
                }

                else if (ghost.direction == 'R')
                {
                    ghost.velocityX = +tileSize /32;
                    ghost.velocityY = 0;
                }

                else if (ghost.direction == 'U')
                {
                    ghost.velocityY = -tileSize /32;
                    ghost.velocityX = 0;
                }

                else if (ghost.direction == 'D')
                {
                    ghost.velocityX = 0;
                    ghost.velocityY = +tileSize /32;
                }
            }

            else
            {
                // Normal movement outside sacred zones
                // ghost.updateVelocity();   // commented out this and now the ghost are moving corectly . previously it was like they were moving very fastly on normal tiles as well
            }

            // Move the ghost
            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;

            // Wall collision
            for (Block wall : walls)
            {
                if (collision(ghost, wall))
                {
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;

                    // Pick a new direction randomly
                    char newDirection = directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                    break;
                }
            }


            // checking collision between wrap entrance and wrap exit for the ghost
            for ( Block warpIn : warpEntrance)
            {
                if ( collision ( ghost , warpIn))
                {
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;

                    // Pick a new direction randomly
                    char newDirection = directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                    break;
                }
            }

            for ( Block warpOut : warpExit)
            {
                if ( collision ( ghost , warpOut))
                {
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;

                    // Pick a new direction randomly
                    char newDirection = directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                    break;
                }
            }

            // ghost and pacman collision
            if ( collision(ghost, pacman))
            {
                soundManager.playDeathSound();
                lives -= 1;
                if (lives == 0) {
                    gameOver = true;
                    soundManager.playGameOverSound();
                    return;   // now since the game ends therefore i want to stop the gameloop so go to actionperformed
                }
                resetPositions();  // i have to declare this new function and i will acces the reset function through it
                // bcz the reset function is in the class Block and i will create this function inside the main PacMan class
                // i had made this function at last just above override

            }



            // adding a condition first to solve the problem 2 we faced
            // a loop ( trap) ....i.e there is a plain row with no obtsacles and if by chance the ghost gets into this..he does not changes direction so we did this
            if ( ghost.y == tileSize*9 && ghost.direction != 'U' && ghost.direction != 'D')
            {
                // ghost.updateDirection('U');  // instead of making the ghost only move up we can make another char array which will choose from up and down only since it is of bound 2 therefore 0 or 1 therefore first two indexes
                char newDirection2 = directions[random.nextInt(2)];
                ghost.updateDirection(newDirection2);
            }







            // Set Pac-Man image based on actual direction he's moving in
            // since in the key released function i was using this but bcz in that i had to control the if revrse control or not conditiosn so it was causing error. as when pacman was moving between two walls and when we press the up or down key the pacman up image or pacman down image gets but it shoudl not come bcz pacman is moving forward or backward thats why we did this thing here
            switch (pacman.direction)
            {
                case 'U':
                    pacman.image = pacmanUpImage;
                    break;
                case 'D':
                    pacman.image = pacmanDownImage;
                    break;
                case 'L':
                    pacman.image = pacmanLeftImage;
                    break;
                case 'R':
                    pacman.image = pacmanRightImage;
                    break;
            }




            // problem 1: now we are facing a problem that the space where i had made no food place there the ghost/pacman when passed if from left then will get out from right and vice versa


            // problem 2: since this problem is solved but now they are running on the same line they are not going else where so since theis is specially happening for a single row so we made a change in the above loop


            // now it is all done all are moving perfectly now we need to do pacman and ghost collision , pacman eating food , increasing score , showing count
            // therefore go in the class PacMan just before constructor




            // Check food collision
            Block foodEaten = null; // No food eaten yet

            for (Block food : foods) {
                if (collision(pacman, food)) {
                    // Pacman eats food
                    foodEaten = food;
                    score += 10;
                    soundManager.playEatSound();
                    // Do NOT remove inside the loop!
                    break; // Exit after first food is eaten (optional: remove if Pacman can eat multiple at once)
                }
            }

// Safe to remove outside the loop
            if (foodEaten != null) {
                foods.remove(foodEaten);
            }
            // it removes that food  . note: it should not be placed inside the for loop as we are using for each loop. so any modifications we wanna made are to done outside it
            // now we want to update the score on the screen so move towards draw function


            // now we are making ghost collison with pacman leading to resetting all the positons therefore we create a reset function in the class Block




            // if food eaten is empty then we reload the map

            // if there was single map we load like this
            // if ( foods.isEmpty())
            // {
            //     loadMap2();
            //     resetPositions();

            //     // map will be reloaded but the lives will not change
            // }


            // since now we had number of maps so

            // if ( foods.isEmpty())
            // {

            //     System.out.println("FOODS ARE EMPTY . CALLING THE LOADNEXT LEVEL FUNCTION");
            //     loadNextLevel();
            //     // System.out.println("NOW CALLING THE RESET POSITIONS FUNCTIONS");
            //     // resetPositions();


            // }




            // if ( foods.isEmpty())
            // {
            //     loadMap2();
            //     resetPositions();

            //     // map will be reloaded but the lives will not change
            // }


            updateGameLogic();


        }
    }



    private boolean isInsideSpiderNet(Block pacman)
    {
        for (Block net : spiderNets)
        {
            if (collision(pacman, net))
            {
                return true;

            }
        }
        return false;
    }


    private boolean isInsideBushes(Block pacman)
    {
        for (Block bushBlock : bush)
        {
            if (collision(pacman, bushBlock))
            {
                return true;

            }

        }
        return false;
    }



    private boolean isInsideSacredZone(Block ghost)
    {
        for (Block sacredZ : sacredZones)
        {
            if (collision(ghost, sacredZ))
            {
                return true;
            }
        }
        return false;
    }

    // private boolean isInsideStickyMist(Block ghost) {
    //     for (Block stickyM : stickyMist) {
    //         if (collision(ghost, stickyM)) {
    //             return true;
    //         }
    //     }
    //     return false;
    // }


    private boolean isInsideSlimePuddle(Block ghost)
    {
        for (Block slime : slimePuddle)
        {
            if (collision(ghost, slime))
            {
                return true;
            }
        }
        return false;
    }


    public void teleport(Block entry, Block exit, Block pacman)
    {
        // Move Pac-Man to exit tile
        pacman.x = exit.x;
        pacman.y = exit.y;

        // Offset Pac-Man based on current direction so he doesn't land inside the exit tile
        switch (pacman.direction)
        {
            case 'U':
                pacman.y -= tileSize;
                break;
            case 'D':
                pacman.y += tileSize;
                break;
            case 'L':
                pacman.x -= tileSize;
                break;
            case 'R':
                pacman.x += tileSize;
                break;
        }
    }





    // ice level checking collisoion
    public boolean isOnIce(Block pacman)
    {
        for (Block ice : iceBlocks)
        {
            if (collision(pacman, ice))
            {
                return true;
            }
        }
        return false;
    }



    // speed
    public boolean isOnSpeedZone(Block ghost)
    {
        for (Block speedZone : speedZones)
        {
            if (collision(ghost, speedZone))
            {
                return true;
            }
        }
        return false;
    }



    public boolean collision(int newX, int newY, Block block)
    {
        // Assuming each block has x, y, width, height attributes
        return newX < block.x + block.width &&
                newX + block.width > block.x &&
                newY < block.y + block.height &&
                newY + block.height > block.y;
    }


    /*
     is a collision detection function based on Axis-Aligned Bounding Box (AABB) logic, which is commonly used in 2D games.


What it does:
It checks whether a rectangle at position (newX, newY) (like Pac-Man’s future position) overlaps with the rectangle defined by the Block (like a wall, food, trap, etc.).

It assumes:
Your Block class has x, y, width, and height fields.
The object being moved (e.g. Pac-Man) has the same size as the block (block.width, block.height).


Logic breakdown:
This is how AABB collision works:

Two rectangles collide/overlap if all these are true:
The left edge of one is to the left of the right edge of the other
The right edge of one is to the right of the left edge of the other
The top edge of one is above the bottom edge of the other
The bottom edge of one is below the top edge of the other
This code checks exactly those four conditions.
     */


    // teleport pacman

    // A HashSet does not allow direct access by index. To convert it to a list, you need to create an ArrayList that contains all the elements from the HashSet
    public void teleportPacMan()
    {
        List<Block> teleportPadList = new ArrayList<>(teleportPads);
        Block randomPad;
        // this loop randomly selects the teleport pad from the hashset and it also prevents from teleporting on same pad
        do {
            randomPad = teleportPadList.get((int) (Math.random() * teleportPadList.size()));
        } while (randomPad.x == pacman.x && randomPad.y == pacman.y); // Ensure it's not the same spot

        // Move Pac-Man to the new teleport pad
        pacman.x = randomPad.x;
        pacman.y = randomPad.y;

        isTeleporting= false;
    }






//     proceding to new level
//     -> method 1: using switch case
//
//     int currentLevel = 1;
//
//     public void updateGameLogic() {
//     if (foods.isEmpty()) {
//         currentLevel++;
//
//         switch (currentLevel) {
//             case 2:
//                 loadMap2();
//                 break;
//             case 3:
//                 loadMap3();
//                 break;
//             case 4:
//                 loadMap4();
//                 break;
//             case 5:
//                 loadMap5();
//                 break;
//             case 6:
//                 loadMap6();
//                 break;
//             case 7:
//                 loadMap7();
//                 break;
//             case 8:
//                 loadMap8();
//                 break;
//             case 9:
//                 loadMap9();
//                 break;
//             case 10:
//                 loadMap10();
//                 break;
//             case 11:
//                 loadMap11();
//                 break;
//             case 12:
//                 loadMap12();
//                 break;
//             default:
//                 gameOver = true;
//                 return;
//         }
//
//         resetPositions();  // Reset positions after each level load
//     }
// }



    // instead of using n number of switch cases we can use runnables

 /*
 A Runnable in Java is an interface that represents a block of code that you want to run (later or in a new thread).
You can think of it like this:
“A thing that can be run.”


Why is Runnable useful?
Run code later (e.g. on level change, as you’re doing)

Pass code as a variable

Run something in a new thread:

in our code

we made an array of Runnables

Each this::loadMapX is a reference to a method

When you call levelLoaders[2].run();, it calls loadMap3()

So you're storing a list of “things to run” — one for each level — and running them when needed

Runnable = a task you can .run() later
It’s perfect for things like levels, animations, background tasks, or threading



*/






    int currentLevel = 0;
    public void startGame() {
        levelLoaders[currentLevel].run();  // load level 1 (index 0)
        resetPositions();
    }
    public void updateGameLogic() {
        if (foods.isEmpty()) {
            currentLevel++;

            // Check for coin collection levels
            if (currentLevel == 15 || currentLevel == 35) {
                soundManager.playCollectCoinsSound();
            }

            if (debugMapLevel + currentLevel < levelLoaders.length)
            {
                levelLoaders[debugMapLevel + currentLevel].run();
                resetPositions();
            }

            else
            {
                gameOver = true;
            }
        }
    }


    // to detect collision between pacman and wall , pacman and ghost , ghost and wall , pacman with food
    public boolean collision(Block a , Block b){  // taking two blocks
        // we will use a specific formula to detect collison between two blocks
        // note: every image/ object on our screen is a rectangle. although pacman is round but its image is a rectagngle and is not visible bcz it is transparent


        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;


        // now i am going to move function to make some checks
            /*
                This is the AABB (Axis-Aligned Bounding Box) Collision Detection formula, which checks whether two rectangular objects (like Pac-Man and ghosts, walls, or food) are colliding.
                a → Represents Pac-Man, a ghost, or any moving object.
                b → Represents another object like a wall, ghost, or food.
                For collision to occur, all these conditions must be true:

                a.x < b.x + b.width
                → Checks if the left side of a is to the left of b's right side.
                a.x + a.width > b.x
                → Checks if the right side of a is to the right of b's left side.
                a.y < b.y + b.height
                → Checks if the top side of a is above b's bottom side.
                a.y + a.height > b.y
                → Checks if the bottom side of a is below b's top side.
                If all four conditions are met, it means a and b are overlapping, so collision has occurred.



                Pac-Man collides with walls → Stop movement.
                ->Pac-Man collides with food → Eat food & increase score.
                ->Pac-Man collides with a ghost → Lose a life or game over.
                ->Ghosts colliding with walls → Change direction.
            */




    }



    public void resetPositions()
    {
        pacman.reset();  // this sets the x , and y positions back to the original position
        // now since it is resetted therefore now i dont want the pacman to move until the user press a key so
        pacman.velocityX = 0;
        pacman.velocityY = 0;
        // lets do the same for each ghost
        for ( Block ghost : ghosts)
        {
            ghost.reset();
            // updating the velocity
            // in case of ghost it will give them a new direction so
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }





    @Override   // this gets created automatically when i press quick fix at the error i was getting when written implements ActionListener
    public void actionPerformed(ActionEvent e)
    {
        move();
        repaint();   // it is going to call paintComponent

        if ( gameOver)   // if this is true
        {
            gameLoop.stop();  // then do this
        }

        // but we noticed that it got stuck at the same place but i want to reset everything to same position when i press any key.  so go to key released





    }// in order to make this function run we need a game loop so we create a timer

    // therefore
    // updating the positions of all the objects and redraw the frame and update again and redraw again

    // now what we had to do is we need to check if the pacman is colliding against the wall. if so then we need to stop it so lets create a function for it



    // keylistner automatically generates three methods when done quickfix
    @Override
    public void keyTyped(KeyEvent e) {  // when we press a key that has a corresponding character

    }



    @Override
    public void keyPressed(KeyEvent e) {  // it is basically when you press on any key . as long as i press a key i would trigger this function. i can hold onto the key as well

    }



    @Override
    public void keyReleased(KeyEvent e) {   // it only gets triggered when we press a key and let it go and release the key
        // System.out.println("KeyEvent: "+ e.getKeyCode());
        // now to make our key presses work . we need to add two things in our constructor adding key listener

        // now i comment out that printing statement

        if (gameOver)
        {
            levelLoaders[debugMapLevel].run();// reloading the map . this is done bcz i want to add all the foods back into the hashset
            resetPositions(); // resetting the positions
            // now we want each object a new direction to move in  so
            lives = 3;  // making lives again 3
            score  = 0;  // making score again 0
            gameOver = false;  // setting gameover to be false
            gameLoop.start();  // starting the game loop
            return;  // exiting this function so it doesn't check key presses when restarting
        }

        // now there maybe a case where the pacman has eaten all the food so i can load new levels but here i will load the same map

        // ----------- WALL CHECKING STARTS HERE ------------
        // storing the next position of pacman
        // Assuming you already have pacman.direction and tileSize set
        int futureX = pacman.x;
        int futureY = pacman.y;

        if (controlsReversed)
        {
            if (e.getKeyCode() == KeyEvent.VK_UP)
            {
                futureY += tileSize / 4;
                if (!willHitWall(futureX, futureY)) pacman.updateDirection('D');
            }


            else if (e.getKeyCode() == KeyEvent.VK_DOWN)
            {
                futureY -= tileSize / 4;
                if (!willHitWall(futureX, futureY)) pacman.updateDirection('U');
            }

            else if (e.getKeyCode() == KeyEvent.VK_LEFT)
            {
                futureX += tileSize / 4;
                if (!willHitWall(futureX, futureY)) pacman.updateDirection('R');
            }

            else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            {
                futureX -= tileSize / 4;
                if (!willHitWall(futureX, futureY)) pacman.updateDirection('L');
            }
        }

        else
        {
            if (e.getKeyCode() == KeyEvent.VK_UP)
            {
                futureY -= tileSize / 4;
                if (!willHitWall(futureX, futureY)) pacman.updateDirection('U');
            }

            else if (e.getKeyCode() == KeyEvent.VK_DOWN)
            {
                futureY += tileSize / 4;
                if (!willHitWall(futureX, futureY)) pacman.updateDirection('D');
            }


            else if (e.getKeyCode() == KeyEvent.VK_LEFT)
            {
                futureX -= tileSize / 4;
                if (!willHitWall(futureX, futureY)) pacman.updateDirection('L');
            }

            else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            {
                futureX += tileSize / 4;
                if (!willHitWall(futureX, futureY)) pacman.updateDirection('R');
            }
        }





    }



    private boolean willHitWall(int futureX, int futureY)
    {
        for (Block wall : walls) {
            if (collision(futureX, futureY, wall)) {
                return true;
            }
        }
        return false;
    }

}

// yes
