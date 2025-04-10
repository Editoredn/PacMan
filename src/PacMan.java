// the idea here is i am going to have PacMan inherit in JPanel. basically i am going to have this pacman class inherit the JPanel so that this class is basically a version of JPanel


import java.awt.*;  // AWT (Abstract Window Toolkit) is a part of Java's GUI toolkit that allows creating graphical user interfaces.The * means all classes from java.awt are imported. common classes are Frame(creates a window) , Button( creates a clickable window) , Label(displays text) , TextField ( accepts user input)
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import javax.swing.*;




public class PacMan extends JPanel implements ActionListener , KeyListener{   // i have written extends JPanel bcz this PacMan class inherits JPanel

    
    // variables for the reverse control
    boolean controlsReversed = false;
    boolean pacmanIsOnReverse = false;

    
    

    // variables for teleporting
    private boolean isTeleporting = false;
    private long lastTeleportTime = 0;
    private static final long TELEPORT_COOLDOWN = 500; // Cooldown period in milliseconds (adjust as needed)




  
    
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
    private int rowCount = 21;
    private int colCount = 19;
    private int tileSize = 32;

    private int widthBoard = colCount*tileSize;
    private int heightBoard = rowCount*tileSize;

    // done this so that our JPanel has same size as the window 
    // VERY IMPORTANT POINT
    // why made them private?
    // bcz we dont want their values to get changed by someone if they were public . these are the base of our game and we dont want bugs in them
       

    // creating variables to store images 
    private Image wallImage;
    private Image blueGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;
    private Image orangeGhostImage;
    private Image scaredGhostImage;
    private Image cherryImage;
    private Image cherry2Image;
    private Image powerFoodImage;
    private Image iceImage;
    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;
    private Image teleportImage;
    private Image speedImage;
    // private Image phantomImage;
    private Image reverseImage;
    private Image warpEntranceImage;
    private Image warpExitImage;
    
    
        

    

    //X = wall, O = skip(dont do anything), P = pac man, ' ' = food   
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap1 ={     // tileMap is an array of strings
        
    
    "XXXXXXXXXXXXXXXXXXX" ,
    "X        X        X" ,
    "X XXXXXX X XXXXXX X" ,
    "X X             X X" ,
    "X X XXXXXXXXXXX X X" ,
    "X                 X" ,
    "XXXXX XXXXX XXXXX X" ,
    "X                 X" ,
    "X XXXXXXX XXXXXXX X" ,
    "X        r        X" ,
    "X XXXXXXX XXXXXXX X" ,
    "e    P            E" ,
    "X XXXXX XXXXX XXXXX" ,
    "X                 X" ,
    "X X XXXXXXXXXXX X X" ,
    "X X             X X" ,
    "X XXXXXX X XXXXXX X" ,
    "X        X        X" ,
    "X XXXXXXXXXXXXXXX X" ,
    "X                 X" ,
    "XXXXXXXXXXXXXXXXXXX" ,

          
                
                          

};
    // this is the tile map which i am using we can create anything which we want
    // here we have array of strings and each string is a row 

    // now i had to go through the tile map and create objects like walls , foods , and ghosts , etc
    // for this i create another function below the PacMan function and i name it as loadMap



    // level 2: easy

