// the idea here is i am going to have PacMan inherit in JPanel. basically i am going to have this pacman class inherit the JPanel so that this class is basically a version of JPanel


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

import javax.swing.*;
import java.util.Iterator;





public class PacMan extends JPanel implements ActionListener , KeyListener{   // i have written extends JPanel bcz this PacMan class inherits JPanel

    
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
                    // then we gonna take a step back 
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






    
    
        

    

    //X = wall, O = skip(dont do anything), P = pac man, ' ' = food   
    //Ghosts: b = blue, o = orange, p = pink, r = red


    // THEME 1 : LEVEL 1 TO 5
    /*
     PACMAN = P
     BUSHES = :
     WALSS = X
     SPACEENTRY = .
     EXIT ENTRY = E
     red ghost = r
     orange ghost = a

     black boxes [ non one can enter there ] = <
     */
    

     // problem : ghost looks very small 
    private String[] tileMap1 ={     // tileMap is an array of strings
        
    "<<<<<<<<<<<<<<<<<<<<<" ,
    "<XXXXXXXXXXXXXXXXXXX<" ,
    "<X       :X        X<" ,
    "<X XXXXXX:X XXXXXX X<" ,
    "<X X     :       X X<" ,
    "<X X XXXXXXXXXXX X X<" ,
    "<X                 X<" ,
    "<XXXXX XXXXX XXXXX X<" ,
    "<X                 X<" ,
    "<X XXXXXXX XXXXXXX X<" ,
    "<X        r        X<" ,
    "<X XXXXXXX XXXXXXX X<" ,
    "E:::  P          :::." ,
    "<X XXXXX XXXXX XXXXX<" ,
    "<X              :  X<" ,
    "<X X XXXXXXXXXXX:X X<" ,
    "<X X            :X X<" ,
    "<X XXXXXX X XXXXXX X<" ,
    "<X        X        X<" ,
    "<X XXXXXXXXXXXXXXX X<" ,
    "<X      :::::      X<" ,
    "<XXXXXXXXXXXXXXXXXXX<" ,
    "<<<<<<<<<<<<<<<<<<<<<" ,

          
                
                          

};
    // this is the tile map which i am using we can create anything which we want
    // here we have array of strings and each string is a row 

    // now i had to go through the tile map and create objects like walls , foods , and ghosts , etc
    // for this i create another function below the PacMan function and i name it as loadMap



    // level 2: easy

    // problem : orange ghost is also tiny
    private String[] tileMap2 = {


"<<<<<<<<<<<<<<<<<<<<<" ,
"<XXXXXXXXXXXXXXXXXXX<" ,
"<X        X        X<" ,
"<X XXXXXX:X:XXXXXX X<" ,
"<X X  X  :X:  X  X X<" ,
"<X X XX X:X:X XX X X<" ,
"<X X             X X<" ,
"<X X XX XXXXX XX X X<" ,
"<X        r        X<" ,
"<XXX X XXX XXX X XXX<" ,
"E::       a       ::." ,
"<XXX X XXX XXX X XXX<" ,
"<X   X         X   X<" ,
"<X X XX XXXXX XX X X<" ,
"<X X             X X<" ,
"<X X XXXX:X:XXXX X X<" ,
"<X X     :P:     X X<" ,
"<X XXXXXX:X:XXXXXX X<" ,
"<X        X        X<" ,
"<X XXXXXXXXXXXXXXX X<" ,
"<X                 X<" ,
"<XXXXXXXXXXXXXXXXXXX<" ,
"<<<<<<<<<<<<<<<<<<<<<" ,

    };



// level 3: easy

private String[] tileMap3 = {
"<<<<<<<<<<<<<<<<<<<<<" ,
"<XXXXXXXXXXXXXXXXXXX<" ,
"<X         a       X<" ,
"<X XXX XXXXXXX XXX X<" ,
"<X X::           X X<" ,
"<X X:XXXXX XXXXX X X<" ,
"<X X X         X X X<" ,
"<X X X XXXXXXX X X X<" ,
"<X   X    r    X::::E" ,
"<XXX XXXXXXXXXXX:XXX<" ,
"<X        :     :  X<" ,
"<XXX XXXXXXXXXXX:XXX<" ,
"<X   X         X   X<" ,
"<X X X XXXXXXX X X X<" ,
"<X X X         X X X<" ,
"<X X XXXXX XXXXX X X<" ,
".::::    P         X<" ,
"<X XXX XXXXXXX XXX X<" ,
"<X X       X     X:X<" ,
"<X XXXXXXX X XXXXX:X<" ,
"<X                :X<" ,
"<XXXXXXXXXXXXXXXXXXX<" ,
"<<<<<<<<<<<<<<<<<<<<<" ,


};



// level 4: easy

private String[] tileMap4 = {
"<<<<<<<<<<<<<<<<<<<<<" ,
"<XXXXXXXXXXXXXXXXXXX<" ,
"<X       :X        X<" ,
"<X XXXXXX:X XXXXXX X<" ,
"<X    :::: r       X<" ,
"<XXX XXXXXXXXXXX XXX<" ,
"<X                 X<" ,
"<X XX XX XXXXX XX:XX<" ,
"<X    X         X: X<" ,
"<X XX X XXXXXXX X:XX<" ,
".       a           E" ,
"<X:XX:X XXXXXXX X XX<" ,
"<X:: :X         X  X<" ,
"<X XX XX XXXXX XX XX<" ,
"<X                 X<" ,
"<XXX XXXXXXXXXXX XXX<" ,
"<X        P        X<" ,
"<X XXXXXX X XXXXXX X<" ,
"<X X::    X      X X<" ,
"<X X:XXXXXXXXXXXXX X<" ,
"<X  :              X<" ,
"<XXXXXXXXXXXXXXXXXXX<" ,
"<<<<<<<<<<<<<<<<<<<<<" ,


};



// level 5: easy

// problem : in this bushes teleports the pacman [ RESOLVED]
private String[] tileMap5 = {
"<<<<<<<<<<<<<<<<<<<<<" ,
"<XXXXXXXXXXXXXXXXXXX<" ,
"<X           r     X<" ,
"<X XXXXX:XXX XXXXX X<" ,
"<X X   a:X X     X X<" ,
"<X X XXX:X X X X X X<" ,
"<X X X     X X X X X<" ,
"<X X X XXX X X X X X<" ,
"<X   X X   X X X   X<" ,
"<XXX X   X X X X XXX<" ,
".:::   X X   X   :::E" ,
"<XXX X X XXX X X XXX<" ,
"<X   X X     X X   X<" ,
"<X X X XX XX X X X X<" ,
"<X X X ::: X X X X X<" ,
"<X X XXX X X X X X X<" ,
"<X X     X X     X X<" ,
"<X XXXXX XXX XXXXX X<" ,
"<X    ::: P  :::   X<" ,
"<X XXXXX XXX XXXXX X<" ,
"<X                 X<" ,
"<XXXXXXXXXXXXXXXXXXX<" ,
"<<<<<<<<<<<<<<<<<<<<<" ,


};


// level 6: easy
// theme 2: 6-10

/*
 
PACMAN = P
     SPEED ZONES = S
     WALLS = [
     SPACEENTRY = .
     EXIT ENTRY = E
     red ghost = g
     orange ghost = s
     PINK GHOST = t
     WRAP ENTRANCE = N
     WRAP EXIT = &

 */


 // problem : ghost images are wrong . their background needs to be removed , speed zones are not looking speed zone
private String[] tileMap6 = {
"<<<<<<<<<<<<<<<<<<<<<" ,
"<[[[[[[[[[[[[[[[[[[[<" ,
"<[               SSE<" ,
"<[ [[[[[ [[[[[ [[[ [<" ,
"<[ [  s[ [   [ [   [<" ,
"<[ [ [ [ [ [ [ [ [ [<" ,
"<[ [ [ [ [ [ [ [ [ [<" ,
"<[ [ [ [[[ [ [[[ [ [<" ,
"<[ [ [   g [     [ [<" ,
"<[ [S[[[ [[[ [[[[[ [<" ,
"<[ [SSS      [     [<" ,
"<[ [[[[[[[[[S[ [[[[[<" ,
"<[    t    [S[     [<" ,
"<[ [[[[[[[[[S[ [[[ [<" ,
"<[ [     [ [     [ [<" ,
"<[ [ [[[ [ [[[ [ [ [<" ,
"<[ [ [   [   [ [ [ [<" ,
"<[ [ [ [[[[[ [ [ [ [<" ,
"<[ [ [       [ [ [ [<" ,
"<[ [ [[[[[[[[[ [ [ [<" ,
".  P               [<" ,
"<[[[[[[[[[[[[[[[[[[[<" ,
"<<<<<<<<<<<<<<<<<<<<<" ,


};

// level 7: easy


// problem is : the pacman can enter from wrapentrance and exit from exit. but the wrap exit is not working as wall it should. and for ghosts the wrap entrance and exit must be like wall but here it is not
// resolved the problem
private String[] tileMap7 = {

"<<<<<<<<<<<<<<<<<<<<<" ,
"<[[[[[[[[[[N[[[[[[[[<" ,
"<[        [        [<" ,
"<[S[[[[[[ [ [[[[[[ [<" ,
"<[S[      [      [ [<" ,
"<[S[ [[[[[[[[[[[ [ [<" ,
"<[S[      g s    [ [<" ,
"<[S[[[[[[[ [ [[[[[[[<" ,
"<[       [ [ [     [<" ,
"<[[[[[[[ [ [S[ [[[[[<" ,
".           S       E" ,
"<[[[[[[[ [ [S[ [[[[[<" ,
"<[       [St [     [<" ,
"<[ [[[[[[[S[ [[[[[[[<" ,
"<[ [      S      [ [<" ,
"<[ [ [[[[[[[[[[[ [ [<" ,
"<[ [      P      [ [<" ,
"<[ [[[[[[ [ [[[[[[ [<" ,
"<[  SSS   [        [<" ,
"<[[[[[[[[[[ [[[[[[[[<" ,
"<[                 [<" ,
"<[[[[[[[[[[&[[[[[[[[<" ,
"<<<<<<<<<<<<<<<<<<<<<" ,

};


// level 8: easy


// problem : in thi the same problem is their [ resolved]
private String[] tileMap8 = {

"<<<<<<<<<<<<<<<<<<<<<" ,
"<[&[[[[[[[[[[[[[[[[[<" ,
"<[S  g    [        [<" ,
"<[S[[[[[[ [ [[[[[[ [<" ,
"<[S[s           t[ [<" ,
"<[ [ [[[[[ [[[[[ [ [<" ,
"<[ [ [ SSS SSS [ [ [<" ,
"<[ [ [ [[   [[ [ [ [<" ,
"<[   [ [     [ [   [<" ,
"<[[[ [ [[[[[[[ [ [[[<" ,
".SSS             SSSE" ,
"<[[[ [ [[[[[[[ [ [[[<" ,
"<[   [ [     [ [   [<" ,
"<[ [ [ [[[S[[[ [ [ [<" ,
"<[ [ [         [ [ [<" ,
"<[ [ [[[[[ [[[[[ [ [<" ,
"<[ [      P      [ [<" ,
"<[S[[[[[[ [ [[[[[[ [<" ,
"<[S[      [      [ [<" ,
"<[S[ [[[[[[[[[[[[[ [<" ,
"<[                 [<" ,
"<[[[[[[[[[[[[[[[[[N[<" ,
"<<<<<<<<<<<<<<<<<<<<<" ,


};


// level 9: easy

private String[] tileMap9 = {

"<<<<<<<<<<<<<<<<<<<<<" ,
"<[[[[[[[[[[[[[&[[[[[<" ,
"<[         s       [<",
"<[ [[[S[[[ [[[S[[[ [<",
"<[ [[[S[[   [[S[[[ [<",
"<[ [[[S[     [S[[[ [<",
"<[        g        [<",
"<[ [[[ [[[[[[[ [[[ [<",
"<[                 [<",
"<[ [[[[[[[ [[[[[[[ [<",
".     SSSSSSSt      E",
"<[ [[[[[[[ [[[[[[[ [<",
"<[                 [<",
"<[ [[[S[[[[[[[ [[[ [<",
"<[    S   P        [<",
"<[ [[[S[     [ [[[ [<",
"<[ [[[ [[ S [[ [[[ [<",
"<[ [[[ [[[S[[[ [[[ [<",
"<[        S        [<",
"<[ [[ [[[[[[[[[[[[S[<",
"<[             SSSS[<",
"<[[[[N[[[[[[[[[[[[[[<",
"<<<<<<<<<<<<<<<<<<<<<",


};


// level 10: easy

// SAME PROBLEM [ rsolved]
private String[] tileMap10 = {

"<<<<<<<<<<<<<<.<<<<<<" ,
"<[[[[[[[[[[[[[ [[[[[<" ,
"<[  t           s  [<" ,
"<[ [[[[[ [ [ [[[[[ [<" ,
"<[ [   [ [S[ [   [ [<" ,
"<[ [ [ [ [S[ [ [ [ [<" ,
"<[ [ [ [ [S[ [ [ [ [<" ,
"<[S[ [ [[[S[[[ [ [S[<" ,
"<[S[ [     g   [ [S[<" ,
"<[S[ [[[[[N[[[[[ [S[<" ,
"<[S[             [S[<" ,
"<[S[[[[[ [[[ [[[[[S[<" ,
"<[       [ P       [<" ,
"<[S[[[[[ [[[ [[[[[S[<" ,
"<[S[             [S[<" ,
"<[S[ [ [[[&[[[ [ [S[<" ,
"<[S[ [         [ [S[<" ,
"<[S[ [ [[[[[[[ [ [S[<" ,
"<[ [ [    S    [ [ [<" ,
"<[ [ [ [[[S[[[ [ [ [<" ,
"<[       SSS       [<" ,
"<[[ [[[[[[[[[[[[[[[[<" ,
"<<<E<<<<<<<<<<<<<<<<<",


};





// level 11: easy

// theme 3: 11-14

/*
 
PACMAN = P
     
     WALLS = ]
     SPACEENTRY1 = .
     EXIT ENTRY1 = E
     SPACEENTRY2 = -
     EXIT ENTRY2 = e
     red ghost = h
     orange ghost = d
     PINK GHOST = b
     blue GHOST = y
     teleport = T
     electric shock = Q

 */



 // PROBLEM : ANIMATION OF ELECTRIC SHOCK IS TO BE ADDED[ add a full screen animation to the screen which looks amazing ], GHOST SIZE INREASE
private String[] tileMap11 = {

"<<<E<<<<<<<<<<<<E<<<<" ,
"<]] ]]]]]]]]]]]] ]]]<" ,
"<]          d      ]<" ,
"<] ]]] ]]]] ]]]] ]]]<" ,
"<] ]  b            ]<" ,
"<] ] ]]]]]]]]]] ]] ]<" ,
"<] ] ]           ] ]<" ,
"<] ] ] ]]]]h ] ] ] ]<" ,
"<] ] ] ]     ] ] ] ]<" ,
"<] ] ]   ]]] ] ] ] ]<" ,
"e    ] ] ] ] ] ] ] Q-" ,
"<] ]]] ] ] y ] ] ]]]<" ,
"<] ]   ] ]   ] ] ] ]<" ,
"<] ] ]]] ]]] ] ] ] ]<" ,
"<] ] ]         ] ] ]<" ,
"<] ] ] ]]]]]]] ] ] ]<" ,
"<] ] ]    P    ] ] ]<" ,
"<] ] ]]]]]]]]]]] ] ]<" ,
"<] ]   Q           ]<" ,
"<] ]]]]]]]] ]]]]]]]]<" ,
"<]                 ]<" ,
"<]]]]] ]]]]]]]]]] ]]<" ,
"<<<<<<.<<<<<<<<<<.<<<",


};

// level 12: easy


// PROBLEM :  GHOST SIZE LESS
private String[] tileMap12 = {

"<<<<<<<<<<<<<<<<<<<<<" ,
"<]]]]]]]]]]]]]]]]]]]<" ,
"<]        Q        ]<" ,
"<] ] ]]]]]]]]]]] ] ]<" ,
"<] ]      h      ] ]<" ,
"<] ]]]]] ]]]]] ]]] ]<" ,
"<] ]             ] ]<" ,
"<] ] ]]]]]]]]]]] ] ]<" ,
"<] ]      y      ] ]<" ,
"<] ]]]]] ]]]]] ]]]]]<" ,
"E   T     ] P  Q    ." ,
"<] ]]]]] ]]]]] ]]]]]<" ,
"<] ]      d      ] ]<" ,
"<] ] ]]]]]]]]]]] ] ]<" ,
"<] ]             ] ]<" ,
"<] ]]]]] ]]]]] ]]] ]<" ,
"<] ]      b      ] ]<" ,
"<] ] ]]]]]]]]]]] ] ]<" ,
"<]                 ]<" ,
"<]]]]]]]]]]]]]]]]]]]<" ,
"<]        T        ]<" ,
"<]]]]]]]]]]]]]]]]]]]<" ,
"<<<<<<<<<<<<<<<<<<<<<" ,


};



// medium levels

// level 13 : medium

// PROBLEM : SAME
private String[] tileMap13 = {

"<<<<<<<<<<<<<<<<<<<<<" ,
"<]]]]]]]]]]]]]]]]]]]<" ,
"<]       ]     ]   ]<" ,
"<] ]]] ] ]]] ] ] ] ]<" ,
"<] ]   ]  h  ] ] ] ]<" ,
"<] ] ]]]]] ]]] ] ] ]<" ,
"<] ]d      ] T   ] ]<" ,
"<] ]]]]] ] ] ]]]]] ]<" ,
"<]     ]Q] ] ]     ]<" ,
"<]]] ] ] ] ] ] ] ]]]<" ,
"E    ] ] ]y] ] ]    ." ,
"<]]] ] ] ] ] ] ] ]]]<" ,
"<]   ] ] ] ] ] ]   ]<" ,
"<] ]]] ] ] ] ]]] ] ]<" ,
"<] ]     ]   Q   ] ]<" ,
"<] ] ]]] ]]]]]]] ] ]<" ,
"<] ] ]b            ]<" ,
"<] ] ]]]]] ]]]]] ] ]<" ,
"<]   T   ] P ]     ]<" ,
"<]]]]]]] ] ] ]]]]]]]<" ,
"<]         ]       ]<" ,
"<]]]]]]]]]]]]]]]]]]]<" ,
"<<<<<<<<<<<<<<<<<<<<<" ,

    
    
};


// level 14: medium

private String[] tileMap14 = {

"<<<<<<<<<<<<<<<<<<<<<" ,
"<]]]]]]]]]]]]]]]]]]]]<" ,
"<]   ]  b    ]     ]<" ,
"<] ] ] ]]]]]T] ]]] ]<" ,
"<] ] ]  h  ] ] ]   ]<" ,
"<] ] ]]] ] ] ] ] ]]]<" ,
"<] ]   ] ] ] ] ]   ]<" ,
"<] ]]] ] ] ] ]]]]] ]<" ,
"<] ]Q  ] ] ]     ] ]<" ,
"<] ] ]]] ] ]]]]] ] ]<" ,
"e    ] ]y] ]   ]    -" ,
"<] ]]] ] ] ] ] ]]] ]<" ,
"<]     ] ] ] ]     ]<" ,
"<] ]]]]] ] ] ]]] ]]]<" ,
"<] ]     ] ] ]     ]<" ,
"<] ] ]]]]] ] ]]]]] ]<" ,
"<]   ]d    ]Q      ]<" ,
"<] ]]]]] ]]]]]]]]] ]<" ,
"<]     ] P   ]     ]<" ,
"<]]]T] ]]]]] ] ]]] ]<" ,
"<]   ]       ]     ]<" ,
"<]]]]]]]]]]]]]]]]]]]<" ,
"<<<<<<<<<<<<<<<<<<<<<" ,



};


// level 15


// THEME 4: BONUS MAP
// only pacman 
// bonus1wall = {

// PROBLEM : NOT EATING FOOD
private String[] tileMap15 = {

"<<<<<<<<<<e<<<<<<<<<<" ,
"<{{{{{{{{{ {{{{{{{{{<" ,
"<{       {         {<" ,
"<{ {{{{{ { {{{{{{  {<" ,
"<{ {   {     {     {<" ,
"<{ { { {{{{{ { {{ {{<" ,
"<{ { {       {     {<" ,
"<{ { {{{ { {{{{{ { {<" ,
"<{ {     {     { { {<" ,
"<{ {{ {{ {{{{{ { { {<" ,
"-     {             e" ,
"<{ {{{{  {{ {{ {{{ {<" ,
"<{    {      { {   {<" ,
"<{ {{ {{{{{{{{ { {{{<" ,
"<{ {         { {   {<" ,
"<{ { {{{{{{{ { {{{ {<" ,
"<{   {           { {<" ,
"<{ {{{{{ {{{{{ {{{ {<" ,
"<{       P         {<" ,
"<{{{{{{{ {{{ {{{{{{{<" ,
"<{                 {<" ,
"<{{{{{{{ {{{{{{{{{{{<" ,
"<<<<<<<<-<<<<<<<<<<<<" ,

};



// LEVEL 16

// theme 5: 16-20

/*
 
PACMAN = P
     
     WALLS = }
     SPACEENTRY1 = .
     EXIT ENTRY1 = E
     SPACEENTRY2 = -
     EXIT ENTRY2 = e
     red ghost = j
     orange ghost = f
     PINK GHOST = n
     blue GHOST = >
     ICE TILES = I
     Button 1 = 1
     gate 1 = A

 */


 // problem :  ghost size increase , when pacman is at ice then the game stucks i.e ghost stop moving [ resolved this issue ]  , ICE GATE IMAGE CHNAGE
private String[] tileMap16 = {

"<<<<<<<<<<<<<<<-<<<<<" ,
"<}}}}}}}}}}}}}} }}}}<" ,
"<}       }      f  }<" ,
"<}I}}}}} } }}}}}}} }<" ,
"<}I}   }  j  }     }<" ,
"<}I} } }}}}} } }}}}}<" ,
"<}I} }       }     }<" ,
"<}I} } }} }} }}}} }}<" ,
"<}I}      }     } }}<" ,
"<}I} }}}} }}}} }} }}<" ,
".         n      IIIE" ,
"<} } }}}} }}}}} } }}<" ,
"<} }      }     }  }<" ,
"<} } } }}} } }}}} }}<" ,
"<} } }       }     }<" ,
"<} } }}}} }}}}} }}I}<" ,
"<}   }  >  }       }<" ,
"<} }}}} } }}}}} }}I}<" ,
"<}       P         }<" ,
"<}}} }}}I} }}}}}} }}<" ,
"<}      I  III     }<" ,
"<}}}}}}}I}}}}}}}}}}}<" ,
"<<<<<<<<e<<<<<<<<<<<<" ,



};


// LEVEL 17 : MEDIUM [TELEPORT]


// problem :  ice stuck here also[ fixed this ], gate are first lokk like black square only and pacman can pass through it but after button toggle it makes the cave image, and now the pacman cannot pass through it [ fixed]
// fixed
private String[] tileMap17 = {

"<<E<<<<<<<<<<<<<<<<<<" ,
"<} }}}}}}}}}}}}}}}}}<" ,
"<} III   }         }<" ,
"<}I}}}}} } }}}}}}} }<" ,
"<}I}A  }  n  }     }<" ,
"<} } } }}}}} } }}}}}<" ,
"<} } }  P    }  III}<" ,
"<} } }}}} }}}} } }I}<" ,
"<} }     }     } }I}<" ,
"<} }}}} } }}}} } }I}<" ,
"e     }   1j    }   -" ,
"<}I}}} }}} } }}}}} }<" ,
"<}I  }>      }     }<" ,
"<}I} }}}}}}}}} }}} }<" ,
"<} }           }   }<" ,
"<} }}}}}}}}}}}}} } }<" ,
"<}   }  f      A   }<" ,
"<} } }}}}}}}}}}} } }<" ,
"<} }             } }<" ,
"<}}}}}} }}} }}}} }I}<" ,
"<}          IIII  I}<" ,
"<}}}}}}}}}}}}}}}}}I}<" ,
"<<<<<<<<<<<<<<<<<<.<<" ,




};


// 
// LEVEL 18: MEDIUM [ TELEPORT ADVANCED]
// PROBLEM : SAME ISSUES
// fixed
private String[] tileMap18 = {

"<<<<<<<<<<<<<<<<<<<<<" ,
"<}}}}}}}}}}}}}}}}}}}<" ,
"<}   f   }       I }<" ,
"<} }}}}} }A}}}}}}}A}<" ,
"<} }   }  j  }   I }<" ,
"<} } } }}}}} } }}}}}<" ,
"<}I} }       }     }<" ,
"<}I} }}}}} }}}I} } }<" ,
"<}I}     }     } } }<" ,
"<}I}}}}} }I}}}I} } }<" ,
".     }    P     IIIE" ,
"<}I}}}1}}}I} }}}}} }<" ,
"<}I  }    I  }     }<" ,
"<}I} }}}}}I}}} }}} }<" ,
"<}I}           }   }<" ,
"<} }}}}}}} }}} } } }<" ,
"<}   }1 n I    }   }<" ,
"<} } }}}}}I} }}} } }<" ,
"<} }     }       } }<" ,
"<}}}}}}}A}}}A}}}}}}}<" ,
"<}          >      }<" ,
"<}}}}}}}}}}}}}}}}}}}<" ,
"<<<<<<<<<<<<<<<<<<<<<" ,




};


// // LEVEL 19: MEDIUM [BUTTON GATES]
// // B -> BUTTON1 WHICH TOGGLES G WHICH IS GATE 1
// // Q -> BUTTON 2 WHICH TOGGLES H WHICH IS GATE 2
// private String [] tileMap19 = {

//     "XXXXXXXXXXXXXXXXXXX",
//     "X B      b        X",
//     "X XXXXX XXXXX XXX X",
//     "X X   X X X X X   X",
//     "X XGGGX X X X XHHHX",
//     "X X H X r o b X G X",
//     "X XXHXX XXXXX XXGXX",
//     "X     X     X     X",
//     "XXXXX X XXX X XXXXX",
//     "X     X X X X     X",
//     "X XXXXX X X XXXXX X",
//     "O   H       G     O",
//     "X XXXXXXX XXXXXXX X",
//     "X X     X X     X X",
//     "X X XXX X X XXX X X",
//     "X X X       X X X X",
//     "X X XXXXXpXXXXX X X",
//     "X X           X X X",
//     "X XXXXXXXXXXXXX X X",
//     "X               Q X",
//     "XXXXXXXXXXXXXXXXXXX"

// };



// // LEVEL 20: MEDIUM [ BUTTON GATES ADVANCED]
// private String[] tileMap20 = {
//     "XXXXXXXXXXXXXXXXXXX",
//     "X B    X       b X",
//     "X XXX XXXXXXXX XXX",
//     "X X G     X    H X",
//     "X X G XXXXXXXX H X",
//     "X X G X r  b X H X",
//     "X XXXXX XXXXXXX XX",
//     "X   X     X     X ",
//     "XXX X XXXXXXX X XX",
//     "X   X X     X X  X",
//     "X XXX X XXX X XXX ",
//     "O     G  X H     O",
//     "X XXXXXXX XXXXXXXX",
//     "X X     X X      X",
//     "X X XXX X X XXXX X",
//     "X X X       H X  X",
//     "X X XXXXXpXXXXXX X",
//     "X XQ           X X",
//     "X XXXXXXXXXXXXXX X",
//     "X           G    X",
//     "XXXXXXXXXXXXXXXXXXX"
// };



// PROBLEM : SAME
// fixed
private String[] tileMap19 = {

"<<<<<<<<<<<<<<<<<<<<<" ,
"<}}}}}}}}}}}}}}}}}}}<" ,
"<}       }      j  }<" ,
"<} }}}}} }A}}}}}}} }<" ,
"<} }   }  f  }     }<" ,
"<} } } }}}}} } }}}}}<" ,
"<} }1}   >   }     }<" ,
"<} } }}}}}A}}}}} } }<" ,
"<} }     }     } }I}<" ,
"<} }}}}} } }}} } }I}<" ,
".   }   }n    }1IIIIE" ,
"<} }}} }}}}} }}}}} }<" ,
"<}   }   I   }     }<" ,
"<} } }}}}I}}}} }}} }<" ,
"<} }     I     }   }<" ,
"<}I}}}}}}}}}}} } } }<" ,
"<}I  }     }1} }   }<" ,
"<}I} }}}A}}} } }}} }<" ,
"<}I}    I} P     } }<" ,
"<}}}}}}}I}}}A}}}}}}}<" ,
"<}     III         }<" ,
"<}}}}}}}}}}}}}}}}}}}<" ,
"<<<<<<<<<<<<<<<<<<<<<" ,




};


// PROBLEM : SAME
// fixed
private String[] tileMap20 = {

"<<<<<<<<<<<<<<<<<<<<<" ,
"<}}}}}}}}}}}}}}}}}}}<" ,
"<}A n   }          }<" ,
"<} }}}}} } }}}}}}} }<" ,
"<} }    j  }       }<" ,
"<} } } }}}}}A} }}}}}<" ,
"<} } }       }     }<" ,
"<} } }}}}}f}}}}} } }<" ,
"<} }         } }   }<" ,
"<} }}}}}A} }}} } } }<" ,
"e   IIII   1    IIII-" ,
"<} }}} }}}A} }}}}} }<" ,
"<}   }       }     }<" ,
"<} } }}}}}}}}} }}} }<" ,
"<} }           }   }<" ,
"<} } }}}}}}}}} } } }<" ,
"<}   A  >  }   }   }<" ,
"<} } }}}}} } }}} } }<" ,
"<}       } P     } }<" ,
"<}}} }}} }}} }}} }}}<" ,
"<}                 }<" ,
"<}}}}}}}}}}}}}}}}}}}<" ,
"<<<<<<<<<<<<<<<<<<<<<" ,



};


// level 21: 

// theme 6: 21-24

/*
 
PACMAN = P
     
     WALLS = `
     SPACEENTRY1 = .
     EXIT ENTRY1 = E
     SPACEENTRY2 = -
     EXIT ENTRY2 = e
     red ghost = k
     orange ghost = z
     PINK GHOST = m
     blue GHOST = O
     LAVA =  V
     TELEPORT 2 = %
     BUTTON 2 = 2 
     GATE 2 = B

 */