    private String[] tileMap2 = {

        "XXXXXXXXXXXXXXXXXXX" ,
        "X        X        X" ,
        "X XXXXXX X XXXXXX X" ,
        "X X  X   X   X  X X" ,
        "X X XX X X X XX X X" ,
        "X X             X X" ,
        "X X XX XXXXX XX X X" ,
        "X        r        X" ,
        "XXX X XXX XXX X XXX" ,
        "e      b   p      E" ,
        "XXX X XXX XXX X XXX" ,
        "X   X         X   X" ,
        "X X XX XXXXX XX X X" ,
        "X X             X X" ,
        "X X XXXX X XXXX X X" ,
        "X X      P      X X" ,
        "X XXXXXX X XXXXXX X" ,
        "X        X        X" ,
        "X XXXXXXXXXXXXXXX X" ,
        "X                 X" ,
        "XXXXXXXXXXXXXXXXXXX" , 
    };



// level 3: easy

private String[] tileMap3 = {
    "XXXXXXXXXXXXXXXXXXX" ,
    "X                 X" ,
    "X XXX XXXXXXX XXX X" ,
    "X X             X X" ,
    "X X XXXXX XXXXX X X" ,
    "X X X         X X X" ,
    "X X X XXXXXXX X X X" ,
    "X   X    r    X   X" ,
    "XXX XXXXXXXXXXX XXX" ,
    "e      b   p      E" ,
    "XXX XXXXXXXXXXX XXX" ,
    "X   X         X   X" ,
    "X X X XXXXXXX X X X" ,
    "X X X         X X X" ,
    "X X XXXXX XXXXX X X" ,
    "X       P         X" ,
    "X XXX XXXXXXX XXX X" ,
    "X X       X     X X" ,
    "X XXXXXXX X XXXXX X" ,
    "X                 X" ,
    "XXXXXXXXXXXXXXXXXXX" ,

};



// level 4: easy

private String[] tileMap4 = {
    "XXXXXXXXXXXXXXXXXXX" ,
    "X        X        X" ,
    "X XXXXXX X XXXXXX X" ,
    "X         r       X" ,
    "XXX XXXXXXXXXXX XXX" ,
    "X                 X" ,
    "X XX XX XXXXX XX XX" ,
    "X   bX         Xp X" ,
    "X XX X XXXXXXX X XX" ,
    "e                 E" ,
    "X XX X XXXXXXX X XX" ,
    "X    X         X  X" ,
    "X XX XX XXXXX XX XX" ,
    "X                 X" ,
    "XXX XXXXXXXXXXX XXX" ,
    "X        P        X" ,
    "X XXXXXX X XXXXXX X" ,
    "X X      X      X X" ,
    "X X XXXXXXXXXXXXX X" ,
    "X                 X" ,
    "XXXXXXXXXXXXXXXXXXX" ,

};



// level 5: easy

private String[] tileMap5 = {

    "XXXXXXXXXXXXXXXXXXX" ,
    "X        p  r     X" ,
    "X XXXXX XXX XXXXX X" ,
    "X X   b X X     X X" ,
    "X X XXX X X X X X X" ,
    "X X X     X X X X X" ,
    "X X X XXX X X X X X" ,
    "X   X X   X X X   X" ,
    "XXX X   X X X X XXX" ,
    "e     X X   X     E" ,
    "XXX X X XXX X X XXX" ,
    "X   X X     X X   X" ,
    "X X X XX XX X X X X" ,
    "X X X     X X X X X" ,
    "X X XXX X X X X X X" ,
    "X X     X X     X X" ,
    "X XXXXX XXX XXXXX X" ,
    "X        P        X" ,
    "X XXXXX XXX XXXXX X" ,
    "X                 X" ,
    "XXXXXXXXXXXXXXXXXXX" ,

};


// level 6: easy

private String[] tileMap6 = {

    "XXXXXXXXXXXXXXXXXXX" ,
    "X                 E" ,
    "X XXXXX XXXXX XXX X" ,
    "X X  oX X   X X   X" ,
    "X X X X X X X X X X" ,
    "X X X X X X X X X X" ,
    "X X X XXX X XXX X X" ,
    "X X X   r X     X X" ,
    "X X XXX XXX XXXXX X" ,
    "X X         X  b  X" ,
    "X XXXXXXXXX X XXXXX" ,
    "X    p    X X     X" ,
    "X XXXXXXX X XXXXX X" ,
    "X X     X X     X X" ,
    "X X XXX X XXX X X X" ,
    "X X X   X   X X X X" ,
    "X X X XXXXX X X X X" ,
    "X X X       X X X X" ,
    "X X XXXXXXXXX X X X" ,
    "e P               X" ,
    "XXXXXXXXXXXXXXXXXXX" ,

};

// level 7: easy

private String[] tileMap7 = {

    "XXXXXXXXXXXXXXXXXXX" ,
    "X        X        X" ,
    "X XXXXXX X XXXXXX X" ,
    "X X      X      X X" ,
    "X X XXXXXXXXXXX X X" ,
    "X X      r o    X X" ,
    "X XXXXXXX X XXXXXXX" ,
    "X       X X X     X" ,
    "XXXXXXX X X X XXXXX" ,
    "e         b       E" ,
    "XXXXXXX X X X XXXXX" ,
    "X       X p X     X" ,
    "X XXXXXXX X XXXXXXX" ,
    "X X             X X" ,
    "X X XXXXXXXXXXX X X" ,
    "X X      P      X X" ,
    "X XXXXXX X XXXXXX X" ,
    "X        X        X" ,
    "XXXXXXXXXX XXXXXXXX" ,
    "X                 X" ,
    "XXXXXXXXXXXXXXXXXXX" ,

};


// level 8: easy

private String[] tileMap8 = {

    "XXXXXXXXXXXXXXXXXXX" ,
    "X   r    X     b  X" ,
    "X XXXXXX X XXXXXX X" ,
    "X Xo           pX X" ,
    "X X XXXXX XXXXX X X" ,
    "X X X         X X X" ,
    "X X X XX   XX X X X" ,
    "X   X X     X X   X" ,
    "XXX X XXXXXXX X XXX" ,
    "e                 E" ,
    "XXX X XXXXXXX X XXX" ,
    "X   X X     X X   X" ,
    "X X X XX   XX X X X" ,
    "X X X         X X X" ,
    "X X XXXXX XXXXX X X" ,
    "X X      P      X X" ,
    "X XXXXXX X XXXXXX X" ,
    "X X      X      X X" ,
    "X X XXXXXXXXXXXXX X" ,
    "X                 X" ,
    "XXXXXXXXXXXXXXXXXXX" ,

};


// level 9: easy

private String[] tileMap9 = {

    "XXXXXXXXXXXXXXXXXXX" ,
    "X         o       X",
    "X XXX XXX XXX XXX X",
    "X XXX XX   XX XXX X",
    "X XXX X     X XXX X",
    "X        r        X",
    "X XXX XXXXXXX XXX X",
    "X                 X",
    "X XXXXXXX XXXXXXX X",
    "e    b     p      E",
    "X XXXXXXX XXXXXXX X",
    "X                 X",
    "X XXX XXXXXXX XXX X",
    "X        P        X",
    "X XXX X     X XXX X",
    "X XXX XX   XX XXX X",
    "X XXX XXX XXX XXX X",
    "X                 X",
    "X XX XXXXXXXXXXXX X",
    "X                 X",
    "XXXXXXXXXXXXXXXXXXX",
};


// level 10: easy

private String[] tileMap10 = {

    "XXXXXXXXXXXXXeXXXXX" ,
    "X  p           o  X" ,
    "X XXXXX X X XXXXX X" ,
    "X X   X X X X   X X" ,
    "X X X X X X X X X X" ,
    "X X X X X X X X X X" ,
    "X X X XXX XXX X X X" ,
    "X X X     r   X X X" ,
    "X X XXXXX XXXXX X X" ,
    "X X             X X" ,
    "X XXXXX XXX XXXXX X" ,
    "m       X P       M" ,
    "X XXXXX XXX XXXXX X" ,
    "X X      b      X X" ,
    "X X XXXXX XXXXX X X" ,
    "X X X         X X X" ,
    "X X X XXXXXXX X X X" ,
    "X X X         X X X" ,
    "X X XXXXXXXXXXX X X" ,
    "X                 X" ,
    "XXEXXXXXXXXXXXXXXXX" ,

};

// level 11: easy

private String[] tileMap11 = {

    "XXOXXXXXXXXXXXXXXXX" ,
    "X          o      X" ,
    "X XXX XXXX XXXX XXX" ,
    "X X  p            X" ,
    "X X XXXXXXXXXX XX X" ,
    "X X X           X X" ,
    "X X X XXXXr X X X X" ,
    "X X X X     X X X X" ,
    "X X X   XXX X X X X" ,
    "m   X X X X X X   M" ,
    "X XXX X X b X X XXX" ,
    "X X   X X   X X X X" ,
    "X X XXX XXX X X X X" ,
    "X X X         X X X" ,
    "X X X XXXXXXX X X X" ,
    "X X X    P    X X X" ,
    "X X XXXXXXXXXXX X X" ,
    "X X               X" ,
    "X XXXXXXXX XXXXXXXX" ,
    "X                 X" ,
    "XXXXXXXXXXXXXXXXOXX" ,

};

// level 12: easy

private String[] tileMap12 = {

    "XXXXXXXXXXXXXXXXXXX" ,
    "X                 X" ,
    "X X XXXXXXXXXXX X X" ,
    "X X      r      X X" ,
    "X XXXXX XXXXX XXXXX" ,
    "X X             X X" ,
    "X X XXXXXXXXXXX X X" ,
    "X X      b      X X" ,
    "X XXXXX XXXXX XXXXX" ,
    "m       X P       M" ,
    "X XXXXX XXXXX XXXXX" ,
    "X X      p      X X" ,
    "X X XXXXXXXXXXX X X" ,
    "X X             X X" ,
    "X XXXXX XXXXX XXXXX" ,
    "X X      o      X X" ,
    "X X XXXXXXXXXXX X X" ,
    "X                 X" ,
    "XXXXXXXXXXXXXXXXX X" ,
    "X                 X" ,
    "XXXXXXXXXXXXXXXXXXX" ,

};



// medium levels

// level 13 : medium

private String[] tileMap13 = {

    "XXXXXXXXXXXXXXXXXXX" ,
    "X       X     X   X" ,
    "X XXX X XXX X X X X" ,
    "X X   X  r  X X X X" ,
    "X X XXXXX XXX X X X" ,
    "X Xo      X     X X" ,
    "X XXXXX X X XXXXX X" ,
    "X     X X X X     X" ,
    "XXX X X X X X X XXX" ,
    "m   X X XpX X X   M" ,
    "XXX X X X X X X XXX" ,
    "X   X X X X X X   X" ,
    "X XXX X X X XXX X X" ,
    "X X     X       X X" ,
    "X X XXX XXXXXXX X X" ,
    "X X Xb            X" ,
    "X X XXXXX XXXXX X X" ,
    "X       X P X     X" ,
    "XXXXXXX X X XXXXXXX" ,
    "X         X       X" ,
    "XXXXXXXXXXXXXXXXXXX" , 
};


// level 14: medium

private String[] tileMap14 = {

    "XXXXXXXXXXXXXXXXXXX" ,
    "X   X  o    X     X" ,
    "X X X XXXXX X XXX X" ,
    "X X X  r  X X X   X" ,
    "X X XXX X X X X XXX" ,
    "X X   X X X X X   X" ,
    "X XXX X X X XXXXX X" ,
    "X X   X X X     X X" ,
    "X X XXX X XXXXX X X" ,
    "e   X XpX X   X   E" ,
    "X XXX X X X X XXX X" ,
    "X     X X X X     X" ,
    "X XXXXX X X XXX XXX" ,
    "X X     X X X     X" ,
    "X X XXXXX X XXXXX X" ,
    "X   Xb    X       X" ,
    "X XXXXX XXXXXXXXX X" ,
    "X     X P   X     X" ,
    "XXX X XXXXX X XXX X" ,
    "X   X       X     X" ,
    "XXXXXXXXXXXXXXXXXXX"     ,

};


// level 15: medium [ICE TILES]

private String[] tileMap15 = {
    "XXXXXXXXXmXXXXXXXXX" ,
    "X       X        IX" ,
    "X XXXXX X XXXXXXXIX" ,
    "X X   X  r  X    IX" ,
    "X X X XXXXX X XXXXX" ,
    "X X X       X     X" ,
    "X X XXXXX XXXXX X X" ,
    "X X    IX     X X X" ,
    "X XX XXIXXXXX X X X" ,
    "eI   X Ip    I    E" ,
    "XIXXXX XXXIXXIXXX X" ,
    "XI   X      XIX   X" ,
    "XIXX XXXXXX XIX XXX" ,
    "XIX         XIX   X" ,
    "XIX XXXXXX XXIXXX X" ,
    "XI  X  b  IIII  X X" ,
    "XIXXXXX XXXXX XXX X" ,
    "XI      P         X" ,
    "XXXXXXX XXX XXXXXXX" ,
    "X                 X" ,
    "XXXXXXXMXXXXXXXXXXX"     ,

};



// LEVEL 16: MEDIUM [ICE TILES ADVANCED]
private String[] tileMap16 = {

    "XXXXXXXXXXXXXXXXXMX" ,
    "X     I X I    o  X" ,
    "X XXXXX X XXXXXXX X" ,
    "X X   X Ir  X     X" ,
    "X X X XXXXX X XXXXX" ,
    "X X X       X     X" ,
    "X X X XXX X XXXX XX" ,
    "X X IIIIIXIIII X XX" ,
    "X X XXXXIXXXXX X XX" ,
    "e   IIIIIpIIII    E" ,
    "X X XXXXIXXXXX X XX" ,
    "X X IIIIIXIIII X  X" ,
    "X X XIXXX XIXXXX XX" ,
    "X X XI     IX     X" ,
    "X X XXXXX XXXXX X X" ,
    "X   X  b  X       X" ,
    "X XXXXX X XXXXX X X" ,
    "X     I P I       X" ,
    "XXXXXXI X IXXXXXXXX" ,
    "X      I I        X" ,
    "XXXXXXXmXXXXXXXXXXX" ,

};


// LEVEL 17 : MEDIUM [TELEPORT]

private String[] tileMap17 = {

    "XXXXXXXXXXXXXXXXXXX" ,
    "X       X         X" ,
    "X XXXXX X XXXXXXX X" ,
    "X X T X  r  X     X" ,
    "X X X XXXXX X XXXXX" ,
    "X X X  P    X     X" ,
    "X X XXXXX XXXXX X X" ,
    "X X     X     X X X" ,
    "X XXXXX X XXX X X X" ,
    "e   X    p  T X   E" ,
    "X XXX XXX X XXXXX X" ,
    "X   X       X     X" ,
    "X X XXXXXXXXX XXX X" ,
    "X X           X   X" ,
    "X XXXXXXXXXXX X X X" ,
    "X   X  b  X   X   X" ,
    "X X XXXXX X XXX X X" ,
    "X X     X T     X X" ,
    "XXXXXXX XXX XXXXXXX" ,
    "X                 X" ,
    "XXXXXXXXXXXXXXXXXXX",

};


// LEVEL 18: MEDIUM [ TELEPORT ADVANCED]
private String[] tileMap18 = {
    "XXXXXXXXXXXXXXXXXXX" ,
    "X   o T X T   T   X" ,
    "X XXXXX X XXXXXXX X" ,
    "X X   X  r  X     X" ,
    "X X X XXXXX X XXXXX" ,
    "X X X T     X     X" ,
    "X X XXXXX XXXXX X X" ,
    "X X     X     X X X" ,
    "X XXXXX X XXX X X X" ,
    "e   X T  P  T X   E" ,
    "X XXX XXX X XXXXX X" ,
    "X   X   T   X     X" ,
    "X X XXXXXXXXX XXX X" ,
    "X X     T     X   X" ,
    "X XXXXXXXXXXX X X X" ,
    "X   X  b  X   X   X" ,
    "X X XXXXX X XXX X X" ,
    "X X  T  X T  T  X X" ,
    "XXXXXXX XXX XXXXXXX" ,
    "X     T    p  T   X" ,
    "XXXXXXXXXXXXXXXXXXX"   ,

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



private String[] tileMap19 = {
    "XXXXXXXXXXXXXXXXXXX" ,
    "X  S    X      b  X" ,
    "X XXXXX X XXXXXXX X" ,
    "X X   X  r  X     X" ,
    "XSX X XXXXX X XXXXX" ,
    "X X X       X     X" ,
    "X X XXXXXSXXXXX X X" ,
    "X X     X     X X X" ,
    "X XXXXX X XXX X X X" ,
    "e  X   Xp    X    E" ,
    "X XXX XXXXX XXXXX X" ,
    "X   X       X     X" ,
    "XSX XXXXoXXXX XXXSX" ,
    "X X     S     X   n" ,
    "X XXXXXXXXXXX X X X" ,
    "X   X     X   X   X" ,
    "X X XXXSXX X XXXX X" ,
    "X X     X P     X X" ,
    "XXXXXXX XXX XXXXXXX" ,
    "X       S         X" ,
    "XXXXXXXXXXXXXXXXXXX" ,

};


private String[] tileMap20 = {
    "XXXXXXXXXXXXXXXXXXX" ,
    "X  o SS X SS      X" ,
    "X XXXXX X XXXXXXX X" ,
    "X XSS  r  X       X" ,
    "XSXSX XXXXX XSXXXXX" ,
    "XSX X       XSS   X" ,
    "X X XXXXXSXXXXX X X" ,
    "  X      S  X X    " ,
    "X XXXXX XSXXX X X X" ,
    "e   X   Xp    X   E" ,
    "X XXX XXX X XXXXXSX" ,
    "X   X       X    SX" ,
    "X X XXXXXXXXX XXX X" ,
    "X X     SS    X   X" ,
    "XSX XXXXXXXXX XSX X" ,
    "XS     b  X   XS  X" ,
    "X X XXXXX X XXXSX X" ,
    "X  S    X P    SX X" ,
    "XXXSXXX XXX XXXSXXX" ,
    "X    SS SSS SS    X" ,
    "XXXXXXXXXXXXXXXXXXX" ,

};


// level 21: phantom zones : here ghosts can pass the walls if near the phantom zones but pacman cant

private String[] tileMap21 = {

    "XXXXXXXXXXXXXXXXXXX" ,
    "X             o   X" ,
    "X XXXXXXX XXXXXXX X" ,
    "X XZ    X X    ZX X" ,
    "X X XXX X X XXX X X" ,
    "X X XZ  r  ZX X X X" ,
    "X X X XXXXX X X X X" ,
    "X   X       X X   X" ,
    "XXX XXXXXXXXX XXXXX" ,
    "e      ZXp        E" ,
    "XXX XXXXX XXX XXXXX" ,
    "X   X       X     X" ,
    "X X X XXXXX X XXX X" ,
    "X X XZ  b  ZX XZX X" ,
    "X X XXX X XXX X X X" ,
    "X XZ         ZX X X" ,
    "X XXXXXXX XXXXX X X" ,
    "X       XZP     X X" ,
    "X XXXXX XXX XXX X X" ,
    "X               X X" ,
    "XXXXXXXXXXXXXXXXXXX" ,

};

private String[] tileMap22 = {
    "XXXXXXXXXXXXXXXXXXX" ,
    "X                 X" ,
    "X XXX XXXZXXX XXX X" ,
    "X X XZ    r  ZX X X" ,
    "X X XXXZXXXZXXX X X" ,
    "X   XZ       ZX   X" ,
    "XXX X XXXXXXX X XXX" ,
    "X   XZ       ZXZ  X" ,
    "X XXX XXX XXX XXX X" ,
    "e      ZXpXZ      E" ,
    "X XXX XXX XXX XXX X" ,
    "X  ZXZ       ZX   X" ,
    "XXX X XXXXXXX X XXX" ,
    "X   XZ   b   ZX   X" ,
    "X X XXXZXXXZXXX X X" ,
    "X X XZ       ZX X X" ,
    "X X X XXXXXXX X X X" ,
    "X XZ Z   P   Z ZX X" ,
    "X XXXXXXXXXXXXXXX X" ,
    "X                 X" ,
    "XXXXXXXXXXXXXXXXXXX" ,

};


private String[] tileMap23 = {
    "XXXXXXXXXXXXXXXXXXX" ,
    "X                 X" ,
    "X XXX XXXXXXXXX X X" ,
    "X   X X       X   X" ,
    "X X X X XXXXX XXX X" ,
    "X X X X X   X   X X" ,
    "X X XXX X X XXX X X" ,
    "X X     X r X   X X" ,
    "X XXXXXXX  XX X X X" ,
    "e     R   p   R   E" ,
    "X XXXXXXX X X XXX X" ,
    "X       X X     X X" ,
    "X XXXXX X X XXX X X" ,
    "X X     X b X X X X" ,
    "X X XXXXX XXX X X X" ,
    "X X X           X X" ,
    "X X XXXXXXXXXXX X X" ,
    "X X      PR       X" ,
    "X XXXXXXX XXXXXXX X" ,
    "X                 X" ,
    "XXXXXXXXXXXXXXXXXXX" ,

};



private String[] tileMap24 = {
    "XXXXXXXXXXXXXXXXXXX" ,
    "X   R     R   R   X" ,
    "X X XXXXX XXX XXX X" ,
    "X X X   X X X X X X" ,
    "X X X X X X X X X X" ,
    "X X X X XRrRX X X X" ,
    "X X X X XXX X X X X" ,
    "X   X X     X   X X" ,
    "X XXX XXX XXX XXX X" ,
    "e  R R   p   R R  E" ,
    "XXXXX XXX XXX XXX X" ,
    "X     X     X     X" ,
    "X XXX X XXX X XXX X" ,
    "X X X X XbX X X   X" ,
    "X X X X X X X X X X" ,
    "m X   X R X R   X M" ,
    "X XXXXX XXX XXXXX X" ,
    "X R     P     R   X" ,
    "X XXXXXXRXXXXXXXX X" ,
    "X     R     R     X" ,
    "XXXXXXXXXXXXXXXXXXX" ,

};


private String[] tileMap25 = {
    
    "XXXXXXXXXXXXXXXXXXX" ,
    "X  oW   X    W    X" ,
    "X XXXXX X XXXXX X X" ,
    "X X   X X X   X X X" ,
    "X X X X X X X XXX X" ,
    "X X X X r X X   X X" ,
    "X X XXX XXX XXX X X" ,
    "X X     X     X X X" ,
    "X XXXXX X XXXXX X X" ,
    "e  w  X p X  W    E" ,
    "X XXX XXX XXX XXX X" ,
    "X X         X X X X" ,
    "X X XXXXXXX X X X X" ,
    "X X    b    X X X X" ,
    "X X X XXX X X X X X" ,
    "X X           X X X" ,
    "X X XXXXXXX XXX X X" ,
    "X   X   P  w    X X" ,
    "X XXX XXXXXXXXXXX X" ,
    "X     X         w X" ,
    "XXXXXXXXXXXXXXXXXXX" , 
    
};


private String[] tileMap26 = {
    "XXXXXXXXXXXXXXXXXXX" ,
    "X  W    X    w  b X" ,
    "X XXXXX X XXXXXXX X" ,
    "X X   X X X     X X" ,
    "X X X X X X XXX X X" ,
    "X X X X X X X X X X" ,
    "X X X X W X X o X X" ,
    "X X X X X X X X X X" ,
    "X X X  r  X X X X X" ,
    "e   XXXXX X X X w E" ,
    "X X X  p  X X X X X" ,
    "X X X XXX X   X X X" ,
    "X X X X X X XXX X X" ,
    "X XXXXX w X X W X X" ,
    "X X X X X X X XXX X" ,
    "X X X     X X     X" ,
    "X X XXXXXXX XXXXX X" ,
    "m w       P     w M" ,
    "X XXXXXXX XXXXXXX X" ,
    "X W             W X" ,
    "XXXXXXXXXXXXXXXXXXX" ,

};

     // creating hashset for the constructor block used above
    HashSet<Block> walls;   // it is called as hashset of block and is named as walls
    HashSet<Block> spaces1;  // symbol : E // for moving pacman from one end to other
    HashSet<Block> spaces2;  // SYMBOL : M // for moving pacman from one end to other
    HashSet<Block> exitSpaces1;  // SYMBOL : e // for letting pacman get back to map after spaces
    HashSet<Block> exitSpaces2;  // symbol : m // for letting pacman get back to map after spaces
    HashSet<Block> foods;  // hashset of block and is named as foods
    HashSet<Block> ghosts;  // hashset of block and is named as ghost
    Block pacman;   // we had declared a block for pacman 
    HashSet<Block> iceBlocks;   // this is for level 15 where i will be using it . 
    HashSet<Block> teleportPads;  // for level 17 
    HashSet<Block> speedZones; // for level 19
    HashSet<Block> phantomZones; //for level 21
    HashSet<Block> reverseControls; // for level 23
    HashSet<Block> warpEntrance;  // for level 25 
    HashSet<Block> warpExit;   // for level 25

    

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
        wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();   // in the resource we need to specify the path of the image where it is located . getClass refers to the class PacMan where we are writing the code. and this file is located inside src folder and our images are also located inside the same folder so we made a string inside getResource "./" this basically means that we are going to look from the same folder and we just need to specify the image name. if i had not written getImage then it would have created an image icon. but since we need the image so i used .getImage()
        blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage(); 
        pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage(); 
        redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage(); 
        orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage(); 
        scaredGhostImage = new ImageIcon(getClass().getResource("./scaredGhost.png")).getImage(); 
        pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage(); 
        pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage(); 
        pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage(); 
        pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage(); 
        powerFoodImage = new ImageIcon(getClass().getResource("./powerFood.png")).getImage(); 
        cherryImage = new ImageIcon(getClass().getResource("./cherry.png")).getImage(); 
        cherry2Image = new ImageIcon(getClass().getResource("./cherry2.png")).getImage();
        iceImage = new ImageIcon(getClass().getResource("./ice.png")).getImage();
        teleportImage = new ImageIcon(getClass().getResource("./teleport.png")).getImage();
        speedImage = new ImageIcon(getClass().getResource("./speed.png")).getImage();
        // phantomImage = new ImageIcon(getClass().getResource("./cherry.png")).getImage();
        reverseImage = new ImageIcon(getClass().getResource("./reverse.png")).getImage();
        warpEntranceImage = new ImageIcon(getClass().getResource("./wrapEntrance.png")).getImage();
        warpExitImage = new ImageIcon(getClass().getResource("./ice.png")).getImage();
        
        
        
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
         loadMap26();  // calling the function loadmap bcz we want to check the number of walls , foods , ghosts and its algo is written inside the function loadMap();
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
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>();   // these hashsets like teleport and iceblocks even if you are not using them in the current level but if you have somehwere in the program the usage of them then you had to implement this here also. 
        iceBlocks = new HashSet<Block>();   // bcz we have used somehwere in our program the for loop which iterates over this hashset so it doesn’t matter if there are actually any ice blocks in the map — if the HashSet was never initialized, then Java throws a NullPointerException.
        // therefore Always initialize iceBlocks, even if it's empty .This way, your code won’t crash if it tries to iterate over or check iceBlocks, because it will just be an empty set instead of null.
        speedZones = new HashSet<Block>();
        reverseControls = new HashSet<Block>();
        phantomZones = new HashSet<Block>();
        warpEntrance = new HashSet<Block>();
        warpExit = new HashSet<Block>();
        spaces1 = new HashSet<Block>();
        spaces2 = new HashSet<Block>();
        exitSpaces1 = new HashSet<Block>();
        exitSpaces2 = new HashSet<Block>();

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


                if ( tileMapChar == 'X'){  // block wall

                    Block wall = new Block(wallImage , x , y , tileSize , tileSize);  // since we had created a class Block and a constructor Block also so here we are creating an object for the constructor Block. this is the object for constructor bcz here i had declared paramters also so it tells the program that we creating an object for constructor
                    walls.add(wall);  // Adds the newly created wall to a HashSet that stores all the walls in the game.
                    // this ensures that all walls are stored in one list which is walls here
                    // it makes easier to draw all the walls in the game 
                    // used for collision detection so that pacman cant go throught walls
                }

                else if ( tileMapChar == ' ')  // food
                {
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMapChar == 'E')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMapChar == 'e')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMapChar == 'M')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMapChar == 'm')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }

                else if( tileMapChar == 'o')   // orange ghost
                {

                    Block OrangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost);
                }


                else if( tileMapChar == 'b')   // blue ghost
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }


                else if( tileMapChar == 'r')   // red ghost
                {

                    Block RedGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost);
                }


                else if( tileMapChar == 'p')   // pink ghost
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
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

                else if ( tileMapChar == 'Z')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tileMapChar == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }


                
                else if ( tileMapChar == 'W')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMapChar == 'w')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }


                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    }; 



    public void loadMap2(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>();
        iceBlocks = new HashSet<Block>();
        speedZones = new HashSet<Block>();
        reverseControls = new HashSet<Block>();
        phantomZones = new HashSet<Block>();
        warpEntrance = new HashSet<Block>();
        warpExit = new HashSet<Block>();
        spaces1 = new HashSet<Block>();
        spaces2 = new HashSet<Block>();
        exitSpaces1 = new HashSet<Block>();
        exitSpaces2 = new HashSet<Block>();


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


                if ( tileMap2Char == 'X'){  // block wall

                    Block wall = new Block(wallImage , x , y , tileSize , tileSize);  // since we had created a class Block and a constructor Block also so here we are creating an object for the constructor Block. this is the object for constructor bcz here i had declared paramters also so it tells the program that we creating an object for constructor
                    walls.add(wall);  // Adds the newly created wall to a HashSet that stores all the walls in the game.
                    // this ensures that all walls are stored in one list which is walls here
                    // it makes easier to draw all the walls in the game 
                    // used for collision detection so that pacman cant go throught walls
                }

                else if ( tileMap2Char == ' ')  // food
                {
                    System.out.println("adding foods");
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMap2Char == 'E')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMap2Char == 'e')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMap2Char == 'M')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMap2Char == 'm')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }

                else if( tileMap2Char == 'o')   // orange ghost
                {

                    Block OrangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost);
                }


                else if( tileMap2Char == 'b')   // blue ghost
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }


                else if( tileMap2Char == 'r')   // red ghost
                {

                    Block RedGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost);
                }


                else if( tileMap2Char == 'p')   // pink ghost
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }


                else if ( tileMap2Char == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }



                else if ( tileMap2Char == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }


                else if ( tileMap2Char == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }


                
                
                else if ( tileMap2Char == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }

                else if ( tileMap2Char == 'Z')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tileMap2Char == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }

                
                else if ( tileMap2Char == 'W')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMap2Char == 'w')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }

                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    };
   



    public void loadMap3(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>();
        iceBlocks = new HashSet<Block>();
        speedZones = new HashSet<Block>();
        reverseControls = new HashSet<Block>();
        phantomZones = new HashSet<Block>();
        warpEntrance = new HashSet<Block>();
        warpExit = new HashSet<Block>();
        spaces1 = new HashSet<Block>();
        spaces2 = new HashSet<Block>();
        exitSpaces1 = new HashSet<Block>();
        exitSpaces2 = new HashSet<Block>();

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


                if ( tileMap3Char == 'X'){  // block wall

                    Block wall = new Block(wallImage , x , y , tileSize , tileSize);  // since we had created a class Block and a constructor Block also so here we are creating an object for the constructor Block. this is the object for constructor bcz here i had declared paramters also so it tells the program that we creating an object for constructor
                    walls.add(wall);  // Adds the newly created wall to a HashSet that stores all the walls in the game.
                    // this ensures that all walls are stored in one list which is walls here
                    // it makes easier to draw all the walls in the game 
                    // used for collision detection so that pacman cant go throught walls
                }

                else if ( tileMap3Char == ' ')  // food
                {
                    System.out.println("adding foods");
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMap3Char == 'E')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMap3Char == 'e')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMap3Char == 'M')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMap3Char == 'm')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }

                else if( tileMap3Char == 'o')   // orange ghost
                {

                    Block OrangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost);
                }


                else if( tileMap3Char == 'b')   // blue ghost
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }


                else if( tileMap3Char == 'r')   // red ghost
                {

                    Block RedGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost);
                }


                else if( tileMap3Char == 'p')   // pink ghost
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }


                else if ( tileMap3Char == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }


                else if ( tileMap3Char == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }


                else if ( tileMap3Char == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }


                
                
                else if ( tileMap3Char == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }


                else if ( tileMap3Char == 'Z')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tileMap3Char == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }

                
                else if ( tileMap3Char == 'W')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMap3Char == 'w')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }

                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    };




    public void loadMap4(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>();
        iceBlocks = new HashSet<Block>();
        speedZones = new HashSet<Block>();
        reverseControls = new HashSet<Block>();
        phantomZones = new HashSet<Block>();
        warpEntrance = new HashSet<Block>();
        warpExit = new HashSet<Block>();
        spaces1 = new HashSet<Block>();
        spaces2 = new HashSet<Block>();
        exitSpaces1 = new HashSet<Block>();
        exitSpaces2 = new HashSet<Block>();


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


                if ( tileMap4Char == 'X'){  // block wall

                    Block wall = new Block(wallImage , x , y , tileSize , tileSize);  // since we had created a class Block and a constructor Block also so here we are creating an object for the constructor Block. this is the object for constructor bcz here i had declared paramters also so it tells the program that we creating an object for constructor
                    walls.add(wall);  // Adds the newly created wall to a HashSet that stores all the walls in the game.
                    // this ensures that all walls are stored in one list which is walls here
                    // it makes easier to draw all the walls in the game 
                    // used for collision detection so that pacman cant go throught walls
                }

                else if ( tileMap4Char == ' ')  // food
                {
                    System.out.println("adding foods");
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMap4Char == 'E')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMap4Char == 'e')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMap4Char == 'M')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMap4Char == 'm')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }

                else if( tileMap4Char == 'o')   // orange ghost
                {

                    Block OrangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost);
                }


                else if( tileMap4Char == 'b')   // blue ghost
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }


                else if( tileMap4Char == 'r')   // red ghost
                {

                    Block RedGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost);
                }


                else if( tileMap4Char == 'p')   // pink ghost
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }


                else if ( tileMap4Char == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }


                else if ( tileMap4Char == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }


                else if ( tileMap4Char == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }


                
                
                else if ( tileMap4Char == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }


                else if ( tileMap4Char == 'Z')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tileMap4Char == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }

                
                else if ( tileMap4Char == 'W')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMap4Char == 'w')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }
                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    };




    public void loadMap5(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>();
        iceBlocks = new HashSet<Block>();
        speedZones = new HashSet<Block>();
        reverseControls = new HashSet<Block>();
        phantomZones = new HashSet<Block>();
        warpEntrance = new HashSet<Block>();
        warpExit = new HashSet<Block>();
        spaces1 = new HashSet<Block>();
        spaces2 = new HashSet<Block>();
        exitSpaces1 = new HashSet<Block>();
        exitSpaces2 = new HashSet<Block>();


        for ( int r = 0 ; r<rowCount ; r++)   // r stands for row
        {
            for (  int c = 0 ; c<colCount ; c++)   // c stands for column
            {
                String row = tileMap5[r];   // getting the current row bcz tileMap is our map and r index is the row index so we are getting the loop indexing 
                char tileMap5Char = row.charAt(c);  // getting the current character
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


                if ( tileMap5Char == 'X'){  // block wall

                    Block wall = new Block(wallImage , x , y , tileSize , tileSize);  // since we had created a class Block and a constructor Block also so here we are creating an object for the constructor Block. this is the object for constructor bcz here i had declared paramters also so it tells the program that we creating an object for constructor
                    walls.add(wall);  // Adds the newly created wall to a HashSet that stores all the walls in the game.
                    // this ensures that all walls are stored in one list which is walls here
                    // it makes easier to draw all the walls in the game 
                    // used for collision detection so that pacman cant go throught walls
                }

                else if ( tileMap5Char == ' ')  // food
                {
                    System.out.println("adding foods");
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMap5Char == 'E')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMap5Char == 'e')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMap5Char == 'M')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMap5Char == 'm')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }

                else if( tileMap5Char == 'o')   // orange ghost
                {

                    Block OrangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost);
                }


                else if( tileMap5Char == 'b')   // blue ghost
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }


                else if( tileMap5Char == 'r')   // red ghost
                {

                    Block RedGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost);
                }


                else if( tileMap5Char == 'p')   // pink ghost
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }


                else if ( tileMap5Char == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }


                else if ( tileMap5Char == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }


                else if ( tileMap5Char == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }


                
                
                else if ( tileMap5Char == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }


                else if ( tileMap5Char == 'Z')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tileMap5Char == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }

                
                else if ( tileMap5Char == 'W')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMap5Char == 'w')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }
                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    };




    public void loadMap6(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>();
        iceBlocks = new HashSet<Block>();
        speedZones = new HashSet<Block>();
        reverseControls = new HashSet<Block>();
        phantomZones = new HashSet<Block>();
        warpEntrance = new HashSet<Block>();
        warpExit = new HashSet<Block>();
        spaces1 = new HashSet<Block>();
        spaces2 = new HashSet<Block>();
        exitSpaces1 = new HashSet<Block>();
        exitSpaces2 = new HashSet<Block>();


        for ( int r = 0 ; r<rowCount ; r++)   // r stands for row
        {
            for (  int c = 0 ; c<colCount ; c++)   // c stands for column
            {
                String row = tileMap6[r];   // getting the current row bcz tileMap is our map and r index is the row index so we are getting the loop indexing 
                char tileMap6Char = row.charAt(c);  // getting the current character
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


                if ( tileMap6Char == 'X'){  // block wall

                    Block wall = new Block(wallImage , x , y , tileSize , tileSize);  // since we had created a class Block and a constructor Block also so here we are creating an object for the constructor Block. this is the object for constructor bcz here i had declared paramters also so it tells the program that we creating an object for constructor
                    walls.add(wall);  // Adds the newly created wall to a HashSet that stores all the walls in the game.
                    // this ensures that all walls are stored in one list which is walls here
                    // it makes easier to draw all the walls in the game 
                    // used for collision detection so that pacman cant go throught walls
                }

                else if ( tileMap6Char == ' ')  // food
                {
                    System.out.println("adding foods");
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMap6Char == 'E')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMap6Char == 'e')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMap6Char == 'M')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMap6Char == 'm')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }

                else if( tileMap6Char == 'o')   // orange ghost
                {

                    Block OrangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost);
                }


                else if( tileMap6Char == 'b')   // blue ghost
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }


                else if( tileMap6Char == 'r')   // red ghost
                {

                    Block RedGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost);
                }


                else if( tileMap6Char == 'p')   // pink ghost
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }


                else if ( tileMap6Char == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }


                else if ( tileMap6Char == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }


                else if ( tileMap6Char == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }


                
                
                else if ( tileMap6Char == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }

                else if ( tileMap6Char == 'Z')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tileMap6Char == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }

                
                else if ( tileMap6Char == 'W')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMap6Char == 'w')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }
                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    };





    public void loadMap7(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>();
        iceBlocks = new HashSet<Block>();
        speedZones = new HashSet<Block>();
        reverseControls = new HashSet<Block>();
        phantomZones = new HashSet<Block>();
        warpEntrance = new HashSet<Block>();
        warpExit = new HashSet<Block>();
        spaces1 = new HashSet<Block>();
        spaces2 = new HashSet<Block>();
        exitSpaces1 = new HashSet<Block>();
        exitSpaces2 = new HashSet<Block>();


        for ( int r = 0 ; r<rowCount ; r++)   // r stands for row
        {
            for (  int c = 0 ; c<colCount ; c++)   // c stands for column
            {
                String row = tileMap7[r];   // getting the current row bcz tileMap is our map and r index is the row index so we are getting the loop indexing 
                char tileMap7Char = row.charAt(c);  // getting the current character
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


                if ( tileMap7Char == 'X'){  // block wall

                    Block wall = new Block(wallImage , x , y , tileSize , tileSize);  // since we had created a class Block and a constructor Block also so here we are creating an object for the constructor Block. this is the object for constructor bcz here i had declared paramters also so it tells the program that we creating an object for constructor
                    walls.add(wall);  // Adds the newly created wall to a HashSet that stores all the walls in the game.
                    // this ensures that all walls are stored in one list which is walls here
                    // it makes easier to draw all the walls in the game 
                    // used for collision detection so that pacman cant go throught walls
                }

                else if ( tileMap7Char == ' ')  // food
                {
                    System.out.println("adding foods");
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMap7Char == 'E')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMap7Char == 'e')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMap7Char == 'M')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMap7Char == 'm')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }

                else if( tileMap7Char == 'o')   // orange ghost
                {

                    Block OrangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost);
                }


                else if( tileMap7Char == 'b')   // blue ghost
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }


                else if( tileMap7Char == 'r')   // red ghost
                {

                    Block RedGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost);
                }


                else if( tileMap7Char == 'p')   // pink ghost
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }


                else if ( tileMap7Char == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }


                else if ( tileMap7Char == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }


                else if ( tileMap7Char == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }


                
                
                else if ( tileMap7Char == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }


                else if ( tileMap7Char == 'Z')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tileMap7Char == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }

                
                else if ( tileMap7Char == 'W')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMap7Char == 'w')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }

                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    };




    public void loadMap8(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>();
        iceBlocks = new HashSet<Block>();
        speedZones = new HashSet<Block>();
        reverseControls = new HashSet<Block>();
        phantomZones = new HashSet<Block>();
        warpEntrance = new HashSet<Block>();
        warpExit = new HashSet<Block>();
        spaces1 = new HashSet<Block>();
        spaces2 = new HashSet<Block>();
        exitSpaces1 = new HashSet<Block>();
        exitSpaces2 = new HashSet<Block>();


        for ( int r = 0 ; r<rowCount ; r++)   // r stands for row
        {
            for (  int c = 0 ; c<colCount ; c++)   // c stands for column
            {
                String row = tileMap8[r];   // getting the current row bcz tileMap is our map and r index is the row index so we are getting the loop indexing 
                char tileMap8Char = row.charAt(c);  // getting the current character
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


                if ( tileMap8Char == 'X'){  // block wall

                    Block wall = new Block(wallImage , x , y , tileSize , tileSize);  // since we had created a class Block and a constructor Block also so here we are creating an object for the constructor Block. this is the object for constructor bcz here i had declared paramters also so it tells the program that we creating an object for constructor
                    walls.add(wall);  // Adds the newly created wall to a HashSet that stores all the walls in the game.
                    // this ensures that all walls are stored in one list which is walls here
                    // it makes easier to draw all the walls in the game 
                    // used for collision detection so that pacman cant go throught walls
                }

                else if ( tileMap8Char == ' ')  // food
                {
                    System.out.println("adding foods");
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMap8Char == 'E')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMap8Char == 'e')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMap8Char == 'M')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMap8Char == 'm')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }

                else if( tileMap8Char == 'o')   // orange ghost
                {

                    Block OrangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost);
                }


                else if( tileMap8Char == 'b')   // blue ghost
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }


                else if( tileMap8Char == 'r')   // red ghost
                {

                    Block RedGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost);
                }


                else if( tileMap8Char == 'p')   // pink ghost
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }


                else if ( tileMap8Char == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }


                else if ( tileMap8Char == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }


                else if ( tileMap8Char == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }


                
                
                else if ( tileMap8Char == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }

                else if ( tileMap8Char == 'Z')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tileMap8Char == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }

                
                else if ( tileMap8Char == 'W')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMap8Char == 'w')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }

                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    };





    public void loadMap9(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>();
        iceBlocks = new HashSet<Block>();
        speedZones = new HashSet<Block>();
        reverseControls = new HashSet<Block>();
        phantomZones = new HashSet<Block>();
        warpEntrance = new HashSet<Block>();
        warpExit = new HashSet<Block>();
        spaces1 = new HashSet<Block>();
        spaces2 = new HashSet<Block>();
        exitSpaces1 = new HashSet<Block>();
        exitSpaces2 = new HashSet<Block>();


        for ( int r = 0 ; r<rowCount ; r++)   // r stands for row
        {
            for (  int c = 0 ; c<colCount ; c++)   // c stands for column
            {
                String row = tileMap9[r];   // getting the current row bcz tileMap is our map and r index is the row index so we are getting the loop indexing 
                char tileMap9Char = row.charAt(c);  // getting the current character
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


                if ( tileMap9Char == 'X'){  // block wall

                    Block wall = new Block(wallImage , x , y , tileSize , tileSize);  // since we had created a class Block and a constructor Block also so here we are creating an object for the constructor Block. this is the object for constructor bcz here i had declared paramters also so it tells the program that we creating an object for constructor
                    walls.add(wall);  // Adds the newly created wall to a HashSet that stores all the walls in the game.
                    // this ensures that all walls are stored in one list which is walls here
                    // it makes easier to draw all the walls in the game 
                    // used for collision detection so that pacman cant go throught walls
                }

                else if ( tileMap9Char == ' ')  // food
                {
                    System.out.println("adding foods");
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMap9Char == 'E')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMap9Char == 'e')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMap9Char == 'M')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMap9Char == 'm')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }

                else if( tileMap9Char == 'o')   // orange ghost
                {

                    Block OrangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost);
                }


                else if( tileMap9Char == 'b')   // blue ghost
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }


                else if( tileMap9Char == 'r')   // red ghost
                {

                    Block RedGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost);
                }


                else if( tileMap9Char == 'p')   // pink ghost
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }


                else if ( tileMap9Char == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }


                else if ( tileMap9Char == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }


                else if ( tileMap9Char == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }


                
                
                else if ( tileMap9Char == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }


                else if ( tileMap9Char == 'Z')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tileMap9Char == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }

                
                else if ( tileMap9Char == 'W')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMap9Char == 'w')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }

                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    };






    public void loadMap10(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>();
        iceBlocks = new HashSet<Block>();
        speedZones = new HashSet<Block>();
        reverseControls = new HashSet<Block>();
        phantomZones = new HashSet<Block>();
        warpEntrance = new HashSet<Block>();
        warpExit = new HashSet<Block>();
        spaces1 = new HashSet<Block>();
        spaces2 = new HashSet<Block>();
        exitSpaces1 = new HashSet<Block>();
        exitSpaces2 = new HashSet<Block>();


        for ( int r = 0 ; r<rowCount ; r++)   // r stands for row
        {
            for (  int c = 0 ; c<colCount ; c++)   // c stands for column
            {
                String row = tileMap10[r];   // getting the current row bcz tileMap is our map and r index is the row index so we are getting the loop indexing 
                char tileMap10Char = row.charAt(c);  // getting the current character
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


                if ( tileMap10Char == 'X'){  // block wall

                    Block wall = new Block(wallImage , x , y , tileSize , tileSize);  // since we had created a class Block and a constructor Block also so here we are creating an object for the constructor Block. this is the object for constructor bcz here i had declared paramters also so it tells the program that we creating an object for constructor
                    walls.add(wall);  // Adds the newly created wall to a HashSet that stores all the walls in the game.
                    // this ensures that all walls are stored in one list which is walls here
                    // it makes easier to draw all the walls in the game 
                    // used for collision detection so that pacman cant go throught walls
                }

                else if ( tileMap10Char == ' ')  // food
                {
                    System.out.println("adding foods");
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMap10Char == 'E')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMap10Char == 'e')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMap10Char == 'M')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMap10Char == 'm')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }

                else if( tileMap10Char == 'o')   // orange ghost
                {

                    Block OrangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost);
                }


                else if( tileMap10Char == 'b')   // blue ghost
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }


                else if( tileMap10Char == 'r')   // red ghost
                {

                    Block RedGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost);
                }


                else if( tileMap10Char == 'p')   // pink ghost
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }


                else if ( tileMap10Char == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }



                else if ( tileMap10Char == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }


                else if ( tileMap10Char == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }


                
                
                else if ( tileMap10Char == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }


                else if ( tileMap10Char == 'Z')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tileMap10Char == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }

                
                else if ( tileMap10Char == 'W')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMap10Char == 'w')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }

                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    };






    public void loadMap11(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>();
        iceBlocks = new HashSet<Block>();
        speedZones = new HashSet<Block>();
        reverseControls = new HashSet<Block>();
        phantomZones = new HashSet<Block>();
        warpEntrance = new HashSet<Block>();
        warpExit = new HashSet<Block>();
        spaces1 = new HashSet<Block>();
        spaces2 = new HashSet<Block>();
        exitSpaces1 = new HashSet<Block>();
        exitSpaces2 = new HashSet<Block>();

        for ( int r = 0 ; r<rowCount ; r++)   // r stands for row
        {
            for (  int c = 0 ; c<colCount ; c++)   // c stands for column
            {
                String row = tileMap11[r];   // getting the current row bcz tileMap is our map and r index is the row index so we are getting the loop indexing 
                char tileMap11Char = row.charAt(c);  // getting the current character
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


                if ( tileMap11Char == 'X'){  // block wall

                    Block wall = new Block(wallImage , x , y , tileSize , tileSize);  // since we had created a class Block and a constructor Block also so here we are creating an object for the constructor Block. this is the object for constructor bcz here i had declared paramters also so it tells the program that we creating an object for constructor
                    walls.add(wall);  // Adds the newly created wall to a HashSet that stores all the walls in the game.
                    // this ensures that all walls are stored in one list which is walls here
                    // it makes easier to draw all the walls in the game 
                    // used for collision detection so that pacman cant go throught walls
                }

                else if ( tileMap11Char == ' ')  // food
                {
                    System.out.println("adding foods");
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMap11Char == 'E')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMap11Char == 'e')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMap11Char == 'M')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMap11Char == 'm')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }

                else if( tileMap11Char == 'o')   // orange ghost
                {

                    Block OrangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost);
                }


                else if( tileMap11Char == 'b')   // blue ghost
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }


                else if( tileMap11Char == 'r')   // red ghost
                {

                    Block RedGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost);
                }


                else if( tileMap11Char == 'p')   // pink ghost
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }


                else if ( tileMap11Char == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }


                else if ( tileMap11Char == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }


                else if ( tileMap11Char == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }


                
                
                else if ( tileMap11Char == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }

                else if ( tileMap11Char == 'Z')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tileMap11Char == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }


                
                else if ( tileMap11Char == 'W')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMap11Char == 'w')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }

                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    };





    public void loadMap12(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>();
        iceBlocks = new HashSet<Block>();
        speedZones = new HashSet<Block>();
        reverseControls = new HashSet<Block>();
        phantomZones = new HashSet<Block>();
        warpEntrance = new HashSet<Block>();
        warpExit = new HashSet<Block>();
        spaces1 = new HashSet<Block>();
        spaces2 = new HashSet<Block>();
        exitSpaces1 = new HashSet<Block>();
        exitSpaces2 = new HashSet<Block>();

        for ( int r = 0 ; r<rowCount ; r++)   // r stands for row
        {
            for (  int c = 0 ; c<colCount ; c++)   // c stands for column
            {
                String row = tileMap12[r];   // getting the current row bcz tileMap is our map and r index is the row index so we are getting the loop indexing 
                char tileMap12Char = row.charAt(c);  // getting the current character
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


                if ( tileMap12Char == 'X'){  // block wall

                    Block wall = new Block(wallImage , x , y , tileSize , tileSize);  // since we had created a class Block and a constructor Block also so here we are creating an object for the constructor Block. this is the object for constructor bcz here i had declared paramters also so it tells the program that we creating an object for constructor
                    walls.add(wall);  // Adds the newly created wall to a HashSet that stores all the walls in the game.
                    // this ensures that all walls are stored in one list which is walls here
                    // it makes easier to draw all the walls in the game 
                    // used for collision detection so that pacman cant go throught walls
                }

                else if ( tileMap12Char == ' ')  // food
                {
                    System.out.println("adding foods");
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMap12Char == 'E')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMap12Char == 'e')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMap12Char == 'M')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMap12Char == 'm')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }

                else if( tileMap12Char == 'o')   // orange ghost
                {

                    Block OrangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost);
                }


                else if( tileMap12Char == 'b')   // blue ghost
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }


                else if( tileMap12Char == 'r')   // red ghost
                {

                    Block RedGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost);
                }


                else if( tileMap12Char == 'p')   // pink ghost
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }


                else if ( tileMap12Char == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }


                else if ( tileMap12Char == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }


                else if ( tileMap12Char == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }

                
                
                else if ( tileMap12Char == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }


                else if ( tileMap12Char == 'Z')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tileMap12Char == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }

                
                else if ( tileMap12Char == 'W')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMap12Char == 'w')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }

                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    };


    public void loadMap13(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>();
        iceBlocks = new HashSet<Block>();
        speedZones = new HashSet<Block>();
        reverseControls  = new HashSet<Block>();
        phantomZones = new HashSet<Block>();
        warpEntrance = new HashSet<Block>();
        warpExit = new HashSet<Block>();
        spaces1 = new HashSet<Block>();
        spaces2 = new HashSet<Block>();
        exitSpaces1 = new HashSet<Block>();
        exitSpaces2 = new HashSet<Block>();

        for ( int r = 0 ; r<rowCount ; r++)   // r stands for row
        {
            for (  int c = 0 ; c<colCount ; c++)   // c stands for column
            {
                String row = tileMap13[r];   // getting the current row bcz tileMap is our map and r index is the row index so we are getting the loop indexing 
                char tileMap13Char = row.charAt(c);  // getting the current character
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


                if ( tileMap13Char == 'X'){  // block wall

                    Block wall = new Block(wallImage , x , y , tileSize , tileSize);  // since we had created a class Block and a constructor Block also so here we are creating an object for the constructor Block. this is the object for constructor bcz here i had declared paramters also so it tells the program that we creating an object for constructor
                    walls.add(wall);  // Adds the newly created wall to a HashSet that stores all the walls in the game.
                    // this ensures that all walls are stored in one list which is walls here
                    // it makes easier to draw all the walls in the game 
                    // used for collision detection so that pacman cant go throught walls
                }

                else if ( tileMap13Char == ' ')  // food
                {
                    System.out.println("adding foods");
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }

                else if ( tileMap13Char == 'E')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMap13Char == 'e')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMap13Char == 'M')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMap13Char == 'm')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }


                else if( tileMap13Char == 'o')   // orange ghost
                {

                    Block OrangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost);
                }


                else if( tileMap13Char == 'b')   // blue ghost
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }


                else if( tileMap13Char == 'r')   // red ghost
                {

                    Block RedGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost);
                }


                else if( tileMap13Char == 'p')   // pink ghost
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }


                else if ( tileMap13Char == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }


                else if ( tileMap13Char == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }


                else if ( tileMap13Char == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }


                
                
                else if ( tileMap13Char == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }


                else if ( tileMap13Char == 'Z')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tileMap13Char == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }


                
                else if ( tileMap13Char == 'W')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMap13Char == 'w')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }

                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    };




    public void loadMap14(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>();
        iceBlocks = new HashSet<Block>();
        speedZones = new HashSet<Block>();
        reverseControls = new HashSet<Block>();
        phantomZones = new HashSet<Block>();
        warpEntrance = new HashSet<Block>();
        warpExit = new HashSet<Block>();
        spaces1 = new HashSet<Block>();
        spaces2 = new HashSet<Block>();
        exitSpaces1 = new HashSet<Block>();
        exitSpaces2 = new HashSet<Block>();

        for ( int r = 0 ; r<rowCount ; r++)   // r stands for row
        {
            for (  int c = 0 ; c<colCount ; c++)   // c stands for column
            {
                String row = tileMap14[r];   // getting the current row bcz tileMap is our map and r index is the row index so we are getting the loop indexing 
                char tileMap14Char = row.charAt(c);  // getting the current character
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


                if ( tileMap14Char == 'X'){  // block wall

                    Block wall = new Block(wallImage , x , y , tileSize , tileSize);  // since we had created a class Block and a constructor Block also so here we are creating an object for the constructor Block. this is the object for constructor bcz here i had declared paramters also so it tells the program that we creating an object for constructor
                    walls.add(wall);  // Adds the newly created wall to a HashSet that stores all the walls in the game.
                    // this ensures that all walls are stored in one list which is walls here
                    // it makes easier to draw all the walls in the game 
                    // used for collision detection so that pacman cant go throught walls
                }

                else if ( tileMap14Char == ' ')  // food
                {
                    System.out.println("adding foods");
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMap14Char == 'E')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMap14Char == 'e')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMap14Char == 'M')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMap14Char == 'm')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }


                else if( tileMap14Char == 'o')   // orange ghost
                {

                    Block OrangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost);
                }


                else if( tileMap14Char == 'b')   // blue ghost
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }


                else if( tileMap14Char == 'r')   // red ghost
                {

                    Block RedGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost);
                }


                else if( tileMap14Char == 'p')   // pink ghost
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }


                else if ( tileMap14Char == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }

                else if ( tileMap14Char == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }


                else if ( tileMap14Char == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }

                
                
                else if ( tileMap14Char == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }

                else if ( tileMap14Char == 'Z')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tileMap14Char == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }

                
                else if ( tileMap14Char == 'W')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMap14Char == 'w')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }

                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    };




    public void loadMap15(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>();
        iceBlocks = new HashSet<Block>();
        speedZones = new HashSet<Block>();
        reverseControls = new HashSet<Block>();
        phantomZones = new HashSet<Block>();
        warpEntrance = new HashSet<Block>();
        warpExit = new HashSet<Block>();
        spaces1 = new HashSet<Block>();
        spaces2 = new HashSet<Block>();
        exitSpaces1 = new HashSet<Block>();
        exitSpaces2 = new HashSet<Block>();

        for ( int r = 0 ; r<rowCount ; r++)   // r stands for row
        {
            for (  int c = 0 ; c<colCount ; c++)   // c stands for column
            {
                String row = tileMap15[r];   // getting the current row bcz tileMap is our map and r index is the row index so we are getting the loop indexing 
                char tileMap15Char = row.charAt(c);  // getting the current character
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


                if ( tileMap15Char == 'X'){  // block wall

                    Block wall = new Block(wallImage , x , y , tileSize , tileSize);  // since we had created a class Block and a constructor Block also so here we are creating an object for the constructor Block. this is the object for constructor bcz here i had declared paramters also so it tells the program that we creating an object for constructor
                    walls.add(wall);  // Adds the newly created wall to a HashSet that stores all the walls in the game.
                    // this ensures that all walls are stored in one list which is walls here
                    // it makes easier to draw all the walls in the game 
                    // used for collision detection so that pacman cant go throught walls
                }

                else if ( tileMap15Char == ' ')  // food
                {
                    System.out.println("adding foods");
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMap15Char == 'E')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMap15Char == 'e')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMap15Char == 'M')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMap15Char == 'm')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }


                else if( tileMap15Char == 'o')   // orange ghost
                {

                    Block OrangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost);
                }


                else if( tileMap15Char == 'b')   // blue ghost
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }


                else if( tileMap15Char == 'r')   // red ghost
                {

                    Block RedGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost);
                }


                else if( tileMap15Char == 'p')   // pink ghost
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }


                else if ( tileMap15Char == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }

                else if ( tileMap15Char == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }


                else if ( tileMap15Char == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }



                
                
                else if ( tileMap15Char == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }


                else if ( tileMap15Char == 'Z')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tileMap15Char == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }

                
                else if ( tileMap15Char == 'W')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMap15Char == 'w')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }

                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    };




    public void loadMap16(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        teleportPads = new HashSet<Block>();
        iceBlocks = new HashSet<Block>();
        speedZones = new HashSet<Block>();
        reverseControls = new HashSet<Block>();
        phantomZones = new HashSet<Block>();
        warpEntrance = new HashSet<Block>();
        warpExit = new HashSet<Block>();
        spaces1 = new HashSet<Block>();
        spaces2 = new HashSet<Block>();
        exitSpaces1 = new HashSet<Block>();
        exitSpaces2 = new HashSet<Block>();

        for ( int r = 0 ; r<rowCount ; r++)   // r stands for row
        {
            for (  int c = 0 ; c<colCount ; c++)   // c stands for column
            {
                String row = tileMap16[r];   // getting the current row bcz tileMap is our map and r index is the row index so we are getting the loop indexing 
                char tileMap16Char = row.charAt(c);  // getting the current character
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


                if ( tileMap16Char == 'X'){  // block wall

                    Block wall = new Block(wallImage , x , y , tileSize , tileSize);  // since we had created a class Block and a constructor Block also so here we are creating an object for the constructor Block. this is the object for constructor bcz here i had declared paramters also so it tells the program that we creating an object for constructor
                    walls.add(wall);  // Adds the newly created wall to a HashSet that stores all the walls in the game.
                    // this ensures that all walls are stored in one list which is walls here
                    // it makes easier to draw all the walls in the game 
                    // used for collision detection so that pacman cant go throught walls
                }

                else if ( tileMap16Char == ' ')  // food
                {
                    System.out.println("adding foods");
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMap16Char == 'E')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMap16Char == 'e')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMap16Char == 'M')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMap16Char == 'm')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }

                else if( tileMap16Char == 'o')   // orange ghost
                {

                    Block OrangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost);
                }


                else if( tileMap16Char == 'b')   // blue ghost
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }


                else if( tileMap16Char == 'r')   // red ghost
                {

                    Block RedGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost);
                }


                else if( tileMap16Char == 'p')   // pink ghost
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }


                else if ( tileMap16Char == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }

                else if ( tileMap16Char == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }


                else if ( tileMap16Char == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }

                
                
                else if ( tileMap16Char == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }

                else if ( tileMap16Char == 'Z')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tileMap16Char == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }

                
                else if ( tileMap16Char == 'W')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMap16Char == 'w')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }
                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    };




    public void loadMap17(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        iceBlocks = new HashSet<Block>();
        teleportPads = new HashSet<Block>();
        speedZones = new HashSet<Block>();
        phantomZones = new HashSet<Block>();
        reverseControls = new HashSet<Block>();
        warpEntrance = new HashSet<Block>();
        warpExit = new HashSet<Block>();
        spaces1 = new HashSet<Block>();
        spaces2 = new HashSet<Block>();
        exitSpaces1 = new HashSet<Block>();
        exitSpaces2 = new HashSet<Block>();


        for ( int r = 0 ; r<rowCount ; r++)   // r stands for row
        {
            for (  int c = 0 ; c<colCount ; c++)   // c stands for column
            {
                String row = tileMap17[r];   // getting the current row bcz tileMap is our map and r index is the row index so we are getting the loop indexing 
                char tileMap17Char = row.charAt(c);  // getting the current character
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


                if ( tileMap17Char == 'X'){  // block wall

                    Block wall = new Block(wallImage , x , y , tileSize , tileSize);  // since we had created a class Block and a constructor Block also so here we are creating an object for the constructor Block. this is the object for constructor bcz here i had declared paramters also so it tells the program that we creating an object for constructor
                    walls.add(wall);  // Adds the newly created wall to a HashSet that stores all the walls in the game.
                    // this ensures that all walls are stored in one list which is walls here
                    // it makes easier to draw all the walls in the game 
                    // used for collision detection so that pacman cant go throught walls
                }

                else if ( tileMap17Char == ' ')  // food
                {
                    System.out.println("adding foods");
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMap17Char == 'E')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMap17Char == 'e')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMap17Char == 'M')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMap17Char == 'm')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }



                else if( tileMap17Char == 'o')   // orange ghost
                {

                    Block OrangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost);
                }


                else if( tileMap17Char == 'b')   // blue ghost
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }


                else if( tileMap17Char == 'r')   // red ghost
                {

                    Block RedGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost);
                }


                else if( tileMap17Char == 'p')   // pink ghost
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }


                else if ( tileMap17Char == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }

                else if ( tileMap17Char == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }


                else if ( tileMap17Char == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }


                
                
                else if ( tileMap17Char == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }


                else if ( tileMap17Char == 'Z')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tileMap17Char == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }


                
                else if ( tileMap17Char == 'W')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMap17Char == 'w')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }

                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    };




    public void loadMap18(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        iceBlocks = new HashSet<Block>();
        teleportPads = new HashSet<Block>();
        speedZones = new HashSet<Block>();
        reverseControls = new HashSet<Block>();
        phantomZones = new HashSet<Block>();
        warpEntrance = new HashSet<Block>();
        warpExit = new HashSet<Block>();
        spaces1 = new HashSet<Block>();
        spaces2 = new HashSet<Block>();
        exitSpaces1 = new HashSet<Block>();
        exitSpaces2 = new HashSet<Block>();


        for ( int r = 0 ; r<rowCount ; r++)   // r stands for row
        {
            for (  int c = 0 ; c<colCount ; c++)   // c stands for column
            {
                String row = tileMap18[r];   // getting the current row bcz tileMap is our map and r index is the row index so we are getting the loop indexing 
                char tileMap18Char = row.charAt(c);  // getting the current character
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


                if ( tileMap18Char == 'X'){  // block wall

                    Block wall = new Block(wallImage , x , y , tileSize , tileSize);  // since we had created a class Block and a constructor Block also so here we are creating an object for the constructor Block. this is the object for constructor bcz here i had declared paramters also so it tells the program that we creating an object for constructor
                    walls.add(wall);  // Adds the newly created wall to a HashSet that stores all the walls in the game.
                    // this ensures that all walls are stored in one list which is walls here
                    // it makes easier to draw all the walls in the game 
                    // used for collision detection so that pacman cant go throught walls
                }

                else if ( tileMap18Char == ' ')  // food
                {
                    System.out.println("adding foods");
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMap18Char == 'E')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMap18Char == 'e')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMap18Char == 'M')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMap18Char == 'm')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }


                else if( tileMap18Char == 'o')   // orange ghost
                {

                    Block OrangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost);
                }


                else if( tileMap18Char == 'b')   // blue ghost
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }


                else if( tileMap18Char == 'r')   // red ghost
                {

                    Block RedGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost);
                }


                else if( tileMap18Char == 'p')   // pink ghost
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }


                else if ( tileMap18Char == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }

                else if ( tileMap18Char == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }


                else if ( tileMap18Char == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }

               
                else if ( tileMap18Char == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }


                else if ( tileMap18Char == 'Z')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tileMap18Char == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }

                else if ( tileMap18Char == 'W')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMap18Char == 'w')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }
                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    };
    




    public void loadMap19(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        iceBlocks = new HashSet<Block>();
        teleportPads = new HashSet<Block>();
        speedZones = new HashSet<Block>();
        reverseControls = new HashSet<Block>();
        phantomZones = new HashSet<Block>();
        warpEntrance = new HashSet<Block>();
        warpExit = new HashSet<Block>();
        spaces1 = new HashSet<Block>();
        spaces2 = new HashSet<Block>();
        exitSpaces1 = new HashSet<Block>();
        exitSpaces2 = new HashSet<Block>();
        
        

        for ( int r = 0 ; r<rowCount ; r++)   // r stands for row
        {
            for (  int c = 0 ; c<colCount ; c++)   // c stands for column
            {
                String row = tileMap19[r];   // getting the current row bcz tileMap is our map and r index is the row index so we are getting the loop indexing 
                char tileMap19Char = row.charAt(c);  // getting the current character
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


                if ( tileMap19Char == 'X'){  // block wall

                    Block wall = new Block(wallImage , x , y , tileSize , tileSize);  // since we had created a class Block and a constructor Block also so here we are creating an object for the constructor Block. this is the object for constructor bcz here i had declared paramters also so it tells the program that we creating an object for constructor
                    walls.add(wall);  // Adds the newly created wall to a HashSet that stores all the walls in the game.
                    // this ensures that all walls are stored in one list which is walls here
                    // it makes easier to draw all the walls in the game 
                    // used for collision detection so that pacman cant go throught walls
                }

                else if ( tileMap19Char == ' ')  // food
                {
                    System.out.println("adding foods");
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMap19Char == 'E')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMap19Char == 'e')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMap19Char == 'M')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMap19Char == 'm')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }

                else if( tileMap19Char == 'o')   // orange ghost
                {

                    Block OrangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost);
                }


                else if( tileMap19Char == 'b')   // blue ghost
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }


                else if( tileMap19Char == 'r')   // red ghost
                {

                    Block RedGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost);
                }


                else if( tileMap19Char == 'p')   // pink ghost
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }


                else if ( tileMap19Char == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }

                else if ( tileMap19Char == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }


                else if ( tileMap19Char == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }


                else if ( tileMap19Char == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }


                else if ( tileMap19Char == 'Z')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tileMap19Char == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }


                else if ( tileMap19Char == 'W')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMap19Char == 'w')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }

                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    };





    public void loadMap20(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        iceBlocks = new HashSet<Block>();
        teleportPads = new HashSet<Block>();
        speedZones = new HashSet<Block>();
        phantomZones = new HashSet<Block>();
        reverseControls = new HashSet<>();
        warpEntrance = new HashSet<Block>();
        warpExit = new HashSet<Block>();
        spaces1 = new HashSet<Block>();
        spaces2 = new HashSet<Block>();
        exitSpaces1 = new HashSet<Block>();
        exitSpaces2 = new HashSet<Block>();

        

        for ( int r = 0 ; r<rowCount ; r++)   // r stands for row
        {
            for (  int c = 0 ; c<colCount ; c++)   // c stands for column
            {
                String row = tileMap20[r];   // getting the current row bcz tileMap is our map and r index is the row index so we are getting the loop indexing 
                char tileMap20Char = row.charAt(c);  // getting the current character
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


                if ( tileMap20Char == 'X'){  // block wall

                    Block wall = new Block(wallImage , x , y , tileSize , tileSize);  // since we had created a class Block and a constructor Block also so here we are creating an object for the constructor Block. this is the object for constructor bcz here i had declared paramters also so it tells the program that we creating an object for constructor
                    walls.add(wall);  // Adds the newly created wall to a HashSet that stores all the walls in the game.
                    // this ensures that all walls are stored in one list which is walls here
                    // it makes easier to draw all the walls in the game 
                    // used for collision detection so that pacman cant go throught walls
                }

                else if ( tileMap20Char == ' ')  // food
                {
                    System.out.println("adding foods");
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMap20Char == 'E')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMap20Char == 'e')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMap20Char == 'M')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMap20Char == 'm')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }



                else if( tileMap20Char == 'o')   // orange ghost
                {

                    Block OrangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost);
                }


                else if( tileMap20Char == 'b')   // blue ghost
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }


                else if( tileMap20Char == 'r')   // red ghost
                {

                    Block RedGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost);
                }


                else if( tileMap20Char == 'p')   // pink ghost
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }


                else if ( tileMap20Char == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }

                else if ( tileMap20Char == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }


                else if ( tileMap20Char == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }


                else if ( tileMap20Char == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }


                else if ( tileMap20Char == 'Z')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tileMap20Char == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }

                else if ( tileMap20Char == 'W')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMap20Char == 'w')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }


                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    };





    public void loadMap21(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        iceBlocks = new HashSet<Block>();
        teleportPads = new HashSet<Block>();
        speedZones = new HashSet<Block>();
        phantomZones = new HashSet<Block>();
        reverseControls = new HashSet<Block>();
        warpEntrance = new HashSet<Block>();
        warpExit = new HashSet<Block>();
        spaces1 = new HashSet<Block>();
        spaces2 = new HashSet<Block>();
        exitSpaces1 = new HashSet<Block>();
        exitSpaces2 = new HashSet<Block>();

        for ( int r = 0 ; r<rowCount ; r++)   // r stands for row
        {
            for (  int c = 0 ; c<colCount ; c++)   // c stands for column
            {
                String row = tileMap21[r];   // getting the current row bcz tileMap is our map and r index is the row index so we are getting the loop indexing 
                char tileMap21Char = row.charAt(c);  // getting the current character
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


                if ( tileMap21Char == 'X'){  // block wall

                    Block wall = new Block(wallImage , x , y , tileSize , tileSize);  // since we had created a class Block and a constructor Block also so here we are creating an object for the constructor Block. this is the object for constructor bcz here i had declared paramters also so it tells the program that we creating an object for constructor
                    walls.add(wall);  // Adds the newly created wall to a HashSet that stores all the walls in the game.
                    // this ensures that all walls are stored in one list which is walls here
                    // it makes easier to draw all the walls in the game 
                    // used for collision detection so that pacman cant go throught walls
                }

                else if ( tileMap21Char == ' ')  // food
                {
                    System.out.println("adding foods");
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMap21Char == 'E')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMap21Char == 'e')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMap21Char == 'M')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMap21Char == 'm')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }

                else if( tileMap21Char == 'o')   // orange ghost
                {

                    Block OrangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost);
                }


                else if( tileMap21Char == 'b')   // blue ghost
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }


                else if( tileMap21Char == 'r')   // red ghost
                {

                    Block RedGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost);
                }


                else if( tileMap21Char == 'p')   // pink ghost
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }


                else if ( tileMap21Char == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }

                else if ( tileMap21Char == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }


                else if ( tileMap21Char == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }


                else if ( tileMap21Char == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }

                else if ( tileMap21Char == 'Z')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }

                else if ( tileMap21Char == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }

                else if ( tileMap21Char == 'W')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMap21Char == 'w')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }
                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    };





    public void loadMap22(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        iceBlocks = new HashSet<Block>();
        teleportPads = new HashSet<Block>();
        speedZones = new HashSet<Block>();
        phantomZones = new HashSet<Block>();
        reverseControls = new HashSet<Block>();
        warpEntrance = new HashSet<Block>();
        warpExit = new HashSet<Block>();
        spaces1 = new HashSet<Block>();
        spaces2 = new HashSet<Block>();
        exitSpaces1 = new HashSet<Block>();
        exitSpaces2 = new HashSet<Block>();

        for ( int r = 0 ; r<rowCount ; r++)   // r stands for row
        {
            for (  int c = 0 ; c<colCount ; c++)   // c stands for column
            {
                String row = tileMap22[r];   // getting the current row bcz tileMap is our map and r index is the row index so we are getting the loop indexing 
                char tileMap22Char = row.charAt(c);  // getting the current character
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


                if ( tileMap22Char == 'X'){  // block wall

                    Block wall = new Block(wallImage , x , y , tileSize , tileSize);  // since we had created a class Block and a constructor Block also so here we are creating an object for the constructor Block. this is the object for constructor bcz here i had declared paramters also so it tells the program that we creating an object for constructor
                    walls.add(wall);  // Adds the newly created wall to a HashSet that stores all the walls in the game.
                    // this ensures that all walls are stored in one list which is walls here
                    // it makes easier to draw all the walls in the game 
                    // used for collision detection so that pacman cant go throught walls
                }

                else if ( tileMap22Char == ' ')  // food
                {
                    System.out.println("adding foods");
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMap22Char == 'E')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMap22Char == 'e')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMap22Char == 'M')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMap22Char == 'm')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }
                


                else if( tileMap22Char == 'o')   // orange ghost
                {

                    Block OrangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost);
                }


                else if( tileMap22Char == 'b')   // blue ghost
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }


                else if( tileMap22Char == 'r')   // red ghost
                {

                    Block RedGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost);
                }


                else if( tileMap22Char == 'p')   // pink ghost
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }


                else if ( tileMap22Char == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }

                else if ( tileMap22Char == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }


                else if ( tileMap22Char == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }


                else if ( tileMap22Char == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }

                else if ( tileMap22Char == 'Z')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tileMap22Char == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }

                else if ( tileMap22Char == 'W')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMap22Char == 'w')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }
                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    };




    public void loadMap23(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        iceBlocks = new HashSet<Block>();
        teleportPads = new HashSet<Block>();
        speedZones = new HashSet<Block>();
        phantomZones = new HashSet<Block>();
        reverseControls = new HashSet<Block>();
        warpEntrance = new HashSet<Block>();
        warpExit = new HashSet<Block>();
        spaces1 = new HashSet<Block>();
        spaces2 = new HashSet<Block>();
        exitSpaces1 = new HashSet<Block>();
        exitSpaces2 = new HashSet<Block>();

        for ( int r = 0 ; r<rowCount ; r++)   // r stands for row
        {
            for (  int c = 0 ; c<colCount ; c++)   // c stands for column
            {
                String row = tileMap23[r];   // getting the current row bcz tileMap is our map and r index is the row index so we are getting the loop indexing 
                char tileMap23Char = row.charAt(c);  // getting the current character
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


                if ( tileMap23Char == 'X'){  // block wall

                    Block wall = new Block(wallImage , x , y , tileSize , tileSize);  // since we had created a class Block and a constructor Block also so here we are creating an object for the constructor Block. this is the object for constructor bcz here i had declared paramters also so it tells the program that we creating an object for constructor
                    walls.add(wall);  // Adds the newly created wall to a HashSet that stores all the walls in the game.
                    // this ensures that all walls are stored in one list which is walls here
                    // it makes easier to draw all the walls in the game 
                    // used for collision detection so that pacman cant go throught walls
                }

                else if ( tileMap23Char == ' ')  // food
                {
                    System.out.println("adding foods");
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMap23Char == 'E')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMap23Char == 'e')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMap23Char == 'M')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMap23Char == 'm')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }


                else if( tileMap23Char == 'o')   // orange ghost
                {

                    Block OrangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost);
                }


                else if( tileMap23Char == 'b')   // blue ghost
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }


                else if( tileMap23Char == 'r')   // red ghost
                {

                    Block RedGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost);
                }


                else if( tileMap23Char == 'p')   // pink ghost
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }


                else if ( tileMap23Char == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }

                else if ( tileMap23Char == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }


                else if ( tileMap23Char == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }


                else if ( tileMap23Char == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }

                else if ( tileMap23Char == 'Z')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tileMap23Char == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }


                else if ( tileMap23Char == 'W')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMap23Char == 'w')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }
                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    };





    public void loadMap24(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        iceBlocks = new HashSet<Block>();
        teleportPads = new HashSet<Block>();
        speedZones = new HashSet<Block>();
        phantomZones = new HashSet<Block>();
        reverseControls = new HashSet<Block>();
        warpEntrance = new HashSet<Block>();
        warpExit = new HashSet<Block>();
        spaces1 = new HashSet<Block>();
        spaces2 = new HashSet<Block>();
        exitSpaces1 = new HashSet<Block>();
        exitSpaces2 = new HashSet<Block>();

        for ( int r = 0 ; r<rowCount ; r++)   // r stands for row
        {
            for (  int c = 0 ; c<colCount ; c++)   // c stands for column
            {
                String row = tileMap24[r];   // getting the current row bcz tileMap is our map and r index is the row index so we are getting the loop indexing 
                char tileMap24Char = row.charAt(c);  // getting the current character
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


                if ( tileMap24Char == 'X'){  // block wall

                    Block wall = new Block(wallImage , x , y , tileSize , tileSize);  // since we had created a class Block and a constructor Block also so here we are creating an object for the constructor Block. this is the object for constructor bcz here i had declared paramters also so it tells the program that we creating an object for constructor
                    walls.add(wall);  // Adds the newly created wall to a HashSet that stores all the walls in the game.
                    // this ensures that all walls are stored in one list which is walls here
                    // it makes easier to draw all the walls in the game 
                    // used for collision detection so that pacman cant go throught walls
                }

                else if ( tileMap24Char == ' ')  // food
                {
                    System.out.println("adding foods");
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMap24Char == 'E')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMap24Char == 'e')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMap24Char == 'M')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMap24Char == 'm')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }


                else if( tileMap24Char == 'o')   // orange ghost
                {

                    Block OrangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost);
                }


                else if( tileMap24Char == 'b')   // blue ghost
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }


                else if( tileMap24Char == 'r')   // red ghost
                {

                    Block RedGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost);
                }


                else if( tileMap24Char == 'p')   // pink ghost
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }


                else if ( tileMap24Char == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }

                else if ( tileMap24Char == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }


                else if ( tileMap24Char == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }


                else if ( tileMap24Char == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }

                else if ( tileMap24Char == 'Z')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tileMap24Char == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }


                else if ( tileMap24Char == 'W')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMap24Char == 'w')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }

                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    };




    public void loadMap25(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        iceBlocks = new HashSet<Block>();
        teleportPads = new HashSet<Block>();
        speedZones = new HashSet<Block>();
        phantomZones = new HashSet<Block>();
        reverseControls = new HashSet<Block>();
        warpEntrance = new HashSet<Block>();
        warpExit = new HashSet<Block>();
        spaces1 = new HashSet<Block>();
        spaces2 = new HashSet<Block>();
        exitSpaces1 = new HashSet<Block>();
        exitSpaces2 = new HashSet<Block>();
        

        for ( int r = 0 ; r<rowCount ; r++)   // r stands for row
        {
            for (  int c = 0 ; c<colCount ; c++)   // c stands for column
            {
                String row = tileMap25[r];   // getting the current row bcz tileMap is our map and r index is the row index so we are getting the loop indexing 
                char tileMap25Char = row.charAt(c);  // getting the current character
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


                if ( tileMap25Char == 'X'){  // block wall

                    Block wall = new Block(wallImage , x , y , tileSize , tileSize);  // since we had created a class Block and a constructor Block also so here we are creating an object for the constructor Block. this is the object for constructor bcz here i had declared paramters also so it tells the program that we creating an object for constructor
                    walls.add(wall);  // Adds the newly created wall to a HashSet that stores all the walls in the game.
                    // this ensures that all walls are stored in one list which is walls here
                    // it makes easier to draw all the walls in the game 
                    // used for collision detection so that pacman cant go throught walls
                }

                else if ( tileMap25Char == ' ')  // food
                {
                    System.out.println("adding foods");
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMap25Char == 'E')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMap25Char == 'e')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMap25Char == 'M')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMap25Char == 'm')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }

                else if( tileMap25Char == 'o')   // orange ghost
                {

                    Block OrangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost);
                }


                else if( tileMap25Char == 'b')   // blue ghost
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }


                else if( tileMap25Char == 'r')   // red ghost
                {

                    Block RedGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost);
                }


                else if( tileMap25Char == 'p')   // pink ghost
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }


                else if ( tileMap25Char == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }

                else if ( tileMap25Char == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }


                else if ( tileMap25Char == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }


                else if ( tileMap25Char == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }

                else if ( tileMap25Char == 'Z')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tileMap25Char == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }


                else if ( tileMap25Char == 'W')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMap25Char == 'w')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }

                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    };




    public void loadMap26(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        iceBlocks = new HashSet<Block>();
        teleportPads = new HashSet<Block>();
        speedZones = new HashSet<Block>();
        phantomZones = new HashSet<Block>();
        reverseControls = new HashSet<Block>();
        warpEntrance = new HashSet<Block>();
        warpExit = new HashSet<Block>();
        spaces1 = new HashSet<Block>();  // E
        exitSpaces1 = new HashSet<Block>(); //e
        spaces2 = new HashSet<Block>();   // M
        exitSpaces2 = new HashSet<Block>(); //m



        for ( int r = 0 ; r<rowCount ; r++)   // r stands for row
        {
            for (  int c = 0 ; c<colCount ; c++)   // c stands for column
            {
                String row = tileMap26[r];   // getting the current row bcz tileMap is our map and r index is the row index so we are getting the loop indexing 
                char tileMap26Char = row.charAt(c);  // getting the current character
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


                if ( tileMap26Char == 'X'){  // block wall

                    Block wall = new Block(wallImage , x , y , tileSize , tileSize);  // since we had created a class Block and a constructor Block also so here we are creating an object for the constructor Block. this is the object for constructor bcz here i had declared paramters also so it tells the program that we creating an object for constructor
                    walls.add(wall);  // Adds the newly created wall to a HashSet that stores all the walls in the game.
                    // this ensures that all walls are stored in one list which is walls here
                    // it makes easier to draw all the walls in the game 
                    // used for collision detection so that pacman cant go throught walls
                }

                else if ( tileMap26Char == ' ')  // food
                {
                    System.out.println("adding foods");
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMap26Char == 'E')   // allowing pacman to move map from end to other
                {
                   Block space1  = new Block(null , x, y, tileSize, tileSize);
                   spaces1.add(space1);
                }
                
                else if ( tileMap26Char == 'e')
                {
                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);
                }


                else if ( tileMap26Char == 'M')   // allowing pacman to move map from end to other
                {
                   Block space2  = new Block(null , x, y, tileSize, tileSize);
                   spaces2.add(space2);
                }
                
                else if ( tileMap26Char == 'm')
                {
                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);
                }


                else if( tileMap26Char == 'o')   // orange ghost
                {

                    Block OrangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost);
                }


                else if( tileMap26Char == 'b')   // blue ghost
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }


                else if( tileMap26Char == 'r')   // red ghost
                {

                    Block RedGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost);
                }


                else if( tileMap26Char == 'p')   // pink ghost
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }


                else if ( tileMap26Char == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }

                else if ( tileMap26Char == 'I')
                {
                    Block ice  = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);
                }


                else if ( tileMap26Char == 'T')
                {
                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);
                }


                else if ( tileMap26Char == 'S')
                {
                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);
                }

                else if ( tileMap26Char == 'Z')
                {
                    Block phantom = new Block(null, x, y, tileSize, tileSize);  // null  bcz we are not taking the image
                    phantomZones.add(phantom);
                }


                else if ( tileMap26Char == 'R')
                {
                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);
                }


                else if ( tileMap26Char == 'W')
                {
                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);
                }

                else if ( tileMap26Char == 'w')
                {
                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);
                }

                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

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

        

 
        // since i want the pacman to move above the ice tiles and not below and since i is a 2d game so the layering depends on the order of draw so i draw the ice tiles first and then pacman
         // DISPLAYING ICE
         for( Block ice : iceBlocks)
         {
             g.drawImage(ice.image, ice.x, ice.y , ice.width , ice.height , null);
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


        // displaying walls
        for ( Block wall : walls){
            g.drawImage(wall.image, wall.x, wall.y, wall.width , wall.height , null);
        }

    

        // displaying phantom zones. it is done after walls are drawn bcz they will display above the wall . if drawn before walls then it will make then hide behind walls
        g.setColor(new Color(100, 0, 100, 120)); // semi-transparent purple
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

        // 4. If still on ice, keep sliding automatically in same direction next frame
        // But if off ice, zero out velocity so it doesn’t keep going
        if (!isOnIce(pacman)) {
            pacman.velocityX = 0;
            pacman.velocityY = 0;
        }
        
        

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



        

        // TELEPORTING THE PACMAN FROM ONE MAP END TO OTHER
        // Handle E ↔ e warp
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

// Handle M ↔ m warp
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
                long currentTime = System.currentTimeMillis();
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
    

       


        // checking wall collision
        // now i want to iterate through all the walls in the hashset
        // so
        for ( Block wall : walls){
            if (collision(pacman , wall))   // if collison happens between pacman and this current wall
            {
                pacman.x -= pacman.velocityX;  
                pacman.y -= pacman.velocityY;
                break; // once we found a collision we dont need to continue with hashset bcz we already foubd the wall that we are colliding to 
                // i will take a step back 
                // what is happening here is 
                // -> If a collision is detected, we undo the last movement, so Pac-Man stays in place.
                // -> Since we break out of the loop after detecting a collision, we avoid unnecessary checks for other walls.
                // -> Instead of stopping abruptly, we just reverse Pac-Man's last movement, making the transition feel natural.
                // -> Pac-Man stays in place instead of going through the wall
                // -> it does not means that the pacman will start moving backwards 

            }
        }

        

        // now our pacman stops after colliding with the wall but it got stuck sometimes when we there is no way
        // so to make that fix the pacman should only change the direction if it is able to which basically means that it should not change the direction if there is a wall 
        // therefore go towards the update direction function




        // ghost collison

        for ( Block ghost : ghosts){  // for each ghost
            

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

                    ghost.phaseTilesRemaining = (tileSize / 8) * 2;  // GHOST SPEED IN OUR PROGRAM IS TILESIZE/4 I.E 32/4
                    // NOTE: HERE NUMBER OF TILES TWO BCZ I THINK IT COUNTS THE PHANTOM ZONE TILE ALSO SO ONE WALL AND ONE PHANTOM ZONE THEREFORE 2


                }
            }
        
            // Move ghost
            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;
        
            // Handle walls ONLY if not in phase mode
            if (ghost.phaseTilesRemaining <= 0) {
                for (Block wall : walls) {
                    if (collision(ghost, wall)) {
                        ghost.x -= ghost.velocityX;
                        ghost.y -= ghost.velocityY;
                        char newDirection = directions[random.nextInt(4)];
                        ghost.updateDirection(newDirection);
                        break;
                    }
                }
            } else {
                // Decrement phase steps
                ghost.phaseTilesRemaining--;
            }
        
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
    this :: loadMap26

    // if we want to add more maps then just write this::loadMapX and done but the last line should not have a comma

};