 // PROBLEM : GHOST SIZE INCREASE
private String[] tileMap21 = {

    

"<<<<<<<<<<<<<<<<<<<<<" ,
"<```````````````````<" ,
"<`    k            `<" ,
"<` ` `````V` ``` ` `<" ,
"<` `       `   O ` `<" ,
"<` ` ``` ` ` ``` ` `<" ,
"<` ` `   ` ` ` ` ` `<" ,
"<` ` ` ``` ` ` ` ` `<" ,
"<` ` `     `V` %   `<" ,
"<` ` ````` ` ` `````<" ,
".      `  Vm    `   E" ,
"<` ```` `````` ``` `<" ,
"<` `        `      `<" ,
"<` ` ``````V` ``````<" ,
"<` ` `           ` `<" ,
"<` ` ` ````` ``` ` `<" ,
"<`   ` V   z   `   `<" ,
"<` ````` ``` ````` `<" ,
"<` `     ` P `     `<" ,
"<` ` ````` ``````` `<" ,
"<`   %     V       `<" ,
"<```````````````````<" , 
"<<<<<<<<<<<<<<<<<<<<<" ,




};


// PROBLEM : BUTTON TOGGLE IS NOT WORKING [ fixed ]
private String[] tileMap22 = {

    


"<<<<<<<<<<<<<<<<<<<<<" ,
"<```````````````````<" ,
"<` k              %`<" ,
"<` ````` `2``` ``` `<" ,
"<` `VV ` ` ` ` `   `<" ,
"<` `V` ` ` ` ` ` ```<" ,
"<` ` `   `V`   `   `<" ,
"<` ` ```V V V``` ` `<" ,
"<` `    V   V  z ` `<" ,
"<` `````V V V````` `<" ,
".       VVVVV       E" ,
"<` `````V V V````` `<" ,
"<` ` m  V   V    ` `<" ,
"<` ` ```V`V`V``` ` `<" ,
"<` ` `   `V`   ` ` `<" ,
"<` ` ` ``` ``` `V` `<" ,
"<`   `         `V` `<" ,
"<` ``` ``````` `V` `<" ,
"<` ` VVVV O     `V `<" ,
"<` ` ````` `````V` `<" ,
"<`  %     P    2   `<" ,
"<```````````````````<" ,
"<<<<<<<<<<<<<<<<<<<<<" ,




};



// problem : same [ fixed ]
private String[] tileMap23 = {

"<<<<<<<<<<<<<<<<e<<<<" ,
"<``````````````` ```<" ,
"<`   z          2VV`<" ,
"<` ``` ````````` `V`<" ,
"<`   `V`       `  V`<" ,
"<` ` `V` ````` ``` `<" ,
"<` ` `V` `   `   ` `<" ,
"<` ` ``` ` ` ``` ` `<" ,
"<` `VVVVV` m `   ` `<" ,
"<` ```````` `` ` ` `<" ,
".           O       E" ,
"<` ``````` ` ` ``` `<" ,
"<`       `V`     ` `<" ,
"<` ````` `V` ``` ` `<" ,
"<` `     `Vk `2` ` `<" ,
"<` ` ````` ``` ` ` `<" ,
"<`V` `  VVVVV    ` `<" ,
"<`V` ``````````` ` `<" ,
"<`V`      P       V`<" ,
"<` ``````` ```````V`<" ,
"<`                V`<" ,
"<``` ```````````````<" ,
"<<<<-<<<<<<<<<<<<<<<<" ,




};



private String[] tileMap24 = {

"<<<<<<<<<<<<<<<<<<<<<" ,
"<```````````````````<" ,
"<`                 `<" ,
"<`%` ````` ``` ``` `<" ,
"<` ` `   `V` ` ` `%`<" ,
"<` ` ` ` `V` ` ` ` `<" ,
"<` ` ` ` `Vk` ` ` ``<" ,
"<` ` ` ` ``` ` ` ` `<" ,
"<`   ` `     `   ` `<" ,
"<` ``` ``` ``` ``` `<" ,
". VVVV    m         E" ,
"< ````` ``` ```V````<" ,
"<`     `     `VV   `<" ,
"<` ``` ` ``` ` ``` `<" ,
"<`%`V` ` `z` ` `V  `<" ,
"<` ` ` ` ` ` ` ` ` `<" ,
"-   `   `   `     ` e" ,
"< ` ````` ``` ``````<" ,
"<`V      P    VVV  `<" ,
"<`V`````` ````````%`<" ,
"<`VV          O    `<" ,
"<```````````````````<" ,
"<<<<<<<<<<<<<<<<<<<<<" ,


};


// theme 7: 25

/*

bonus 2
 
PACMAN = P
     
     WALLS = /
     SPACEENTRY1 = .
     EXIT ENTRY1 = E
     SPACEENTRY2 = -
     EXIT ENTRY2 = e
     

 */

 // problem :  pacman not eating food

private String[] tileMap25 = {
    
"<<<<<<<<<<<<<<<<<<<<<" ,
"<///////////////////<" ,
"</       /         /<" ,
"</ ///// / ///// / /<" ,
"</ /   / / /   / / /<" ,
"</ / / / / / / /// /<" ,
"</ / / /   / /   / /<" ,
"</ / /// /// /// / /<" ,
"</ /     /     / / /<" ,
"</ ///// / ///// / /<" ,
".      /   /        E" ,
"</ /// /// /// /// /<" ,
"</ /         / / / /<" ,
"</ / /////// / / / /<" ,
"</ /         / / / /<" ,
"</ / / /// / / / / /<" ,
"</ /           / / /<" ,
"</ / /////// /// / /<" ,
"</   /   P       / /<" ,
"</ /// /////////// /<" ,
"</     /           /<" ,
"<///////////////////<" , 
"<<<<<<<<<<<<<<<<<<<<<" ,



};




// theme 8: 26-30

/*
 
PACMAN = P
     
     WALLS = _
     SPACEENTRY1 = .
     EXIT ENTRY1 = E
     SPACEENTRY2 = -
     EXIT ENTRY2 = e
     red ghost = y
     orange ghost = x
     PINK GHOST = q
     blue GHOST = K
     phantom = ~
     reverse = R

 */


 // problem :  the phantom zoens are not working for ghost[fixed] , wall image change[ fixed]
private String[] tileMap26 = {

"<<<<<<<<<<<<<<<<<<<<<" ,
"<___________________<" ,
"<_      ~_~      y _<" ,
"<_ _______________ _<" ,
"<_ _~  _ _ _~  ~ _ _<" ,
"<_ _ _ _ _ _ __ __ _<" ,
"<_ _ _ _ _ _ _ _ _ _<" ,
"<_ _ _ _   _ _ q _ _<" ,
"<_ _ _ _ _ _ _ _ _ _<" ,
"<_ _ _  x  _ _ _ _ _<" ,
"e    _____ _ _ _~   -" ,
"<_ _ _  K  _ _ _ _ _<" ,
"<_ _ _ ___ _   _ _ _<" ,
"<_ _~_ _ _ _ ___ _ _<" ,
"<_ _____   _ _~  _ _<" ,
"<_ _~_ _ _ _ _ ___ _<" ,
"<_ _ _    ~_ _     _<" ,
"<_ _ _______ _____ _<" ,
".         P         E" ,
"<_ _______ _______ _<" ,
"<_                 _<" ,
"<___________________<" ,
"<<<<<<<<<<<<<<<<<<<<<" ,




};


    // LEVEL 27: 


    // problem same
private String[] tileMap27 = {

"<<<<<<<<<<<<<<<<<<<<<" ,
"<___________________<" ,
"<_                 _<" , 
"<_ _____ _____ ___ _<" , 
"<_ _~ ~_ _ _ _ _~  _<" , 
"<_ _ _ _ _ _ _ _ _ _<" , 
"<_ _ K _ y   x _~  _<" , 
"<_ __ __ _____ __ __<" , 
"<_     _     _     _<" , 
"<_____ _ ___ _ _____<" , 
"<_    ~_ _~_ _~    _<" , 
"<_ _____ _ _ _____ _<" , 
"e        P          -" , 
"<_ _______ _______ _<" , 
"<_ _     _ _     _ _<" , 
"<_ _ ___ _ _ ___ _ _<" , 
"<_ _ _       _~_ _ _<" , 
"<_ _ _____q___ _ _ _<" , 
"<_ _~           ~_ _<" , 
"<_ _____________ _ _<" , 
"<_                 _<" , 
"<___________________<" ,
"<<<<<<<<<<<<<<<<<<<<<" ,





};


// SAME PROBLEM
private String[] tileMap28 = {

"<<<<<<<<<<<<<<<<.<<<<" ,
"<_______________R___<" ,
"<_    y_    x_     _<" ,
"<___ ___ ___ ___ _ _<" ,
"e   R_       _~  _ _<" ,
"<_ ___ _____ ___ _ _<" ,
"<_ _       _  ~_ _ _<" ,
"<_ _ _ _ ___ _ _ _ _<" ,
"<_ _q_   _   _ _ _ _<" ,
"<_ _ _ _ _ ___ _ _ _<" ,
"<_   _ _ _ _~      _<" ,
"<___ _ _ _ _ _____ _<" ,
"<_   _ _~    _   _ _<" ,
"<_ ___ _____ ___ _ _<" ,
"<_ _~        _K  _ _<" ,
"<_ _ _______ ___ _ _<" ,
"<_ _ _~    _ _   _ _<" ,
"<_ _ _ ___ _ _ ___ _<" ,
"<_ _ _   _ _ _R     -" ,
"<_ _ ___ _ _ _____ _<" ,
"<_       P         _<" ,
"<_____ _____________<" ,
"<<<<<<E<<<<<<<<<<<<<<" ,



};

private String[] tileMap29 = {

"<<<<<<<<<<<<<<<<<-<<<" ,
"<________________ __<" ,
"<_        K        _<" ,
"<_ _____ ___ _____ _<" ,
"<_ _~  _         _ _<" ,
"<_ _ _R_______ _ _ _<" ,
"<_ _ _     q   _ _ _<" ,
"<_ _ _ ___ _____ _ _<" ,
"<_ _       _     _ _<" ,
"<_ ___ _ _ _ ___ _ _<" ,
".     ~_ y _~       E" ,
"<_ ___ _ _ _ ___ _ _<" ,
"<_ _   _ _ _     _ _<" ,
"<_ _ ___ _ ___ ___ _<" ,
"<_ _     _     _   _<" ,
"<_ _____ _ _____ _ _<" ,
"<_ _~    x      ~_ _<" ,
"<_ _ _________ ___ _<" ,
"<_ _    P          _<" ,
"<_ _____ _____ ___ _<" ,
"<_                 _<" ,
"<__R________________<" ,
"<<<e<<<<<<<<<<<<<<<<<" ,


};



// PROBLEM WALL IS DIFFERENT IT SHOULD NOT BE [ fixed ]
private String[] tileMap30 = {

"<<<<<<<<<<<<<<<<<-<<<" ,
"<________________ __<" ,
"<_ q       _    K  _<" ,
"<_ _______ _ _____ _<" ,
"<_ _~   ~_ _ _  ~_ _<" ,
"<_ _ _ _ _ _ _ _ _ _<" ,
"<_ _ _ _ _ _ _     _<" ,
"<_ _ _ _ _ _ _ _ _ _<" ,
"<_   _ _   R   _   _<" ,
"<_____ _____ ___ ___<" ,
"<_     _~ x  _     _<" ,
"<_ ___ _ ___ _ ___ _<" ,
".    _   P     _    E" ,
"<_ ___ _____ ___ _ _<" ,
"<_ _~        _ _ _ _<" ,
"<_ _ _ ___ ___ _ _ _<" ,
"<_ _ _    y    _ _ _<" ,
"<_ _ _____ _ _ _ _ _<" ,
"<_ _~        _  ~_ _<" ,
"<_ _________ _ ___ _<" ,
"<_           _     _<" ,
"<_____R_____________<" ,
"<<<<<<e<<<<<<<<<<<<<<" ,



};





// theme 9: 31-35

/*
 
PACMAN = P
     
     WALLS -> =
     SPACEENTRY1 = .
     EXIT ENTRY1 = E
     SPACEENTRY2 = -
     EXIT ENTRY2 = e
     red ghost = u
     orange ghost = c
     PINK GHOST = w
     blue GHOST = H
     LAVA =  V
     SACRED = Z
     SLIME = L
     SPIDER = W

 */


 // no problem only ghost size small
private String[] tileMap31 = {

"<<<<<<<<<<<<<<<<<<<<<" ,
"<===================<" ,
"<=      Z      u   =<" ,
"<= ======= ======= =<" ,
"<= =  V  = =     = =<" ,
"<= = === = = = = = =<" ,
"<= = =   c   = = = =<" ,
"<= = = ===== = = = =<" ,
"<=   = Z     = =   =<" ,
"<= = ===============<" ,
".   V    =w   Z     E" ,
"<= = ===== === =====<" ,
"<=   =   Z   =     =<" ,
"<= = = ===== = === =<" ,
"<= = =   H   = = = =<" ,
"<= = === = === = = =<" ,
"<= =   V   Z   = = =<" ,
"<= ======= ===== = =<" ,
"<=   Z   = P     = =<" ,
"<= ===== === === = =<" ,
"<=   V     Z     = =<" ,
"<===================<" ,
"<<<<<<<<<<<<<<<<<<<<<" ,



    
};


private String[] tileMap32 = {

    
"<<<<<<<<<<<<<<<<<<<<<" ,
"<===================<" ,
"<=        Z        =<" ,
"<= === === === === =<" ,
"<= = =     u   = = =<" ,
"<= = === === === = =<" ,
"<=   =    V    =   =<" ,
"=== = ======= = ====<" ,
"<=   =    H    =   =<" ,
"<= === === === === =<" ,
".        =c=        E" ,
"<= === === === === =<" ,
"<=   =   Z     =   =<" ,
"=== = ======= = ====<" ,
"<=   =    w    =   =<" ,
"<= = === === === = =<" ,
"<= = =    V    = = =<" ,
"<= = = ======= = = =<" ,
"<= =      P      = =<" ,
"<= = = = = = = = = =<" ,
"<=                 =<" ,
"<===================<" ,
"<<<<<<<<<<<<<<<<<<<<<" ,




};


private String[] tileMap33 = {

"<<<<<<<<<<<<<<<<<<<<<" ,
"<===================<" ,
"<=                 =<" ,
"<= ====L=====L==== =<" ,
"<= =      =      = =<" ,
"<= =====L=L == = = =<" ,
"<= = =   H= =  = = =<" ,
"<= = = ==== =  = = =<" ,
"<=L= =  u     =  = =<" ,
"<= = = == ==== =   =<" ,
".  = L         L    E" ,
"<= = ======== ==   =<" ,
"<= = =   c     = = =<" ,
"<= = == ======== = =<" ,
"<=   =         = = =<" ,
"<= = =========== = =<" ,
"<= =   L     L   = =<" ,
"<= = ==L=== ==L= = =<" ,
"<=  =      w       =<" ,
"<= = ===== === === =<" ,
"<=      P          =<" ,
"<===================<" ,
"<<<<<<<<<<<<<<<<<<<<<" ,




};

private String[] tileMap34 = {

"<<<<<<<<<<<<<<<<<<<<<" ,
"<= = = = = = = = = =<" ,
"<=       =          -" ,
"<= ===== = ======= =<" ,
"<= =       L     = =<" ,
"<= = ======== ==== =<" ,
"<=                 =<" ,
"===== V===== V==== =<" ,
"<=     = c = =   = =<" ,
"<= === = = = = = = =<" ,
"<= = H = = W = = u =<" ,
"<= === === === === =<" ,
".                   E" ,
"<= ===== === ===== =<" ,
"<= =     = =     = =<" ,
"<= = === = = === = =<" ,
"<= = = P     = = = =<" ,
"<= = ======= = = = =<" ,
"<=       =       = =<" ,
"===== == = = ===== =<" ,
"e          =       =<" ,
"<===================<" ,
"<<<<<<<<<<<<<<<<<<<<<" ,







};

private String[] tileMap35 = {

"<<<<<<<<<<<<<<<<<<<<<" ,
"<===================<" ,
"<=       =    =     E" ,
"<= === = ===  = = ==<" ,
"<= = = = =      = ==<" ,
"<=== = = = ====== ==<" ,
"<=   = = =        ==<" ,
"<= === = ======== ==<" ,
"<= =    c     W =  =<" ,
"<= ===== ===== ==  =<" ,
"-      = =   V      e" ,
"<==== === == =======<" ,
"<=   H =   = P     L<" ,
"<= === == == ===== =<" ,
"<=   =       =     =<" ,
"=== ======= = === ==<" ,
"<=         = = =   =<" ,
"<= ======= = = = ===<" ,
"<=   u   =   =     =<" ,
"===== === ======== =<" ,
".      =   L     = =<" ,
"<===================<" ,
"<<<<<<<<<<<<<<<<<<<<<" ,





};



// GAME ENDING IN NEXT TWO MAPS

private String[] tileMap36  = {


"<<<<<<<<<<<<<<<<<<<<<" ,
"<===================<" ,
"<=     r     =     =<" ,
"<= ========= = === =<" ,
"<= =Y      =   =   =<" ,
"<= = ===== ===== = =<" ,
"<=Y= =Y  =       = =<" ,
"<=Y= === = ======= =<" ,
"<=Y=     = =Y    = =<" ,
"<=Y===== = ===== = =<" ,
"<=Y=Y  = b       = =<" ,
"<=Y= = ========= = =<" ,
"<=Y= =       =YY=  =<" ,
"<= = ===== = ==== ==<" ,
"<=       = = P     =<" ,
"<======= = = =======<" ,
"<=Y    = = =     =Y=<" ,
"<= === = = ===== = =<" ,
"<= =   = p       = =<" ,
"<= = =========== = =<" ,
"<=      YY  o      =<" ,
"<===================<" ,
"<<<<<<<<<<<<<<<<<<<<<" ,




};

private String[] tileMap37 = {


"<<<<<<<<<<<<<<<<<<<<<" ,
"<= = = = = = = = = =<" ,
"<=       =  r      =<" ,
"<= ===== = ======= =<" ,
"<= =   = = =     = =<" ,
"<= = = = = = === = =<" ,
"<= = = =c=   = =   =<" ,
"<= = = === === =====<" ,
"<= = =   = =   =   =<" ,
"<= = === = = === = =<" ,
"<=     =   =   = = =<" ,
"<===== ===== = = = =<" ,
"<=   = =  c= = = = =<" ,
"<= = = = === = === =<" ,
"<= = = =     =     =<" ,
"<= = = ======= =====<" ,
"<= = =  c    = b   =<" ,
"<= = = ===== ===== =<" ,
"<= =   P  =  p     =<" ,
"<= ===== = ======= =<" ,
"<=     = c    o    =<" ,
"<= = = = = = = = = =<" ,
"<<<<<<<<<<<<<<<<<<<<<" ,



};


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
    

    

    // now we had done the representation of the objects in the game 
    // moving further with the tile map




    // creating a timer for game loop
    Timer gameLoop;
    // now inside the below constructor i initialize this variable




    // making the ghost move
    // creating an array of character
    char [] directions = {'U' , 'D' , 'R' , 'L'}; // up down right left
    // creating a rando object
    Random random = new Random();
    // basically for each ghost we are going to randomly select the direction
    // moving to the constructor PacMan



    // adding reamining things like score , lives and etc
    int score =0;
    int lives  = 3; // by default we start with 3 lives 
    boolean gameOver = false;  
    // pacman has 3 lives and if he collides with the ghost it will lose one life and when lives = 0 then game over becomes true . and if it is gameover then we stop the pacman from moving 
    
    // lets starts with the score
    // everytime the packman collides the the food it gonna eat it and gain 10 points . and everytime the packman eats the food we want to remove that food from hashSet

    // go to move function





    // creating a constructor ( constrictor is a special method that rus automaticaly when an object of the class is created)
    PacMan(){   // here the PacMan constructor sets up the game board ( which extends JPanel)

        setPreferredSize(new Dimension(widthBoard , heightBoard));   // this sets the preffered size of the JPanel which means it tells the layout manager that widht = widthboard and height = heightboard
        setBackground(Color.BLACK);

        addKeyListener(this);  // when the class PacMan implements keylistener it takes on the properties of keylistener . so i dont need to create a seperate key listener object i can just reference pacman using this 
        // this keylistner is going to listner the three functions which are written keytyped, keypressed , keyreleased to process the key processes

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

         therefore we had three main repaeting things - wall , ghost , and food so i will create a 3 different hash sets to ietrate through it and i need not have a hashset for pacman bcz it is used a single time and hence i will create a variable for it 

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



         // checking number of walls , foods , ghosts
         loadMap24();  // calling the function loadmap bcz we want to check the number of walls , foods , ghosts and its algo is written inside the function loadMap();
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
        

    }  //  this is the end of PacMan function


    

    public void loadMap1(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>();  
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();  // " "

        
        teleportPads = new HashSet<Block>(); // T AND %  // these hashsets like teleport and iceblocks even if you are not using them in the current level but if you have somehwere in the program the usage of them then you had to implement this here also. 
        iceBlocks = new HashSet<Block>();   // I   // bcz we have used somehwere in our program the for loop which iterates over this hashset so it doesn’t matter if there are actually any ice blocks in the map — if the HashSet was never initialized, then Java throws a NullPointerException.
        // therefore Always initialize iceBlocks, even if it's empty .This way, your code won’t crash if it tries to iterate over or check iceBlocks, because it will just be an empty set instead of null.
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



        for ( int r = 0 ; r<rowCount ; r++)   // r stands for row
        {
            for (  int c = 0 ; c<colCount ; c++)   // c stands for column
            {
                String row = tileMap1[r];   // getting the current row bcz tileMap is our map and r index is the row index so we are getting the loop indexing 
                char tileMapChar = row.charAt(c);  // getting the current character
                // what we had done in the above two lines is that we getting the row and then we are getting the character present at that row with the col index
                // we had declared two variables 1) row 2) tileMapChar
                // the string row represents the single row in the tile map from index r=0 to r<rowCount
                // now we are using inbuilt string operations charAt() to acces the character present at that position
                // row.charAt(c) acceses the character from the row at index c where c represents the columns 


                // we got a char at a specific tile now we need to figure out where this tile is. therefore to draw we need x , y positions and width and height of tile 

                // getting x position ( it will be the 'how many columns we are from the left')
                int x = c*tileSize;
                // getting y position (  it will be the 'how many rows from the top')
                int y = r*tileSize;



                if ( tileMapChar == 'X'){  // theme 1: wood wall

                    Block wood = new Block(woodWallImage , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMapChar == '<')
                {
                    Block bS = new Block(null , x , y , tileSize , tileSize);  
                    blackSpace.add(bS); 
                }

                else if ( tileMapChar == '['){  // theme 2: desert wall

                    Block wood = new Block(desertWallImage , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMapChar == ']'){  // theme 3: sea wall

                    Block wood = new Block(seaWallImage , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMapChar == '{'){  // theme 4: bonus 1 wall

                    Block wood = new Block(bonus1Wall , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMapChar == '}'){  // theme 5: frost wall

                    Block wood = new Block(frostWallImage , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMapChar == '`'){  // theme 6: lava wall

                    Block wood = new Block(lavaWallImage , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMapChar == '/'){  // theme 7: bonus 2 wall

                    Block wood = new Block(bonus2Wall , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMapChar == '_'){  // theme 8:  machine wall

                    Block wood = new Block(machineWallImage , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMapChar == '='){  // theme 9: moltenspire wall

                    Block wood = new Block(moltenSphireWallImage , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }


                
                
                else if ( tileMapChar == ' ')  // food
                {
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMapChar == '.')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMapChar == 'E')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMapChar == '-')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMapChar == 'e')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }

                else if( tileMapChar == 'a')   // orange ghost 1 [theme 1]
                {

                    Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost1);
                }

                else if( tileMapChar == 's')   // orange ghost 2 [ theme 2]
                {

                    Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost2);
                }

                else if( tileMapChar == 'd')   // orange ghost 3 [ thme 3]
                {

                    Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost3);
                }

                else if( tileMapChar == 'f')   // orange ghost 4 [ theme 5]
                {

                    Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost4);
                }

                else if( tileMapChar == 'z')   // orange ghost 5 [theme 6]
                {

                    Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost5);
                }

                else if( tileMapChar == 'x')   // orange ghost 6 [ theme 8]
                {

                    Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost6);
                }

                else if( tileMapChar == 'c')   // orange ghost 7 [ theme 9]
                {

                    Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost7);
                }

                
                else if( tileMapChar == 'r')   // red ghost 1
                {

                    Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost1);
                }