int currentLevel = 0;

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
        // now to make our key presses work . we need to add two things in our constructor adding key listner 

        // now i comment out that prinitng statement


        if ( gameOver)
        {
            loadMap1(); // reloading the map . this is done bcz i want to add all the foods back into the hashset 
            resetPositions(); // resetting the positions 
            // now we want each object a new direction to move in  so
            lives = 3;  // making lives again 3
            score  = 0;  // making score again 0
            gameOver = false;  // setting gameover to be false
            gameLoop.start();  // starting the game loop 

        }

        // now there maybe a case where the pacman has eaten all the food so i can load new levels but here i will load the same map



        if (controlsReversed) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                pacman.updateDirection('D');
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                pacman.updateDirection('U');
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                pacman.updateDirection('R');
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                pacman.updateDirection('L');
            }
        } else {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                pacman.updateDirection('U');
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                pacman.updateDirection('D');
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                pacman.updateDirection('L');
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                pacman.updateDirection('R');
            }
        }
        
    
        

        // if (e.getKeyCode() == KeyEvent.VK_UP)   // when i pressed the up arrow then i call this function
        // {
        //     pacman.updateDirection('U');   // passing U
        // }

        // else if (e.getKeyCode() == KeyEvent.VK_DOWN)   // when i pressed the down arrow then i call this function
        // {
        //     pacman.updateDirection('D');   // passing D
        // }

        // else if (e.getKeyCode() == KeyEvent.VK_LEFT)   // when i pressed the left arrow then i call this function
        // {
        //     pacman.updateDirection('L');   // passing L
        // }

        // else if (e.getKeyCode() == KeyEvent.VK_RIGHT)   // when i pressed the right arrow then i call this function
        // {
        //     pacman.updateDirection('R');   // passing R
        // }

        // // this will update the direction and velocity but we actually need to move the pacman so we need to keep updating the paintComponent also 
        // // so create a function move just above the override function of action listener

    
        

        // // now we are trying to update the image with the direction updation

        // if ( pacman.direction == 'U')
        // {
        //     pacman.image = pacmanUpImage;
        // }


        // else if ( pacman.direction == 'D')
        // {
        //     pacman.image = pacmanDownImage;
        // }


        // else if ( pacman.direction == 'L')
        // {
        //     pacman.image = pacmanLeftImage;
        // }


        // else if ( pacman.direction == 'R')
        // {
        //     pacman.image = pacmanRightImage;
        // }

        // now we had created different cases to do this instead of writing this one line inside the above if else cases where we are checking for the key up down left and right
        // and updating the direction
        // but if you press the key up then it does not mean that the packman will move upwards bcz there maybe a wall so if i could have placed this image upadation line inisde them just below updateDirection('U') then it would have resulted in falsy manner 



        // now we are going to make the ghost move 
        // they will move randomly 

        // for that lets move on to the class PacMan
    }


}