                else if( tileMapChar == 'g')   // red ghost 2
                {

                    Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost2);
                }

                else if( tileMapChar == 'h')   // red ghost 3
                {

                    Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost3);
                }

                else if( tileMapChar == 'j')   // red ghost4
                {

                    Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost4);
                }


                else if( tileMapChar == 'k')   // red ghost5
                {

                    Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost5);
                }


                else if( tileMapChar == 'y')   // red ghost6
                {

                    Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost6);
                }

                else if( tileMapChar == 'u')   // red ghost7
                {

                    Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost7);
                }


                else if( tileMapChar == 't')   // pink ghost 1
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }

                else if( tileMapChar == 'b')   // pink ghost 2
                {

                    Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost2);
                }

                else if( tileMapChar == 'n')   // pink ghost 3
                {

                    Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost3);
                }

                else if( tileMapChar == 'm')   // pink ghost 4
                {

                    Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost4);
                }

                else if( tileMapChar == 'q')   // pink ghost 5
                {

                    Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost5);
                }

                else if( tileMapChar == 'w')   // pink ghost 6
                {

                    Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost6);
                }



                else if( tileMapChar == 'Y')   // blue ghost 1
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }

                else if( tileMapChar == 'U')   // blue ghost 2
                {

                    Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost2);
                }
                
                else if( tileMapChar == 'O')   // blue ghost 3
                {

                    Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost3);
                }

                else if( tileMapChar == 'K')   // blue ghost 4
                {

                    Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost4);
                }

                else if( tileMapChar == 'J')   // blue ghost 5
                {

                    Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost5);
                }


                else if ( tileMapChar == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }

                
                else if ( tileMapChar == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }

                else if ( tileMapChar == '%')
                {
                    Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                    teleportPads.add(teleport2);
                }

                else if ( tileMapChar == ':')
                {
                    Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                    bush.add(bushe);
                }

                else if ( tileMapChar == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }
                                
                else if ( tileMapChar == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }

                else if ( tileMapChar == '~')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tileMapChar == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }


                
                else if ( tileMapChar == 'N')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMapChar == '&')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }


                else if ( tileMapChar == '1')
                {
                    Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                    Button1.add(button1);
                }

                else if ( tileMapChar == 'A')
                {
                    Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                    Gate1.add(gate1);
                    walls.add(gate1);
                }


                else if ( tileMapChar == '2')
                {
                    Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                    Button2.add(button2);
                }

                else if ( tileMapChar == 'B')
                {
                    Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                    Gate2.add(gate2);
                    walls.add(gate2);
                }

                else if ( tileMapChar == 'W')
                {
                    Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                    spiderNets.add(web);
                }

                else if ( tileMapChar == 'V')
                {
                    Block lava = new Block(lavaImage, x, y, tileSize, tileSize);
                    lavaTiles.add(lava);
                }

                else if ( tileMapChar == 'Z')
                {
                    Block sacred = new Block ( sacredZone , x, y , tileSize , tileSize);
                    sacredZones.add(sacred); 
                }

                

                else if ( tileMapChar == 'L')
                {
                    Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                    slimePuddle.add(slimemist);
                }

                else if (tileMapChar == 'Q')
                {
                    Block electricS = new Block(electric, x, y, tileSize, tileSize);
                    electricShocks.add(electricS);
                }

                
                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    }; 


    public void loadMap2(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>(); 
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();  // " "


        
        teleportPads = new HashSet<Block>(); // T AND %  // these hashsets like teleport and iceblocks even if you are not using them in the current level but if you have somehwere in the program the usage of them then you had to implement this here also. 
        iceBlocks = new HashSet<Block>();   // I   // bcz we have used somehwere in our program the for loop which iterates over this hashset so it doesn’t matter if there are actually any ice blocks in the map — if the HashSet was never initialized, then Java throws a NullPointerException.
        // therefore Always initialize iceBlocks, even if it's empty .This way, your code won’t crash if it tries to iterate over or check iceBlocks, because it will just be an empty set instead of null.
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
        blackSpace = new HashSet<Block>(); // <



        for ( int r = 0 ; r<rowCount ; r++)   // r stands for row
        {
            for (  int c = 0 ; c<colCount ; c++)   // c stands for column
            {
                String row = tileMap2[r];   // getting the current row bcz tileMap is our map and r index is the row index so we are getting the loop indexing 
                char tileMap2Char = row.charAt(c);  // getting the current character
                // what we had done in the above two lines is that we getting the row and then we are getting the character present at that row with the col index
                // we had declared two variables 1) row 2) tileMapChar
                // the string row represents the single row in the tile map from index r=0 to r<rowCount
                // now we are using inbuilt string operations charAt() to acces the character present at that position
                // row.charAt(c) acceses the character from the row at index c where c represents the columns 


                // we got a char at a specific tile now we need to figure out where this tile is. therefore to draw we need x , y positions and width and height of tile 

                // getting x position ( it will be the 'how many columns we are from the left')
                int x = c*tileSize;
                // getting y position (  it will be the 'how many rows from the top')
                int y = r*tileSize;



                if ( tileMap2Char == 'X'){  // theme 1: wood wall

                    Block wood = new Block(woodWallImage , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMap2Char == '<')
                {
                    Block bS = new Block(null , x , y , tileSize , tileSize);  
                    blackSpace.add(bS); 
                }

                else if ( tileMap2Char == '['){  // theme 2: desert wall

                    Block wood = new Block(desertWallImage , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMap2Char == ']'){  // theme 3: sea wall

                    Block wood = new Block(seaWallImage , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMap2Char == '{'){  // theme 4: bonus 1 wall

                    Block wood = new Block(bonus1Wall , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMap2Char == '}'){  // theme 5: frost wall

                    Block wood = new Block(frostWallImage , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMap2Char == '`'){  // theme 6: lava wall

                    Block wood = new Block(lavaWallImage , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMap2Char == '/'){  // theme 7: bonus 2 wall

                    Block wood = new Block(bonus2Wall , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMap2Char == '_'){  // theme 8:  machine wall

                    Block wood = new Block(machineWallImage , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMap2Char == '='){  // theme 9: moltenspire wall

                    Block wood = new Block(moltenSphireWallImage , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }


                
                
                else if ( tileMap2Char == ' ')  // food
                {
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMap2Char == '.')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMap2Char == 'E')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMap2Char == '-')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMap2Char == 'e')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }

                else if( tileMap2Char == 'a')   // orange ghost 1 [theme 1]
                {

                    Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost1);
                }

                else if( tileMap2Char == 's')   // orange ghost 2 [ theme 2]
                {

                    Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost2);
                }

                else if( tileMap2Char == 'd')   // orange ghost 3 [ thme 3]
                {

                    Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost3);
                }

                else if( tileMap2Char == 'f')   // orange ghost 4 [ theme 5]
                {

                    Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost4);
                }

                else if( tileMap2Char == 'z')   // orange ghost 5 [theme 6]
                {

                    Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost5);
                }

                else if( tileMap2Char == 'x')   // orange ghost 6 [ theme 8]
                {

                    Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost6);
                }

                else if( tileMap2Char == 'c')   // orange ghost 7 [ theme 9]
                {

                    Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost7);
                }

                
                else if( tileMap2Char == 'r')   // red ghost 1
                {

                    Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost1);
                }

                else if( tileMap2Char == 'g')   // red ghost 2
                {

                    Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost2);
                }

                else if( tileMap2Char == 'h')   // red ghost 3
                {

                    Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost3);
                }

                else if( tileMap2Char == 'j')   // red ghost4
                {

                    Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost4);
                }


                else if( tileMap2Char == 'k')   // red ghost5
                {

                    Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost5);
                }


                else if( tileMap2Char == 'y')   // red ghost6
                {

                    Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost6);
                }

                else if( tileMap2Char == 'u')   // red ghost7
                {

                    Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost7);
                }


                else if( tileMap2Char == 't')   // pink ghost 1
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }

                else if( tileMap2Char == 'b')   // pink ghost 2
                {

                    Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost2);
                }

                else if( tileMap2Char == 'n')   // pink ghost 3
                {

                    Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost3);
                }

                else if( tileMap2Char == 'm')   // pink ghost 4
                {

                    Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost4);
                }

                else if( tileMap2Char == 'q')   // pink ghost 5
                {

                    Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost5);
                }

                else if( tileMap2Char == 'w')   // pink ghost 6
                {

                    Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost6);
                }



                else if( tileMap2Char == 'Y')   // blue ghost 1
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }

                else if( tileMap2Char == 'U')   // blue ghost 2
                {

                    Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost2);
                }
                
                else if( tileMap2Char == 'O')   // blue ghost 3
                {

                    Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost3);
                }

                else if( tileMap2Char == 'K')   // blue ghost 4
                {

                    Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost4);
                }

                else if( tileMap2Char == 'J')   // blue ghost 5
                {

                    Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost5);
                }


                else if ( tileMap2Char == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }

                
                else if ( tileMap2Char == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }

                else if ( tileMap2Char == '%')
                {
                    Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                    teleportPads.add(teleport2);
                }

                else if ( tileMap2Char == ':')
                {
                    Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                    bush.add(bushe);
                }

                else if ( tileMap2Char == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }
                                
                else if ( tileMap2Char == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }

                else if ( tileMap2Char == '~')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tileMap2Char == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }


                
                else if ( tileMap2Char == 'N')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMap2Char == '&')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }


                else if ( tileMap2Char == '1')
                {
                    Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                    Button1.add(button1);
                }

                else if ( tileMap2Char == 'A')
                {
                    Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                    Gate1.add(gate1);
                    walls.add(gate1);
                }


                else if ( tileMap2Char == '2')
                {
                    Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                    Button2.add(button2);
                }

                else if ( tileMap2Char == 'B')
                {
                    Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                    Gate2.add(gate2);
                    walls.add(gate2);
                }

                else if ( tileMap2Char == 'W')
                {
                    Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                    spiderNets.add(web);
                }

                else if ( tileMap2Char == 'V')
                {
                    Block lava = new Block(lavaImage, x, y, tileSize, tileSize);
                    lavaTiles.add(lava);
                }

                else if ( tileMap2Char == 'Z')
                {
                    Block sacred = new Block ( sacredZone , x, y , tileSize , tileSize);
                    sacredZones.add(sacred); 
                }

                

                else if ( tileMap2Char == 'L')
                {
                    Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                    slimePuddle.add(slimemist);
                }

                else if (tileMap2Char == 'Q')
                {
                    Block electricS = new Block(electric, x, y, tileSize, tileSize);
                    electricShocks.add(electricS);
                }

                

                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    }; 



    public void loadMap3(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>();  
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();  // " "

        
        teleportPads = new HashSet<Block>(); // T AND %  // these hashsets like teleport and iceblocks even if you are not using them in the current level but if you have somehwere in the program the usage of them then you had to implement this here also. 
        iceBlocks = new HashSet<Block>();   // I   // bcz we have used somehwere in our program the for loop which iterates over this hashset so it doesn’t matter if there are actually any ice blocks in the map — if the HashSet was never initialized, then Java throws a NullPointerException.
        // therefore Always initialize iceBlocks, even if it's empty .This way, your code won’t crash if it tries to iterate over or check iceBlocks, because it will just be an empty set instead of null.
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
        blackSpace = new HashSet<Block>(); // <



        for ( int r = 0 ; r<rowCount ; r++)   // r stands for row
        {
            for (  int c = 0 ; c<colCount ; c++)   // c stands for column
            {
                String row = tileMap3[r];   // getting the current row bcz tileMap is our map and r index is the row index so we are getting the loop indexing 
                char tileMap3Char = row.charAt(c);  // getting the current character
                // what we had done in the above two lines is that we getting the row and then we are getting the character present at that row with the col index
                // we had declared two variables 1) row 2) tileMapChar
                // the string row represents the single row in the tile map from index r=0 to r<rowCount
                // now we are using inbuilt string operations charAt() to acces the character present at that position
                // row.charAt(c) acceses the character from the row at index c where c represents the columns 


                // we got a char at a specific tile now we need to figure out where this tile is. therefore to draw we need x , y positions and width and height of tile 

                // getting x position ( it will be the 'how many columns we are from the left')
                int x = c*tileSize;
                // getting y position (  it will be the 'how many rows from the top')
                int y = r*tileSize;



                if ( tileMap3Char == 'X'){  // theme 1: wood wall

                    Block wood = new Block(woodWallImage , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMap3Char == '<')
                {
                    Block bS = new Block(null , x , y , tileSize , tileSize);  
                    blackSpace.add(bS); 
                }

                else if ( tileMap3Char == '['){  // theme 2: desert wall

                    Block wood = new Block(desertWallImage , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMap3Char == ']'){  // theme 3: sea wall

                    Block wood = new Block(seaWallImage , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMap3Char == '{'){  // theme 4: bonus 1 wall

                    Block wood = new Block(bonus1Wall , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMap3Char == '}'){  // theme 5: frost wall

                    Block wood = new Block(frostWallImage , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMap3Char == '`'){  // theme 6: lava wall

                    Block wood = new Block(lavaWallImage , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMap3Char == '/'){  // theme 7: bonus 2 wall

                    Block wood = new Block(bonus2Wall , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMap3Char == '_'){  // theme 8:  machine wall

                    Block wood = new Block(machineWallImage , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMap3Char == '='){  // theme 9: moltenspire wall

                    Block wood = new Block(moltenSphireWallImage , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }


                
                
                else if ( tileMap3Char == ' ')  // food
                {
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMap3Char == '.')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMap3Char == 'E')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMap3Char == '-')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMap3Char == 'e')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }

                else if( tileMap3Char == 'a')   // orange ghost 1 [theme 1]
                {

                    Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost1);
                }

                else if( tileMap3Char == 's')   // orange ghost 2 [ theme 2]
                {

                    Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost2);
                }

                else if( tileMap3Char == 'd')   // orange ghost 3 [ thme 3]
                {

                    Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost3);
                }

                else if( tileMap3Char == 'f')   // orange ghost 4 [ theme 5]
                {

                    Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost4);
                }

                else if( tileMap3Char == 'z')   // orange ghost 5 [theme 6]
                {

                    Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost5);
                }

                else if( tileMap3Char == 'x')   // orange ghost 6 [ theme 8]
                {

                    Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost6);
                }

                else if( tileMap3Char == 'c')   // orange ghost 7 [ theme 9]
                {

                    Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost7);
                }

                
                else if( tileMap3Char == 'r')   // red ghost 1
                {

                    Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost1);
                }

                else if( tileMap3Char == 'g')   // red ghost 2
                {

                    Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost2);
                }

                else if( tileMap3Char == 'h')   // red ghost 3
                {

                    Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost3);
                }

                else if( tileMap3Char == 'j')   // red ghost4
                {

                    Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost4);
                }


                else if( tileMap3Char == 'k')   // red ghost5
                {

                    Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost5);
                }


                else if( tileMap3Char == 'y')   // red ghost6
                {

                    Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost6);
                }

                else if( tileMap3Char == 'u')   // red ghost7
                {

                    Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost7);
                }


                else if( tileMap3Char == 't')   // pink ghost 1
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }

                else if( tileMap3Char == 'b')   // pink ghost 2
                {

                    Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost2);
                }

                else if( tileMap3Char == 'n')   // pink ghost 3
                {

                    Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost3);
                }

                else if( tileMap3Char == 'm')   // pink ghost 4
                {

                    Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost4);
                }

                else if( tileMap3Char == 'q')   // pink ghost 5
                {

                    Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost5);
                }

                else if( tileMap3Char == 'w')   // pink ghost 6
                {

                    Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost6);
                }



                else if( tileMap3Char == 'Y')   // blue ghost 1
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }

                else if( tileMap3Char == 'U')   // blue ghost 2
                {

                    Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost2);
                }
                
                else if( tileMap3Char == 'O')   // blue ghost 3
                {

                    Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost3);
                }

                else if( tileMap3Char == 'K')   // blue ghost 4
                {

                    Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost4);
                }

                else if( tileMap3Char == 'J')   // blue ghost 5
                {

                    Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost5);
                }


                else if ( tileMap3Char == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }

                
                else if ( tileMap3Char == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }

                else if ( tileMap3Char == '%')
                {
                    Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                    teleportPads.add(teleport2);
                }

                else if ( tileMap3Char == ':')
                {
                    Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                    bush.add(bushe);
                }

                else if ( tileMap3Char == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }
                                
                else if ( tileMap3Char == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }

                else if ( tileMap3Char == '~')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tileMap3Char == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }


                
                else if ( tileMap3Char == 'N')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMap3Char == '&')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }


                else if ( tileMap3Char == '1')
                {
                    Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                    Button1.add(button1);
                }

                else if ( tileMap3Char == 'A')
                {
                    Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                    Gate1.add(gate1);
                    walls.add(gate1);
                }


                else if ( tileMap3Char == '2')
                {
                    Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                    Button2.add(button2);
                }

                else if ( tileMap3Char == 'B')
                {
                    Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                    Gate2.add(gate2);
                    walls.add(gate2);
                }

                else if ( tileMap3Char == 'W')
                {
                    Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                    spiderNets.add(web);
                }

                else if ( tileMap3Char == 'V')
                {
                    Block lava = new Block(lavaImage, x, y, tileSize, tileSize);
                    lavaTiles.add(lava);
                }

                else if ( tileMap3Char == 'Z')
                {
                    Block sacred = new Block ( sacredZone , x, y , tileSize , tileSize);
                    sacredZones.add(sacred); 
                }

                

                else if ( tileMap3Char == 'L')
                {
                    Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                    slimePuddle.add(slimemist);
                }

                else if (tileMap3Char == 'Q')
                {
                    Block electricS = new Block(electric, x, y, tileSize, tileSize);
                    electricShocks.add(electricS);
                }

                
                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    }; 



    public void loadMap4(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>();  
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();  // " "

        
        teleportPads = new HashSet<Block>(); // T AND %  // these hashsets like teleport and iceblocks even if you are not using them in the current level but if you have somehwere in the program the usage of them then you had to implement this here also. 
        iceBlocks = new HashSet<Block>();   // I   // bcz we have used somehwere in our program the for loop which iterates over this hashset so it doesn’t matter if there are actually any ice blocks in the map — if the HashSet was never initialized, then Java throws a NullPointerException.
        // therefore Always initialize iceBlocks, even if it's empty .This way, your code won’t crash if it tries to iterate over or check iceBlocks, because it will just be an empty set instead of null.
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
        blackSpace = new HashSet<Block>(); // <



        for ( int r = 0 ; r<rowCount ; r++)   // r stands for row
        {
            for (  int c = 0 ; c<colCount ; c++)   // c stands for column
            {
                String row = tileMap4[r];   // getting the current row bcz tileMap is our map and r index is the row index so we are getting the loop indexing 
                char tileMap4Char = row.charAt(c);  // getting the current character
                // what we had done in the above two lines is that we getting the row and then we are getting the character present at that row with the col index
                // we had declared two variables 1) row 2) tileMapChar
                // the string row represents the single row in the tile map from index r=0 to r<rowCount
                // now we are using inbuilt string operations charAt() to acces the character present at that position
                // row.charAt(c) acceses the character from the row at index c where c represents the columns 


                // we got a char at a specific tile now we need to figure out where this tile is. therefore to draw we need x , y positions and width and height of tile 

                // getting x position ( it will be the 'how many columns we are from the left')
                int x = c*tileSize;
                // getting y position (  it will be the 'how many rows from the top')
                int y = r*tileSize;



                if ( tileMap4Char == 'X'){  // theme 1: wood wall

                    Block wood = new Block(woodWallImage , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMap4Char == '<')
                {
                    Block bS = new Block(null , x , y , tileSize , tileSize);  
                    blackSpace.add(bS); 
                }

                else if ( tileMap4Char == '['){  // theme 2: desert wall

                    Block wood = new Block(desertWallImage , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMap4Char == ']'){  // theme 3: sea wall

                    Block wood = new Block(seaWallImage , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMap4Char == '{'){  // theme 4: bonus 1 wall

                    Block wood = new Block(bonus1Wall , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMap4Char == '}'){  // theme 5: frost wall

                    Block wood = new Block(frostWallImage , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMap4Char == '`'){  // theme 6: lava wall

                    Block wood = new Block(lavaWallImage , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMap4Char == '/'){  // theme 7: bonus 2 wall

                    Block wood = new Block(bonus2Wall , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMap4Char == '_'){  // theme 8:  machine wall

                    Block wood = new Block(machineWallImage , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }

                else if ( tileMap4Char == '='){  // theme 9: moltenspire wall

                    Block wood = new Block(moltenSphireWallImage , x , y , tileSize , tileSize);  
                    walls.add(wood);  
                }


                
                
                else if ( tileMap4Char == ' ')  // food
                {
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMap4Char == '.')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMap4Char == 'E')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMap4Char == '-')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMap4Char == 'e')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }

                else if( tileMap4Char == 'a')   // orange ghost 1 [theme 1]
                {

                    Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost1);
                }

                else if( tileMap4Char == 's')   // orange ghost 2 [ theme 2]
                {

                    Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost2);
                }

                else if( tileMap4Char == 'd')   // orange ghost 3 [ thme 3]
                {

                    Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost3);
                }

                else if( tileMap4Char == 'f')   // orange ghost 4 [ theme 5]
                {

                    Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost4);
                }

                else if( tileMap4Char == 'z')   // orange ghost 5 [theme 6]
                {

                    Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost5);
                }

                else if( tileMap4Char == 'x')   // orange ghost 6 [ theme 8]
                {

                    Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost6);
                }

                else if( tileMap4Char == 'c')   // orange ghost 7 [ theme 9]
                {

                    Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost7);
                }

                
                else if( tileMap4Char == 'r')   // red ghost 1
                {

                    Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost1);
                }

                else if( tileMap4Char == 'g')   // red ghost 2
                {

                    Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost2);
                }

                else if( tileMap4Char == 'h')   // red ghost 3
                {

                    Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost3);
                }

                else if( tileMap4Char == 'j')   // red ghost4
                {

                    Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost4);
                }


                else if( tileMap4Char == 'k')   // red ghost5
                {

                    Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost5);
                }


                else if( tileMap4Char == 'y')   // red ghost6
                {

                    Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost6);
                }

                else if( tileMap4Char == 'u')   // red ghost7
                {

                    Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost7);
                }


                else if( tileMap4Char == 't')   // pink ghost 1
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }

                else if( tileMap4Char == 'b')   // pink ghost 2
                {

                    Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost2);
                }

                else if( tileMap4Char == 'n')   // pink ghost 3
                {

                    Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost3);
                }

                else if( tileMap4Char == 'm')   // pink ghost 4
                {

                    Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost4);
                }

                else if( tileMap4Char == 'q')   // pink ghost 5
                {

                    Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost5);
                }

                else if( tileMap4Char == 'w')   // pink ghost 6
                {

                    Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost6);
                }



                else if( tileMap4Char == 'Y')   // blue ghost 1
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }

                else if( tileMap4Char == 'U')   // blue ghost 2
                {

                    Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost2);
                }
                
                else if( tileMap4Char == 'O')   // blue ghost 3
                {

                    Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost3);
                }

                else if( tileMap4Char == 'K')   // blue ghost 4
                {

                    Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost4);
                }

                else if( tileMap4Char == 'J')   // blue ghost 5
                {

                    Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost5);
                }


                else if ( tileMap4Char == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }

                
                else if ( tileMap4Char == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }

                else if ( tileMap4Char == '%')
                {
                    Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                    teleportPads.add(teleport2);
                }

                else if ( tileMap4Char == ':')
                {
                    Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                    bush.add(bushe);
                }

                else if ( tileMap4Char == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }
                                
                else if ( tileMap4Char == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }

                else if ( tileMap4Char == '~')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tileMap4Char == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }


                
                else if ( tileMap4Char == 'N')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMap4Char == '&')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }


                else if ( tileMap4Char == '1')
                {
                    Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                    Button1.add(button1);
                }

                else if ( tileMap4Char == 'A')
                {
                    Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                    Gate1.add(gate1);
                    walls.add(gate1);
                }


                else if ( tileMap4Char == '2')
                {
                    Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                    Button2.add(button2);
                }

                else if ( tileMap4Char == 'B')
                {
                    Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                    Gate2.add(gate2);
                    walls.add(gate2);
                }

                else if ( tileMap4Char == 'W')
                {
                    Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                    spiderNets.add(web);
                }

                else if ( tileMap4Char == 'V')
                {
                    Block lava = new Block(lavaImage, x, y, tileSize, tileSize);
                    lavaTiles.add(lava);
                }

                else if ( tileMap4Char == 'Z')
                {
                    Block sacred = new Block ( sacredZone , x, y , tileSize , tileSize);
                    sacredZones.add(sacred); 
                }

                

                else if ( tileMap4Char == 'L')
                {
                    Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                    slimePuddle.add(slimemist);
                }

                else if (tileMap4Char == 'Q')
                {
                    Block electricS = new Block(electric, x, y, tileSize, tileSize);
                    electricShocks.add(electricS);
                }

                
                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    }; 


    
    public void loadMap5(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <

    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap5[r];
                char tileMap5Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap5Char) {
                    case '<':  // black space
                        Block bS  = new Block(null, x, y, tileSize, tileSize);
                        blackSpace.add(bS);
                        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        bush.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };


    public void loadMap6(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <

    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap6[r];
                char tileMap6Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap6Char) {
                    case '<':  // black space
                        Block bS  = new Block(null, x, y, tileSize, tileSize);
                        blackSpace.add(bS);
                        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };



    public void loadMap7(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <

    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap7[r];
                char tileMap7Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap7Char) {
                            case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };

    
    

    public void loadMap8(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <
        
    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap8[r];
                char tileMap8Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap8Char) {
                    case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
        case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };



    public void loadMap9(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <
       
    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap9[r];
                char tileMap9Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap9Char) { case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };



    public void loadMap10(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <
        
    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap10[r];
                char tileMap10Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap10Char) {case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };



    public void loadMap11(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <
        
    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap11[r];
                char tileMap11Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap11Char) {case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };



    public void loadMap12(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <
       
    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap12[r];
                char tileMap12Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap12Char) { case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };


    public void loadMap13(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
    
        blackSpace = new HashSet<Block>(); // <
        
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap13[r];
                char tileMap13Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap13Char) {case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };


    public void loadMap14(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <
        
    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap14[r];
                char tileMap14Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap14Char) {case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };


    public void loadMap15(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <
        
    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap15[r];
                char tileMap15Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap15Char) {case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };


    public void loadMap16(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <
       
    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap16[r];
                char tileMap16Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap16Char) { case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };


    public void loadMap17(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <
        
    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap17[r];
                char tileMap17Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap17Char) {case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };


    public void loadMap18(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <
        
    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap18[r];
                char tileMap18Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap18Char) {case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };


    public void loadMap19(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <
        
    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap19[r];
                char tileMap19Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap19Char) {case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };

    public void loadMap20(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <
        
    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap20[r];
                char tileMap20Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap20Char) {case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };


    public void loadMap21(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <
        
    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap21[r];
                char tileMap21Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap21Char) {case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };


    public void loadMap22(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <
        
    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap22[r];
                char tileMap22Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap22Char) {case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };


    public void loadMap23(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <
        
    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap23[r];
                char tileMap23Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap23Char) {case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };


    public void loadMap24(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <
        
    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap24[r];
                char tileMap24Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap24Char) {case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };


    public void loadMap25(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <
        
    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap25[r];
                char tileMap25Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap25Char) {case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };


    public void loadMap26(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // ~
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <
        
    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap26[r];
                char tileMap26Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap26Char) {case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };


    public void loadMap27(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <
       
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap27[r];
                char tileMap27Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap27Char) { case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
    
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };


    public void loadMap28(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <
       
    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap28[r];
                char tileMap28Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap28Char) { case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };


    public void loadMap29(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <
        
    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap29[r];
                char tileMap29Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap29Char) {case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };


    public void loadMap30(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <
        
    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap30[r];
                char tileMap30Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap30Char) {case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };


    public void loadMap31(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <
        
    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap31[r];
                char tileMap31Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap31Char) {case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };


    public void loadMap32(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <
        
    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap32[r];
                char tileMap32Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap32Char) {case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };


    public void loadMap33(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <
        
    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap33[r];
                char tileMap33Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap33Char) {case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };

    public void loadMap34(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <
        
    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap34[r];
                char tileMap34Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap34Char) {case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };

    public void loadMap35(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <
        

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap35[r];
                char tileMap35Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap35Char) {case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };


    public void loadMap36(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <
        
    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap36[r];
                char tileMap36Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap36Char) {case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };


    public void loadMap37(){
        // Initializing all HashSets first
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>(); // T AND %
        iceBlocks = new HashSet<Block>();   // I
        speedZones = new HashSet<Block>();  // S
        reverseControls = new HashSet<Block>(); // R
        phantomZones = new HashSet<Block>();  // J
        warpEntrance = new HashSet<Block>(); // N
        warpExit = new HashSet<Block>(); // &
        spaces1 = new HashSet<Block>(); // .
        spaces2 = new HashSet<Block>();  // -
        exitSpaces1 = new HashSet<Block>();  // E
        exitSpaces2 = new HashSet<Block>(); // e
        Button1 = new HashSet<Block>();  // 1
        Gate1 = new HashSet<Block>();  // A
        Button2 = new HashSet<Block>(); // 2
        Gate2 = new HashSet<Block>();  // B
        spiderNets = new HashSet<Block>();  // W
        lavaTiles = new HashSet<Block>();  // V
        sacredZones = new HashSet<Block>(); // Z
        slimePuddle = new HashSet<Block>();  // L
        electricShocks = new HashSet<Block>();  // Q
        bush = new HashSet<Block>();  // :
        blackSpace = new HashSet<Block>(); // <
        
    
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap37[r];
                char tileMap37Char = row.charAt(c);
    
                int x = c * tileSize;
                int y = r * tileSize;
    
                switch (tileMap37Char) {case '<':  // black space
        Block bS  = new Block(null, x, y, tileSize, tileSize);
        blackSpace.add(bS);
        break;
                    case 'X': // theme 1: wood wall
                        Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                        walls.add(wood);
                        break;
                    case '[': // theme 2: desert wall
                        Block desert = new Block(desertWallImage, x, y, tileSize, tileSize);
                        walls.add(desert);
                        break;
                    case ']': // theme 3: sea wall
                        Block sea = new Block(seaWallImage, x, y, tileSize, tileSize);
                        walls.add(sea);
                        break;
                    case '{': // theme 4: bonus 1 wall
                        Block bonus1 = new Block(bonus1Wall, x, y, tileSize, tileSize);
                        walls.add(bonus1);
                        break;
                    case '}': // theme 5: frost wall
                        Block frost = new Block(frostWallImage, x, y, tileSize, tileSize);
                        walls.add(frost);
                        break;
                    case '`': // theme 6: lava wall
                        Block lava = new Block(lavaWallImage, x, y, tileSize, tileSize);
                        walls.add(lava);
                        break;
                    case '/': // theme 7: bonus 2 wall
                        Block bonus2 = new Block(bonus2Wall, x, y, tileSize, tileSize);
                        walls.add(bonus2);
                        break;
                    case '_': // theme 8: machine wall
                        Block machine = new Block(machineWallImage, x, y, tileSize, tileSize);
                        walls.add(machine);
                        break;
                    case '=': // theme 9: moltenspire wall
                        Block molten = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                        walls.add(molten);
                        break;
                    case ' ': // food
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                    case '.': // allowing pacman to move map from end to other
                        Block space1 = new Block(null, x, y, tileSize, tileSize);
                        spaces1.add(space1);
                        break;
                    case 'E':
                        Block exit1 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces1.add(exit1);
                        break;
                    case '-': // allowing pacman to move map from end to other
                        Block space2 = new Block(null, x, y, tileSize, tileSize);
                        spaces2.add(space2);
                        break;
                    case 'e':
                        Block exit2 = new Block(null, x, y, tileSize, tileSize);
                        exitSpaces2.add(exit2);
                        break;
                    case 'a': // orange ghost 1 [theme 1]
                        Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost1);
                        break;
                    case 's': // orange ghost 2 [ theme 2]
                        Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost2);
                        break;
                    case 'd': // orange ghost 3 [ thme 3]
                        Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost3);
                        break;
                    case 'f': // orange ghost 4 [ theme 5]
                        Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost4);
                        break;
                    case 'z': // orange ghost 5 [theme 6]
                        Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost5);
                        break;
                    case 'x': // orange ghost 6 [ theme 8]
                        Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost6);
                        break;
                    case 'c': // orange ghost 7 [ theme 9]
                        Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(OrangeGhost7);
                        break;
                    case 'r': // red ghost 1
                        Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost1);
                        break;
                    case 'g': // red ghost 2
                        Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost2);
                        break;
                    case 'h': // red ghost 3
                        Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost3);
                        break;
                    case 'j': // red ghost4
                        Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost4);
                        break;
                    case 'k': // red ghost5
                        Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost5);
                        break;
                    case 'y': // red ghost6
                        Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost6);
                        break;
                    case 'u': // red ghost7
                        Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                        ghosts.add(RedGhost7);
                        break;
                    case 't': // pink ghost 1
                        Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost);
                        break;
                    case 'b': // pink ghost 2
                        Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost2);
                        break;
                    case 'n': // pink ghost 3
                        Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost3);
                        break;
                    case 'm': // pink ghost 4
                        Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost4);
                        break;
                    case 'q': // pink ghost 5
                        Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost5);
                        break;
                    case 'w': // pink ghost 6
                        Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                        ghosts.add(PinkGhost6);
                        break;
                    case 'Y': // blue ghost 1
                        Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost);
                        break;
                    case 'U': // blue ghost 2
                        Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost2);
                        break;
                    case 'O': // blue ghost 3
                        Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost3);
                        break;
                    case 'K': // blue ghost 4
                        Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost4);
                        break;
                    case 'J': // blue ghost 5
                        Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                        ghosts.add(BlueGhost5);
                        break;
                    case 'P': // PACMAN
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case 'W': // spider net
                        Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                        spiderNets.add(web);
                        break;
                    case 'T': // teleportal pad
                        Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                        teleportPads.add(teleport);
                        break;
                    case '%': // teleportal pad 2
                        Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                        teleportPads.add(teleport2);
                        break;
                    case ':': // bushes
                        Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                        teleportPads.add(bushe);
                        break;
                    case 'I': // ice tiles
                        Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                        iceBlocks.add(ice);
                        break;
                    case 'Z': // sacred zones
                        Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                        sacredZones.add(sacred);
                        break;
                    case 'S': // speed zone
                        Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                        speedZones.add(speed);
                        break;
                    case 'R': // reverse controls
                        Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                        reverseControls.add(rev);
                        break;
                    case '~': // phantom zone
                        Block phantom = new Block(null, x, y, tileSize, tileSize);
                        phantomZones.add(phantom);
                        break;
                    
                    case 'N': // warp entrance
                        Block warpIn = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                        warpEntrance.add(warpIn);
                        break;
                    case '&': // warp exit
                        Block warpOut = new Block(warpExitImage, x, y, tileSize, tileSize);
                        warpExit.add(warpOut);
                        break;
                    case '1': // button 1
                        Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                        Button1.add(button1);
                        break;
                    case 'A': // gate 1
                        Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                        Gate1.add(gate1);
                        break;
                    case '2': // button 2
                        Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                        Button2.add(button2);
                        break;
                    case 'B': // gate 2
                        Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                        Gate2.add(gate2);
                        break;
                    case 'V': // LAVA tiles
                        Block lavat = new Block(lavaImage, x, y, tileSize, tileSize);
                        lavaTiles.add(lavat);
                        break;
                    case 'L': // SLIME
                        Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                        slimePuddle.add(slimemist);
                        break;
                    case 'Q': // ELECTRICSHOCK
                        Block electricS = new Block(electric, x, y, tileSize, tileSize);
                        electricShocks.add(electricS);
                        break;
                }
            }
        }
    };
    





    


    // creating a function paintComponent in this we will draw all of our objects onto the game


    public void paintComponent(Graphics g)   // it will take in graphics g
    {
        super.paintComponent(g);  // basically super is going to import the function "paintComponent" from JPanel

        // calling draw function
        draw(g);  // this will draw graphics g
    }

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
        

 
        // since i want the pacman to move above the ice tiles and not below and since i is a 2d game so the layering depends on the order of draw so i draw the ice tiles first and then pacman
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

        long currentTime = System.currentTimeMillis();

        boolean pacmanStunnedThisFrame = false;

        // Check if Pac-Man is stunned
        if (pacmanStunEndTimes.containsKey(pacman)) {
            long end1 = pacmanStunEndTimes.get(pacman);
            if (System.currentTimeMillis() < end1) {

                pacman.velocityX =0;
                pacman.velocityY = 0;
                pacman.x += pacman.velocityX;
                pacman.y += pacman.velocityY;
                pacmanStunnedThisFrame = true;
            } else {
                pacmanStunEndTimes.remove(pacman);  // Stun ended
            }
        }
        
        // Check for collision with shock tile
        for (Block shock : electricShocks) {
            if (collision(pacman, shock)) {
                pacmanStunEndTimes.put(pacman, System.currentTimeMillis() + 2000);
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                pacman.velocityX =0;
                pacman.velocityY = 0;
                pacmanStunnedThisFrame = false;
                System.out.println("⚡ Pac-Man stunned!");
                break;  // exit shock tile loop
            }
        }
        
        // 🔒 Skip movement ONLY if stunned this frame
        if (pacmanStunnedThisFrame) {
            return;  // ← safe now, no velocity updates or motion
        }
        
        


        // reverse controls 

        boolean currentlyOnReverse = false;

for (Block reverse : reverseControls) {
    if (collision(pacman, reverse)) {
        currentlyOnReverse = true;

        if (!pacmanIsOnReverse) {
            // Just stepped onto the R tile
            controlsReversed = !controlsReversed; // Toggle it!
            System.out.println("Controls reversed: " + controlsReversed);
        }

        break; // Found one, no need to check more
    }
}

// Update tracking flag
pacmanIsOnReverse = currentlyOnReverse;



        // phantom zones
        // Check if Pac-Man is entering a Phantom Zone
        for (Block phantom : phantomZones) {
            if (collision(pacman, phantom)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }



      
// STEP 1: Check where Pac-Man is (before moving)
boolean alreadyInWeb = isInsideSpiderNet(pacman); // checks if pacman is currently on a spider web
boolean alreadyInBush = isInsideBushes(pacman); // checks if pacman is currently on a BUSHES

if (alreadyInWeb) {
    // Slow down Pac-Man's movement inside the web
    if (pacman.direction == 'L') {
        pacman.velocityX = -tileSize / 8;  // Slow down horizontally
        pacman.velocityY = 0;  // No vertical movement
    } else if (pacman.direction == 'R') {
        pacman.velocityX = +tileSize / 8;
        pacman.velocityY = 0;
    } else if (pacman.direction == 'U') {
        pacman.velocityY = -tileSize / 8;  // Slow down vertically
        pacman.velocityX = 0;
    } else if (pacman.direction == 'D') {
        pacman.velocityX = 0;
        pacman.velocityY = +tileSize / 8;
    }
} 
else if ( alreadyInBush)
{
    // Slow down Pac-Man's movement inside the web
    if (pacman.direction == 'L') {
        pacman.velocityX = -tileSize / 8;  // Slow down horizontally
        pacman.velocityY = 0;  // No vertical movement
    } else if (pacman.direction == 'R') {
        pacman.velocityX = +tileSize / 8;
        pacman.velocityY = 0;
    } else if (pacman.direction == 'U') {
        pacman.velocityY = -tileSize / 8;  // Slow down vertically
        pacman.velocityX = 0;
    } else if (pacman.direction == 'D') {
        pacman.velocityX = 0;
        pacman.velocityY = +tileSize / 8;
    }
}
else if (isOnIce(pacman)) {
    // ICE SLIDING LOGIC
    boolean onIceNow = isOnIce(pacman); // check current tile. isOnIce is a function which returns boolean value. this functions checks if the tile is ice or not. if yes then returns true therefore here boolean onIceNow = true

    // 1. Update velocity based on current direction
    pacman.updateVelocity();

    // 2. Try moving
    pacman.x += pacman.velocityX;
    pacman.y += pacman.velocityY;
    // these two are increasing the speed of pacman when on ice tiles

    // 3. Check for wall hit
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
                System.out.println("🔥 Lava turned ON");
            } else {
                System.out.println("🧯 Lava turned OFF");
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
                if (!warpExit.isEmpty())
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
                        case 'R':
                            pacman.x = chosenExit.x + tileSize;
                            pacman.y = chosenExit.y;
                            break;
                    }
                    
        
                    System.out.println("Pac-Man teleported to random warp exit at (" + chosenExit.x + ", " + chosenExit.y + ")");
                }
                break; // Exit after teleporting
            }
        }


        for ( Block warpOut : warpExit)
        {
            if ( collision(pacman, warpOut))
            {

                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
        
                // 🛑 Completely stop movement
                pacman.velocityX = 0;
                pacman.velocityY = 0;
        
                hitWall = true;
                break;
            }
        }


        

        // TELEPORTING THE PACMAN FROM ONE MAP END TO OTHER
        // Handle E ↔ e warp for pacman 
for (Block space1 : spaces1) {
    if (collision(pacman, space1)) {
        for (Block exit1 : exitSpaces1) {
            teleport(space1, exit1, pacman);
            break;
        }
        break;
    }
}

for (Block exit1 : exitSpaces1) {
    if (collision(pacman, exit1)) {
        for (Block space1 : spaces1) {
            teleport(exit1, space1, pacman);
            break;
        }
        break;
    }
}

// Handle M ↔ m warp for pacman
for (Block space2 : spaces2) {
    if (collision(pacman, space2)) {
        for (Block exit2 : exitSpaces2) {
            teleport(space2, exit2, pacman);
            break;
        }
        break;
    }
}

for (Block exit2 : exitSpaces2) {
    if (collision(pacman, exit2)) {
        for (Block space2 : spaces2) {
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
        for ( Block ghost : ghosts) {
            boolean onSpeedZone = isOnSpeedZone(ghost); // Check if the ghost is on a speed zone (Speed tile "S")
            
            // 1. Update velocity based on the current direction
            ghost.updateVelocity();
    
            // 2. If ghost is on speed zone, increase speed (like ice sliding)
            if (onSpeedZone) {
                ghost.velocityX *= 2;  // Double the velocity when on speed zone
                ghost.velocityY *= 2;
                
            }
    
            // 5. If ghost is no longer on the speed zone, reset velocity to normal
            if (!onSpeedZone) {  // WHEN THIS WAS PLACED AT LAST THEN THE GHOST WERE MOVING AT 2X SPEED ALWAYS BUT WHEN I PLACED IT HERE THEN THEY STARTED MOVING NORMAL
                ghost.velocityX /= 2; // Reset speed to normal
                ghost.velocityY /= 2;
            }
           

            int newX = ghost.x + ghost.velocityX;
            int newY = ghost.y + ghost.velocityY;

        // 4. Check for collision with walls at the new position
        boolean collisionDetected = false;
        for (Block wall : walls) {
            if (collision(newX, newY, wall)) { // Check if the ghost's new position collides with any wall
                collisionDetected = true;
                break;
            }
        }

        // 5. If no collision, update ghost's position
        if (!collisionDetected) {
            ghost.x = newX;
            ghost.y = newY;
        }
    
            
        }
    

       
       // --- Toggle for Button 1 & Gate 1 ---
boolean currentlyOnButton1 = false;

for (Block button : Button1) {
    if (collision(pacman, button)) {
        currentlyOnButton1 = true;

        if (!pacmanOnButton1) { // Just stepped onto it
            gate1Open = !gate1Open;

            if (gate1Open) {
                for (Block gate : Gate1) {
                    walls.remove(gate); // gate becomes walkable
                }
                System.out.println("Gate 1 opened!");
            } else {
                for (Block gate : Gate1) {
                    walls.add(gate); // gate becomes solid again
                }
                System.out.println("Gate 1 closed!");
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


        for (Block wall : walls) {
            if (collision(pacman, wall)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
        
                // 🛑 Completely stop movement
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

        for (Block ghost : ghosts) {
            for (Block button : Button2) {
                if (collision(ghost, button)) {
                    currentlyGhostOnLavaButton = true;
        
                    if (!ghostOnLavaButton) {  // Just stepped ON it
                        lavaEnabled = !lavaEnabled;
        
                        if (lavaEnabled) {
                            System.out.println("🔥 Lava turned ON by ghost!");
                        } else {
                            System.out.println("🧯 Lava turned OFF by ghost!");
                        }
                    }
        
                    break;  // Don't check more buttons
                }
            }
            if (currentlyGhostOnLavaButton) break;  // Don't check more ghosts
        }
        
        // 💡 Update toggle tracker each frame
        ghostOnLavaButton = currentlyGhostOnLavaButton;
        
        
        
                // lava collsiion
                if (lavaEnabled) {
                    // Pac-Man lava logic
                    for (Block lava : lavaTiles) {
                        if (collision(pacman, lava)) {
                            lives--;
                            System.out.println("Pac-Man stepped on lava! Lives: " + lives);
                
                            if (lives <= 0) {
                                gameOver = true;
                                return;
                            }
                
                            resetPositions();
                            break;
                        }
                    }
                }
        
                if ( lavaEnabled && (ghostLavaKillTemporary ) )
                {
                    Iterator<Block> ghostIterator = ghosts.iterator();
        while (ghostIterator.hasNext()) {
            Block ghost = ghostIterator.next();
        
            for (Block lava : lavaTiles) {
                if (lavaEnabled && collision(ghost, lava)) {
                    ghostIterator.remove();  // remove from current ghosts
                    System.out.println("👻 Ghost removed on lava!");
                    break;
                }
            }
        }
        
                }
        
       
            
                boolean currentlyGhostOnIceButton1 = false;

        for (Block ghost : ghosts) {
            for (Block button : Button1) {
                if (collision(ghost, button)) {
                    currentlyGhostOnIceButton1 = true;
        
                    if (!ghostOnButton1) {  // Just stepped ON it
                        gate1Open = !gate1Open;
        
                        if (gate1Open) {
                            for (Block gate : Gate1) {
                                walls.remove(gate); // gate becomes walkable
                                System.out.println("🔥 gate 1 ON by ghost!");
                            }
                            System.out.println("Gate 1 opened!");
                        } else {
                            for (Block gate : Gate1) {
                                walls.add(gate); // gate becomes solid again
                                System.out.println("🧯 gate 2 OFF by ghost!");
                            }
                            System.out.println("Gate 1 closed!");
                        }
                       
                    }
        
                    break;  // Don't check more buttons
                }
            }
            if (currentlyGhostOnIceButton1) break;  // Don't check more ghosts
        }
        
        // 💡 Update toggle tracker each frame
        ghostOnButton1 = currentlyGhostOnIceButton1;
        
        

        

        
        
            
        


        // ghost collison

        for ( Block ghost : ghosts){  // for each ghost

 for (Block phantom : phantomZones) {
                if (collision(ghost, phantom)) {
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

🚨 If you decrement once per move(), and each frame moves 8 pixels, then:

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

            if (ghost.phaseTilesRemaining > 0) {
        // Ghost is phasing, so skip wall collision
        
        ghost.phaseTilesRemaining--;
    } else {
        // Ghost is not phasing, so check walls
        for (Block wall : walls) {
            if (collision(ghost, wall)) {
                ghost.x += ghost.velocityX;
                ghost.y += ghost.velocityY;
                ghost.updateDirection(directions[random.nextInt(4)]);
                break;
            }
        }
    }

} 

            // Check if this ghost is stunned
if (ghostStunEndTimes.containsKey(ghost)) {
    long end = ghostStunEndTimes.get(ghost);
    if (System.currentTimeMillis() < end) {
        ghost.velocityX = 0;
        ghost.velocityY = 0;
        continue;  // Skip movement
    } else {
        ghostStunEndTimes.remove(ghost);  // Stun ended
    }
}

// Check for collision with shock tile
for (Block shock : electricShocks) {
    if (collision(ghost, shock)) {
        ghostStunEndTimes.put(ghost, System.currentTimeMillis() + 2000);  // 2 sec stun
        ghost.velocityX = 0;
        ghost.velocityY = 0;
        System.out.println("👻 Ghost stunned!");
        continue;  // Skip movement this frame
    }
}




            boolean inSacred = isInsideSacredZone(ghost);  // your sacred zone check

    if (inSacred) {
        // Slow down ghost like spider webs
        if (ghost.direction == 'L') {
            ghost.velocityX = -tileSize /32;
            ghost.velocityY = 0;
        } else if (ghost.direction == 'R') {
            ghost.velocityX = +tileSize /32;
            ghost.velocityY = 0;
        } else if (ghost.direction == 'U') {
            ghost.velocityY = -tileSize /32;
            ghost.velocityX = 0;
        } else if (ghost.direction == 'D') {
            ghost.velocityX = 0;
            ghost.velocityY = +tileSize /32;
        }
    } else {
        // Normal movement outside sacred zones
        // ghost.updateVelocity();   // commented out this and now the ghost are moving corectly . previously it was like they were moving very fastly on normal tiles as well
    }




    // ghost gets slow doown in slime 
    boolean inSlime = isInsideSlimePuddle(ghost);
    if (inSlime) {
        // Slow down ghost like spider webs
        if (ghost.direction == 'L') {
            ghost.velocityX = -tileSize /32;
            ghost.velocityY = 0;
        } else if (ghost.direction == 'R') {
            ghost.velocityX = +tileSize /32;
            ghost.velocityY = 0;
        } else if (ghost.direction == 'U') {
            ghost.velocityY = -tileSize /32;
            ghost.velocityX = 0;
        } else if (ghost.direction == 'D') {
            ghost.velocityX = 0;
            ghost.velocityY = +tileSize /32;
        }
    } else {
        // Normal movement outside sacred zones
        // ghost.updateVelocity();   // commented out this and now the ghost are moving corectly . previously it was like they were moving very fastly on normal tiles as well
    }

    // 👻 Move the ghost
    ghost.x += ghost.velocityX;
    ghost.y += ghost.velocityY;

    // Wall collision
    for (Block wall : walls) {
        if (collision(ghost, wall)) {
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

                lives += 1;
                if ( lives == 0)
                {
                    gameOver = true; 
                    return;   // now since the game ends therefore i want to stop the gameloop so go to actionperformed
                }
                resetPositions();  // i have to declare this new function and i will acces the reset function through it 
                // bcz the reset function is in the class Block and i will create this function inside the main PacMan class
                // i had made this function at last just above override

            }



            // adding a condition first to solve the problem 2 we faced
            if ( ghost.y == tileSize*9 && ghost.direction != 'U' && ghost.direction != 'D')
            {
                // ghost.updateDirection('U');  // instead of making the ghost only move up we can make another char array which will choose from up and down only since it is of bound 2 therefore 0 or 1 therefore first two indexes
                char newDirection2 = directions[random.nextInt(2)];
                ghost.updateDirection(newDirection2);
            }
            



           


        // Set Pac-Man image based on actual direction he's moving in
        // since in the key released function i was using this but bcz in that i had to control the if revrse control or not conditiosn so it was causing error. as when pacman was moving between two walls and when we press the up or down key the pacman up image or pacman down image gets but it shoudl not come bcz pacman is moving forward or backward thats why we did this thing here
switch (pacman.direction) {
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




        // checking food collision
        Block foodEaten = null; // currently pacman has not eaten anything 
    
        // iterating over the loop
        for ( Block food : foods)
        {
            if ( collision(pacman , food))
            {
                // pacman will eat the food
                foodEaten = food;
                score += 10;
                // now we want to remove the food from hashSet
                
            }
        }
        foods.remove(foodEaten);  // it removes that food  . note: it should not be placed inside the for loop as we are using for each loop. so any modifications we wanna made are to done outside it 
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

    private boolean isInsideSpiderNet(Block pacman) {
        for (Block net : spiderNets) {
            if (collision(pacman, net)) return true;
        }
        return false;
    }


    private boolean isInsideBushes(Block pacman) {
        for (Block bushBlock : bush) {
            if (collision(pacman, bushBlock)) return true;
        }
        return false;
    }
    
    

    private boolean isInsideSacredZone(Block ghost) {
        for (Block sacredZ : sacredZones) {
            if (collision(ghost, sacredZ)) {
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


    private boolean isInsideSlimePuddle(Block ghost) {
        for (Block slime : slimePuddle) {
            if (collision(ghost, slime)) {
                return true;
            }
        }
        return false;
    }


    public void teleport(Block entry, Block exit, Block pacman) {
        // Move Pac-Man to exit tile
        pacman.x = exit.x;
        pacman.y = exit.y;
    
        // Offset Pac-Man based on current direction so he doesn't land inside the exit tile
        switch (pacman.direction) {
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
    public boolean isOnIce(Block pacman) {
        for (Block ice : iceBlocks) {
            if (collision(pacman, ice)) {
                return true;
            }
        }
        return false;
    }

    

    // speed 
    public boolean isOnSpeedZone(Block ghost) {
        for (Block speedZone : speedZones) {
            if (collision(ghost, speedZone)) {
                return true;
            }
        }
        return false;
    }

    public boolean collision(int newX, int newY, Block block) {
        // Assuming each block has x, y, width, height attributes
        return newX < block.x + block.width &&
               newX + block.width > block.x &&
               newY < block.y + block.height &&
               newY + block.height > block.y;
    }


    // teleport pacman
    
    // A HashSet does not allow direct access by index. To convert it to a list, you need to create an ArrayList that contains all the elements from the HashSet
    public void teleportPacMan() {
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





   
    // proceding to new level
    // -> method 1: using switch case

//     int currentLevel = 1;

//     public void updateGameLogic() {
//     if (foods.isEmpty()) {
//         currentLevel++;

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

    

Runnable[] levelLoaders = new Runnable[]{   // it is an array of runnables where levelLoaders is an object or reference variable to access the members

    this :: loadMap1,
    this :: loadMap2,
    this :: loadMap3,
    this :: loadMap4,
    this :: loadMap5,
    this :: loadMap6,
    this :: loadMap7,
    this :: loadMap8,
    this :: loadMap9,
    this :: loadMap10,
    this :: loadMap11,
    this :: loadMap12,  
    this :: loadMap13,
    this :: loadMap14,
    this :: loadMap15,
    this :: loadMap16,
    this :: loadMap17,
    this :: loadMap18,
    this :: loadMap19,
    this :: loadMap20,
    this :: loadMap21,
    this :: loadMap22,
    this :: loadMap23,
    this :: loadMap24,
    this :: loadMap25,
    this :: loadMap26,
    this :: loadMap27,
    this :: loadMap28,
    this :: loadMap29,
    this :: loadMap30,
    this :: loadMap31,
    this :: loadMap32,
    this :: loadMap33

    // if we want to add more maps then just write this::loadMapX and done but the last line should not have a comma

};


int currentLevel = 24;

public void updateGameLogic()
{
    if (foods.isEmpty())
    {
        currentLevel++;

        if ( currentLevel < levelLoaders.length)
        {
            levelLoaders[currentLevel].run();
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
    public void actionPerformed(ActionEvent e) {
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

    if (gameOver) {
        loadMap1(); // reloading the map . this is done bcz i want to add all the foods back into the hashset 
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

if (controlsReversed) {
    if (e.getKeyCode() == KeyEvent.VK_UP) {
        futureY += tileSize / 4;
        if (!willHitWall(futureX, futureY)) pacman.updateDirection('D');
    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        futureY -= tileSize / 4;
        if (!willHitWall(futureX, futureY)) pacman.updateDirection('U');
    } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        futureX += tileSize / 4;
        if (!willHitWall(futureX, futureY)) pacman.updateDirection('R');
    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        futureX -= tileSize / 4;
        if (!willHitWall(futureX, futureY)) pacman.updateDirection('L');
    }
} else {
    if (e.getKeyCode() == KeyEvent.VK_UP) {
        futureY -= tileSize / 4;
        if (!willHitWall(futureX, futureY)) pacman.updateDirection('U');
    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        futureY += tileSize / 4;
        if (!willHitWall(futureX, futureY)) pacman.updateDirection('D');
    } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        futureX -= tileSize / 4;
        if (!willHitWall(futureX, futureY)) pacman.updateDirection('L');
    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
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
